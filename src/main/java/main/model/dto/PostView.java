package main.model.dto;

import lombok.Getter;
import main.model.enums.ModerationStatus;
import main.model.entities.Post;
import main.model.entities.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostView {

    private int id;
    private long timestamp;
    private boolean active;
    private UserBasic user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<Comment> comments = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    public void get(Post post) {
        this.id = post.getId();
        timestamp = post.getTime().getTime() / 1000;
        active = post.isActive();
        user = new UserBasic(post.getUser().getId(), post.getUser().getName());
        title = post.getTitle();
        text = post.getText();
        setVotes(post);
        viewCount = post.getViewCount();
        setComments(post);
        post.getTags().forEach(tag -> tags.add(tag.getName()));
    }

    private void setVotes(Post post) {
        likeCount = 0;
        dislikeCount = 0;
        post.getPostVotes().forEach(postVote -> {
            int value = postVote.getValue();
            if (value == 1) {
                this.likeCount++;
            }
            if (value == -1) {
                this.dislikeCount++;
            }
        });
    }

    private void setComments(Post post) {
        post.getPostComments().forEach(postComment -> {
            Comment comment = new Comment();
            comment.setId(postComment.getId());
            comment.setTimestamp(postComment.getTime().getTime() / 1000);
            comment.setText(postComment.getText());
            User user = postComment.getUserId();
            comment.setUser(new UserComment(user.getId(), user.getName(), user.getPhoto()));
            comments.add(comment);
        });
    }

    public boolean isAllowed(Post post) {
        long time = System.currentTimeMillis();
        long postTime = post.getTime().getTime();
        return post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime;
    }

    public void addViewCount() {
        viewCount = viewCount + 1;
    }
}
