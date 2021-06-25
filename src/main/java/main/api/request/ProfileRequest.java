package main.api.request;

import lombok.Getter;

@Getter
public class ProfileRequest {

    private String name;
    private String email;
    private String password;
    private int removePhoto;
    private String photo;
}
