package com.example.demo.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PromptLoader {
    @Value("${general.prompt}")
    private String generalPrompt;

    private List<String> prompts;

    @PostConstruct
    public void init() {
        prompts = new ArrayList<>();
        try {
            loadPrompts();
        } catch (IOException e) {
            System.err.println("Error al cargar los prompts. Se carga el general.prompt");
            prompts.add(generalPrompt);
        }
    }

    private void loadPrompts() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/prompts/prompts.json"));
        prompts = new ArrayList<>();
        rootNode.path("prompts").forEach(prompt -> prompts.add(prompt.asText()));
    }

    public String getRandomPrompts() {
        Random random = new Random();
        int indexRandom = random.nextInt(prompts.size());
        return prompts.get(indexRandom);
    }
}
