package main.service;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
import main.api.request.VoteRequest;
import main.api.response.*;
import main.model.dto.PostView;
import main.model.entities.*;
import main.model.enums.ModerationStatus;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostCommentRepository commentRepository;
    @Autowired
    private PostVoteRepository voteRepository;

    public PostsResponse postResponse(int offset, int limit, String mode) {
        Sort sort;
        int page = offset / limit;
        switch (mode) {
            case "popular":
                sort = Sort.by("postComments.size").descending();
                break;
            case "best":
                Pageable pageable = PageRequest.of(page, limit);
                Page<Post> postPage = postsRepository.findAvailablePostsBest(new Date(), pageable);
                return new PostsResponse(postPage);
            case "early":
                sort = Sort.by("time").ascending();
                break;
            case "recent":
            default:
                sort = Sort.by("time").descending();
        }
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Post> postPage = postsRepository.findAvailablePosts(new Date(), pageable);
        return new PostsResponse(postPage);
    }

    public PostsResponse search(int offset, int limit, String query) {
        String s = query.trim();
        if (s.length() == 0) {
            postResponse(offset, limit, "recent");
        }
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("time").descending());

        Page<Post> postPage = postsRepository.findByQuery(new Date(), query, pageable);
        return new PostsResponse(postPage);
    }

    public PostsResponse byDate(int offset, int limit, String date) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("time").descending());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date local = null;
        try {
            local = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Page<Post> postPage = postsRepository.findByDate(new Date(), local, pageable);
        return new PostsResponse(postPage);
    }

    public PostsResponse byTag(int offset, int limit, String tagName) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("time").descending());
        Page<Post> postPage = postsRepository.findByTag(new Date(), tagName, pageable);
        return new PostsResponse(postPage);
    }

    public PostView getPost(int id) {
        if (postsRepository.findById(id).isPresent()) {
            Post post = postsRepository.findById(id).get();
            PostView postView = new PostView();
            User user = getUser();
            if (postView.isAllowed(post) || post.getUser().equals(user) || (user != null && user.isModerator())) {
                postView.get(post);
            }
            if (user == null || (!user.isModerator() && !post.getUser().equals(user))) {
                postsRepository.addViewCount(id);
            }
            return postView;
        }
        return null;
    }

    public PostsResponse getMyPosts(int offset, int limit, String status) {
        User user = getUser();
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("time").descending());
        Page<Post> postPage = null;

        if (user != null) {
            switch (status) {
                case "inactive":
                    postPage = postsRepository.findByUserInactive(user, pageable);
                    break;
                case "pending":
                    postPage = postsRepository.findByUserPending(user, pageable);
                    break;
                case "declined":
                    postPage = postsRepository.findByUserDeclined(user, pageable);
                    break;
                case "published":
                    postPage = postsRepository.findByUserPublished(user, pageable);
                    break;
                default:
                    postPage = null;
            }
        }
        return new PostsResponse(postPage);
    }

    public PostResponse addPost(PostRequest request, SettingsResponse settings) {
        PostResponse postResponse = new PostResponse().checkPost(request);
        if (postResponse.getErrors().isEmpty()) {
            User user = getUser();
            List<Tag> tags = tagList(request);
            Post post = new Post();
            request.toPost(post, user, tags);
            if (!settings.isPostPreModeration()) {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
            }
            postsRepository.save(post);
        }
        return postResponse;
    }

    public PostResponse putPost(PostRequest request, int id) {
        PostResponse postResponse = new PostResponse().checkPost(request);
        if (postResponse.getErrors().isEmpty()) {
            User user = getUser();
            if (postsRepository.findById(id).isPresent()) {
                Post post = postsRepository.findById(id).get();
                if (user != null && (user.isModerator() || post.getUser().equals(user))) {
                    List<Tag> tags = tagList(request);
                    request.toPost(post, user, tags);
                    if (!user.isModerator()) {
                        post.setModerationStatus(ModerationStatus.NEW);
                    }
                    postsRepository.save(post);
                }
            }
        }
        return postResponse;
    }

    public CommentResponse addComment(CommentRequest request) {
        CommentResponse response = new CommentResponse();
        if (request.getParentId() != null) {
            if (!commentRepository.existsById(request.getParentId())) {
                response.addError();
            }
        }
        if (!postsRepository.existsById(request.getPostId())) {
            response.addError();
        }
        if (request.getText().length() < 3) {
            response.addError();
        }
        if (response.getErrors().isEmpty()) {
            User user = getUser();
            if (user != null && postsRepository.findById(request.getPostId()).isPresent()) {
                PostComment comment = response.toPostComment(request,
                        postsRepository.findById(request.getPostId()).get(), user);
                commentRepository.save(comment);
                response.setId(comment.getId());
            }
        }
        return response;
    }

    public boolean like(VoteRequest request) {
        return addVote(request.getPostId(), (byte) 1);
    }

    public boolean dislike(VoteRequest request) {
        return addVote(request.getPostId(), (byte) -1);
    }

    public StatisticsResponse statisticsByUser() {
        User user = getUser();
        if (user != null) {
            StatisticsResponse statistics = new StatisticsResponse();
            statistics.setPostsCount(postsRepository.getPostCountByUser(user));
            statistics.setLikesCount(voteRepository.getVoteCountByUser(user, (byte) 1));
            statistics.setDislikesCount(voteRepository.getVoteCountByUser(user, (byte) -1));
            statistics.setViewsCount(postsRepository.getViewCountByUser(user));
            statistics.setFirstPublication(postsRepository.findFirstByUser(user));
            return statistics;
        }
        return null;
    }

    public StatisticsResponse statistics(SettingsResponse settings) {
        User user = getUser();
        if ((user != null && user.isModerator()) || settings.isStatisticsIsPublic()) {
            StatisticsResponse statistics = new StatisticsResponse();
            statistics.setPostsCount(postsRepository.getCountAllAvailable(new Date()));
            statistics.setLikesCount(voteRepository.getVoteCount((byte) 1));
            statistics.setDislikesCount(voteRepository.getVoteCount((byte) -1));
            statistics.setViewsCount(postsRepository.getViewCount());
            statistics.setFirstPublication(postsRepository.findFirst());
            return statistics;
        }
        return null;
    }

    public PostsResponse moderationList(int offset, int limit, String status) {
        User user = getUser();
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("time").descending());
        Page<Post> postPage = null;

        if (user != null && user.isModerator()) {
            int id = user.getId();
            switch (status) {
                case "new":
                    postPage = postsRepository.findByModeratorNew(pageable);
                    break;
                case "declined":
                    postPage = postsRepository.findByModeratorDeclined(id, pageable);
                    break;
                case "accepted":
                    postPage = postsRepository.findByModeratorAccepted(id, pageable);
                    break;
                default:
                    postPage = null;
            }
        }
        return new PostsResponse(postPage);
    }

    public boolean moderationPost(ModerationRequest request) {
        boolean result = false;
        User user = getUser();
        if (user != null && user.isModerator()) {
            if (postsRepository.findById(request.getPostId()).isPresent()) {
                Post post = postsRepository.findById(request.getPostId()).get();
                switch (request.getDecision()) {
                    case "accept":
                        post.setModerationStatus(ModerationStatus.ACCEPTED);
                        post.setModeratorId(user.getId());
                        break;
                    case "decline":
                        post.setModerationStatus(ModerationStatus.DECLINED);
                        post.setModeratorId(user.getId());
                        break;
                    default:
                        return false;
                }
                postsRepository.save(post);
                result = true;
            }
        }
        return result;
    }

    public String uploadImage(MultipartFile file) {
        if (getUser() != null) {
            return ImageUtils.upload(file);
        }
        return null;
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            var securityUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return userRepository.findByEmail(securityUser.getUsername()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Tag> tagList(PostRequest request) {
        List<Tag> tags = new ArrayList<>();
        for (String s : request.getTags()) {
            if (tagsRepository.findByName(s) == null) {
                Tag tag = new Tag();
                tag.setName(s);
                tagsRepository.save(tag);
            }
            Tag tag = tagsRepository.findByName(s);
            tags.add(tag);
        }
        return tags;
    }

    private boolean addVote(int id, byte value) {
        boolean result = false;
        User user = getUser();
        if (user != null && postsRepository.findById(id).isPresent()) {
            Post post = postsRepository.findById(id).get();
            if (voteRepository.findVote(post, user, value).isPresent()) {
                return result;
            }
            value *= -1;
            Optional<PostVote> optional = voteRepository.findVote(post, user, value);
            if (optional.isPresent()) {
                PostVote vote = optional.get();
                value *= -1;
                vote.setValue(value);
                voteRepository.save(vote);
                result = true;
                return result;
            }
            value *= -1;
            PostVote vote = new PostVote(post, getUser());
            vote.setValue(value);
            voteRepository.save(vote);
            result = true;
        }
        return result;
    }

}
