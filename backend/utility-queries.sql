-- ============================================
-- 城市资源一张图 常用运维查询脚本
-- PostgreSQL + PostGIS
-- ============================================

-- ============================================
-- 一、空间分析查询
-- ============================================

-- 1.1 框选查询：获取多边形范围内的所有资源
-- 说明：前端传 GeoJSON 多边形，查询范围内的建筑/摄像头/井盖等
WITH search_area AS (
    SELECT ST_GeomFromText('POLYGON((121.240 31.370, 121.270 31.370, 121.270 31.390, 121.240 31.390, 121.240 31.370))', 4326) AS geom
)
SELECT 'building' AS resource_type, b.id, b.building_name AS name, b.geom
FROM buildings b, search_area s
WHERE ST_Within(b.geom, s.geom)
UNION ALL
SELECT 'camera', c.id, c.camera_name, c.geom
FROM cameras c, search_area s
WHERE ST_Within(c.geom, s.geom)
UNION ALL
SELECT 'manhole_cover', m.id, m.cover_code, m.geom
FROM manhole_covers m, search_area s
WHERE ST_Within(m.geom, s.geom)
UNION ALL
SELECT 'street_light', l.id, l.light_code, l.geom
FROM street_lights l, search_area s
WHERE ST_Within(l.geom, s.geom)
ORDER BY resource_type, id;

-- 1.2 缓冲区分析：查找某点周围 500 米内的所有资源
WITH center AS (
    SELECT ST_SetSRID(ST_MakePoint(121.250, 31.380), 4326) AS geom
)
SELECT 'building' AS resource_type, b.id, b.building_name AS name,
       ST_DistanceSphere(b.geom, c.geom) AS distance_m
FROM buildings b, center c
WHERE ST_DWithin(b.geom::geography, c.geom::geography, 500)
UNION ALL
SELECT 'camera', cam.id, cam.camera_name,
       ST_DistanceSphere(cam.geom, c.geom)
FROM cameras cam, center c
WHERE ST_DWithin(cam.geom::geography, c.geom::geography, 500)
ORDER BY distance_m;

-- 1.3 最近邻查询：查找离某点最近的 5 个消防栓
SELECT id, hydrant_code, hydrant_type, status,
       ST_DistanceSphere(geom, ST_SetSRID(ST_MakePoint(121.250, 31.380), 4326)) AS distance_m
FROM fire_hydrants
WHERE status = 'normal'
ORDER BY geom <-> ST_SetSRID(ST_MakePoint(121.250, 31.380), 4326)
LIMIT 5;

-- 1.4 道路与建筑的关联：查找沿某条道路的建筑
SELECT r.road_name, b.building_name, b.address,
       ST_DistanceSphere(b.geom, r.geom) AS distance_m
FROM buildings b, roads r
WHERE r.id = 'road-001'
  AND ST_DWithin(b.geom::geography, r.geom::geography, 100)
ORDER BY distance_m;

-- ============================================
-- 二、统计聚合查询
-- ============================================

-- 2.1 全局统计摘要（用于大屏）
SELECT
    (SELECT COUNT(*) FROM buildings) AS building_count,
    (SELECT COUNT(*) FROM roads) AS road_count,
    (SELECT COUNT(*) FROM cameras) AS camera_count,
    (SELECT COUNT(*) FROM manhole_covers) AS manhole_count,
    (SELECT COUNT(*) FROM street_lights) AS light_count,
    (SELECT COUNT(*) FROM ecology_stations) AS station_count,
    (SELECT COUNT(*) FROM fire_hydrants) AS hydrant_count,
    (SELECT COUNT(*) FROM police_stations) AS police_count,
    (SELECT COUNT(*) FROM populations) AS population_count,
    (SELECT COUNT(*) FROM enterprises) AS enterprise_count,
    (SELECT COUNT(*) FROM grids) AS grid_count,
    (SELECT COUNT(*) FROM alert_events WHERE status = 'pending') AS pending_alert_count;

-- 2.2 按行政区统计建筑分布
SELECT district,
       COUNT(*) AS building_count,
       SUM(total_area) AS total_area,
       ROUND(AVG(floor_count)) AS avg_floors,
       ROUND(AVG(occupancy_rate), 2) AS avg_occupancy
