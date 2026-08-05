package com.example.agent_status.dto;

import com.example.agent_status.dto.CallEventType;
import lombok.Data;

@Data
public class CallEvent {
    private String callId;
    private String agentId;
    private CallEventType eventType;
}