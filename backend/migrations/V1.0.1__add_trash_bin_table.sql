-- ============================================
-- V1.0.1 新增垃圾桶表
-- 说明：垃圾桶满溢告警规则的资源实体
-- 日期：2026-05-21
-- ============================================

BEGIN;

-- ============================================
-- UP: 建表
-- ============================================

CREATE TABLE IF NOT EXISTS trash_bins (
    id              VARCHAR(64) PRIMARY KEY,
    bin_code        VARCHAR(50) NOT NULL,
    bin_type        VARCHAR(20),       -- 可回收/厨余/其他/有害
    capacity        NUMERIC(6, 2),     -- 容量(L)
    fill_level      NUMERIC(5, 2),     -- 当前填充百分比
    temperature     NUMERIC(5, 2),     -- 内部温度(℃)
    status          VARCHAR(10) DEFAULT 'normal',
    district        VARCHAR(50),
    street          VARCHAR(50),
    community       VARCHAR(50),
    geom            GEOMETRY(POINT, 4326),
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW(),
    data_source     VARCHAR(32)
);
CREATE INDEX IF NOT EXISTS idx_trash_bins_status ON trash_bins(status);
CREATE INDEX IF NOT EXISTS idx_trash_bins_geom_gist ON trash_bins USING GIST(geom);

-- ============================================
-- DOWN: 回滚
-- DROP TABLE IF EXISTS trash_bins;
-- ============================================

COMMIT;
