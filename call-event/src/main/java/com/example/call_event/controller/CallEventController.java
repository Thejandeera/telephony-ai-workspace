package com.example.call_event.controller;

import com.example.call_event.model.CallEvent;
import com.example.call_event.service.CallEventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class CallEventController {
    private final CallEventProcessor eventProcessor;

    @PostMapping("/events")
    public ResponseEntity<String> receiveCallEvent(@RequestBody CallEvent event) {
        boolean success = eventProcessor.processEvent(event);
        if (success) {
            return ResponseEntity.ok("Event processed successfully");
        } else {
            return ResponseEntity.internalServerError().body("Failed to update agent");
        }
    }
}