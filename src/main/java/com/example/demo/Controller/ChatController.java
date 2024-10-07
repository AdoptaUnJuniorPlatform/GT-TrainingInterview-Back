package com.example.demo.Controller;

import com.example.demo.model.Chat;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/ask")
    public String ask(@RequestBody Chat chat) {
        return chatService.getChatResponse(chat);
    }

    // Only to check the backend
    @PostMapping("/check")
    public String checkApp(@RequestBody Chat chat) {
        return chatService.checkApp(chat);
    }
}
