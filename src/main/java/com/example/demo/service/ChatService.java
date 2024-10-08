package com.example.demo.service;

import com.cohere.api.Cohere;
import com.cohere.api.requests.ChatRequest;
import com.cohere.api.types.ChatMessage;
import com.cohere.api.types.Message;
import com.cohere.api.types.NonStreamedChatResponse;
import com.example.demo.model.Chat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.query.JSqlParserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ChatService {
    private final Cohere cohere;
    private List<String> chatQuestionHistory = generateChatQuestion();
    private List<String> promptQuestionList = new ArrayList<>();

    public ChatService(@Value("${cohere.api.token}") String apiKey) {
        this.cohere = Cohere.builder().token(apiKey).clientName("Pildoras UX").build();
    }

    public String getChatResponse(Chat chat) {
        generatePrompt(chat, promptQuestionList);
        try {
            String randomPrompt = getRandomQuestion(promptQuestionList);

            NonStreamedChatResponse response = cohere.chat(
                    ChatRequest.builder()
                            .message(randomPrompt + "Quiero que me devuelvas solamente la pregunta.")
                            .chatHistory(
                                    List.of(
                                            Message.user(ChatMessage.builder().message(getRandomQuestion(chatQuestionHistory)).build()),
                                            Message.user(ChatMessage.builder().message(getRandomQuestion(chatQuestionHistory)).build()),
                                            Message.user(ChatMessage.builder().message(getRandomQuestion(chatQuestionHistory)).build()),
                                            Message.user(ChatMessage.builder().message(getRandomQuestion(chatQuestionHistory)).build()),
                                            Message.user(ChatMessage.builder().message(getRandomQuestion(chatQuestionHistory)).build())
                                    )
                            )
                            .build()
            );
            fillChatHistory(chatQuestionHistory, response.getText());

            return response.getText();

        } catch (Exception e) {
            return "Ocurrió un error al generar la pregunta. Inténtalo otra vez";
        }
    }

    public String getFeedback(String question) {
        try {
            NonStreamedChatResponse response = cohere.chat(
                    ChatRequest.builder()
                            .message(question + ". ¿Puedes darme feedback de esta respuesta, que es la que le contesto a un reclutador para un puesto que puede ser de backend, frontend o diseñador ux/ui?")
                            .build()
            );
            return response.getText();

        } catch (Exception e) {
            return "Ocurrió un error al generar la pregunta. Inténtalo otra vez";
        }
    }

    public void generatePrompt(Chat chat, List<String> promptQuestions) {
        promptQuestions.add("Imagina que eres un reclutador y estás realizando una entrevista para un puesto de " + chat.getRank() + " a un candidato " + chat.getExperience() + ". ¿Cuál sería una de las preguntas que le harías?");
        promptQuestions.add("Supongamos que eres un reclutador experimentado. ¿Qué pregunta plantearías a un candidato " + chat.getExperience() + " para un puesto de " + chat.getRank() + "?");
        promptQuestions.add("Como reclutador con experiencia, ¿qué pregunta inicial le harías a un postulante " + chat.getExperience() + " para un trabajo de " + chat.getRank() + "?\"");
        promptQuestions.add("Si fueras un reclutador en una entrevista para un puesto de " + chat.getRank() + " " + chat.getExperience() + ", ¿cuál sería una pregunta que consideras clave?");
        promptQuestions.add("Eres un reclutador experimentado. ¿Cuál sería una pregunta que harías a un candidato " + chat.getExperience() + " que está solicitando un puesto en " + chat.getRank() + "?");
        promptQuestions.add("Simula que eres un reclutador que está entrevistando a un candidato " + chat.getExperience() + " para un puesto de " + chat.getRank() + ". ¿Qué pregunta le harías?");
        promptQuestions.add("Imagina que eres un reclutador buscando un desarrollador " + chat.getRank() + " " + chat.getExperience() + ". ¿Cuál sería tu primera pregunta para el candidato?");
        promptQuestions.add("Como un reclutador con experiencia, ¿qué pregunta inicial considerarías importante para un puesto de " + chat.getRank() + " " + chat.getExperience() + "?");
        promptQuestions.add("Supongamos que estás llevando a cabo entrevistas para un puesto de " + chat.getRank() + " " + chat.getExperience() + ". ¿Qué pregunta te gustaría hacer al candidato?");
        promptQuestions.add("Eres un reclutador experimentado en la búsqueda de un desarrollador " + chat.getRank() + " " + chat.getExperience() + ". ¿Cuál sería una pregunta que le harías en la entrevista?");
    }

    public List<String> generateChatQuestion() {
        ArrayList<String> questions = new ArrayList<>();
        questions.add("¿Cómo te comunicas y colaboras con otros miembros del equipo, como desarrolladores backend, frontend y diseñadores?");
        questions.add("Descríbeme un momento en el que tuviste que resolver un problema crítico relacionado con la tecnología o la usabilidad. ¿Cómo lo abordaste?");
        questions.add("¿Cómo manejas múltiples tareas y proyectos con deadlines ajustados?");
        questions.add("¿Qué mejores prácticas sigues en tu trabajo diario para asegurarte de que el código o el diseño sea escalable y mantenible?");
        questions.add("¿Qué herramientas sueles utilizar para tu trabajo diario? ¿Cómo te ayudan a ser más eficiente?");

        return questions;
    }

    public String getRandomQuestion(List<String> questionList) {
        Random random = new Random();
        int randomIndex = random.nextInt(questionList.size());
        return questionList.get(randomIndex);
    }

    public void fillChatHistory(List<String> chatHistory, String question) {
        chatHistory.add(question);
    }

    // Only to check the backend
    public String checkApp(Chat chat) {
        NonStreamedChatResponse response = cohere.chat(
                ChatRequest.builder()
                        .message(chat.getPrompt())
                        .build()
        );
        return response.getText();
    }
}
