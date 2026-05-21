# 城市资源一张图系统

城市各类静态、动态资源可视化落地平台，实现**底数清、情况明**的城市治理目标。

## 核心功能

- **五大资源可视化**：建筑资源、市政资源、安防资源、生态资源、人口与网格资源
- **三维地图引擎**：基于 CesiumJS 的三维 GIS 展示，支持白模加载和数字孪生
- **空间分析**：框选查询、缓冲区分析、距离测量、多边形绘制
- **跨资源关联**：楼栋-人口-企业关联、场所周边安防、网格全息画像
- **告警规则引擎**：阈值/基线/事件/复合规则，支持多渠道通知推送
- **综合态势大屏**：全局统计、趋势分析、区域排名

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端框架 | React 18 + TypeScript 5 |
| 地图引擎 | CesiumJS 1.120+ |
| 可视化 | ECharts 5 + Deck.gl |
| 状态管理 | Zustand |
| 后端框架 | Spring Boot 3.3 + Java 17 |
| ORM | MyBatis-Plus 3.5 |
| 空间数据库 | PostgreSQL 15 + PostGIS 3.4 |
| 时序数据库 | InfluxDB 2.x |
| 缓存 | Redis 7.x |
| 消息队列 | Kafka 3.x |
| MQTT Broker | EMQX 5.x |
| API 网关 | Spring Cloud Gateway |

## 快速开始

### 前置条件

- Docker & Docker Compose
- Java 17+、Maven 3.9+
- Node.js 20+、npm

### 1. 启动基础设施

```bash
cd backend
docker-compose up -d
```

### 2. 启动后端

```bash
cd backend
mvn clean package -DskipTests -T 4
java -jar city-rag-gateway/target/city-rag-gateway-1.0.0.jar
```

后端默认运行在 `http://localhost:8080`，API 文档：`http://localhost:8080/swagger-ui.html`

### 3. 启动前端

```bash
cd frontend
npm install
cp .env.example .env    # 编辑 .env 填入 Cesium Ion Token 和天地图 Token
npm run dev
```

前端默认运行在 `http://localhost:3000`

## 项目结构

```
city-rag/
├── backend/                          # 后端（Spring Boot Maven 多模块）
│   ├── city-rag-common/              # 公共模块：DTO、常量、异常
│   ├── city-rag-dao/                 # 数据访问层：实体、Mapper
│   ├── city-rag-api/                 # REST API：Controller
│   ├── city-rag-service/             # 业务服务层
│   ├── city-rag-gateway/             # API 网关：路由、认证
│   ├── city-rag-stream/              # Kafka Streams 流处理
│   ├── city-rag-ingestion/           # 数据接入：MQTT、文件导入
│   ├── city-rag-notification/        # 通知推送：WebSocket、短信
│   ├── docker-compose.yml            # 基础设施容器编排
│   └── init-db.sql                   # 数据库初始化脚本
├── frontend/                         # 前端（React + Vite）
│   ├── src/
│   │   ├── engine/                   # CesiumJS 引擎封装
│   │   ├── components/               # UI 组件
│   │   ├── pages/                    # 页面
│   │   ├── stores/                   # Zustand 状态管理
│   │   ├── api/                      # API 客户端
│   │   └── styles/                   # 全局样式
│   └── vite.config.ts
├── docs/                             # 文档
│   ├── 城市资源一张图可视化需求文档.md
│   ├── 城市资源一张图系统设计文档.md
│   └── 部署文档.md
└── scripts/                          # 工具脚本
```

## 系统架构（六层）

```
应用呈现层    GIS Map | 告警中心 | 综合大屏 | 第三方 API
API 网关层    REST API | WebSocket | OAuth2 认证鉴权
业务服务层    资源查询 | 空间分析 | 统计聚合 | 告警规则引擎
消息流处理层   Kafka Streams 规则评估 | 消息队列 | 事件总线
数据存储层    PostGIS 空间数据 | 业务数据 | InfluxDB 时序 | Redis 缓存
数据接入层    MQTT IoT 接入 | ETL 调度 | 批量导入 | 第三方 API 对接
```

更多架构细节请参阅 [系统设计文档](docs/城市资源一张图系统设计文档.md)。

## API 概览

| 端点 | 说明 |
|------|------|
| `GET /api/v1/buildings` | 建筑资源列表（支持空间过滤） |
| `GET /api/v1/spatial/within` | 多边形空间包含查询 |
| `GET /api/v1/spatial/buffer` | 缓冲区查询 |
| `GET /api/v1/alerts` | 告警事件列表 |
| `PUT /api/v1/alerts/{id}/confirm` | 确认告警 |
| `GET /api/v1/stats/summary` | 全局统计摘要 |
| `GET /api/v1/cross/building-population/{id}` | 楼栋-人口-企业关联 |

## 部署

详见 [部署文档](docs/部署文档.md)。

## 开发阶段

| Phase | 内容 | 周期 |
|-------|------|------|
| Phase 1 | 基础框架与建筑资源 | 第 1-3 周 |
| Phase 2 | 市政与安防资源 | 第 4-6 周 |
| Phase 3 | 生态与人口网格资源 | 第 7-9 周 |
| Phase 4 | 跨资源关联与空间分析 | 第 10-12 周 |
| Phase 5 | 告警规则引擎 | 第 13-16 周 |
| Phase 6 | 大屏与综合态势 | 第 17-18 周 |
| Phase 7 | API 开放与安全 | 第 19-21 周 |
| Phase 8 | 性能优化与运维 | 第 22-23 周 |

## License

MIT
