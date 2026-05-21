package com.cityrag.dao.mapper;

import com.cityrag.dao.entity.BuildingEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库集成测试。
 * 使用 H2 内存数据库验证 MyBatis-Plus Mapper 层基本 CRUD 和 SQL 映射正确性。
 * 空间查询（ST_Within/ST_DWithin）需要 PostGIS 扩展，
 * 建议使用 Testcontainers + postgis/postgres 容器运行完整 PostGIS 集成测试。
 */
@SpringBootTest
@Sql(statements = {
    "CREATE TABLE IF NOT EXISTS buildings (" +
    "  id VARCHAR(64) PRIMARY KEY, building_name VARCHAR(200), address VARCHAR(500)," +
    "  district VARCHAR(50), street VARCHAR(50), community VARCHAR(50)," +
    "  total_area DOUBLE, floor_count INT, building_height DOUBLE," +
    "  structure_type VARCHAR(20), build_year INT, use_type VARCHAR(30)," +
    "  occupancy_rate DOUBLE, data_source VARCHAR(32)," +
    "  created_at TIMESTAMP DEFAULT NOW(), updated_at TIMESTAMP DEFAULT NOW())",
    "INSERT INTO buildings (id, building_name, district, use_type, floor_count, build_year) VALUES" +
    "  (''B-001'', ''测试大厦A'', ''海珠区'', ''办公'', 20, 2020)",
    "INSERT INTO buildings (id, building_name, district, use_type, floor_count, build_year) VALUES" +
    "  (''B-002'', ''测试大厦B'', ''天河区'', ''住宅'', 30, 2015)",
    "INSERT INTO buildings (id, building_name, district, use_type, floor_count, build_year) VALUES" +
    "  (''B-003'', ''测试大厦C'', ''海珠区'', ''商业'', 10, 2022)"
})
class BuildingMapperIntegrationTest {

    @Autowired
    private BuildingMapper buildingMapper;

    @Test
    void testSelectById() {
        BuildingEntity result = buildingMapper.selectById("B-001");
        assertNotNull(result);
        assertEquals("测试大厦A", result.getBuildingName());
        assertEquals("海珠区", result.getDistrict());
        assertEquals(20, result.getFloorCount());
    }

    @Test
    void testSelectByIdNotFound() {
        assertNull(buildingMapper.selectById("NONEXISTENT"));
    }

    @Test
    void testSelectByDistrict() {
        // Query by district using MyBatis-Plus wrapper
        List<BuildingEntity> results = buildingMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BuildingEntity>()
                .eq(BuildingEntity::getDistrict, "海珠区"));
        assertEquals(2, results.size());
    }

    @Test
    void testSelectByUseType() {
        List<BuildingEntity> results = buildingMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BuildingEntity>()
                .eq(BuildingEntity::getUseType, "住宅"));
        assertEquals(1, results.size());
        assertEquals("B-002", results.get(0).getId());
    }

    @Test
    void testPagination() {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<BuildingEntity> page =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 2);
        var result = buildingMapper.selectPage(page, null);
        assertEquals(3, result.getTotal());
        assertEquals(2, result.getRecords().size());
    }

    @Test
    void testInsertAndDelete() {
        BuildingEntity b = new BuildingEntity();
        b.setId("B-004");
        b.setBuildingName("测试新楼");
        b.setDistrict("越秀区");
        b.setUseType("办公");

        int insertCount = buildingMapper.insert(b);
        assertEquals(1, insertCount);

        BuildingEntity inserted = buildingMapper.selectById("B-004");
        assertNotNull(inserted);
        assertEquals("测试新楼", inserted.getBuildingName());

        int deleteCount = buildingMapper.deleteById("B-004");
        assertEquals(1, deleteCount);
        assertNull(buildingMapper.selectById("B-004"));
    }
}
