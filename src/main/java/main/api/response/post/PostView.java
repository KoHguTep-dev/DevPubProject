package main.api.response.post;

import lombok.Getter;
import lombok.Setter;
import main.model.*;
import main.api.response.Comment;
import main.api.response.user.UserBasic;
import main.api.response.user.UserComment;

import java.util.ArrayList;
import java.util.List;

public class PostView {

    @Getter
    private int id;
    @Getter
    private long timestamp;
    @Getter
    private boolean active;
    @Getter
    private UserBasic user;
    @Getter
    private String title;
    @Getter
    private String text;
    @Getter
    private int likeCount;
    @Getter
    private int dislikeCount;
    @Getter
    private int viewCount;
    @Getter
    private List<Comment> comments;
    @Getter @Setter
    private List<String> tags = new ArrayList<>();

    public static List<PostVote> postVotes;
    public static List<PostComment> postComments;

    public void get(int id, Post post) {
        if (isAllowed(post)) {
            this.id = post.getId();
            timestamp = post.getTime().getTime() / 1000;
            active = post.isActive();
            user = new UserBasic(post.getUser().getId(), post.getUser().getName());
            title = post.getTitle();
            text = post.getText();
            setVotes(id);
            viewCount = post.getViewCount();
            comments = new ArrayList<>();
            setComments(id);
            post.getTags().forEach(tag -> tags.add(tag.getName()));
        }
    }

    private void setVotes(int postId) {
        likeCount = 0;
        dislikeCount = 0;
        postVotes.forEach(postVote -> {
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
        postComments.forEach(postComment -> {
            if (postComment.getPostId().getId() == postId) {
                Comment comment = new Comment();
                comment.setId(postComment.getId());
                comment.setTimestamp(postComment.getTime().getTime() / 1000);
                comment.setText(postComment.getText());
                User user = postComment.getUserId();
                comment.setUser(new UserComment(user.getId(), user.getName(), user.getPhoto()));
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
