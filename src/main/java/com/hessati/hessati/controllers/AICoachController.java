package com.hessati.hessati.controllers;

import com.hessati.hessati.entities.ChatMessage;
import com.hessati.hessati.services.AICoachService;
import com.hessati.hessati.services.AiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-coach")
public class AICoachController {

    @Autowired
    private AICoachService aiCoachService;

    @Autowired
    private AiLogService aiLogService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, String> body) {
        String message = body.getOrDefault("message", "");
        String userName = body.getOrDefault("userName", "Utilisateur");
        Long userId = null;
        try { userId = Long.parseLong(body.getOrDefault("userId", "")); } catch (Exception ignored) {}

        long start = System.currentTimeMillis();
        String response = aiCoachService.getResponseFromN8n(message, userId, userName);
        long elapsed = System.currentTimeMillis() - start;

        ChatMessage saved = aiCoachService.saveMessage(userId, userName, message, response);
        aiLogService.log(userId, userName, "ai_coach_chat", message.length() + response.length(), elapsed + "ms", "success");

        return ResponseEntity.ok(Map.of("response", response, "messageId", saved.getId()));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(aiCoachService.getHistory(userId));
    }
}
