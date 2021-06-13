package main.service;

import main.api.response.PostResponse;
import main.model.dto.PostPreview;
import main.model.dto.PostView;
import main.model.dto.PostsList;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.model.entities.User;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class PostService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private UserRepository userRepository;

    public PostResponse postResponse(int offset, int limit, String mode) {
        PostResponse postResponse = new PostResponse(postsList());
        postResponse.addPosts(offset, limit, mode);
        return postResponse;
    }

    public PostResponse search(int offset, int limit, String query) {
        PostResponse postResponse = new PostResponse(postsList());
        postResponse.search(offset, limit, query);
        return postResponse;
    }

    public PostResponse byDate(int offset, int limit, String date) {
        PostResponse postResponse = new PostResponse(postsList());
        postResponse.byDate(offset, limit, date);
        return postResponse;
    }

    public PostResponse byTag(int offset, int limit, String tagName) {
        PostResponse postResponse = new PostResponse(postsList());
        Tag tag = tagsRepository.findByName(tagName);
        postResponse.byTag(offset, limit, tag);
        return postResponse;
    }

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

    public PostView getPost(int id, SessionConfig sessionConfig) {
        if (postsRepository.existsById(id)) {
            Post post = postsRepository.findById(id).get();
            PostView postView = new PostView();
            postView.get(post);

            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            User user = userRepository.findById(sessionConfig.getSessions().get(sessionId)).get();

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

    public PostResponse getMyPosts(int offset, int limit, String status, SessionConfig sessionConfig) {
        PostResponse postResponse = new PostResponse(postsList(sessionConfig));
        postResponse.getMy(offset, limit, status);

        return postResponse;
    }
}
