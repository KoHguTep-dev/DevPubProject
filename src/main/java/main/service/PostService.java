package main.service;

import main.api.response.post.PostPreview;
import main.api.response.post.PostResponse;
import main.api.response.post.PostView;
import main.api.response.post.PostsList;
import main.model.Post;
import main.model.Tag;
import main.repository.PostCommentRepository;
import main.repository.PostVotesRepository;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private PostVotesRepository voteRepository;
    @Autowired
    private PostCommentRepository commentRepository;

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
        if (PostsList.posts == null) {
            PostsList.posts = postsRepository.findAll();
        }
        PostsList postsList = new PostsList(postPreview());
        postsList.getCount();
        return postsList;
    }

    public PostPreview postPreview() {
        if (PostPreview.postComments == null && PostPreview.postVotes == null) {
            PostPreview.postComments = commentRepository.findAll();
            PostPreview.postVotes = voteRepository.findAll();
        }
        return new PostPreview();
    }

    public PostView getPost(int id) {
        if (postsRepository.findById(id).isPresent()) {
            PostView postView = new PostView();

            Post post = postsRepository.findById(id).get();
            PostView.postVotes = voteRepository.findAll();
            PostView.postComments = commentRepository.findAll();

            postView.get(id, post);

//            post.setViewCount(postView.getViewCount() + 1);
//            postsRepository.save(post);
//            postsRepository.addViewCount(id);

            return postView;
        }
        return null;
    }
}
