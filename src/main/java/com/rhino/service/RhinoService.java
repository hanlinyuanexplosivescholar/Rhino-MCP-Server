package com.rhino.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Service
public class RhinoService {
    
    private static final String RHINO_HOST = "localhost";
    private static final int RHINO_PORT = 8081;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    public boolean connectToRhino() {
        try {
            socket = new Socket(RHINO_HOST, RHINO_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log.info("Successfully connected to Rhino");
            return true;
        } catch (IOException e) {
            log.error("Failed to connect to Rhino: {}", e.getMessage());
            return false;
        }
    }
    
    public void sendCommand(String command) {
        if (out != null) {
            out.println(command);
            log.info("Sent command to Rhino: {}", command);
        } else {
            log.error("Not connected to Rhino");
        }
    }
    
    public String receiveResponse() {
        try {
            if (in != null) {
                String response = in.readLine();
                log.info("Received response from Rhino: {}", response);
                return response;
            }
        } catch (IOException e) {
            log.error("Error receiving response from Rhino: {}", e.getMessage());
        }
        return null;
    }
    
    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
                log.info("Disconnected from Rhino");
            }
        } catch (IOException e) {
            log.error("Error disconnecting from Rhino: {}", e.getMessage());
        }
    }
} 