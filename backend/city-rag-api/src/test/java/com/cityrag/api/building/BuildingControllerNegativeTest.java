package com.cityrag.api.building;

import com.cityrag.service.building.BuildingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuildingController.class)
class BuildingControllerNegativeTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private BuildingService buildingService;

    @Test
    void testGetBuildingNotFoundReturnsNull() throws Exception {
        when(buildingService.getById("NOT_EXIST")).thenReturn(null);
        mockMvc.perform(get("/api/v1/buildings/NOT_EXIST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testInvalidPageParam() throws Exception {
        mockMvc.perform(get("/api/v1/buildings?page=-1"))
                .andExpect(status().isOk());
    }

    @Test
    void testMissingPathParam() throws Exception {
        mockMvc.perform(get("/api/v1/buildings/"))
                .andExpect(status().is4xxClientError());
    }
}
