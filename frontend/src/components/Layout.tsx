import React from 'react';
import { Layout as AntLayout } from 'antd';
import TopToolbar from './TopToolbar';
import SideNav from './SideNav';

const { Content, Footer } = AntLayout;

interface LayoutProps {
  children: React.ReactNode;
  showSider?: boolean;
}

const AppLayout: React.FC<LayoutProps> = ({ children, showSider = true }) => {
  return (
    <AntLayout style={{ height: '100vh', display: 'flex', flexDirection: 'column' }}>
      <TopToolbar />
      <AntLayout style={{ flex: 1, overflow: 'hidden' }}>
        {showSider && <SideNav />}
        <Content style={{ position: 'relative', overflow: 'hidden' }}>
          {children}
        </Content>
      </AntLayout>
      <Footer style={{
        height: 28,
        lineHeight: '28px',
        padding: '0 24px',
        background: '#fafafa',
        borderTop: '1px solid #f0f0f0',
        fontSize: 12,
        color: '#999',
        textAlign: 'right'
      }}>
        城市资源一张图 v1.0 | 数据实时更新
      </Footer>
    </AntLayout>
  );
};

export default AppLayout;
