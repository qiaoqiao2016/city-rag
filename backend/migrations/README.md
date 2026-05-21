# 数据库迁移脚本

## 命名规范

迁移脚本按版本号命名，格式：`V<主版本>.<次版本>.<序号>__<描述>.sql`

示例：
- `V1.0.1__add_trash_bin_table.sql`
- `V1.1.0__add_alert_escalation.sql`
- `V1.2.0__optimize_spatial_indexes.sql`

## 使用方式

### 开发环境
直接执行对应的 SQL 文件：
```bash
docker exec -i city-rag-postgres psql -U cityrag -d city_rag < migrations/V1_0_1__add_trash_bin_table.sql
```

### 生产环境
使用 Flyway 或 Liquibase 管理迁移，或手动执行并记录变更日志。

## 编写规范

1. **幂等性**：使用 `IF NOT EXISTS` / `IF EXISTS`
2. **可回滚**：提供 `-- DOWN` 注释段注明回滚语句
3. **事务包裹**：DDL 和 DML 放在 `BEGIN; ... COMMIT;` 内
4. **注释完整**：说明变更原因和影响范围
