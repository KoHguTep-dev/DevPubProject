package main.service;

import main.api.request.*;
import main.api.response.*;
import main.model.dto.UserLogin;
import main.model.entities.CaptchaCode;
import main.model.entities.User;
import main.repository.CaptchaCodeRepository;
import main.repository.PostsRepository;
import main.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private EmailService emailService;

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

    public RegisterResponse registerResponse(RegisterRequest request, SettingsResponse settings) {
        if (!settings.isMultiUserMode()) {
            return null;
        }
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
            request.toUser(user);
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

    public boolean authLogoutResponse(HttpServletRequest request, HttpServletResponse response) {
        boolean result = true;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return result;
    }

    public ProfileResponse editProfile(MultipartFile photo, String name, String email, String password, int removePhoto) {
        ProfileResponse response = new ProfileResponse();
        User user = getUser();
        if (user != null) {
            if (email != null && userRepository.findByEmail(email).isPresent() &&
                    !userRepository.findByEmail(email).get().equals(user)) {
                response.addEmailError();
            }
            if (name != null && name.matches(".*[^А-яёA-z0-9_]+")) {
                response.addNameError();
            }
            if (password != null && password.length() < 6) {
                response.addPasswordError();
            }
            if (photo != null && photo.getSize() > 5242880) {
                response.addPhotoError();
            }
            if (response.getErrors().isEmpty()) {
                if (name != null && !user.getName().equals(name)) {
                    user.setName(name);
                }
                if (email != null && !user.getEmail().equals(email)) {
                    user.setEmail(email);
                }
                if (password != null) {
                    user.setPassword(user.calculatePassword(password));
                }
                if (removePhoto == 0 && photo != null) {
                    user.setPhoto(ImageUtils.resizeUpload(photo));
                }
                userRepository.save(user);
            }
        }
        return response;
    }

    public ProfileResponse editProfile(ProfileRequest request) {
        ProfileResponse response = new ProfileResponse();
        User user = getUser();
        if (user != null) {
            String email = request.getEmail();
            String name = request.getName();
            String password = request.getPassword();
            String photo = request.getPhoto();
            int removePhoto = request.getRemovePhoto();

            if (email != null && userRepository.findByEmail(email).isPresent() &&
                    !userRepository.findByEmail(email).get().equals(user)) {
                response.addEmailError();
            }
            if (name != null && name.matches(".*[^А-яёA-z0-9_]+")) {
                response.addNameError();
            }
            if (password != null && password.length() < 6) {
                response.addPasswordError();
            }
            if (response.getErrors().isEmpty()) {
                if (name != null && !user.getName().equals(name)) {
                    user.setName(name);
                }
                if (email != null && !user.getEmail().equals(email)) {
                    user.setEmail(email);
                }
                if (password != null) {
                    user.setPassword(user.calculatePassword(password));
                }
                if (removePhoto == 1 && photo.equals("")) {
                    user.setPhoto(null);
                }
                userRepository.save(user);
            }
        }
        return response;
    }

    public boolean restorePassword(PasswordRestoreRequest mail) {
        boolean result = false;
        String email = mail.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            String hash = RandomStringUtils.randomAlphanumeric(64);
            User user = userRepository.findByEmail(email).get();
            user.setCode(hash);
            userRepository.save(user);
            String text = "/login/change-password/" + hash;
            String subject = "Ссылка для восстановления пароля";
            emailService.sendEmail(email, subject, text);
            result = true;
        }
        return result;
    }

    public PasswordResponse replacePassword(PasswordRequest request) {
        PasswordResponse response = new PasswordResponse();
        if (request.getPassword().length() < 6) {
            response.addPasswordError();
        }
        CaptchaCode captchaCode = captchaCodeRepository.findBySecretCode(request.getCaptchaSecret());
        if (!captchaCode.getCode().equals(request.getCaptcha())) {
            response.addCaptchaError();
        }
        if (userRepository.findByCode(request.getCode()).isEmpty()) {
            response.addCodeError();
        }
        if (response.getErrors().isEmpty()) {
            User user = userRepository.findByCode(request.getCode()).get();
            user.setPassword(user.calculatePassword(request.getPassword()));
            user.setCode(null);
            userRepository.save(user);
        }
        return response;
    }

    private AuthResponse getAuthResponse(Authentication authentication) {
        var user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        if (userRepository.findByEmail(user.getUsername()).isEmpty()) {
            return new AuthResponse();
        }
        User modelUser = userRepository.findByEmail(user.getUsername()).get();
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        authResponse.setUser(new UserLogin(modelUser));
        if (modelUser.isModerator()) {
            authResponse.getUser().setModerationCount(postsRepository.getCountForModeration());
        }
        return authResponse;
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            var securityUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            return userRepository.findByEmail(securityUser.getUsername()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

}
