package main.model.dto;

import lombok.Getter;
import lombok.Setter;
import main.model.entities.Post;
import org.jsoup.Jsoup;

@Getter @Setter
public class PostPreview {

    private int id;
    private long timestamp;
    private UserBasic user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

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

    public String formatText(String text) {
        String announce = Jsoup.parse(text).text();
        if (announce.length() < 150) {
            announce = announce + "...";
        } else announce = announce.substring(0, 150) + "...";
        return announce;
    }

}
