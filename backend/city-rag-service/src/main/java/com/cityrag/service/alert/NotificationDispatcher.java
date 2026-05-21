package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.dao.entity.AlertRuleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationDispatcher {

    public void dispatch(AlertEventEntity event, AlertRuleEntity rule) {
        String channels = rule.getNotifyChannels();
        if (channels == null || channels.isBlank()) {
            log.info("No notification channels configured for rule: {}", rule.getRuleName());
            return;
        }

        String[] channelList = channels.split(",");
        for (String channel : channelList) {
            switch (channel.trim().toLowerCase()) {
                case "popup":
                    dispatchPopup(event);
                    break;
                case "sms":
                    dispatchSms(event);
                    break;
                case "voice":
                    dispatchVoice(event);
                    break;
                default:
                    log.warn("Unknown notification channel: {}", channel);
            }
        }
    }

    private void dispatchPopup(AlertEventEntity event) {
        log.info("[WebSocket] Sending alert popup: {} - {}", event.getRuleName(), event.getTriggerValue());
    }

    private void dispatchSms(AlertEventEntity event) {
        log.info("[SMS] Sending alert SMS: {} - {}", event.getRuleName(), event.getTriggerValue());
    }

    private void dispatchVoice(AlertEventEntity event) {
        log.info("[Voice] Sending voice alert: {} - {}", event.getRuleName(), event.getTriggerValue());
    }
}
