package main.api.response;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterResponse {

    private boolean result;
    private List<String> errors;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
