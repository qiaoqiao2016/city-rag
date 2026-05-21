package com.cityrag.common.dto;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {

    private int code;
    private String message;
    private T data;
    private long timestamp;
    private String requestId;

    public static <T> ApiResult<T> success() {
        return success(null);
    }

    public static <T> ApiResult<T> success(T data) {
        return ApiResult.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .timestamp(Instant.now().toEpochMilli())
                .requestId(IdUtil.fastSimpleUUID())
                .build();
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return ApiResult.<T>builder()
                .code(code)
                .message(message)
                .timestamp(Instant.now().toEpochMilli())
                .requestId(IdUtil.fastSimpleUUID())
                .build();
    }

    public static <T> ApiResult<T> error(String message) {
        return error(500, message);
    }
}
