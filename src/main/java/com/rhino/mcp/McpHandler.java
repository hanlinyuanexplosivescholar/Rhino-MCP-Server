package com.rhino.mcp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class McpHandler {
    
    public void handleMcpMessage(String message) {
        log.info("Received MCP message: {}", message);
        // TODO: 实现MCP消息处理逻辑
    }
} 