package main.service;

import main.repository.PostCommentRepository;
import main.repository.PostVotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostPreview {

    @Autowired
    private PostVotesRepository voteRepository;

    @Autowired
    private PostCommentRepository commentRepository;

    private int id;
    private long timestamp;
    private UserBasic user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

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

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        if (announce.length() < 150) {
            this.announce = announce + "...";
        } else this.announce = announce.substring(0, 150) + "...";
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int postId) {
        commentCount = 0;
        commentRepository.findAll().forEach(postComment -> {
            if (postComment.getPostId().getId() == postId) {
                commentCount++;
            }
        });
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setVotes(int postId) {
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

    public void getCopy(PostPreview preview) {
        this.id = preview.id;
        this.timestamp = preview.timestamp;
        this.user = preview.user;
        this.title = preview.title;
        this.announce = preview.announce;
        this.likeCount = preview.likeCount;
        this.dislikeCount = preview.dislikeCount;
        this.commentCount = preview.commentCount;
        this.viewCount = preview.viewCount;
    }

}
