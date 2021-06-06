package main.api.response.user;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserComment {

    private int id;
    private String name;
    private String photo;

    public UserComment(int id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

}
