package com.hessati.hessati.controllers;

import com.hessati.hessati.entities.AiLog;
import com.hessati.hessati.services.AiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/logs")
public class AiLogController {

    @Autowired
    private AiLogService aiLogService;

    @GetMapping("/ai")
    public ResponseEntity<List<Map<String, Object>>> getLogs(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        List<AiLog> logs = aiLogService.getLogs(search, page, size);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Map<String, Object>> result = new ArrayList<>();
        for (AiLog log : logs) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("id", log.getId());
            entry.put("timestamp", log.getTimestamp() != null ? log.getTimestamp().format(fmt) : "");
            entry.put("user", log.getUserName());
            entry.put("action", log.getAction());
            entry.put("model", log.getModel());
            entry.put("tokens", log.getTokens());
            entry.put("latency", log.getLatency());
            entry.put("status", log.getStatus());
            result.add(entry);
        }
        return ResponseEntity.ok(result);
    }
}
