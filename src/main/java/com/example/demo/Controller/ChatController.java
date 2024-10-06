package com.example.demo.Controller;

import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/ask")
    public String ask() {
        return chatService.getChatEmptyResponse();
    }

    @PostMapping("/ask")
    public String ask(@RequestBody String userMessage) {
        String responseText = chatService.getChatResponse(userMessage);
        return chatService.getChatResponse(userMessage);
    }
}
