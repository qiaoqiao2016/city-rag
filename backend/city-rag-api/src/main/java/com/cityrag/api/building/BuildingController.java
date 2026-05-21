package com.cityrag.api.building;

import com.cityrag.common.dto.ApiResult;
import com.cityrag.common.dto.PageRequest;
import com.cityrag.common.dto.PageResult;
import com.cityrag.dao.entity.BuildingEntity;
import com.cityrag.service.building.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping
    public ApiResult<PageResult<BuildingEntity>> list(
            @RequestParam(required = false) String spatialWkt,
            @RequestParam(required = false) List<String> types,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) {
        PageRequest pageRequest = PageRequest.builder().page(page).size(size).sort(sort).build();
        return ApiResult.success(buildingService.pageQuery(pageRequest, spatialWkt, types));
    }

    @GetMapping("/{id}")
    public ApiResult<BuildingEntity> getById(@PathVariable String id) {
        return ApiResult.success(buildingService.getById(id));
    }

    @GetMapping("/building-stats")
    public ApiResult<Map<String, Object>> getStats() {
        return ApiResult.success(Map.of("status", "available"));
    }
}
