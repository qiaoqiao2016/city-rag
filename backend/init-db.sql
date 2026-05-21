-- ============================================
-- 城市资源一张图 数据库初始化脚本
-- PostgreSQL + PostGIS
-- ============================================

-- 启用 PostGIS 扩展
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;

-- ============================================
-- 公共字段说明（每张资源表均包含）：
--   id            VARCHAR(64) PRIMARY KEY
--   created_at    TIMESTAMP DEFAULT NOW()
--   updated_at    TIMESTAMP DEFAULT NOW()
--   data_source   VARCHAR(32)  -- iot | api | manual | estimated
-- ============================================

-- ============================================
-- 建筑表
-- ============================================
CREATE TABLE IF NOT EXISTS buildings (
    id              VARCHAR(64) PRIMARY KEY,
    building_name   VARCHAR(200) NOT NULL,
    address         VARCHAR(500),
    district        VARCHAR(50),
    street          VARCHAR(50),
    community       VARCHAR(50),
    total_area      NUMERIC(12, 2),
    floor_count     INT,
    underground_floors INT DEFAULT 0,
    building_height NUMERIC(8, 2),
    structure_type  VARCHAR(20),
    build_year      INT,
    use_type        VARCHAR(30),
    occupancy_rate  NUMERIC(5, 2),
    geom            GEOMETRY(POLYGON, 4326),
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW(),
    data_source     VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_buildings_district ON buildings(district);
CREATE INDEX IF NOT EXISTS idx_buildings_use_type ON buildings(use_type);
CREATE INDEX IF NOT EXISTS idx_buildings_geom_gist ON buildings USING GIST(geom);
CREATE INDEX IF NOT EXISTS idx_buildings_updated ON buildings(updated_at);

-- ============================================
-- 道路表
-- ============================================
CREATE TABLE IF NOT EXISTS roads (
    id                  VARCHAR(64) PRIMARY KEY,
    road_name           VARCHAR(200) NOT NULL,
    road_level          VARCHAR(20),
    length              NUMERIC(10, 2),
    width               NUMERIC(6, 2),
    lane_count          INT,
    start_point         VARCHAR(200),
    end_point           VARCHAR(200),
    maintenance_status  VARCHAR(20),
    road_manager        VARCHAR(100),
    manager_phone       VARCHAR(20),
    traffic_status      VARCHAR(10) DEFAULT '畅通',
    geom                GEOMETRY(LINESTRING, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_roads_level ON roads(road_level);
CREATE INDEX IF NOT EXISTS idx_roads_geom_gist ON roads USING GIST(geom);

-- ============================================
-- 摄像头表
-- ============================================
CREATE TABLE IF NOT EXISTS cameras (
    id              VARCHAR(64) PRIMARY KEY,
    camera_name     VARCHAR(200),
    camera_type     VARCHAR(20),
    resolution      VARCHAR(20),
    install_height  NUMERIC(6, 2),
    direction       NUMERIC(5, 2),
    focal_length    NUMERIC(6, 2),
    ip_address      VARCHAR(45),
    stream_url      VARCHAR(500),
    protocol        VARCHAR(20),
    storage_days    INT DEFAULT 30,
    ai_capability   TEXT[],
    online_status   VARCHAR(10) DEFAULT 'online',
    geom            GEOMETRY(POINT, 4326),
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW(),
    data_source     VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_cameras_status ON cameras(online_status);
CREATE INDEX IF NOT EXISTS idx_cameras_type ON cameras(camera_type);
CREATE INDEX IF NOT EXISTS idx_cameras_geom_gist ON cameras USING GIST(geom);

-- ============================================
-- 网格表
-- ============================================
CREATE TABLE IF NOT EXISTS grids (
    id              VARCHAR(64) PRIMARY KEY,
    grid_code       VARCHAR(50) NOT NULL,
    grid_type       VARCHAR(20),
    grid_master     VARCHAR(100),
    master_phone    VARCHAR(20),
    grid_worker     VARCHAR(100),
    worker_phone    VARCHAR(20),
    population_count INT,
    building_count  INT,
    enterprise_count INT,
    place_count     INT,
    geom            GEOMETRY(POLYGON, 4326),
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_grids_code ON grids(grid_code);
CREATE INDEX IF NOT EXISTS idx_grids_geom_gist ON grids USING GIST(geom);

-- ============================================
-- 人口表
-- ============================================
CREATE TABLE IF NOT EXISTS populations (
    id                  VARCHAR(64) PRIMARY KEY,
    id_card             VARCHAR(18) UNIQUE,
    name                VARCHAR(100) NOT NULL,
    gender              VARCHAR(10),
    birth_date          DATE,
    nationality         VARCHAR(50),
    phone               VARCHAR(20),
    household_id        VARCHAR(64),
    building_id         VARCHAR(64),
    residence_type      VARCHAR(20),
    special_tag         VARCHAR(50),
    is_rental           BOOLEAN DEFAULT FALSE,
    district            VARCHAR(50),
    street              VARCHAR(50),
    community           VARCHAR(50),
    grid_id             VARCHAR(64),
    geom                GEOMETRY(POINT, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_populations_grid ON populations(grid_id);
CREATE INDEX IF NOT EXISTS idx_populations_building ON populations(building_id);
CREATE INDEX IF NOT EXISTS idx_populations_type ON populations(residence_type);
CREATE INDEX IF NOT EXISTS idx_populations_geom_gist ON populations USING GIST(geom);

-- ============================================
-- 户表
-- ============================================
CREATE TABLE IF NOT EXISTS households (
    id                  VARCHAR(64) PRIMARY KEY,
    household_code      VARCHAR(50) NOT NULL,
    building_id         VARCHAR(64),
    floor               VARCHAR(10),
    room_number         VARCHAR(30),
    head_name           VARCHAR(100),
    member_count        INT DEFAULT 1,
    area                NUMERIC(10, 2),
    ownership           VARCHAR(20),
    district            VARCHAR(50),
    street              VARCHAR(50),
    community           VARCHAR(50),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_households_building ON households(building_id);

-- ============================================
-- 企业表
-- ============================================
CREATE TABLE IF NOT EXISTS enterprises (
    id                  VARCHAR(64) PRIMARY KEY,
    enterprise_name     VARCHAR(300) NOT NULL,
    credit_code         VARCHAR(18),
    legal_person        VARCHAR(100),
    phone               VARCHAR(20),
    industry_type       VARCHAR(50),
    enterprise_scale    VARCHAR(20),
    employee_count      INT,
    annual_revenue      NUMERIC(16, 2),
    building_id         VARCHAR(64),
    district            VARCHAR(50),
    street              VARCHAR(50),
    community           VARCHAR(50),
    geom                GEOMETRY(POINT, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_enterprises_building ON enterprises(building_id);
CREATE INDEX IF NOT EXISTS idx_enterprises_geom_gist ON enterprises USING GIST(geom);

-- ============================================
-- 井盖表
-- ============================================
CREATE TABLE IF NOT EXISTS manhole_covers (
    id                  VARCHAR(64) PRIMARY KEY,
    cover_code          VARCHAR(50) NOT NULL,
    cover_type          VARCHAR(30),
    material            VARCHAR(20),
    diameter            NUMERIC(6, 2),
    status              VARCHAR(10) DEFAULT 'normal',
    water_level         NUMERIC(6, 2),
    last_maintain_at    TIMESTAMP,
    maintain_company    VARCHAR(200),
    district            VARCHAR(50),
    street              VARCHAR(50),
    geom                GEOMETRY(POINT, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_manhole_status ON manhole_covers(status);
CREATE INDEX IF NOT EXISTS idx_manhole_geom_gist ON manhole_covers USING GIST(geom);

-- ============================================
-- 路灯表
-- ============================================
CREATE TABLE IF NOT EXISTS street_lights (
    id                  VARCHAR(64) PRIMARY KEY,
    light_code          VARCHAR(50) NOT NULL,
    light_type          VARCHAR(30),
    pole_height         NUMERIC(6, 2),
    power_rating        NUMERIC(6, 2),
    status              VARCHAR(10) DEFAULT 'on',
    brightness          INT DEFAULT 100,
    control_mode        VARCHAR(20),
    district            VARCHAR(50),
    street              VARCHAR(50),
    geom                GEOMETRY(POINT, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_lights_status ON street_lights(status);
CREATE INDEX IF NOT EXISTS idx_lights_geom_gist ON street_lights USING GIST(geom);

-- ============================================
-- 生态监测站表
-- ============================================
CREATE TABLE IF NOT EXISTS ecology_stations (
    id                  VARCHAR(64) PRIMARY KEY,
    station_name        VARCHAR(200) NOT NULL,
    station_type        VARCHAR(30),
    monitor_factors     TEXT[],
    status              VARCHAR(10) DEFAULT 'online',
    district            VARCHAR(50),
    street              VARCHAR(50),
    geom                GEOMETRY(POINT, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_ecology_type ON ecology_stations(station_type);
CREATE INDEX IF NOT EXISTS idx_ecology_geom_gist ON ecology_stations USING GIST(geom);

-- ============================================
-- 消防栓表
-- ============================================
CREATE TABLE IF NOT EXISTS fire_hydrants (
    id                  VARCHAR(64) PRIMARY KEY,
    hydrant_code        VARCHAR(50) NOT NULL,
    hydrant_type        VARCHAR(30),
    pressure            NUMERIC(6, 2),
    flow_rate           NUMERIC(6, 2),
    status              VARCHAR(10) DEFAULT 'normal',
    last_inspect_at     TIMESTAMP,
    district            VARCHAR(50),
    street              VARCHAR(50),
    geom                GEOMETRY(POINT, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_hydrants_geom_gist ON fire_hydrants USING GIST(geom);

-- ============================================
-- 安防岗点表
-- ============================================
CREATE TABLE IF NOT EXISTS police_stations (
    id                  VARCHAR(64) PRIMARY KEY,
    station_name        VARCHAR(200) NOT NULL,
    station_type        VARCHAR(30),
    personnel_count     INT,
    duty_phone          VARCHAR(20),
    district            VARCHAR(50),
    street              VARCHAR(50),
    geom                GEOMETRY(POINT, 4326),
    created_at          TIMESTAMP DEFAULT NOW(),
    updated_at          TIMESTAMP DEFAULT NOW(),
    data_source         VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_police_geom_gist ON police_stations USING GIST(geom);

-- ============================================
-- 告警规则表
-- ============================================
CREATE TABLE IF NOT EXISTS alert_rules (
    rule_id         VARCHAR(64) PRIMARY KEY,
    rule_name       VARCHAR(200) NOT NULL,
    resource_type   VARCHAR(50),
    rule_type       VARCHAR(20),
    enabled         BOOLEAN DEFAULT TRUE,
    priority        VARCHAR(10) DEFAULT 'medium',
    conditions      JSONB,
    logic           VARCHAR(5) DEFAULT 'AND',
    eval_interval   INT DEFAULT 60,
    notify_channels TEXT[],
    notify_roles    TEXT[],
    escalation_delay INT DEFAULT 300,
    auto_recover    BOOLEAN DEFAULT TRUE,
    mute_periods    JSONB,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_rules_enabled ON alert_rules(enabled, resource_type);

-- ============================================
-- 告警事件表（按月分区）
-- ============================================
CREATE TABLE IF NOT EXISTS alert_events (
    id              VARCHAR(64) PRIMARY KEY,
    rule_id         VARCHAR(64) REFERENCES alert_rules(rule_id),
    rule_name       VARCHAR(200),
    resource_type   VARCHAR(50),
    resource_id     VARCHAR(64),
    resource_name   VARCHAR(200),
    priority        VARCHAR(10),
    status          VARCHAR(20) DEFAULT 'pending',
    trigger_value   TEXT,
    threshold       TEXT,
    duration        INT,
    confirmed_by    VARCHAR(100),
    confirmed_at    TIMESTAMP,
    resolved_by     VARCHAR(100),
    resolved_at     TIMESTAMP,
    resolution_note TEXT,
    trigger_count   INT DEFAULT 1,
    geom            GEOMETRY(POINT, 4326),
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
) PARTITION BY RANGE (created_at);

-- 创建初始分区
CREATE TABLE IF NOT EXISTS alert_events_202605 PARTITION OF alert_events
    FOR VALUES FROM ('2026-05-01') TO ('2026-06-01');
CREATE TABLE IF NOT EXISTS alert_events_202606 PARTITION OF alert_events
    FOR VALUES FROM ('2026-06-01') TO ('2026-07-01');
CREATE TABLE IF NOT EXISTS alert_events_202607 PARTITION OF alert_events
    FOR VALUES FROM ('2026-07-01') TO ('2026-08-01');

CREATE INDEX IF NOT EXISTS idx_alerts_status ON alert_events(status);
CREATE INDEX IF NOT EXISTS idx_alerts_priority ON alert_events(priority);
CREATE INDEX IF NOT EXISTS idx_alerts_created ON alert_events(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_alerts_rule_resource ON alert_events(rule_id, resource_id);

-- ============================================
-- 数据冲突记录表
-- ============================================
CREATE TABLE IF NOT EXISTS data_conflicts (
    id                  VARCHAR(64) PRIMARY KEY,
    resource_type       VARCHAR(50),
    resource_id         VARCHAR(64),
    field_name          VARCHAR(100),
    source_a            VARCHAR(50),
    value_a             TEXT,
    source_b            VARCHAR(50),
    value_b             TEXT,
    resolved_by_rule    VARCHAR(50),
    resolved_value      TEXT,
    status              VARCHAR(10) DEFAULT 'pending',
    created_at          TIMESTAMP DEFAULT NOW(),
    resolved_at         TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_conflicts_status ON data_conflicts(status);
CREATE INDEX IF NOT EXISTS idx_conflicts_resource ON data_conflicts(resource_type, resource_id);

-- ============================================
-- 操作审计日志表
-- ============================================
CREATE TABLE IF NOT EXISTS audit_logs (
    id              BIGSERIAL PRIMARY KEY,
    user_id         VARCHAR(64),
    username        VARCHAR(100),
    action          VARCHAR(50),
    resource_type   VARCHAR(50),
    resource_id     VARCHAR(64),
    params          JSONB,
    ip_address      VARCHAR(45),
    duration_ms     INT,
    result          VARCHAR(10),
    created_at      TIMESTAMP DEFAULT NOW()
) PARTITION BY RANGE (created_at);

CREATE TABLE IF NOT EXISTS audit_logs_202605 PARTITION OF audit_logs
    FOR VALUES FROM ('2026-05-01') TO ('2026-06-01');
CREATE TABLE IF NOT EXISTS audit_logs_202606 PARTITION OF audit_logs
    FOR VALUES FROM ('2026-06-01') TO ('2026-07-01');

CREATE INDEX IF NOT EXISTS idx_audit_user ON audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_action ON audit_logs(action);
CREATE INDEX IF NOT EXISTS idx_audit_created ON audit_logs(created_at DESC);

-- ============================================
-- 物化视图
-- ============================================
CREATE MATERIALIZED VIEW IF NOT EXISTS v_building_aggregation AS
SELECT
    b.id AS building_id,
    b.building_name,
    b.district, b.street, b.community,
    b.total_area, b.floor_count, b.use_type, b.occupancy_rate,
    COUNT(DISTINCT h.id) AS household_count,
    COUNT(DISTINCT p.id) AS population_count,
    COUNT(DISTINCT e.id) AS enterprise_count,
    b.geom
FROM buildings b
LEFT JOIN households h ON h.building_id = b.id
LEFT JOIN populations p ON p.household_id = h.id
LEFT JOIN enterprises e ON e.building_id = b.id
GROUP BY b.id;

CREATE UNIQUE INDEX IF NOT EXISTS idx_v_building ON v_building_aggregation(building_id);

-- ============================================
-- 插入默认告警规则
-- ============================================
INSERT INTO alert_rules (rule_id, rule_name, resource_type, rule_type, enabled, priority, conditions, logic, eval_interval, notify_channels, notify_roles, escalation_delay, auto_recover)
VALUES
('rule-manhole-waterlevel', '雨水井水位超限', 'manhole_cover', 'threshold', TRUE, 'high', '[{"metric":"water_level","operator":">","value":0.8,"unit":"m","duration":30}]', 'AND', 60, '{popup,sms}', '{grid_worker,supervisor}', 300, TRUE),
('rule-manhole-open', '井盖异常开启', 'manhole_cover', 'event', TRUE, 'high', '[{"metric":"tilt_sensor","operator":"==","value":"opened"}]', 'AND', 30, '{popup,sms}', '{grid_worker}', 600, TRUE),
('rule-camera-offline', '摄像头离线', 'camera', 'threshold', TRUE, 'high', '[{"metric":"last_heartbeat","operator":">","value":300,"unit":"s"}]', 'AND', 60, '{popup}', '{supervisor}', 600, TRUE),
('rule-trash-bin-full', '垃圾桶满溢', 'trash_bin', 'threshold', TRUE, 'low', '[{"metric":"fill_level","operator":">","value":95,"unit":"%","duration":60}]', 'AND', 120, '{popup}', '{grid_worker}', 1800, TRUE);
