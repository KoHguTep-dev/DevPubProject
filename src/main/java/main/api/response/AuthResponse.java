package main.api.response;

import main.model.User;

public class AuthResponse {

    private boolean result;

    private User user;

    public boolean isResult() {
        return result;
    }

    public void setResult() {
        if (user != null) {
            this.result = true;
        }
    }

}
