package main.api.response;

import main.service.PostsService;
import main.service.PostPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostsResponse {

    @Autowired
    private PostsService postsService;

    private int count;
    private List<PostPreview> posts = new ArrayList<>();

    public List<PostPreview> getPosts() {
        return posts;
    }

    public void setPosts(List<PostPreview> posts) {
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addPosts(int offset, int limit, String mode) {
        switch (mode) {
            case "popular":
                postsService.popular();
                break;

            case "best":
                postsService.best();
                break;

            case "early":
                postsService.early();
                break;

            default:
                postsService.recent();
        }
        toPage(offset, limit);
    }

    public void search(int offset, int limit, String query) {
        query.trim();
        if (query.length() == 0) {
            postsService.recent();
        } else postsService.search(query);
        toPage(offset, limit);
    }

    public void byDate(int offset, int limit, String date) {
        postsService.byDate(date);
        toPage(offset, limit);
    }

    public void byTag(int offset, int limit, String tag) {
        postsService.byTag(tag);
        toPage(offset, limit);
    }

    private void toPage(int offset, int limit) {
        posts.clear();
        count = postsService.getCount();
        int off = offset;
        for (int i = 0; i <= limit; i++) {
            posts.add(i, postsService.getPosts().get(off++));
            if (count - (off + 1) < 0) {
                break;
            }
        }
    }


}
