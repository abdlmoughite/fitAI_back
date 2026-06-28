package com.hessati.hessati.services;

import com.hessati.hessati.entities.AiLog;
import com.hessati.hessati.repositories.AiLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiLogService {

    @Autowired
    private AiLogRepository aiLogRepository;

    public void log(Long userId, String userName, String action, int tokens, String latency, String status) {
        AiLog log = new AiLog();
        log.setUserId(userId);
        log.setUserName(userName != null ? userName : "Utilisateur");
        log.setAction(action);
        log.setTokens(tokens);
        log.setLatency(latency);
        log.setStatus(status);
        aiLogRepository.save(log);
    }

    public List<AiLog> getLogs(String search, int page, int size) {
        return aiLogRepository.searchLogs(search, PageRequest.of(page, size));
    }
}
