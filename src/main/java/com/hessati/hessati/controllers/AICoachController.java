package com.hessati.hessati.controllers;

import com.hessati.hessati.services.AICoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai-coach")
public class AICoachController {

    @Autowired
    private AICoachService aiCoachService;

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {
        String message = body.getOrDefault("message", "");
        String response = aiCoachService.getResponse(message);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
