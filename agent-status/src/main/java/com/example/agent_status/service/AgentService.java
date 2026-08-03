package com.example.agentstatus.service;

import com.example.agentstatus.model.Agent;
import com.example.agentstatus.model.AgentStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgentService {
    private final Map<String, Agent> agentDatabase = new ConcurrentHashMap<>();

    public AgentService() {
        agentDatabase.put("agent-1", new Agent("agent-1", "Alice", AgentStatus.AVAILABLE));
        agentDatabase.put("agent-2", new Agent("agent-2", "Bob", AgentStatus.OFFLINE));
    }

    public Optional<Agent> getAgent(String id) {
        return Optional.ofNullable(agentDatabase.get(id));
    }

    public Optional<Agent> updateAgentStatus(String id, AgentStatus newStatus) {
        Agent agent = agentDatabase.get(id);
        if (agent != null) {
            agent.setStatus(newStatus);
            agentDatabase.put(id, agent);
            return Optional.of(agent);
        }
        return Optional.empty();
    }
}