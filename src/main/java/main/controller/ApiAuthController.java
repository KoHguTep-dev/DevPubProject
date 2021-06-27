package main.controller;

import main.api.request.LoginRequest;
import main.api.request.PasswordRequest;
import main.api.request.PasswordRestoreRequest;
import main.api.request.RegisterRequest;
import main.api.response.*;
import main.service.AuthService;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller()
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private SettingsService settingsService;

    @GetMapping("/check")
    @ResponseBody
    private ResponseEntity<AuthResponse> authCheck() {
        return ResponseEntity.ok(authService.authCheckResponse());
    }

    @GetMapping("/captcha")
    @ResponseBody
    private ResponseEntity<CaptchaResponse> getCaptcha() {
        return ResponseEntity.ok(authService.captchaResponse());
    }

    @PostMapping("/register")
    @ResponseBody
    private ResponseEntity<RegisterResponse> addUser(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.registerResponse(request, settingsService.settingsResponse());
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @ResponseBody
    private ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authLoginResponse(loginRequest));
    }

    @GetMapping("/logout")
    @ResponseBody
    private ResponseEntity<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.authLogoutResponse(request, response));
    }

    @PostMapping("/restore")
    @ResponseBody
    private ResponseEntity<Boolean> passRestore(@RequestBody PasswordRestoreRequest email) {
        return ResponseEntity.ok(authService.restorePassword(email));
    }

    @PostMapping("/password")
    @ResponseBody
    private ResponseEntity<PasswordResponse> password(@RequestBody PasswordRequest request) {
        return ResponseEntity.ok(authService.replacePassword(request));
    }

}
