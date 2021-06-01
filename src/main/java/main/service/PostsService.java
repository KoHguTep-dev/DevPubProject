package main.service;

import main.model.ModerationStatus;
import main.model.Post;
import main.repository.PostsRepository;
import main.repository.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class PostsService {

    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostPreview preview;
    @Autowired
    private TagsRepository tagsRepository;

    private int count;
    private List<PostPreview> posts = new ArrayList<>();

    public int getCount() {
        if (count == 0) {
            getPosts();
        }
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostPreview> getPosts() {
        if (posts.isEmpty()) {
            long time = System.currentTimeMillis();
            postsRepository.findAll().forEach(post -> {
                long postTime = post.getTime().getTime();
                if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                    addPreview(post, postTime);
                }
            });
        }
        return posts;
    }

    public void setPosts(List<PostPreview> posts) {
        this.posts = posts;
    }

    public void recent() {
        posts.clear();
        count = 0;
        posts = getPosts();
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

    public void search(String query) {
        posts.clear();
        count = 0;
        long time = System.currentTimeMillis();
        postsRepository.findAll().forEach(post -> {
            long postTime = post.getTime().getTime();
            if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                String text = post.getTitle() + post.getText();
                if (text.contains(query)) {
                    addPreview(post, postTime);
                }
            }
        });
    }

    public void byDate(String date) {
        posts.clear();
        count = 0;
        long time = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        postsRepository.findAll().forEach(post -> {
            long postTime = post.getTime().getTime();
            if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                String postDate = format.format(post.getTime());
                if (date.equals(postDate)) {
                    addPreview(post, postTime);
                }
            }
        });
    }

    public void byTag(String tag) {
        posts.clear();
        count = 0;
        long time = System.currentTimeMillis();
        List<Post> list = tagsRepository.findByName(tag).getPosts();
        for (Post post : list) {
            long postTime = post.getTime().getTime();
            if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                addPreview(post, postTime);
            }
        }
    }

    private void addPreview(Post post, long time) {
        preview.setId(post.getId());
        preview.setTimestamp(time / 1000);
        preview.setUser(new UserBasic(post.getUser().getId(), post.getUser().getName()));
        preview.setTitle(post.getTitle());
        preview.setAnnounce(post.getText());
        preview.setVotes(post.getId());
        preview.setCommentCount(post.getId());
        preview.setViewCount(post.getViewCount());
        PostPreview postPreview = new PostPreview();
        postPreview.getCopy(preview);
        posts.add(postPreview);
        count++;
    }

}
