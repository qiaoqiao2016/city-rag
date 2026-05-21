package com.cityrag.service.building;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cityrag.common.dto.PageRequest;
import com.cityrag.common.dto.PageResult;
import com.cityrag.dao.entity.BuildingEntity;
import com.cityrag.dao.mapper.BuildingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

    @Autowired
    private BuildingMapper buildingMapper;

    public PageResult<BuildingEntity> pageQuery(PageRequest pageRequest, String spatialWkt, List<String> types) {
        Page<BuildingEntity> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        List<BuildingEntity> records;
        long total;

        if (spatialWkt != null && !spatialWkt.isBlank()) {
            records = buildingMapper.selectBySpatial(spatialWkt, types);
            total = records.size();
        } else {
            Page<BuildingEntity> result = buildingMapper.selectPage(page, null);
            records = result.getRecords();
            total = result.getTotal();
        }

        return PageResult.of(records, total, pageRequest);
    }

    public BuildingEntity getById(String id) {
        return buildingMapper.selectById(id);
    }
}
