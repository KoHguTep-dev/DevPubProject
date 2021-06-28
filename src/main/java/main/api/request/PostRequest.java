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
        long current = new Date().getTime() / 1000;
        if (timestamp < current) {
            timestamp = current;
        }
        post.setActive(active == 1);
        if (user.isModerator()) {
            post.setModeratorId(user.getId());
        }
        if (post.getUser() == null) {
            post.setUser(user);
        }
        post.setTime(new Date(timestamp * 1000));
        post.setTitle(title);
        post.setText(text);
        post.setViewCount(0);
        post.setTags(tags);
    }
}
