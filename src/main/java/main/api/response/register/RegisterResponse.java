package main.api.response.register;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RegisterResponse {

    private boolean result = true;
    private Map<String, String> errors = new HashMap<>();

    public void addEmailError() {
        errors.put("email", "Этот e-mail уже зарегистрирован");
        result = false;
    }

    public void addNameError() {
        errors.put("name", "Имя указано неверно");
        result = false;
    }

    public void addPassError() {
        errors.put("password", "Пароль короче 6-ти символов");
        result = false;
    }

    public void addCaptchaError() {
        errors.put("captcha", "Код с картинки введён неверно");
        result = false;
    }
}
