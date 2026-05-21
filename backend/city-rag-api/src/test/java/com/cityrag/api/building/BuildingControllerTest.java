package com.cityrag.api.building;

import com.cityrag.common.dto.ApiResult;
import com.cityrag.common.dto.PageResult;
import com.cityrag.dao.entity.BuildingEntity;
import com.cityrag.service.building.BuildingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuildingController.class)
class BuildingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BuildingService buildingService;

    @Test
    void testGetBuildingById() throws Exception {
        BuildingEntity building = new BuildingEntity();
        building.setId("B-001");
        building.setBuildingName("测试大厦");

        when(buildingService.getById("B-001")).thenReturn(building);

        mockMvc.perform(get("/api/v1/buildings/B-001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.buildingName").value("测试大厦"));
    }

    @Test
    void testListBuildings() throws Exception {
        BuildingEntity building = new BuildingEntity();
        building.setId("B-001");
        building.setBuildingName("测试大厦");

        when(buildingService.pageQuery(any(), isNull(), isNull()))
                .thenReturn(PageResult.of(List.of(building), 1,
                        new com.cityrag.common.dto.PageRequest(1, 20, null)));

        mockMvc.perform(get("/api/v1/buildings")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records[0].buildingName").value("测试大厦"));
    }

    @Test
    void testBuildingStats() throws Exception {
        mockMvc.perform(get("/api/v1/buildings/building-stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("available"));
    }
}
