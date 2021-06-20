package main.controller;

import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.api.response.AuthResponse;
import main.api.response.RegisterResponse;
import main.service.AuthService;
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
        return ResponseEntity.ok(authService.registerResponse(request));
    }

    @PostMapping("/login")
    @ResponseBody
    private ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authLoginResponse(loginRequest));
    }

    @GetMapping("/logout")
    @ResponseBody
    private ResponseEntity<AuthResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.authLogoutResponse(request, response));
    }
}
