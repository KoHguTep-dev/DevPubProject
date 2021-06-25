package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.api.request.CommentRequest;
import main.model.entities.Post;
import main.model.entities.PostComment;
import main.model.entities.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CommentResponse {

    private boolean result = true;
    private Map<String, String> errors = new HashMap<>();
    @Setter
    private int id;

    public void addError() {
        result = false;
        errors.put("text", "Текст комментария не задан или слишком короткий");
    }

    public PostComment toPostComment(CommentRequest request, Post post, User user) {
        PostComment comment = new PostComment();
        if (request.getParentId() != null) {
            comment.setParentId(request.getParentId());
        }
        comment.setPostId(post);
        comment.setUserId(user);
        comment.setTime(new Date());
        comment.setText(request.getText());
        return comment;
    }

}
