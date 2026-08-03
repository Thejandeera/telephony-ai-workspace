package com.example.call_event.service;

import com.example.call_event.model.CallEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CallEventProcessor {

    private final RestTemplate restTemplate;

    @Value("${services.agent-status.url}")
    private String agentServiceUrl;

    public boolean processEvent(CallEvent event) {
        String targetStatus = determineTargetStatus(event);
        if (targetStatus == null) {
            return true;
        }
        return updateAgentStatus(event.getAgentId(), targetStatus);
    }

    private String determineTargetStatus(CallEvent event) {
        return switch (event.getEventType()) {
            case STARTED, ANSWERED -> "BUSY";
            case ENDED -> "AVAILABLE";
            default -> null;
        };
    }

    private boolean updateAgentStatus(String agentId, String status) {
        String url = agentServiceUrl + "/" + agentId + "/status";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("status", status);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.PUT, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}