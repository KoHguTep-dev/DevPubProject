package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class VoteRequest {

    @JsonProperty("post_id")
    private int postId;
}
