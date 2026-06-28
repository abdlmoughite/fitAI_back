package com.hessati.hessati.controllers;

import com.hessati.hessati.services.AICoachService;
import com.hessati.hessati.services.AiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai-coach")
public class AICoachController {

    @Autowired
    private AICoachService aiCoachService;

    @Autowired
    private AiLogService aiLogService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {
        String message = body.getOrDefault("message", "");
        String userName = body.getOrDefault("userName", "Utilisateur");
        Long userId = null;
        try { userId = Long.parseLong(body.getOrDefault("userId", "")); } catch (Exception ignored) {}

        long start = System.currentTimeMillis();
        String response = aiCoachService.getResponse(message);
        long elapsed = System.currentTimeMillis() - start;

        aiLogService.log(userId, userName, "ai_coach_chat", message.length() + response.length(), elapsed + "ms", "success");

        return ResponseEntity.ok(Map.of("response", response));
    }
}
