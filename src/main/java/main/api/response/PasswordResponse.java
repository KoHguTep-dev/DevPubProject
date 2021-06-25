package main.api.response;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PasswordResponse {

    private boolean result = true;
    private Map<String, String> errors = new HashMap<>();

    public void addCodeError() {
        result = false;
        errors.put("code", "Ссылка для восстановления пароля устарела.<a href=\"/auth/restore\">Запросить ссылку снова</a>");
    }
    public void addCaptchaError() {
        result = false;
        errors.put("captcha", "Код с картинки введён неверно");
    }
    public void addPasswordError() {
        result = false;
        errors.put("password", "Пароль короче 6-ти символов");
    }

}
