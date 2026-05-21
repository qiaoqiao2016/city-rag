package com.cityrag.service.building;

import com.cityrag.common.dto.PageRequest;
import com.cityrag.common.dto.PageResult;
import com.cityrag.dao.entity.BuildingEntity;
import com.cityrag.dao.mapper.BuildingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildingServiceTest {

    @Mock private BuildingMapper buildingMapper;
    @InjectMocks private BuildingService buildingService;
    private BuildingEntity mockBuilding;

    @BeforeEach
    void setUp() {
        mockBuilding = new BuildingEntity();
        mockBuilding.setId("B-001");
        mockBuilding.setBuildingName("测试大厦");
        mockBuilding.setDistrict("海珠区");
        mockBuilding.setUseType("办公");
        mockBuilding.setFloorCount(20);
    }

    @Test
    void testGetById() {
        when(buildingMapper.selectById("B-001")).thenReturn(mockBuilding);
        BuildingEntity result = buildingService.getById("B-001");
        assertEquals("测试大厦", result.getBuildingName());
    }

    @Test
    void testGetByIdNotFound() {
        when(buildingMapper.selectById("NOT_EXIST")).thenReturn(null);
        assertNull(buildingService.getById("NOT_EXIST"));
    }

    @Test
    void testPageQueryWithSpatial() {
        when(buildingMapper.selectBySpatial(anyString(), isNull())).thenReturn(List.of(mockBuilding));
        PageRequest req = PageRequest.builder().page(1).size(20).build();
        PageResult<BuildingEntity> result = buildingService.pageQuery(req, "POLYGON((...))", null);
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void testPageQueryWithoutSpatial() {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<BuildingEntity> mpPage =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 20);
        mpPage.setRecords(List.of(mockBuilding));
        mpPage.setTotal(1);
        when(buildingMapper.selectPage(any(), isNull())).thenReturn(mpPage);
        PageRequest req = PageRequest.builder().page(1).size(20).build();
        PageResult<BuildingEntity> result = buildingService.pageQuery(req, null, null);
        assertEquals(1, result.getRecords().size());
        assertEquals(1, result.getTotal());
    }
}
