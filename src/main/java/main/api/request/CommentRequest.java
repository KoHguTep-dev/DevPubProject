package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CommentRequest {

    @JsonProperty("parent_id")
    private Integer parentId;
    @JsonProperty("post_id")
    private int postId;
    private String text;
}
