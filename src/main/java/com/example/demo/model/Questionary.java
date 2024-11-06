package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Questionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String question;
    private String role;
    private String experience;
    private String theme;
    @Column(length = 3000)
    private String correctAnswer;
    @Column(length = 3000)
    private String wrongAnswerA;
    @Column(length = 3000)
    private String wrongAnswerB;
    @Column(length = 3000)
    private String correctFeedback;
    @Column(length = 3000)
    private String wrongFeedback;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getWrongAnswerA() {
        return wrongAnswerA;
    }

    public void setWrongAnswerA(String wrongAnswerA) {
        this.wrongAnswerA = wrongAnswerA;
    }

    public String getWrongAnswerB() {
        return wrongAnswerB;
    }

    public void setWrongAnswerB(String wrongAnswerB) {
        this.wrongAnswerB = wrongAnswerB;
    }

    public String getCorrectFeedback() {
        return correctFeedback;
    }

    public void setCorrectFeedback(String correctFeedback) {
        this.correctFeedback = correctFeedback;
    }

    public String getWrongFeedback() {
        return wrongFeedback;
    }

    public void setWrongFeedback(String wrongFeedback) {
        this.wrongFeedback = wrongFeedback;
    }
}
