package com.cityrag.common.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiResultTest {

    @Test
    void testSuccess() {
        ApiResult<String> result = ApiResult.success("hello");
        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals("hello", result.getData());
        assertNotNull(result.getTimestamp());
        assertNotNull(result.getRequestId());
    }

    @Test
    void testError() {
        ApiResult<String> result = ApiResult.error(400, "bad request");
        assertEquals(400, result.getCode());
        assertEquals("bad request", result.getMessage());
    }

    @Test
    void testErrorDefaultCode() {
        ApiResult<String> result = ApiResult.error("server error");
        assertEquals(500, result.getCode());
    }
}
