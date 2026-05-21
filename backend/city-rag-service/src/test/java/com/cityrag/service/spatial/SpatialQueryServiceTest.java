package com.cityrag.service.spatial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SpatialQueryServiceTest {

    @InjectMocks
    private SpatialQueryService spatialQueryService;

    @Test
    void testDistance() {
        double distance = spatialQueryService.distance(116.397, 39.908, 121.505, 31.245);
        assertTrue(distance > 1000000 && distance < 1100000,
                "Expected ~1068km but got " + distance);
    }

    @Test
    void testDistanceSamePoint() {
        assertEquals(0.0, spatialQueryService.distance(113.33, 23.15, 113.33, 23.15), 0.01);
    }

    @Test
    void testDistanceNorthSouth() {
        double distance = spatialQueryService.distance(0, 0, 0, 89);
        assertTrue(distance > 9900000 && distance < 10010000,
                "Expected ~10000km but got " + distance);
    }
}
