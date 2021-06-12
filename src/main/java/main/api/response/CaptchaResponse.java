package main.api.response;

import com.github.cage.GCage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Base64;

@Getter @Setter
public class CaptchaResponse {

    private String secret;
    private String image;

    public String get() {
        GCage gCage = new GCage();
        String text = gCage.getTokenGenerator().next();
        byte[] bytes = gCage.draw(text);
        String encode = Base64.getEncoder().encodeToString(bytes);
        image = "data:image/png;base64, " + encode;
        secret = RandomStringUtils.randomAlphanumeric(32);
        return text;
    }

}
