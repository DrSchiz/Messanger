package com.example.messenger;

public class User {
    private String email;
    private String key;
    private String firstname;
    private String name;
    private String nickname;

    private String image;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User(String email, String key, String firstname, String name, String nickname, String image) {
        this.email = email;
        this.key = key;
        this.firstname = firstname;
        this.name = name;
        this.nickname = nickname;
        this.image = image;
    }

    public User() {

    }
}
