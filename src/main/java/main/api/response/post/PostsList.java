package main.api.response.post;

import lombok.Setter;
import main.api.response.user.UserBasic;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.Tag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostsList {

    @Setter
    private int count;
    @Setter
    private List<PostPreview> previews = new ArrayList<>();

    public static List<Post> posts;

    private PostPreview preview;

    public PostsList(PostPreview preview) {
        this.preview = preview;
    }

    public int getCount() {
        if (count == 0) {
            getPreviews();
        }
        return count;
    }

    public List<PostPreview> getPreviews() {
        if (previews.isEmpty()) {
            long time = System.currentTimeMillis();
            posts.forEach(post -> {
                long postTime = post.getTime().getTime();
                if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                    addPreview(post, postTime);
                }
            });
        }
        return previews;
    }

    public void recent() {
        previews.clear();
        count = 0;
        previews = getPreviews();
        previews.sort(Comparator.comparing(PostPreview::getTimestamp).reversed());
    }

    public void popular() {
        if (previews.isEmpty()) {
            previews = getPreviews();
        }
        previews.sort(Comparator.comparing(PostPreview::getCommentCount).reversed());
    }

    public void best() {
        if (previews.isEmpty()) {
            previews = getPreviews();
        }
        previews.sort(Comparator.comparing(PostPreview::getLikeCount).reversed());
    }

    public void early() {
        if (previews.isEmpty()) {
            previews = getPreviews();
        }
        previews.sort(Comparator.comparing(PostPreview::getTimestamp));
    }

    public void search(String query) {
        previews.clear();
        count = 0;
        long time = System.currentTimeMillis();
        posts.forEach(post -> {
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
        previews.clear();
        count = 0;
        long time = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        posts.forEach(post -> {
            long postTime = post.getTime().getTime();
            if (post.isActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED && time > postTime) {
                String postDate = format.format(post.getTime());
                if (date.equals(postDate)) {
                    addPreview(post, postTime);
                }
            }
        });
    }

    public void byTag(Tag tag) {
        previews.clear();
        count = 0;
        long time = System.currentTimeMillis();
        List<Post> list = tag.getPosts();
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
        previews.add(postPreview);
        count++;
    }

}
