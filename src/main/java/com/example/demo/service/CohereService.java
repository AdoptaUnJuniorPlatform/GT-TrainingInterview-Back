package com.example.demo.service;

import com.cohere.api.Cohere;
import com.cohere.api.requests.ChatRequest;
import com.cohere.api.types.ChatMessage;
import com.cohere.api.types.Message;
import com.cohere.api.types.NonStreamedChatResponse;
import com.example.demo.Utils.FileLoader;
import com.example.demo.model.Question;
import com.example.demo.model.Questionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CohereService {
    private final Cohere cohere;
    private final FileLoader fileLoader;

    private List<String> uxuiDesignChatHistory;
    private List<String> frontendChatHistory;
    private List<String> backendChatHistory;

    private List<String> chatHistory;

    @Value("${client}")
    private String client;
    @Value("${final.prompt}")
    private String finalPrompt;

    @Autowired
    public CohereService(@Value("${cohere.api.token}") String apiKey, FileLoader promptLoader, FileLoader fileLoader, List<String> uxuiDesignChatHistory, List<String> frontendChatHistory, List<String> backendChatHistory) throws IOException {
        this.cohere = Cohere.builder().token(apiKey).clientName(client).build();
        this.fileLoader = fileLoader;
        this.uxuiDesignChatHistory = uxuiDesignChatHistory;
        this.frontendChatHistory = frontendChatHistory;
        this.backendChatHistory = backendChatHistory;
    }

    public Questionary generateQuestionary(Questionary questionary) {
        try {
            String prompt = generatePrompt(fileLoader, questionary, "src/main/resources/prompts/questionaryPrompts.json", "prompts");
            chatHistory = generateChatHistoryList(fileLoader, questionary);

            NonStreamedChatResponse response = cohere.chat(
                    ChatRequest.builder()
                            .message(prompt + finalPrompt)
                            .chatHistory(
                                    List.of(
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 5)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 4)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 3)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 2)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 1)).build())
                                    )
                            )
                            .build()
            );

            Questionary questionaryObj = splitResponse(questionary, response.getText());

            return questionaryObj;

        } catch (Exception e) {
            // TODO - Recoger pregunta y comentario de la base de datos
            return questionary;
        }
    }

    // New Proyect
    private Questionary splitResponse(Questionary questinaryObj, String response) {
        // Buscar el índice del primer signo de apertura '¿'
        int firstQuestionMarkIndex = response.indexOf("¿");
        // Buscar el índice del primer signo de cierre '?' después del signo '¿'
        int lastQuestionMarkIndex = response.indexOf("?", firstQuestionMarkIndex);

        if (firstQuestionMarkIndex != -1 && lastQuestionMarkIndex != -1) {
            // Extraer la pregunta entre ¿ y ?
            String question = response.substring(firstQuestionMarkIndex, lastQuestionMarkIndex + 1).trim();
            questinaryObj.setQuestion(question);

            // Extraer las respuestas a partir del texto que sigue después del signo '?'
            String remainingText = response.substring(lastQuestionMarkIndex + 1).trim();

            // Buscar cada respuesta usando los prefijos "1.-", "2.-", y "3.-"
            String[] responses = new String[3];
            responses[0] = extractResponse(remainingText, "1)", "2)");
            responses[1] = extractResponse(remainingText, "2)", "3)");
            responses[2] = extractResponse(remainingText, "3)", "4)");

            // Asignar las respuestas al objeto Question
            questinaryObj.setCorrectAnswer(responses[0]);
            questinaryObj.setWrongAnswerA(responses[1]);
            questinaryObj.setWrongAnswerB(responses[2]);

            System.out.println("=================== PREGUNTA ===========================");
            System.out.println(questinaryObj.getQuestion());
            System.out.println("=================== A ===========================");
            System.out.println("A -> " + questinaryObj.getCorrectAnswer());
            System.out.println("=================== B ===========================");
            System.out.println("B -> " + questinaryObj.getWrongAnswerA());
            System.out.println("=================== C ===========================");
            System.out.println("C -> " + questinaryObj.getWrongAnswerB());
        }
        return questinaryObj;
    }

    // Método auxiliar para extraer la respuesta entre dos prefijos específicos
    private String extractResponse(String text, String startDelimiter, String endDelimiter) {
        int startIndex = text.indexOf(startDelimiter);
        int endIndex = endDelimiter != null ? text.indexOf(endDelimiter) : text.length();

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return text.substring(startIndex + startDelimiter.length(), endIndex).trim();
        }
        return "";
    }


