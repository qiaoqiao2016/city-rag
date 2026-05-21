package com.cityrag.service.spatial;

import com.cityrag.dao.entity.BuildingEntity;
import com.cityrag.dao.mapper.BuildingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpatialQueryService {

    @Autowired
    private BuildingMapper buildingMapper;

    /**
     * Find resources within a given geometry (GeoJSON).
     */
    public List<BuildingEntity> within(String geometryGeoJson) {
        // Convert GeoJSON to WKT for PostGIS query
        return buildingMapper.selectBySpatial(geometryGeoJson, null);
    }

    /**
     * Find resources within a buffer around a point.
     */
    public List<BuildingEntity> buffer(double lng, double lat, double radius) {
        String wkt = String.format("POINT(%f %f)", lng, lat);
        return buildingMapper.selectByBuffer(wkt, radius, null);
    }

    /**
     * Calculate distance between two points.
     */
    public double distance(double fromLng, double fromLat, double toLng, double toLat) {
        double dLat = Math.toRadians(toLat - fromLat);
        double dLng = Math.toRadians(toLng - fromLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371000 * c;
    }
}
