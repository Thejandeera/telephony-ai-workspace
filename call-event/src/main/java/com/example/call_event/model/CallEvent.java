package com.example.call_event.model;

import lombok.Data;

@Data
public class CallEvent {
    private String callId;
    private String agentId;
    private CallEventType eventType;
}