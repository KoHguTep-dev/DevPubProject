package main.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Comment {

    private int id;
    private long timestamp;
    private String text;
    private UserComment user;
}
