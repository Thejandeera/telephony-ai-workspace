package com.example.agent_status.service;

import com.example.agent_status.dto.CallEvent;
import com.example.agent_status.model.AgentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventListener {

    private final AgentService agentService;

    @KafkaListener(topics = "telephony.events", groupId = "agent-status-group")
    public void consumeCallEvent(CallEvent event) {

        String targetStatus = determineTargetStatus(event);

        if (targetStatus != null) {
            agentService.updateAgentStatus(
                    event.getAgentId(),
                    AgentStatus.valueOf(targetStatus)
            );
            System.out.println("Kafka Listener: Updated agent " + event.getAgentId() + " to " + targetStatus);
        }
    }

    private String determineTargetStatus(CallEvent event) {
        return switch (event.getEventType()) {
            case STARTED, ANSWERED -> "BUSY";
            case ENDED -> "AVAILABLE";
            default -> null;
        };
    }
}