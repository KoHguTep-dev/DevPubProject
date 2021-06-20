package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.model.dto.PostPreview;
import main.model.dto.PostsList;
import main.model.dto.UserBasic;
import main.model.entities.Post;
import main.model.entities.Tag;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostResponse {

    private long count;
    private List<PostPreview> posts = new ArrayList<>();

//    private PostsList postsList;

//    public PostResponse(PostsList postsList) {
//        this.postsList = postsList;
//    }

    public PostResponse(Page<Post> postPage) {
        count = postPage.getTotalElements();
        for (Post p : postPage) {
            posts.add(postToPreview(p));
        }
    }

    private PostPreview postToPreview(Post post) {
        PostPreview preview = new PostPreview();

        preview.setId(post.getId());
        preview.setTimestamp(post.getTime().getTime() / 1000);
        preview.setUser(new UserBasic(post.getUser().getId(), post.getUser().getName()));
        preview.setTitle(post.getTitle());
        preview.setAnnounce(post.getText());
        preview.setVotes(post);
        preview.setCommentCount(post.getPostComments().size());
        preview.setViewCount(post.getViewCount());

        return preview;
    }

    /*
    public void addPosts(int offset, int limit, String mode) {
        switch (mode) {
            case "popular":
                postsList.popular();
                break;
            case "best":
                postsList.best();
                break;
            case "early":
                postsList.early();
                break;
            default:
                postsList.recent();
        }
        toPage(offset, limit);
    }

    public void search(int offset, int limit, String query) {
        String s = query.trim();
        if (s.length() == 0) {
            postsList.recent();
        } else postsList.search(query);
        if (!postsList.getPreviews().isEmpty()) {
            toPage(offset, limit);
        }
    }

    public void byDate(int offset, int limit, String date) {
        postsList.byDate(date);
        if (!postsList.getPreviews().isEmpty()) {
            toPage(offset, limit);
        }
    }

    public void byTag(int offset, int limit, Tag tag) {
        postsList.byTag(tag);
        if (!postsList.getPreviews().isEmpty()) {
            toPage(offset, limit);
        }
    }

    public void getMy(int offset, int limit, String status) {
        postsList.myPosts(status);
        if (!postsList.getPreviews().isEmpty()) {
            toPage(offset, limit);
        }
    }

    private void toPage(int offset, int limit) {
        posts.clear();
        count = postsList.getCount();
        int off = offset;
        for (int i = 0; i < limit; i++) {
            posts.add(i, postsList.getPreviews().get(off++));
            if (count - (off + 1) < 0) {
                break;
            }
        }
    }

     */

}
