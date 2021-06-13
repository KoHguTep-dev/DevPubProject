package main.service;

import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
import main.model.dto.UserLogin;
import main.model.entities.CaptchaCode;
import main.model.entities.User;
import main.model.enums.ModerationStatus;
import main.repository.CaptchaCodeRepository;
import main.repository.PostsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

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

    public AuthResponse authCheckResponse(SessionConfig sessionConfig) {
        AuthResponse authResponse = new AuthResponse();

        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        if (sessionConfig.getSessions().containsKey(sessionId)) {
            User user = userRepository.findById(sessionConfig.getSessions().get(sessionId)).get();
            authResponse.setResult(true);
            authResponse.setUser(new UserLogin(user));
        }
        return authResponse;
    }

    public CaptchaResponse captchaResponse() {
        CaptchaResponse captcha = new CaptchaResponse();
        String code = captcha.get();

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

        if (userRepository.findByName(request.getName()) != null || request.getName().matches("\\W*")) {
            response.addNameError();
        }
        if (userRepository.findByEmail(request.getEmail()) != null) {
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

    public AuthResponse authLoginResponse(LoginRequest loginRequest, SessionConfig sessionConfig) {
        AuthResponse authResponse = new AuthResponse();
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            return authResponse;
        }
        if (authResponse.checkPass(user, loginRequest.getPassword())) {
            if (user.isModerator()) {
                authResponse.getUser().setModerationCount(
                        (int) postsRepository.findAll().stream().filter
                                (post -> post.getModerationStatus() == ModerationStatus.NEW).count());
            }

//            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                    loginRequest.getEmail(), loginRequest.getPassword());
//            Authentication authentication = manager.authenticate(auth);
//            SecurityContext securityContext = SecurityContextHolder.getContext();
//            securityContext.setAuthentication(authentication);

            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            sessionConfig.getSessions().put(sessionId, user.getId());
        }
        return authResponse;
    }

    public AuthResponse authLogoutResponse(SessionConfig sessionConfig) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);

        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        sessionConfig.getSessions().remove(sessionId);
        return authResponse;
    }

}
