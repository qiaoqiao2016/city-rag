package com.cityrag.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
// Base fields for all resource entities, inherited by subclasses
public class BaseResourceEntity {

    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String dataSource;
}
