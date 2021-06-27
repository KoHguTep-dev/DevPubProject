package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import main.model.entities.User;

import java.util.Date;

@Getter
public class RegisterRequest {

    @JsonProperty("e_mail")
    private String email;
    private String password;
    private String name;
    private String captcha;
    @JsonProperty("captcha_secret")
    private String captchaSecret;

    public void toUser(User user) {
        user.setModerator(false);
        user.setRegTime(new Date());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(user.calculatePassword(password));
    }

}
