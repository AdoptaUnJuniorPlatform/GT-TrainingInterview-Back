package com.example.demo.service;

import com.example.demo.Repository.QuestionRepository;
import com.example.demo.model.Question;
import org.apache.catalina.util.ToStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private final CohereService cohereService;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(CohereService cohereService, QuestionRepository questionRepository) {
        this.cohereService = cohereService;
        this.questionRepository = questionRepository;
    }

    public Question showQuestion(Question question) {
        Question cohereQuestion = cohereService.getIAResponse(question);
        question.setQuestion(cohereQuestion.getQuestion());
        question.setComment(cohereQuestion.getComment());

        questionRepository.save(question);

        return question;
    }
}
