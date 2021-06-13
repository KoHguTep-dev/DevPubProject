package main.model.dto;

import lombok.Getter;
import lombok.Setter;
import main.model.entities.Post;

@Getter
public class PostPreview {

    @Setter
    private int id;
    @Setter
    private long timestamp;
    @Setter
    private UserBasic user;
    @Setter
    private String title;

    private String announce;
    @Setter
    private int likeCount;
    @Setter
    private int dislikeCount;
    @Setter
    private int commentCount;
    @Setter
    private int viewCount;

    public void setAnnounce(String announce) {
        if (announce.length() < 150) {
            this.announce = announce + "...";
        } else this.announce = announce.substring(0, 150) + "...";
    }

    public void setVotes(Post post) {
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
