package main.api.request;

import lombok.Getter;
import main.model.entities.Post;
import main.model.entities.Tag;
import main.model.entities.User;

import java.util.Date;
import java.util.List;

@Getter
public class PostRequest {

    private long timestamp;
    private int active;
    private String title;
    private String[] tags;
    private String text;

    public void toPost(Post post, User user, List<Tag> tags) {
        long current = new Date().getTime();
        if (timestamp < current) {
            timestamp = current;
        }
        if (active == 1) {
            post.setActive(true);
        }
        post.setUser(user);
        post.setTime(new Date(timestamp));
        post.setTitle(title);
        post.setText(text);
        post.setViewCount(0);
        post.setTags(tags);
    }
}
