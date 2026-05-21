package com.cityrag.dao.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testBuildingEntity() {
        BuildingEntity b = new BuildingEntity();
        b.setId("B-001");
        b.setBuildingName("测试大厦");
        b.setDistrict("海珠区");
        b.setFloorCount(20);
        b.setUseType("办公");
        b.setBuildYear(2020);
        assertEquals("B-001", b.getId());
        assertEquals(2020, b.getBuildYear().intValue());
    }

    @Test
    void testAlertRuleEntityGetThreshold() {
        AlertRuleEntity r = new AlertRuleEntity();
        r.setRuleId("rule-001");
        r.setConditions("[{"metric":"water_level","value":0.8}]");
        assertEquals(0.8, r.getThreshold(), 0.001);
        assertEquals(1, r.getConditionsList().size());
    }

    @Test
    void testAlertRuleEntityEmptyConditions() {
        AlertRuleEntity r = new AlertRuleEntity();
        assertNull(r.getThreshold());
        assertTrue(r.getConditionsList().isEmpty());
    }

    @Test
    void testPopulationEntity() {
        PopulationEntity p = new PopulationEntity();
        p.setId("P-001");
        p.setName("张三");
        p.setGender("男");
        assertEquals("张三", p.getName());
    }

    @Test
    void testAlertEventEntity() {
        AlertEventEntity e = new AlertEventEntity();
        e.setId("alert-001");
        e.setPriority("high");
        e.setStatus("pending");
        assertEquals("high", e.getPriority());
    }
}
