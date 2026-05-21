package com.cityrag.ingestion;

import com.cityrag.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileImportService {

    public int importFile(MultipartFile file, String resourceType) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new BusinessException("File name is required");
        }

        if (filename.endsWith(".csv")) {
            return importCsv(file, resourceType);
        } else if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
            return importExcel(file, resourceType);
        } else {
            throw new BusinessException("Unsupported file format: " + filename);
        }
    }

    private int importCsv(MultipartFile file, String resourceType) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                rows.add(columns);
            }
        } catch (Exception e) {
            log.error("Failed to read CSV file", e);
            throw new BusinessException("Failed to parse CSV file: " + e.getMessage());
        }

        log.info("Imported {} rows from CSV for resourceType={}", rows.size(), resourceType);
        return rows.size();
    }

    private int importExcel(MultipartFile file, String resourceType) {
        log.info("Excel import placeholder for resourceType={}, file={}", resourceType, file.getOriginalFilename());
        return 0;
    }
}
