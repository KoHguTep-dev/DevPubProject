package main.service;

import main.api.response.PostResponse;
import main.model.dto.PostView;
import main.model.entities.Post;
import main.model.entities.User;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private UserRepository userRepository;

    public PostResponse postResponse(int offset, int limit, String mode) {
        Sort sort;
        switch (mode) {
            case "popular":
                sort = Sort.by("time");
//                sort = JpaSort.unsafe("count(postComments)");
                break;
            case "best":
                sort = Sort.by("time");
//                sort = JpaSort.unsafe("count(postVotes.value = 1)");
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
        Page<Post> postPage = postsRepository.findAllAvailable(new Date(), pageable);
        return new PostResponse(postPage);
    }

    public PostResponse search(int offset, int limit, String query) {
        String s = query.trim();
        if (s.length() == 0) {
            postResponse(offset, limit, "recent");
        }
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);

        Page<Post> postPage = postsRepository.findByQuery(new Date(), query, pageable);
        return new PostResponse(postPage);
    }

    public PostResponse byDate(int offset, int limit, String date) {
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
        return new PostResponse(postPage);
    }

    public PostResponse byTag(int offset, int limit, String tagName) {
        List<Post> list = tagsRepository.findByName(tagName).getPosts();
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);

        Page<Post> postPage = new PageImpl<>(list, pageable, list.size());
        return new PostResponse(postPage);
    }


    /*
    public PostsList postsList() {
        PostsList.posts = postsRepository.findAll();
        PostsList postsList = new PostsList(new PostPreview());
        postsList.getCount();
        return postsList;
    }

    public PostsList postsList(SessionConfig sessionConfig) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = userRepository.findById(sessionConfig.getSessions().get(sessionId)).get();

        PostsList.posts.clear();
        postsRepository.findAll().forEach(post -> {
            if (post.getUser().getId() == user.getId()) {
                PostsList.posts.add(post);
            }
        });
        return new PostsList(new PostPreview());
    }
    */

    public PostView getPost(int id) {
        if (postsRepository.existsById(id)) {
            Post post = postsRepository.findById(id).get();
            PostView postView = new PostView();
            postView.get(post);

            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            User user = null;
//            if (securityConfig.getSessions().containsKey(sessionId)) {
//                user = userRepository.findById(securityConfig.getSessions().get(sessionId)).get();
//            }
            if (user == null) {
                postsRepository.addViewCount(id);
                return postView;
            }
            if (user.isModerator() || post.getUser().equals(user)) {
                return postView;
            } else if (postView.isAllowed(post)) {
                postsRepository.addViewCount(id);
                return postView;
            }
            return null;
        }
        return null;
    }

    public PostResponse getMyPosts(int offset, int limit, String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userRepository.findByEmail(securityUser.getUsername()).orElseThrow(() -> new UsernameNotFoundException(securityUser.getUsername()));

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
                postPage = postsRepository.findByUser(user, pageable);
        }
        return new PostResponse(postPage);
    }

}
