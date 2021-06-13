package main.model.dto;

import lombok.Getter;
import lombok.Setter;
import main.model.entities.User;

@Getter @Setter
public class UserLogin {

    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

    public UserLogin(User user) {
        id = user.getId();
        name = user.getName();
        photo = user.getPhoto();
        email = user.getEmail();
        if (user.isModerator()) {
            moderation = true;
            settings = true;
        }
    }

}
