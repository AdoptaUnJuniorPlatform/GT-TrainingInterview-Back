package com.example.demo.service;

import com.cohere.api.Cohere;
import com.cohere.api.requests.ChatRequest;
import com.cohere.api.types.NonStreamedChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final Cohere cohere;

    public ChatService(@Value("${cohere.api.token}") String apiKey) {
        this.cohere = Cohere.builder().token(apiKey).clientName("snippet").build();
    }

    public String getChatResponse(String userMessage) {
        NonStreamedChatResponse response = cohere.chat(
                ChatRequest.builder()
                        .message(userMessage)
                        .chatHistory(
                                List.of(
                                        //Message.user(ChatMessage.builder().message("").build())
                                )
                        )
                        .build()
        );
        return response.getText();
    }

    public String getChatEmptyResponse() {
        NonStreamedChatResponse response = cohere.chat(
                ChatRequest.builder()
                        .message("Can you give me a question that I might be asked in a job interview for a UX/UI designer position?")
                        .chatHistory(
                                List.of(
                                        //Message.user(ChatMessage.builder().message("").build())
                                )
                        )
                        .build()
        );
        return response.getText();
    }
}
