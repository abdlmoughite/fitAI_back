package com.hessati.hessati.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hessati.hessati.entities.ChatMessage;
import com.hessati.hessati.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AICoachService {

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getResponseFromN8n(String message, Long userId, String userName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of(
                "message", message,
                "userId", userId != null ? userId : 0,
                "userName", userName != null ? userName : "Utilisateur"
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(n8nWebhookUrl, request, String.class);

            String rawBody = responseEntity.getBody();
            if (rawBody != null && !rawBody.isBlank()) {
                try {
                    Map<?, ?> parsed = objectMapper.readValue(rawBody, Map.class);
                    for (String key : new String[]{"response", "output", "text", "message", "reply"}) {
                        Object val = parsed.get(key);
                        if (val != null) return val.toString();
                    }
                } catch (Exception jsonEx) {
                    // n8n returned plain text directly
                    return rawBody.trim();
                }
            }
        } catch (Exception e) {
            // n8n unreachable or returned unexpected format
        }
        return "Désolé, je n'arrive pas à obtenir une réponse pour le moment. Réessaie dans un instant. 🙏";
    }

    public ChatMessage saveMessage(Long userId, String userName, String userMessage, String aiResponse) {
        ChatMessage msg = new ChatMessage();
        msg.setUserId(userId);
        msg.setUserName(userName);
        msg.setUserMessage(userMessage);
        msg.setAiResponse(aiResponse);
        return chatMessageRepository.save(msg);
    }

    public List<ChatMessage> getHistory(Long userId) {
        return chatMessageRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }
}
