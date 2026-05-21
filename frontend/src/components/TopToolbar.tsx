import React from 'react';
import { Layout, Space, Input, Badge, Popover, Switch, Cascader, Button, Tooltip } from 'antd';
import {
  BellOutlined,
  FullscreenOutlined,
  FullscreenExitOutlined,
  SearchOutlined,
  LayerOutlined
} from '@ant-design/icons';
import { useAlertStore } from '@/stores/alertStore';
import { useNavigate } from 'react-router-dom';

const { Header } = Layout;

const regionOptions = [
  {
    value: 'haidian',
    label: '海淀区',
    children: [
      {
        value: 'zhongguancun',
        label: '中关村街道',
        children: [
          { value: 'huayuan', label: '华苑社区' },
          { value: 'keji', label: '科技园社区' }
        ]
      }
    ]
  },
  {
    value: 'chaoyang',
    label: '朝阳区',
    children: [
      {
        value: 'guomao',
        label: '国贸街道',
        children: [
          { value: 'jinsong', label: '劲松社区' },
          { value: 'jianguomen', label: '建国门社区' }
        ]
      }
    ]
  }
];

const layerList = [
  { id: 'building', label: '建筑资源', color: '#1890ff' },
  { id: 'grid', label: '网格划分', color: '#52c41a' },
  { id: 'camera', label: '监控点位', color: '#faad14' },
  { id: 'ecology', label: '生态绿地', color: '#13c2c2' }
];

const TopToolbar: React.FC = () => {
  const [fullscreen, setFullscreen] = React.useState(false);
  const unreadCount = useAlertStore((s) => s.unreadCount);
  const navigate = useNavigate();

  const toggleFullscreen = () => {
    if (!document.fullscreenElement) {
      document.documentElement.requestFullscreen();
      setFullscreen(true);
    } else {
      document.exitFullscreen();
      setFullscreen(false);
    }
  };

  const layerContent = (
    <div style={{ width: 180 }}>
      {layerList.map((layer) => (
        <div key={layer.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '4px 0' }}>
          <span>
            <span style={{ color: layer.color, marginRight: 8 }}>●</span>
            {layer.label}
          </span>
          <Switch defaultChecked size="small" />
        </div>
      ))}
    </div>
  );

  return (
    <Header style={{
      background: '#fff',
      height: 56,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      padding: '0 24px',
      borderBottom: '1px solid #f0f0f0',
      boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
      position: 'relative',
      zIndex: 100
    }}>
      <div style={{ fontSize: 18, fontWeight: 600, color: '#1677ff' }}>
        城市资源一张图
      </div>
      <Space size="middle">
        <Cascader
          options={regionOptions}
          placeholder="区域选择"
          allowClear
          style={{ width: 200 }}
        />
        <Input.Search
          placeholder="搜索资源..."
          prefix={<SearchOutlined />}
          style={{ width: 280 }}
        />
        <Popover content={layerContent} title="图层控制" trigger="click">
          <Button type="text" icon={<LayerOutlined />}>图层控制</Button>
        </Popover>
        <Badge count={unreadCount} size="small">
          <Button
            type="text"
            icon={<BellOutlined />}
            onClick={() => navigate('/alert-center')}
          >
            告警中心
          </Button>
        </Badge>
        <Tooltip title={fullscreen ? '退出全屏' : '全屏'}>
          <Button
            type="text"
            icon={fullscreen ? <FullscreenExitOutlined /> : <FullscreenOutlined />}
            onClick={toggleFullscreen}
          />
        </Tooltip>
      </Space>
    </Header>
  );
};

export default TopToolbar;
