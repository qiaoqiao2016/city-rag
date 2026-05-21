package com.cityrag.api.report;

import com.cityrag.common.dto.ApiResult;
import com.cityrag.service.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/upload")
    public ApiResult<String> upload(
            @RequestParam("images") MultipartFile[] images,
            @RequestParam String location,
            @RequestParam String description,
            @RequestParam String resourceType) {
        String reportId = reportService.handleUpload(images, location, description, resourceType);
        return ApiResult.success(reportId);
    }
}
