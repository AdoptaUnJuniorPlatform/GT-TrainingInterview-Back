package com.example.demo.service;

import com.example.demo.Repository.QuestionRepository;
import com.example.demo.model.Questionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionaryService {

    private QuestionRepository questionRepository;
    private List<Long> idQuestionShowed = new ArrayList<>();

    @Autowired
    public QuestionaryService(QuestionRepository questionRepository) {
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
            questionary.setRole("diseñador ux/ui");
        }
        if (questionary.getExperience().isEmpty()) {
            questionary.setExperience("junior");
        }
        if (questionary.getTheme().isEmpty()) {
            questionary.setTheme("general");
        }
    }
}
