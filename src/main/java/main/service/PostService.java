package main.service;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.request.PostRequest;
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
        switch (mode) {
            case "popular":
                sort = Sort.by("postComments.size").descending();
                break;
            case "best":
                sort = Sort.by("postVotes.size").descending();
                break;
            case "early":
                sort = Sort.by("time").ascending();
                break;
            case "recent":
            default:
                sort = Sort.by("time").descending();
        }
        int page = offset / limit;
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
        Pageable pageable = PageRequest.of(page, limit);

        Page<Post> postPage = postsRepository.findByQuery(new Date(), query, pageable);
        return new PostsResponse(postPage);
    }

    public PostsResponse byDate(int offset, int limit, String date) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
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
        List<Post> posts = tagsRepository.findByName(tagName).getPosts();
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);

        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());
        return new PostsResponse(postPage);
    }

    public PostView getPost(int id) {
        if (postsRepository.existsById(id)) {
            Post post = postsRepository.findById(id).get();
            PostView postView = new PostView();
            postView.get(post);

            User user = getUser();
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
        Pageable pageable = PageRequest.of(page, limit);
        Page<Post> postPage;

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
        return new PostsResponse(postPage);
    }

    public PostResponse addPost(PostRequest request) {
        PostResponse postResponse = checkPost(request);
        if (postResponse.getErrors().isEmpty()) {
            User user = getUser();
            List<Tag> tags = tagList(request);
            Post post = new Post();
            request.toPost(post, user, tags);
            postsRepository.save(post);
        }
        return postResponse;
    }

    public PostResponse putPost(PostRequest request, int id) {
        PostResponse postResponse = checkPost(request);
        if (postResponse.getErrors().isEmpty()) {
            User user = getUser();
            Post post = postsRepository.findById(id).get();
            if (user.isModerator() || post.getUser().equals(user)) {
                List<Tag> tags = tagList(request);
                ModerationStatus status = post.getModerationStatus();
                request.toPost(post, user, tags);
                if (user.isModerator()) {
                    post.setModerationStatus(status);
                }
                postsRepository.save(post);
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
            PostComment comment = response.toPostComment(request,
                    postsRepository.findById(request.getPostId()).get(), getUser());
            commentRepository.save(comment);
            response.setId(comment.getId());
        }
        return response;
    }

    public boolean like(String id) {
        return addVote(id, (byte) 1);
    }

    public boolean dislike(String id) {
        return addVote(id, (byte) -1);
    }

    public StatisticsResponse statisticsByUser() {
        User user = getUser();
        StatisticsResponse statistics = new StatisticsResponse();
        statistics.setPostsCount(postsRepository.getPostCountByUser(user));
        statistics.setLikesCount(voteRepository.getVoteCountByUser(user, (byte) 1));
        statistics.setDislikesCount(voteRepository.getVoteCountByUser(user, (byte) -1));
        statistics.setViewsCount(postsRepository.getViewCountByUser(user));
        if (postsRepository.findFirstByUser(user) == null) {
            statistics.setFirstPublication(0);
            return statistics;
        }
        statistics.setFirstPublication(postsRepository.findFirstByUser(user).getTime() / 1000);
        return statistics;
    }

    public StatisticsResponse statistics(SettingsResponse settings) {
        StatisticsResponse statistics = new StatisticsResponse();
        if (!settings.isStatisticsIsPublic() && !getUser().isModerator()) {
            return statistics;
        }
        statistics.setPostsCount(postsRepository.getCountAllAvailable(new Date()));
        statistics.setLikesCount(voteRepository.getVoteCount((byte) 1));
        statistics.setDislikesCount(voteRepository.getVoteCount((byte) -1));
        statistics.setViewsCount(postsRepository.getViewCount());
        statistics.setFirstPublication(postsRepository.findFirst().getTime() / 1000);
        return statistics;
    }

    public PostsResponse moderationList(int offset, int limit, String status) {
        User user = getUser();
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
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
                        break;
                    case "decline":
                        post.setModerationStatus(ModerationStatus.DECLINED);
                        break;
                    default:
                        return result;
                }
                postsRepository.save(post);
                result = true;
            }
        }
        return result;
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

    private PostResponse checkPost(PostRequest request) {
        PostResponse postResponse = new PostResponse();
        if (request.getTitle().length() < 3) {
            postResponse.addTitleError();
        }
        if (request.getText().length() < 50) {
            postResponse.addTextError();
        }
        return postResponse;
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

    private boolean addVote(String id, byte value) {
        String s = id.replaceAll("\\D", "");
        int postId = Integer.parseInt(s);
        boolean result = true;
        Post post = postsRepository.findById(postId).get();
        User user = getUser();
        if (user == null) {
            result = false;
            return result;
        }
        if (voteRepository.findVote(post, user, value).isPresent()) {
            result = false;
            return result;
        }
        value *= -1;
        Optional<PostVote> optional = voteRepository.findVote(post, user, value);
        if (optional.isPresent()) {
            PostVote vote = optional.get();
            value *= -1;
            vote.setValue(value);
            voteRepository.save(vote);
            return result;
        }
        value *= -1;
        PostVote vote = new PostVote(post, getUser());
        vote.setValue(value);
        voteRepository.save(vote);
        return result;
    }

}