//    public Question getIAResponse(Question question) {
//
//        try {
//            String prompt = generatePrompt(fileLoader, question, "src/main/resources/prompts/prompts.json", "prompts");
//            chatHistory = generateChatHistoryList(fileLoader, question);
//
//            NonStreamedChatResponse response = cohere.chat(
//                    ChatRequest.builder()
//                            .message(prompt + finalPrompt)
//                            .chatHistory(
//                                    List.of(
//                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 5)).build()),
//                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 4)).build()),
//                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 3)).build()),
//                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 2)).build()),
//                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 1)).build())
//                                    )
//                            )
//                            .build()
//            );
//            Question questionObj = splitResponse(question, response.getText());
//            fillChatHistory(questionObj);
//
//            return questionObj;
//
//        } catch (Exception e) {
//            // TODO - Recoger pregunta y comentario de la base de datos
//            return question;
//        }
//    }

    public String getIAFeedback(String userResponse, Question question) {
        String userResponseLimited  = "";
        if(userResponse.length() > 120) {
            userResponseLimited = userResponse.substring(0, 120);
        } else {
            userResponseLimited = userResponse;
        }
        try {
            NonStreamedChatResponse response = cohere.chat(
                    ChatRequest.builder()
                            .message("Dame feedback sobre esta respuesta: " + userResponseLimited)
                            .chatHistory(
                                    List.of(
                                            Message.user(ChatMessage.builder().message("Es una respuesta para una entrevista de trabajo en el sector tecnologico").build()),
                                            Message.user(ChatMessage.builder().message("Respuesta de un candidato a un puesto de trabajo para un puesto tecnologico").build()),
                                            Message.user(ChatMessage.builder().message("La finalidad es poder dar feedback al entrevistado sobre su respuesta").build())
                                    )
                            )
                            .build()
            );

            return response.getText();

        } catch (Exception e) {
            // TODO - Recoger pregunta y comentario de la base de datos
            return null;
        }
    }

    private Question splitResponseQuestion(Question questionObj, String response) {
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

    private String generatePrompt(FileLoader fileLoader, Questionary questionary, String path, String jsonType) throws IOException {
        List<String> list = fileLoader.loadPromptsFromFile(path, jsonType);
        String prompt = fileLoader.getRandomPrompt(list);
        String formattedPrompt = prompt
                .replace("{role}", questionary.getRole())
                .replace("{experience}", questionary.getExperience())
                .replace("{tematica}", questionary.getTheme());
        System.out.println(formattedPrompt);
        return formattedPrompt;
    }

    private List<String> generateChatHistoryList(FileLoader fileLoader, Questionary questionary) throws IOException {
        switch(questionary.getRole()) {
            case "diseñador ux/ui":
                if(uxuiDesignChatHistory.isEmpty()){
                    return uxuiDesignChatHistory = fileLoader.loadPromptsFromFile("src/main/resources/questions/UXUIDesignQuestions.json", "ux/ui design");
                } else {
                    return uxuiDesignChatHistory;
                }
            case "frontend":
                if (frontendChatHistory.isEmpty()) {
                    return frontendChatHistory = fileLoader.loadPromptsFromFile("src/main/resources/questions/FrontendQuestions.json", "frontend");
                } else {
                    return frontendChatHistory;
                }
            case "backend":
                if (backendChatHistory.isEmpty()) {
                    return backendChatHistory = fileLoader.loadPromptsFromFile("src/main/resources/questions/BackendQuestions.json", "backend");
                } else {
                    return backendChatHistory;
                }
        }
        // TODO -> Revisar este return
        return List.of();
    }

    private void fillChatHistory(Question question) {
        switch(question.getRole()) {
            case "diseñador ux/ui":
                uxuiDesignChatHistory.add(question.getQuestion());
                break;
            case "frontend":
                frontendChatHistory.add(question.getQuestion());
                break;
            case "backend":
                backendChatHistory.add(question.getQuestion());
                break;
        }
    }


}
