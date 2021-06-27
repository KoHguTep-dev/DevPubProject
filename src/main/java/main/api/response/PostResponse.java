package main.api.response;

import lombok.Getter;
import main.api.request.PostRequest;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PostResponse {

    private boolean result = true;
    private Map<String, String> errors = new HashMap<>();

    public PostResponse checkPost(PostRequest request) {
        PostResponse postResponse = new PostResponse();
        if (request.getTitle().length() < 3) {
            postResponse.addTitleError();
        }
        if (request.getText().length() < 50) {
            postResponse.addTextError();
        }
        return postResponse;
    }

    private void addTitleError() {
        errors.put("title", "Заголовок не установлен");
        result = false;
    }

    private void addTextError() {
        errors.put("text", "Текст публикации слишком короткий");
        result = false;
    }

}