FROM buildings
GROUP BY district
ORDER BY building_count DESC;

-- 2.3 按类型统计摄像头
SELECT camera_type,
       COUNT(*) AS total,
       COUNT(*) FILTER (WHERE online_status = 'online') AS online,
       COUNT(*) FILTER (WHERE online_status = 'offline') AS offline,
       ROUND(COUNT(*) FILTER (WHERE online_status = 'online') * 100.0 / COUNT(*), 1) AS online_rate
FROM cameras
GROUP BY camera_type;

-- 2.4 资源状态概览
SELECT '摄像头' AS 资源类型, online_status AS 状态, COUNT(*) AS 数量 FROM cameras GROUP BY online_status
UNION ALL
SELECT '井盖', status, COUNT(*) FROM manhole_covers GROUP BY status
UNION ALL
SELECT '路灯', status, COUNT(*) FROM street_lights GROUP BY status
UNION ALL
SELECT '生态监测站', status, COUNT(*) FROM ecology_stations GROUP BY status
UNION ALL
SELECT '消防栓', status, COUNT(*) FROM fire_hydrants GROUP BY status
ORDER BY 资源类型, 状态;

-- 2.5 企业行业分布
SELECT industry_type,
       COUNT(*) AS enterprise_count,
       SUM(employee_count) AS total_employees,
       ROUND(SUM(annual_revenue) / 100000000.0, 2) AS revenue_billion
FROM enterprises
WHERE annual_revenue > 0
GROUP BY industry_type
ORDER BY revenue_billion DESC;

-- ============================================
-- 三、跨资源关联查询
-- ============================================

-- 3.1 楼栋全息画像：楼栋 + 人口 + 户 + 企业
SELECT
    b.id AS building_id,
    b.building_name,
    b.district, b.street, b.community,
    b.total_area, b.floor_count, b.use_type,
    b.occupancy_rate,
    ROUND(b.occupancy_rate * b.total_area / NULLIF(b.floor_count - b.underground_floors, 0), 2) AS avg_floor_area,
    COUNT(DISTINCT h.id) AS household_count,
    COUNT(DISTINCT p.id) AS population_count,
    COUNT(DISTINCT e.id) AS enterprise_count,
    JSON_AGG(DISTINCT jsonb_build_object('name', e.enterprise_name, 'industry', e.industry_type))
        FILTER (WHERE e.id IS NOT NULL) AS enterprises
FROM buildings b
LEFT JOIN households h ON h.building_id = b.id
LEFT JOIN populations p ON p.building_id = b.id
LEFT JOIN enterprises e ON e.building_id = b.id
WHERE b.id = 'bld-005'
GROUP BY b.id;

-- 3.2 网格全息画像
SELECT
    g.id AS grid_id,
    g.grid_code, g.grid_type,
    g.grid_master, g.master_phone,
    g.population_count AS grid_population,
    COUNT(DISTINCT b.id) AS buildings,
    COUNT(DISTINCT p.id) AS actual_population,
    COUNT(DISTINCT e.id) AS enterprises,
    COUNT(DISTINCT cam.id) AS cameras,
    COUNT(DISTINCT m.id) AS manhole_covers,
    COUNT(DISTINCT l.id) AS street_lights
FROM grids g
LEFT JOIN buildings b ON ST_Within(ST_Centroid(b.geom), g.geom)
LEFT JOIN populations p ON ST_Within(p.geom, g.geom)
LEFT JOIN enterprises e ON ST_Within(e.geom, g.geom)
LEFT JOIN cameras cam ON ST_Within(cam.geom, g.geom)
LEFT JOIN manhole_covers m ON ST_Within(m.geom, g.geom)
LEFT JOIN street_lights l ON ST_Within(l.geom, g.geom)
WHERE g.id = 'grid-001'
GROUP BY g.id;

