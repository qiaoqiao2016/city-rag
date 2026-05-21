package com.cityrag.service.alert;

import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.dao.entity.AlertRuleEntity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificationDispatcherTest {

    private final NotificationDispatcher dispatcher = new NotificationDispatcher();

    @Test
    void testDispatchPopupChannel() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleName("水位超限");
        rule.setNotifyChannels("popup");
        // Should not throw any exception
        assertDoesNotThrow(() -> dispatcher.dispatch(new AlertEventEntity(), rule));
    }

    @Test
    void testDispatchMultipleChannels() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleName("摄像头离线");
        rule.setNotifyChannels("popup,sms,voice");
        assertDoesNotThrow(() -> dispatcher.dispatch(new AlertEventEntity(), rule));
    }

    @Test
    void testDispatchEmptyChannels() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleName("测试规则");
        rule.setNotifyChannels("");
        assertDoesNotThrow(() -> dispatcher.dispatch(new AlertEventEntity(), rule));
    }

    @Test
    void testDispatchNullChannels() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleName("测试规则");
        rule.setNotifyChannels(null);
        assertDoesNotThrow(() -> dispatcher.dispatch(new AlertEventEntity(), rule));
    }

    @Test
    void testDispatchUnknownChannel() {
        AlertRuleEntity rule = new AlertRuleEntity();
        rule.setRuleName("测试规则");
        rule.setNotifyChannels("email");
        assertDoesNotThrow(() -> dispatcher.dispatch(new AlertEventEntity(), rule));
    }
}
