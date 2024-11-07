package com.example.demo.Controller;

import com.example.demo.model.Questionary;
import com.example.demo.service.QuestionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questionary")
public class QuestionaryController {

    private QuestionaryService questionaryService;

    @Autowired
    public QuestionaryController(QuestionaryService questionaryService) {
        this.questionaryService = questionaryService;
    }

    @PostMapping("/loadQuestions")
    public List<Questionary> loadQuestions(@RequestBody Questionary questionary) {
        return questionaryService.loadQuestionary(questionary);
    }

}
