package main.service;

import main.api.request.RegisterRequest;
import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.register.RegisterResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private CaptchaCodeRepository captchaCodeRepository;
    @Autowired
    private UserRepository userRepository;

    public AuthResponse authResponse() {
        return new AuthResponse();
    }

    public CaptchaResponse captchaResponse() {
        CaptchaResponse captcha = new CaptchaResponse();
        String code = captcha.getCaptcha();

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(new Date());
        captchaCode.setCode(code);
        captchaCode.setSecretCode(captcha.getSecret());
        captchaCodeRepository.save(captchaCode);

        long time = (System.currentTimeMillis()) - 3600_000;
        captchaCodeRepository.findAll().forEach(c -> {
            if (c.getTime().getTime() < time) {
                captchaCodeRepository.delete(c);
            }
        });
        return captcha;
    }

    public RegisterResponse registerResponse(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        String email = request.getEmail();
        String name = request.getName();

        List<User> users = userRepository.findAll();
        users.forEach(u -> {
            if (u.getEmail().equals(email)) {
                response.addEmailError();
            }
            if (u.getName().equals(name)) {
                response.addNameError();
            }
        });

        if (request.getPassword().length() < 6) {
            response.addPassError();
        }
        CaptchaCode captchaCode = captchaCodeRepository.findBySecretCode(request.getCaptchaSecret());
        if (!captchaCode.getCode().equals(request.getCaptcha())) {
            response.addCaptchaError();
        }
        if (response.getErrors().isEmpty()) {
            User user = new User();
            user.setModerator(false);
            user.setRegTime(new Date());
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            userRepository.save(user);
        }
        return response;
    }

}
