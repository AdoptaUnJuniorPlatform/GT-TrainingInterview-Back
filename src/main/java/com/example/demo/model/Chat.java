package com.example.demo.model;

public class Chat {
    private String text;
    private String rank;
    private String experience;
    private String userResponse;
    private String prompt;

    public Chat() {
    }

    public Chat(String text, String level, String experience) {
        this.text = text;
        this.rank = level;
        this.experience = experience;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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
