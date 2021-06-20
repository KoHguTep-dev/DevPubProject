package main.service;

import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
import main.model.dto.UserLogin;
import main.model.entities.CaptchaCode;
import main.model.entities.User;
import main.repository.CaptchaCodeRepository;
import main.repository.PostsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private CaptchaCodeRepository captchaCodeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private AuthenticationManager manager;

    public AuthResponse authCheckResponse() {
        AuthResponse authResponse = new AuthResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() == "anonymousUser") {
            return authResponse;
        }
        return getAuthResponse(authentication);
    }

    public CaptchaResponse captchaResponse() {
        CaptchaResponse captcha = new CaptchaResponse();
        String code = captcha.get();

        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(new Date());
        captchaCode.setCode(code);
        captchaCode.setSecretCode(captcha.getSecret());
        captchaCodeRepository.save(captchaCode);

        Date date = new Date(System.currentTimeMillis() - 3600_000);
        captchaCodeRepository.deleteOldCaptcha(date);
        return captcha;
    }

    public RegisterResponse registerResponse(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        if (userRepository.findByName(request.getName()) != null || request.getName().matches(".*[^А-яёA-z0-9_]+")) {
            response.addNameError();
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.addEmailError();
        }
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
            user.setPassword(user.calculatePassword(request.getPassword()));
            userRepository.save(user);
        }
        return response;
    }

    public AuthResponse authLoginResponse(LoginRequest loginRequest) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return getAuthResponse(authentication);
    }

    public AuthResponse authLogoutResponse(HttpServletRequest request, HttpServletResponse response) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return authResponse;
    }

    private AuthResponse getAuthResponse(Authentication authentication) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User modelUser = userRepository.findByEmail(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        authResponse.setUser(new UserLogin(modelUser));
        if (modelUser.isModerator()) {
            authResponse.getUser().setModerationCount(postsRepository.getCountForModeration());
        }
        return authResponse;
    }

}
