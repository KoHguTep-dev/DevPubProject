package main.api.response;

import main.model.ModerationStatus;
import main.repository.PostsRepository;
import main.service.PostPreview;
import main.service.UserBasic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class PostsResponse {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostPreview preview;

    private int count;
    private List<PostPreview> posts = new ArrayList<>();

    public int getCount() {
        if (count == 0) {
            postsRepository.findAll().forEach(post -> {
                if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    count++;
                }
            });
        }
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostPreview> getPosts() {
        if (posts.isEmpty()) {
            postsRepository.findAll().forEach(post -> {
                if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    preview.setId(post.getId());
                    preview.setTimestamp(post.getTime().getTime() / 1000);
                    preview.setUser(new UserBasic(post.getUser().getId(), post.getUser().getName()));
                    preview.setTitle(post.getTitle());
                    preview.setAnnounce(post.getText());
                    preview.setVotes(post.getId());
                    preview.setCommentCount(post.getId());
                    preview.setViewCount(post.getViewCount());

                    PostPreview postPreview = new PostPreview();
                    postPreview.getCopy(preview);

                    posts.add(postPreview);
                }
            });
        }
        return posts;
    }

    public void setPosts(List<PostPreview> posts) {
        this.posts = posts;
    }

    public void recent() {
        if (posts.isEmpty()) {
            posts = getPosts();
        }
        posts.sort(Comparator.comparing(PostPreview::getTimestamp).reversed());
    }

    public void popular() {
        if (posts.isEmpty()) {
            posts = getPosts();
        }
        posts.sort(Comparator.comparing(PostPreview::getCommentCount).reversed());
    }

    public void best() {
        if (posts.isEmpty()) {
            posts = getPosts();
        }
        posts.sort(Comparator.comparing(PostPreview::getLikeCount).reversed());
    }

    public void early() {
        if (posts.isEmpty()) {
            posts = getPosts();
        }
        posts.sort(Comparator.comparing(PostPreview::getTimestamp));
    }
}
