package com.example.demo.service;

import com.example.demo.Repository.QuestionRepository;
import com.example.demo.model.Question;
import com.example.demo.model.Questionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Value("${default.role}")
    private String defaultRole;

    @Value("${default.experience}")
    private String defaultExperience;

    @Value("${default.theme}")
    private String defaultTheme;

    private final CohereService cohereService;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(CohereService cohereService, QuestionRepository questionRepository) {
        this.cohereService = cohereService;
        this.questionRepository = questionRepository;
    }

    public Questionary generateQuestion(Questionary questionary) {
        String role = questionary.getRole();
        String experience = questionary.getExperience();
        String theme = questionary.getTheme();

        avoidEmptyFields(questionary);

        return cohereService.generateQuestionary(questionary);
    }

//    public Question showQuestion(Question question) {
//        Question cohereQuestion = cohereService.getIAResponse(question);
//        question.setQuestion(cohereQuestion.getQuestion());
//        question.setComment(cohereQuestion.getComment());
//
//        questionRepository.save(question);
//
//        return question;
//    }

    public String getIAFeedback(String userResponse, Question question) {
        return cohereService.getIAFeedback(userResponse, question);
    }

    public Questionary avoidEmptyFields(Questionary qestionary) {
        if(qestionary.getRole().isEmpty()) {
            qestionary.setRole(defaultRole);
        }
        if(qestionary.getExperience().isEmpty()) {
            qestionary.setExperience(defaultExperience);
        }
        if(qestionary.getTheme().isEmpty()) {
            qestionary.setTheme(defaultTheme);
        }
        return qestionary;
    }
}
