package com.cityrag.api.municipal;

import com.cityrag.common.dto.ApiResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MunicipalController {

    @GetMapping("/manhole-covers")
    public ApiResult<List<Map<String, Object>>> manholeCovers() {
        return ApiResult.success(List.of());
    }

    @GetMapping("/street-lights")
    public ApiResult<List<Map<String, Object>>> streetLights() {
        return ApiResult.success(List.of());
    }
}
