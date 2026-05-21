import React from 'react';
import { Drawer, Tabs, Descriptions, Table, Tag } from 'antd';
import { useUIStore } from '@/stores/uiStore';
import { useMapStore } from '@/stores/mapStore';

const { TabPane } = Tabs;

const relatedDataColumns = [
  { title: '资源名称', dataIndex: 'name', key: 'name' },
  { title: '类型', dataIndex: 'type', key: 'type' },
  { title: '状态', dataIndex: 'status', key: 'status', render: (v: string) => <Tag color={v === '正常' ? 'green' : 'orange'}>{v}</Tag> }
];

const historyColumns = [
  { title: '时间', dataIndex: 'time', key: 'time' },
  { title: '操作', dataIndex: 'action', key: 'action' },
  { title: '操作人', dataIndex: 'operator', key: 'operator' }
];

const DetailPanel: React.FC = () => {
  const detailPanelOpen = useUIStore((s) => s.detailPanelOpen);
  const setDetailPanelOpen = useUIStore((s) => s.setDetailPanelOpen);
  const selectedFeature = useMapStore((s) => s.selectedFeature);

  const feature = selectedFeature || {};

  return (
    <Drawer
      title="资源详情"
      placement="right"
      width={480}
      open={detailPanelOpen}
      onClose={() => setDetailPanelOpen(false)}
    >
      <Tabs defaultActiveKey="basic">
        <TabPane tab="基本信息" key="basic">
          <Descriptions column={2} size="small" bordered>
            <Descriptions.Item label="名称" span={2}>{feature.name || '--'}</Descriptions.Item>
            <Descriptions.Item label="地址">{feature.address || '--'}</Descriptions.Item>
            <Descriptions.Item label="所属区域">{feature.district || '--'}</Descriptions.Item>
            <Descriptions.Item label="街道">{feature.street || '--'}</Descriptions.Item>
            <Descriptions.Item label="楼层数">{feature.floorCount ?? '--'}</Descriptions.Item>
            <Descriptions.Item label="总面积">{feature.totalArea ? ${feature.totalArea} m² : '--'}</Descriptions.Item>
            <Descriptions.Item label="使用类型">{feature.useType || '--'}</Descriptions.Item>
            <Descriptions.Item label="入住率">{feature.occupancyRate != null ? ${feature.occupancyRate}% : '--'}</Descriptions.Item>
          </Descriptions>
        </TabPane>
        <TabPane tab="关联数据" key="related">
          <Table
            dataSource={[]}
            columns={relatedDataColumns}
            rowKey="name"
            pagination={false}
            size="small"
            locale={{ emptyText: '暂无关联数据' }}
          />
        </TabPane>
        <TabPane tab="历史记录" key="history">
          <Table
            dataSource={[]}
            columns={historyColumns}
            rowKey="time"
            pagination={false}
            size="small"
            locale={{ emptyText: '暂无历史记录' }}
          />
        </TabPane>
      </Tabs>
    </Drawer>
  );
};

export default DetailPanel;
