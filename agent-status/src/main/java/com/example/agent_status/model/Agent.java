package com.example.agentstatus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agent {
    private String id;
    private String name;
    private AgentStatus status;
}