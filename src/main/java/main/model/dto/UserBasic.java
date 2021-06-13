package main.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserBasic {

    private int id;
    private String name;

    public UserBasic(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
