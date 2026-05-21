package com.cityrag.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> records;
    private long total;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public static <T> PageResult<T> of(List<T> records, long total, PageRequest pageRequest) {
        int pageSize = pageRequest.getSize();
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) total / pageSize) : 0;
        return PageResult.<T>builder()
                .records(records)
                .total(total)
                .totalPages(totalPages)
                .currentPage(pageRequest.getPage())
                .pageSize(pageSize)
                .build();
    }
}
