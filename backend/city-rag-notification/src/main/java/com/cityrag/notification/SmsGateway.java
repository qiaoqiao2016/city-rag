package com.cityrag.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsGateway {

    /**
     * Send an SMS message. Currently logs the message; real implementation
     * should integrate with a third-party SMS provider SDK.
     *
     * @param phoneNumber recipient phone number
     * @param message     SMS content
     */
    public void send(String phoneNumber, String message) {
        log.info("[SMS Gateway] To: {}, Message: {}", phoneNumber, message);
        // TODO: Replace with real SMS provider SDK integration
    }
}
