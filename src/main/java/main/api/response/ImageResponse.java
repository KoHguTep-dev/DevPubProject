package main.api.response;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ImageResponse {

    private boolean result;
    private Map<String, String> errors = new HashMap<>();

    public ImageResponse() {
        result = false;
        errors.put("image", "Размер файла превышает допустимый размер, или файл не формата изображения jpg, png");
    }
}
