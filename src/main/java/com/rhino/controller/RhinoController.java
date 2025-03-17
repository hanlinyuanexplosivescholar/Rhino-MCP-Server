package com.rhino.controller;

import com.rhino.service.RhinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rhino")
@RequiredArgsConstructor
public class RhinoController {
    
    private final RhinoService rhinoService;
    
    @PostMapping("/connect")
    public ResponseEntity<String> connect() {
        boolean connected = rhinoService.connectToRhino();
        return ResponseEntity.ok(connected ? "Connected to Rhino" : "Failed to connect to Rhino");
    }
    
    @PostMapping("/command")
    public ResponseEntity<String> sendCommand(@RequestBody String command) {
        rhinoService.sendCommand(command);
        String response = rhinoService.receiveResponse();
        return ResponseEntity.ok(response != null ? response : "No response received");
    }
    
    @PostMapping("/disconnect")
    public ResponseEntity<String> disconnect() {
        rhinoService.disconnect();
        return ResponseEntity.ok("Disconnected from Rhino");
    }
} 