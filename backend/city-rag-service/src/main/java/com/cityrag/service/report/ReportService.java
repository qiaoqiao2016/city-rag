package com.cityrag.service.report;

import com.cityrag.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ReportService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");

    public String handleUpload(MultipartFile[] images, String location, String description, String resourceType) {
        if (images == null || images.length == 0) {
            throw new BusinessException("At least one image is required");
        }

        for (MultipartFile file : images) {
            validateFile(file);
        }

        String reportId = UUID.randomUUID().toString();
        log.info("Report uploaded: id={}, location={}, description={}, resourceType={}, files={}",
                reportId, location, description, resourceType, images.length);

        return reportId;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("Uploaded file is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("File size exceeds maximum allowed (10MB)");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new BusinessException("File type not allowed: " + contentType);
        }
    }
}