-- 3.3 场所周边安防：查询某建筑周边 300 米安防资源
SELECT
    b.building_name,
    jsonb_build_object(
        'cameras', (SELECT JSON_AGG(jsonb_build_object('name', cam.camera_name, 'type', cam.camera_type, 'status', cam.online_status))
                    FROM cameras cam
                    WHERE ST_DWithin(cam.geom::geography, b.geom::geography, 300)),
        'police', (SELECT JSON_AGG(jsonb_build_object('name', ps.station_name, 'phone', ps.duty_phone))
                   FROM police_stations ps
                   WHERE ST_DWithin(ps.geom::geography, b.geom::geography, 300)),
        'hydrants', (SELECT JSON_AGG(jsonb_build_object('code', fh.hydrant_code, 'status', fh.status))
                     FROM fire_hydrants fh
                     WHERE ST_DWithin(fh.geom::geography, b.geom::geography, 300))
    ) AS security_resources
FROM buildings b
WHERE b.id = 'bld-002';

-- 3.4 人口结构分析（按网格）
SELECT
    g.grid_code,
    COUNT(p.id) AS total_population,
    COUNT(*) FILTER (WHERE p.gender = '男') AS male,
    COUNT(*) FILTER (WHERE p.gender = '女') AS female,
    COUNT(*) FILTER (WHERE p.is_rental = TRUE) AS rental_count,
    ROUND(COUNT(*) FILTER (WHERE p.is_rental = TRUE) * 100.0 / NULLIF(COUNT(*), 0), 1) AS rental_rate,
    COUNT(*) FILTER (WHERE p.special_tag IS NOT NULL) AS special_care_count,
    COUNT(*) FILTER (WHERE EXTRACT(YEAR FROM AGE(p.birth_date)) >= 65) AS elderly_count,
    COUNT(*) FILTER (WHERE EXTRACT(YEAR FROM AGE(p.birth_date)) < 18) AS minor_count
FROM populations p
JOIN grids g ON p.grid_id = g.id
GROUP BY g.grid_code
ORDER BY g.grid_code;

-- ============================================
-- 四、告警相关查询
-- ============================================

-- 4.1 待处理告警列表（按优先级排序）
SELECT a.id, a.rule_name, a.resource_type, a.resource_name,
       a.priority, a.status, a.trigger_value, a.trigger_count,
       a.created_at,
       EXTRACT(EPOCH FROM (NOW() - a.created_at)) / 60 AS pending_minutes
FROM alert_events a
WHERE a.status = 'pending'
ORDER BY
    CASE a.priority
        WHEN 'critical' THEN 1
        WHEN 'high' THEN 2
        WHEN 'medium' THEN 3
        WHEN 'low' THEN 4
    END,
    a.created_at ASC;

-- 4.2 告警趋势（按天统计）
SELECT DATE(created_at) AS alert_date,
       COUNT(*) AS total,
       COUNT(*) FILTER (WHERE priority = 'critical' OR priority = 'high') AS high_priority,
       COUNT(*) FILTER (WHERE status = 'resolved') AS resolved,
       COUNT(*) FILTER (WHERE status = 'pending') AS pending
FROM alert_events
WHERE created_at >= NOW() - INTERVAL '7 days'
GROUP BY DATE(created_at)
ORDER BY alert_date DESC;

-- 4.3 告警规则概览
SELECT rule_id, rule_name, resource_type, rule_type,
       enabled, priority, eval_interval,
       notify_channels,
       CASE WHEN enabled THEN '运行中' ELSE '已停用' END AS status
FROM alert_rules
ORDER BY enabled DESC, priority;

-- ============================================
-- 五、运维管理查询
-- ============================================

-- 5.1 近期审计日志
SELECT created_at, username, action, resource_type, resource_id,
       duration_ms, result, ip_address
FROM audit_logs
WHERE created_at >= NOW() - INTERVAL '24 hours'
ORDER BY created_at DESC
LIMIT 50;

-- 5.2 异常操作审计（失败操作）
SELECT created_at, username, action, resource_type, resource_id,
       params, ip_address
FROM audit_logs
WHERE result = 'FAIL'
ORDER BY created_at DESC
LIMIT 20;

-- 5.3 数据冲突待处理
SELECT id, resource_type, resource_id, field_name,
       source_a, value_a,
       source_b, value_b,
       created_at
FROM data_conflicts
WHERE status = 'pending'
ORDER BY created_at DESC;

