package com.example.demo.service;

import com.cohere.api.Cohere;
import com.cohere.api.requests.ChatRequest;
import com.cohere.api.types.NonStreamedChatResponse;
import com.example.demo.Utils.PromptLoader;
import com.example.demo.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CohereService {
    private final Cohere cohere;
    private final PromptLoader promptLoader;

    @Value("${client}")
    private String client;
    @Value("${final.prompt}")
    private String finalPrompt;

    @Autowired
    public CohereService(@Value("${cohere.api.token}") String apiKey, PromptLoader promptLoader) throws IOException {
        this.cohere = Cohere.builder().token(apiKey).clientName(client).build();
        this.promptLoader = promptLoader;
    }

    public Question getIAResponse(Question question) {
        try {
            String prompt = promptLoader.getRandomPrompts();
            String formattedPrompt = prompt
                    .replace("{role}", question.getRole())
                    .replace("{experience}", question.getExperience())
                    .replace("{tematica}", question.getTheme());

            NonStreamedChatResponse response = cohere.chat(
                    ChatRequest.builder()
                            .message(formattedPrompt + finalPrompt)
                            .chatHistory(
                                    List.of(
                                    )
                            )
                            .build()
            );
            Question questionObj = splitResponse(question, response.getText());
            return questionObj;

        } catch (Exception e) {
            // TODO - Recoger pregunta y comentario de la base de datos
            return question;
        }
    }

    private Question splitResponse(Question questionObj, String response) {
        // Buscar el índice del primer signo de apertura '¿'
        int firstQuestionMarkIndex = response.indexOf("¿");
        // Buscar el índice del primer signo de cierre '?' después del signo '¿'
        int lastQuestionMarkIndex = response.indexOf("?", firstQuestionMarkIndex);

        if (firstQuestionMarkIndex != -1 && lastQuestionMarkIndex != -1) {
            String question = response.substring(firstQuestionMarkIndex, lastQuestionMarkIndex + 1).trim();
            String comment = response.substring(lastQuestionMarkIndex + 1).trim();

            questionObj.setQuestion(question);
            questionObj.setComment(comment);
        }
        return questionObj;
    }
}
