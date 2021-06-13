package main.api.response;

import lombok.Getter;
import lombok.Setter;
import main.model.dto.UserLogin;
import main.model.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter @Setter
public class AuthResponse {

    private boolean result = false;
    private UserLogin user;

    public boolean checkPass(User user, String pass) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(pass, user.getPassword())) {
            result = true;
            this.user = new UserLogin(user);
            return true;
        }
        return false;
    }

}
