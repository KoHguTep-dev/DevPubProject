package main.controller;

import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.api.response.AuthResponse;
import main.api.response.RegisterResponse;
import main.service.SessionConfig;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    SessionConfig sessionConfig;

    @GetMapping("/check")
    @ResponseBody
    private ResponseEntity<AuthResponse> authCheck() {
        return ResponseEntity.ok(authService.authCheckResponse(sessionConfig));
    }

    @GetMapping("/captcha")
    @ResponseBody
    private ResponseEntity<CaptchaResponse> getCaptcha() {
        return ResponseEntity.ok(authService.captchaResponse());
    }

    @PostMapping("/register")
    @ResponseBody
    private ResponseEntity<RegisterResponse> addUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerResponse(request));
    }

    @PostMapping("/login")
    @ResponseBody
    private ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authLoginResponse(loginRequest, sessionConfig));
    }

    @GetMapping("/logout")
    @ResponseBody
    private ResponseEntity<AuthResponse> logout() {
        return ResponseEntity.ok(authService.authLogoutResponse(sessionConfig));
    }
}
