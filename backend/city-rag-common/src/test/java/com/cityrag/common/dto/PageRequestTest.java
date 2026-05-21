package com.cityrag.common.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PageRequestTest {

    @Test
    void testDefaults() {
        PageRequest req = PageRequest.builder().build();
        assertEquals(1, req.getPage());
        assertEquals(20, req.getSize());
    }

    @Test
    void testCustom() {
        PageRequest req = PageRequest.builder().page(3).size(50).sort("name,asc").build();
        assertEquals(3, req.getPage());
        assertEquals(50, req.getSize());
        assertEquals("name,asc", req.getSort());
    }
}
