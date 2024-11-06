package com.example.demo.service;

import com.example.demo.Repository.QuestionaryRepository;
import com.example.demo.model.Questionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionaryService {

    private QuestionaryRepository questionRepository;
    private List<Long> idQuestionShowed = new ArrayList<>();

    @Autowired
    public QuestionaryService(QuestionaryRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Questionary> findQuestionsByCriteria(String role, String experience, String theme) {
        return questionRepository.findByRoleAndExperienceAndTheme(role, experience, theme);
    }

    public List<Questionary> loadQuestionary(Questionary questionary) {

        checkParams(questionary);

        List<Questionary> questionsByCriteria= findQuestionsByCriteria(questionary.getRole(), questionary.getExperience(), questionary.getTheme());

        // Mezclar la lista de preguntas
        Collections.shuffle(questionsByCriteria);

        List<Questionary> selectedQuestions = new ArrayList<>();

        for (int i = 0; i < Math.min(5, questionsByCriteria.size()); i++) {
            selectedQuestions.add(questionsByCriteria.get(i));
        }

        return selectedQuestions;

    }

    public void checkParams(Questionary questionary) {
        if (questionary.getRole().isEmpty()) {
            questionary.setRole("design");
        }
        if (questionary.getExperience().isEmpty()) {
            questionary.setExperience("junior");
        }
        if (questionary.getTheme().isEmpty()) {
            questionary.setTheme("general");
        }
    }
}
