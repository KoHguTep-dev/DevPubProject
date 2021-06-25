package main.api.response;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PostResponse {

    private boolean result = true;
    private Map<String, String> errors = new HashMap<>();

    public void addTitleError() {
        errors.put("title", "Заголовок не установлен");
        result = false;
    }

    public void addTextError() {
        errors.put("text", "Текст публикации слишком короткий");
        result = false;
    }
}
