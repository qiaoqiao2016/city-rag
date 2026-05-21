package com.cityrag.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")  // 开发环境使用 Stub，生产环境需集成真实短信网关
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
        // TODO: Replace with real SMS provider SDK integration (阿里云/腾讯云短信)
    }
}
