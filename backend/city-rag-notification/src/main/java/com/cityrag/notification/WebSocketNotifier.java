package com.cityrag.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebSocketNotifier {

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public void notifyAlert(Object alertPayload) {
        if (messagingTemplate == null) {
            log.warn("SimpMessagingTemplate not available, skipping WebSocket notification");
            return;
        }
        messagingTemplate.convertAndSend("/topic/alerts", alertPayload);
        log.info("Sent alert notification to /topic/alerts");
    }

    public void notifyAlertByPriority(Object alertPayload, String priority) {
        if (messagingTemplate == null) {
            log.warn("SimpMessagingTemplate not available, skipping WebSocket notification");
            return;
        }
        messagingTemplate.convertAndSend("/topic/alerts/" + priority.toLowerCase(), alertPayload);
        log.info("Sent alert notification to /topic/alerts/{}", priority.toLowerCase());
    }
}
