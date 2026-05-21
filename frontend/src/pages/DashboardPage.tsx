import React from 'react';
import {
  Row,
  Col,
  Card,
  Statistic,
  Table,
  Typography,
  Space,
  Button
} from 'antd';
import {
  HomeOutlined,
  CameraOutlined,
  TeamOutlined,
  AlertOutlined,
  ArrowLeftOutlined
} from '@ant-design/icons';
import ReactEChartsCore from 'echarts-for-react';
import { useNavigate } from 'react-router-dom';
import AppLayout from '@/components/Layout';

const { Title } = Typography;

const statsCards = [
  { title: '建筑总数', value: 12580, icon: <HomeOutlined />, color: '#1890ff' },
  { title: '监控点位', value: 3420, icon: <CameraOutlined />, color: '#52c41a' },
  { title: '常住人口', value: '约 215 万', icon: <TeamOutlined />, color: '#722ed1' },
  { title: '今日告警', value: 23, icon: <AlertOutlined />, color: '#ff4d4f' }
];

const trendOption = {
  tooltip: { trigger: 'axis' },
  legend: { data: ['建筑新增', '告警事件'] },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
  },
  yAxis: { type: 'value' },
  series: [
    {
      name: '建筑新增',
      type: 'bar',
      data: [18, 25, 32, 20, 45, 38, 42, 30, 28, 35, 40, 48],
      itemStyle: { color: '#1890ff' }
    },
    {
      name: '告警事件',
      type: 'line',
      data: [8, 12, 15, 10, 18, 22, 20, 16, 14, 19, 25, 23],
      itemStyle: { color: '#ff4d4f' }
    }
  ]
};

const topAreaColumns = [
  { title: '排名', key: 'rank', width: 60, render: (_: any, __: any, i: number) => i + 1 },
  { title: '区域名称', dataIndex: 'name', key: 'name' },
  { title: '人口数量', dataIndex: 'population', key: 'population', sorter: (a: any, b: any) => a.population - b.population },
  { title: '告警数', dataIndex: 'alerts', key: 'alerts', sorter: (a: any, b: any) => a.alerts - b.alerts }
];

const topAreaData = [
  { name: '中关村街道', population: 125000, alerts: 45 },
  { name: '国贸街道', population: 112000, alerts: 38 },
  { name: '五道口街道', population: 98000, alerts: 32 },
  { name: '望京街道', population: 89000, alerts: 28 },
  { name: '三里屯街道', population: 76000, alerts: 25 },
  { name: '西二旗街道', population: 72000, alerts: 22 },
  { name: '金融街街道', population: 68000, alerts: 20 },
  { name: 'CBD街道', population: 65000, alerts: 18 },
  { name: '上地街道', population: 61000, alerts: 15 },
  { name: '知春路街道', population: 58000, alerts: 12 }
];

const DashboardPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <AppLayout showSider={false}>
      <div style={{ padding: 24, height: '100%', overflow: 'auto' }}>
        <Row align="middle" style={{ marginBottom: 24 }}>
          <Col>
            <Space>
              <Button
                type="text"
                icon={<ArrowLeftOutlined />}
                onClick={() => navigate('/')}
              />
              <Title level={4} style={{ margin: 0 }}>数据看板</Title>
            </Space>
          </Col>
        </Row>

        <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
          {statsCards.map((card) => (
            <Col span={6} key={card.title}>
              <Card hoverable>
                <Statistic
                  title={card.title}
                  value={card.value}
                  prefix={
                    <span style={{ color: card.color }}>{card.icon}</span>
                  }
                  valueStyle={{ color: card.color }}
                />
              </Card>
            </Col>
          ))}
        </Row>

        <Row gutter={[16, 16]}>
          <Col span={16}>
            <Card title="年度趋势" size="small">
              <ReactEChartsCore option={trendOption} style={{ height: 360 }} />
            </Card>
          </Col>
          <Col span={8}>
            <Card title="区域排名 TOP10" size="small" bodyStyle={{ padding: 0 }}>
              <Table
                columns={topAreaColumns}
                dataSource={topAreaData}
                rowKey="name"
                pagination={false}
                size="small"
              />
            </Card>
          </Col>
        </Row>
      </div>
    </AppLayout>
  );
};

export default DashboardPage;
