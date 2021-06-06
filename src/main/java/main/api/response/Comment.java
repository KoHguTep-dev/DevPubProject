package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.api.response.user.UserComment;

@Getter @Setter
public class Comment {

    private int id;
    private long timestamp;
    private String text;
    private UserComment user;
}
