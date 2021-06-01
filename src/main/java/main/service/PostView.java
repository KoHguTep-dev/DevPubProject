package main.service;

import main.model.ModerationStatus;
import main.model.Post;
import main.model.Tag;
import main.repository.PostCommentRepository;
import main.repository.PostVotesRepository;
import main.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
//@Service
//@Transactional
public class PostView {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    private PostVotesRepository voteRepository;

    @Autowired
    private PostCommentRepository commentRepository;

    private int id;
    private long timestamp;
    private boolean active;
    private UserBasic user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<Comment> comments;
    private List<String> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserBasic getUser() {
        return user;
    }

    public void setUser(UserBasic user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void get(int id) {
        Post post = postsRepository.findById(id).get();
        if (isAllowed(post)) {
            this.id = post.getId();
            timestamp = post.getTime().getTime() / 1000;
            active = post.isActive();
            user = new UserBasic(post.getUser().getId(), post.getUser().getName());
            title = post.getTitle();
            text = post.getText();
            setVotes(id);
//            postsRepository.addViewCount(id);
            viewCount = post.getViewCount();
            comments = new ArrayList<>();
            setComments(id);
            tags = new ArrayList<>();
            for (Tag t : post.getTags()) {
                tags.add(t.getName());
            }
        }

    }

    private void setVotes(int postId) {
        likeCount = 0;
        dislikeCount = 0;
        voteRepository.findAll().forEach(postVote -> {
            if (postVote.getPostId().getId() == postId) {
                int value = postVote.getValue();
                if (value == 1) {
                    this.likeCount++;
                }
                if (value == -1) {
                    this.dislikeCount++;
                }
            }
        });
    }

    private void setComments(int postId) {
        commentRepository.findAll().forEach(postComment -> {
            if (postComment.getPostId().getId() == postId) {
                Comment comment = new Comment();
                comment.setId(postComment.getId());
                comment.setTimestamp(postComment.getTime().getTime() / 1000);
                comment.setText(postComment.getText());
                comment.setUser(new UserComment(postComment.getUserId().getId(),
                        postComment.getUserId().getName(),
                        postComment.getUserId().getPhoto()));
                comments.add(comment);
            }
        });
    }

    private boolean isAllowed(Post post) {
        long time = System.currentTimeMillis();
        long postTime = post.getTime().getTime();
        return post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime;
    }

}
