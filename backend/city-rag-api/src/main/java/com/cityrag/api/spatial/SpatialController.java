package com.cityrag.api.spatial;

import com.cityrag.common.dto.ApiResult;
import com.cityrag.dao.entity.BuildingEntity;
import com.cityrag.service.spatial.SpatialQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/spatial")
public class SpatialController {

    @Autowired
    private SpatialQueryService spatialQueryService;

    @GetMapping("/within")
    public ApiResult<java.util.List<BuildingEntity>> within(@RequestParam String geometry) {
        return ApiResult.success(spatialQueryService.within(geometry));
    }

    @GetMapping("/buffer")
    public ApiResult<java.util.List<BuildingEntity>> buffer(
            @RequestParam double lng,
            @RequestParam double lat,
            @RequestParam double radius) {
        return ApiResult.success(spatialQueryService.buffer(lng, lat, radius));
    }

    @GetMapping("/distance")
    public ApiResult<Map<String, Object>> distance(
            @RequestParam double fromLng,
            @RequestParam double fromLat,
            @RequestParam double toLng,
            @RequestParam double toLat) {
        double distance = spatialQueryService.distance(fromLng, fromLat, toLng, toLat);
        return ApiResult.success(Map.of("distance", distance, "unit", "meters"));
    }
}
