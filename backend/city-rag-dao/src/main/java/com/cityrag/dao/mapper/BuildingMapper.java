package com.cityrag.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cityrag.dao.entity.BuildingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BuildingMapper extends BaseMapper<BuildingEntity> {

    List<BuildingEntity> selectBySpatial(@Param("wkt") String wkt, @Param("resourceTypes") List<String> types);

    List<BuildingEntity> selectByBuffer(@Param("centerWkt") String centerWkt, @Param("radius") double radius, @Param("resourceTypes") List<String> types);
}
