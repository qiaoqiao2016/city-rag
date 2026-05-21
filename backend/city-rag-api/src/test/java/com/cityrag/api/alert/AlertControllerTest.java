package com.cityrag.api.alert;

import com.cityrag.common.dto.PageResult;
import com.cityrag.dao.entity.AlertEventEntity;
import com.cityrag.dao.mapper.AlertEventMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertController.class)
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertEventMapper alertEventMapper;

    @Test
    void testListAlerts() throws Exception {
        AlertEventEntity alert = new AlertEventEntity();
        alert.setId("alert-001");
        alert.setRuleName("水位超限");
        alert.setPriority("high");
        alert.setStatus("pending");
        alert.setResourceType("manhole_cover");
        alert.setCreatedAt(LocalDateTime.now());

        mockMvc.perform(get("/api/v1/alerts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testConfirmAlert() throws Exception {
        mockMvc.perform(put("/api/v1/alerts/alert-001/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"confirmedBy":"张三"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
