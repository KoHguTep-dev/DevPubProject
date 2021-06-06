package main.api.response.post;

import lombok.Getter;
import lombok.Setter;
import main.api.response.user.UserBasic;
import main.model.PostComment;
import main.model.PostVote;

import java.util.List;

public class PostPreview {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private long timestamp;
    @Getter @Setter
    private UserBasic user;
    @Getter @Setter
    private String title;
    @Getter
    private String announce;
    @Getter @Setter
    private int likeCount;
    @Getter @Setter
    private int dislikeCount;
    @Getter
    private int commentCount;
    @Getter @Setter
    private int viewCount;

    public static List<PostComment> postComments;
    public static List<PostVote> postVotes;

    public void setAnnounce(String announce) {
        if (announce.length() < 150) {
            this.announce = announce + "...";
        } else this.announce = announce.substring(0, 150) + "...";
    }

    public void setCommentCount(int postId) {
        commentCount = 0;
        for (PostComment postComment : postComments) {
            if (postComment.getPostId().getId() == postId) {
                commentCount++;
            }
        }
    }

    public void setVotes(int postId) {
        likeCount = 0;
        dislikeCount = 0;
        for (PostVote postVote : postVotes) {
            if (postVote.getPostId().getId() == postId) {
                int value = postVote.getValue();
                if (value == 1) {
                    this.likeCount++;
                }
                if (value == -1) {
                    this.dislikeCount++;
                }
            }
        }
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
