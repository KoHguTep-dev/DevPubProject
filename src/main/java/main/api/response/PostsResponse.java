package main.api.response;


import main.service.PostPreview;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostsResponse {

    private int count;
    private List<PostPreview> posts = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostPreview> getPosts() {
        return posts;
    }

    public void setPosts(List<PostPreview> posts) {
        this.posts = posts;
    }
}
