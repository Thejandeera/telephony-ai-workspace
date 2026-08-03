package com.example.agentstatus.controller;

import com.example.agentstatus.model.Agent;
import com.example.agentstatus.model.AgentStatus;
import com.example.agentstatus.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgent(@PathVariable String id) {
        return agentService.getAgent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Agent> updateStatus(@PathVariable String id, @RequestBody Map<String, String> payload) {
        try {
            AgentStatus status = AgentStatus.valueOf(payload.get("status").toUpperCase());
            return agentService.updateAgentStatus(id, status)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}