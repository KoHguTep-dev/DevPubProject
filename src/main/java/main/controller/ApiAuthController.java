package main.controller;

import main.api.response.AuthResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private AuthResponse authCheck;

    @Autowired
    private CaptchaResponse captchaResponse;

    @Autowired
    private RegisterResponse registerResponse;

    @GetMapping("/check")
    @ResponseBody
    private AuthResponse authCheck() {
        return authCheck;
    }

    @GetMapping("/captcha")
    @ResponseBody
    private CaptchaResponse getCaptcha() {
        return captchaResponse;
    }

    @PostMapping("/register")
    @ResponseBody
    private RegisterResponse addUser() {
        return registerResponse;
    }
}
