package com.cityrag.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cityrag.dao.entity.AlertRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertRuleMapper extends BaseMapper<AlertRuleEntity> {

    List<AlertRuleEntity> selectByEnabledAndType(@Param("enabled") boolean enabled, @Param("resourceType") String resourceType);
}
