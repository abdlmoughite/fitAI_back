package com.hessati.hessati.controllers;

import com.hessati.hessati.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class SystemController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/system/health")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        long activeUsers = userRepository.countByStatus("active");

        List<Map<String, Object>> servers = new ArrayList<>();
        servers.add(serverEntry("API Backend (Spring Boot)", "operational", "45ms", "32%"));
        servers.add(serverEntry("Base de données (MySQL)", "operational", "12ms", "45%"));
        servers.add(serverEntry("Service IA (Génération)", "operational", "180ms", "58%"));
        servers.add(serverEntry("Stockage fichiers", "operational", "8ms", "21%"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Map<String, Object>> events = new ArrayList<>();
        events.add(eventEntry("deploy", LocalDateTime.now().minusHours(2).format(fmt), "Déploiement v2.4.1 réussi"));
        events.add(eventEntry("info", LocalDateTime.now().minusHours(5).format(fmt), "Sauvegarde automatique de la base de données"));
        events.add(eventEntry("warning", LocalDateTime.now().minusHours(8).format(fmt), "Pic de charge détecté sur le service IA"));
        events.add(eventEntry("maintenance", LocalDateTime.now().minusDays(1).format(fmt), "Maintenance planifiée terminée"));

        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "Opérationnel");
        health.put("uptime", "99.9%");
        health.put("activeUsers", activeUsers);
        health.put("apiLatency", "45ms");
        health.put("servers", servers);
        health.put("recentEvents", events);

        return ResponseEntity.ok(health);
    }

    @GetMapping("/api/platform/stats")
    public ResponseEntity<List<Map<String, Object>>> getPlatformStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus("active");

        List<Map<String, Object>> kpis = new ArrayList<>();
        kpis.add(kpiEntry("1", "Utilisateurs actifs", String.valueOf(activeUsers), "+12%", "users"));
        kpis.add(kpiEntry("2", "Revenus mensuel", "48k€", "+8%", "flame"));
        kpis.add(kpiEntry("3", "Rétention", "87%", "+3%", "chart"));
        kpis.add(kpiEntry("4", "Total inscrits", String.valueOf(totalUsers), "+21%", "brain"));

        return ResponseEntity.ok(kpis);
    }

    private Map<String, Object> serverEntry(String name, String status, String latency, String load) {
        Map<String, Object> s = new LinkedHashMap<>();
        s.put("name", name);
        s.put("status", status);
        s.put("latency", latency);
        s.put("load", load);
        return s;
    }

    private Map<String, Object> eventEntry(String type, String time, String message) {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("type", type);
        e.put("time", time);
        e.put("message", message);
        return e;
    }

    private Map<String, Object> kpiEntry(String id, String label, String value, String change, String icon) {
        Map<String, Object> k = new LinkedHashMap<>();
        k.put("id", id);
        k.put("label", label);
        k.put("value", value);
        k.put("change", change);
        k.put("icon", icon);
        return k;
    }
}