-- 5.4 物化视图刷新状态
SELECT schemaname || '.' || matviewname AS matview_name,
       pg_size_pretty(pg_total_relation_size(schemaname || '.' || matviewname)) AS size,
       pg_stat_get_last_analyze_time(schemaname || '.' || matviewname::regclass) AS last_refresh
FROM pg_matviews
WHERE matviewname LIKE 'v_%'
ORDER BY matviewname;

-- ============================================
-- 六、空间数据维护
-- ============================================

-- 6.1 空间索引健康检查
SELECT schemaname || '.' || tablename AS table_name,
       indexname AS index_name,
       indexdef
FROM pg_indexes
WHERE indexdef LIKE '%GIST%'
ORDER BY table_name, index_name;

-- 6.2 统计表大小
SELECT relname AS table_name,
       pg_size_pretty(pg_total_relation_size(relid)) AS total_size,
       pg_size_pretty(pg_relation_size(relid)) AS table_size,
       pg_size_pretty(pg_total_relation_size(relid) - pg_relation_size(relid)) AS index_size,
       n_live_tup AS row_count
FROM pg_stat_user_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(relid) DESC;

-- 6.3 创建/刷新物化视图
REFRESH MATERIALIZED VIEW CONCURRENTLY v_building_aggregation;

-- 6.4 创建月度告警分区（运维用，每月执行）
-- DO $$
-- DECLARE
--     next_month DATE;
--     partition_name TEXT;
--     start_date TEXT;
--     end_date TEXT;
-- BEGIN
--     next_month := DATE_TRUNC('month', NOW()) + INTERVAL '1 month';
--     partition_name := 'alert_events_' || TO_CHAR(next_month, 'YYYYMM');
--     start_date := TO_CHAR(next_month, 'YYYY-MM-DD');
--     end_date := TO_CHAR(next_month + INTERVAL '1 month', 'YYYY-MM-DD');
--
--     EXECUTE format(
--         'CREATE TABLE IF NOT EXISTS %I PARTITION OF alert_events FOR VALUES FROM (%L) TO (%L)',
--         partition_name, start_date, end_date
--     );
-- END $$;

-- ============================================
-- 七、性能诊断
-- ============================================

-- 7.1 慢查询分析（需开启 pg_stat_statements）
-- SELECT queryid, query,
--        calls, total_exec_time / 1000 AS total_sec,
--        mean_exec_time AS avg_ms,
--        rows,
--        shared_blks_hit, shared_blks_read
-- FROM pg_stat_statements
-- WHERE query LIKE '%FROM buildings%' OR query LIKE '%FROM cameras%'
-- ORDER BY total_exec_time DESC
-- LIMIT 10;

-- 7.2 表膨胀检查
SELECT schemaname || '.' || relname AS table_name,
       n_dead_tup AS dead_rows,
       n_live_tup AS live_rows,
       ROUND(n_dead_tup * 100.0 / NULLIF(n_live_tup + n_dead_tup, 0), 1) AS dead_pct,
       pg_size_pretty(pg_total_relation_size(relid)) AS total_size
FROM pg_stat_user_tables
WHERE n_dead_tup > 1000
ORDER BY n_dead_tup DESC;

-- 7.3 VACUUM 建议
SELECT schemaname || '.' || relname AS table_name,
       n_dead_tup,
       n_live_tup,
       last_autovacuum,
       CASE
           WHEN n_dead_tup > 100000 THEN '立即 VACUUM'
           WHEN n_dead_tup > 10000 THEN '建议 VACUUM'
           ELSE '正常'
       END AS建议
FROM pg_stat_user_tables
ORDER BY n_dead_tup DESC;

-- ============================================
-- 八、数据导出（示例）
-- ============================================

-- 8.1 导出建筑 GeoJSON（用于前端 GIS 图层）
SELECT jsonb_build_object(
    'type', 'FeatureCollection',
    'features', JSON_AGG(jsonb_build_object(
        'type', 'Feature',
        'geometry', ST_AsGeoJSON(geom)::jsonb,
        'properties', jsonb_build_object(
            'id', id,
            'name', building_name,
            'district', district,
            'floors', floor_count,
            'use_type', use_type,
            'area', total_area
        )
    ))
) AS geojson
FROM buildings
WHERE district = '嘉定区';
