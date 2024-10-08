package com.example.demo.model;

public class Chat {
    private String role;
    private String experience;
    private String userResponse;
    private String prompt;

    public Chat() {
    }

    public Chat(String prompt, String level, String experience) {
        this.prompt = prompt;
        this.role = level;
        this.experience = experience;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
