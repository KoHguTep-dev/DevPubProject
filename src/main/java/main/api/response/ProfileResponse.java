package main.api.response;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProfileResponse {

    private boolean result = true;
    private Map<String, String> errors = new HashMap<>();

    public void addEmailError() {
        result = false;
        errors.put("email", "Этот e-mail уже зарегистрирован");
    }
    public void addPhotoError() {
        result = false;
        errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
    }
    public void addNameError() {
        result = false;
        errors.put("name", "Имя указано неверно");
    }
    public void addPasswordError() {
        result = false;
        errors.put("password", "Пароль короче 6-ти символов");
    }

}
