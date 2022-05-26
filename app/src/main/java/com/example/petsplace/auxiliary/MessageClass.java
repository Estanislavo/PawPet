package com.example.petsplace.auxiliary;

public class MessageClass {
    String username;
    String message;

    public MessageClass(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public MessageClass(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
