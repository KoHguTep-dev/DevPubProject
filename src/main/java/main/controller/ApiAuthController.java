package main.controller;

import main.api.response.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private AuthResponse authCheck;

    @GetMapping("/check")
    @ResponseBody
    private AuthResponse authCheck() {
        return authCheck;
    }
}
