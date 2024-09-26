package com.dam.dietasgpt;

public class Message {
    private String role; // "user" o "assistant"
    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // Getters y Setters
    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
}

