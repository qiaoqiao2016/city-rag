import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import MapPage from './pages/MapPage';
import AlertCenter from './pages/AlertCenter';
import DashboardPage from './pages/DashboardPage';

const App: React.FC = () => {
  return (
    <ConfigProvider locale={zhCN}>
      <Routes>
        <Route path="/" element={<MapPage />} />
        <Route path="/alert-center" element={<AlertCenter />} />
        <Route path="/dashboard" element={<DashboardPage />} />
      </Routes>
    </ConfigProvider>
  );
};

export default App;
