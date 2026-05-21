import React from 'react';
import { Card, Tag, Button, Space } from 'antd';
import {
  HomeOutlined,
  CarOutlined,
  SafetyOutlined,
  EnvironmentOutlined,
  TeamOutlined,
  ClockCircleOutlined,
  DatabaseOutlined
} from '@ant-design/icons';
import { useMapStore } from '@/stores/mapStore';
import { useUIStore } from '@/stores/uiStore';

const resourceTypeMap: Record<string, { label: string; color: string; icon: React.ReactNode }> = {
  building: { label: '建筑资源', color: '#1890ff', icon: <HomeOutlined /> },
  municipal: { label: '市政资源', color: '#52c41a', icon: <CarOutlined /> },
  security: { label: '安防资源', color: '#faad14', icon: <SafetyOutlined /> },
  ecology: { label: '生态资源', color: '#13c2c2', icon: <EnvironmentOutlined /> },
  population: { label: '人口与网格', color: '#722ed1', icon: <TeamOutlined /> }
};

const InfoCard: React.FC = () => {
  const selectedFeature = useMapStore((s) => s.selectedFeature);
  const setDetailPanelOpen = useUIStore((s) => s.setDetailPanelOpen);

  if (!selectedFeature) return null;

  const type = selectedFeature.resourceType || 'building';
  const typeInfo = resourceTypeMap[type] || resourceTypeMap.building;

  return (
    <Card
      size="small"
      style={{
        position: 'absolute',
        top: 16,
        right: 16,
        width: 280,
        zIndex: 10,
        boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
      }}
    >
      <Space direction="vertical" style={{ width: '100%' }} size={8}>
        <Space>
          <span style={{ color: typeInfo.color }}>{typeInfo.icon}</span>
          <strong>{typeInfo.label}</strong>
          <Tag color={typeInfo.color}>{selectedFeature.name || '未知'}</Tag>
        </Space>

        <div style={{ fontSize: 13, color: '#666' }}>
          <div>地址：{selectedFeature.address || '暂无'}</div>
          <div>区域：{selectedFeature.district || '暂无'}</div>
          {selectedFeature.floorCount && <div>楼层数：{selectedFeature.floorCount}</div>}
          {selectedFeature.totalArea && <div>总面积：{selectedFeature.totalArea} m²</div>}
        </div>

        <Space style={{ justifyContent: 'space-between', width: '100%' }}>
          <Space size={4}>
            <ClockCircleOutlined style={{ fontSize: 12, color: '#999' }} />
            <Tag color="green" style={{ fontSize: 11 }}>实时</Tag>
          </Space>
          <Tag icon={<DatabaseOutlined />} color="blue" style={{ fontSize: 11 }}>
            系统数据
          </Tag>
        </Space>

        <Button type="primary" block onClick={() => setDetailPanelOpen(true)}>
          查看详情
        </Button>
      </Space>
    </Card>
  );
};

export default InfoCard;
