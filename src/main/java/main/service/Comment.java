package main.service;

public class Comment {

    private int id;
    private long timestamp;
    private String text;
    private UserComment user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserComment getUser() {
        return user;
    }

    public void setUser(UserComment user) {
        this.user = user;
    }
}
