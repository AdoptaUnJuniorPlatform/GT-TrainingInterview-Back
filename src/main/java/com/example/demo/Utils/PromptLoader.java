package com.example.demo.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PromptLoader {
    private List<String> prompts;

    public PromptLoader() throws IOException {
        try {
            loadPrompts();
        } catch (IOException e) {
            e.printStackTrace();
            // Especificar que hacer si no se carga el prompt
            prompts = List.of();
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
