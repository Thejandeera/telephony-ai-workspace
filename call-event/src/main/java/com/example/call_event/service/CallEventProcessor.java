package com.example.call_event.service;

import com.example.call_event.model.CallEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallEventProcessor {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "telephony.events";

    public boolean processEvent(CallEvent event) {
        try {

            kafkaTemplate.send(TOPIC, event.getAgentId(), event);
            System.out.println("Published event to Kafka: " + event.getEventType() + " for agent: " + event.getAgentId());
            return true;
        } catch (Exception e) {
            System.err.println("Failed to publish to Kafka: " + e.getMessage());
            return false;
        }
    }
}