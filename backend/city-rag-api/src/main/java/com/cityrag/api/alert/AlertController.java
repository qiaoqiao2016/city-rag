package com.cityrag.api.alert;

import com.cityrag.common.dto.ApiResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    @GetMapping
    public ApiResult<?> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return ApiResult.success(Map.of("status", status, "priority", priority));
    }

    @PutMapping("/{id}/confirm")
    public ApiResult<Void> confirm(@PathVariable String id) {
        return ApiResult.success();
    }

    @PutMapping("/{id}/resolve")
    public ApiResult<Void> resolve(@PathVariable String id) {
        return ApiResult.success();
    }

    @PostMapping("/subscribe")
    public ApiResult<String> subscribe(@RequestBody Map<String, Object> body) {
        return ApiResult.success("subscribed");
    }

    @DeleteMapping("/subscribe/{id}")
    public ApiResult<Void> unsubscribe(@PathVariable String id) {
        return ApiResult.success();
    }
}
