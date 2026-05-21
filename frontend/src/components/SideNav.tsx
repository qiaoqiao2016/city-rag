import React from 'react';
import { Layout, Menu } from 'antd';
import {
  HomeOutlined,
  CarOutlined,
  SafetyOutlined,
  EnvironmentOutlined,
  TeamOutlined
} from '@ant-design/icons';
import { useUIStore, ResourceType } from '@/stores/uiStore';

const { Sider } = Layout;

const menuItems = [
  { key: 'building', icon: <HomeOutlined />, label: '建筑资源' },
  { key: 'municipal', icon: <CarOutlined />, label: '市政资源' },
  { key: 'security', icon: <SafetyOutlined />, label: '安防资源' },
  { key: 'ecology', icon: <EnvironmentOutlined />, label: '生态资源' },
  { key: 'population', icon: <TeamOutlined />, label: '人口与网格' }
];

const SideNav: React.FC = () => {
  const activeResourceType = useUIStore((s) => s.activeResourceType);
  const setActiveResourceType = useUIStore((s) => s.setActiveResourceType);

  return (
    <Sider
      width={64}
      style={{
        background: '#fff',
        borderRight: '1px solid #f0f0f0',
        overflow: 'auto'
      }}
    >
      <Menu
        mode="inline"
        selectedKeys={[activeResourceType]}
        onClick={({ key }) => setActiveResourceType(key as ResourceType)}
        items={menuItems}
        style={{ borderRight: 0, paddingTop: 8 }}
      />
    </Sider>
  );
};

export default SideNav;
