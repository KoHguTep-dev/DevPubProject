package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.model.dto.PostPreview;
import main.model.dto.UserBasic;
import main.model.entities.Post;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostsResponse {

    private long count;
    private List<PostPreview> posts = new ArrayList<>();

    public PostsResponse(Page<Post> postPage) {
        if (postPage == null) {
            return;
        }
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
        preview.setAnnounce(preview.formatText(post.getText()));
        preview.setVotes(post);
        preview.setCommentCount(post.getPostComments().size());
        preview.setViewCount(post.getViewCount());

        return preview;
    }

}
