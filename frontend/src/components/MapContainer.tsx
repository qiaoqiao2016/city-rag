import React, { useRef, useEffect, useState } from 'react';
import { Button } from 'antd';
import { HomeOutlined } from '@ant-design/icons';
import { CesiumEngine } from '@/engine/CesiumEngine';
import { useMapStore } from '@/stores/mapStore';

const MapContainer: React.FC = () => {
  const containerRef = useRef<HTMLDivElement>(null);
  const engineRef = useRef<CesiumEngine | null>(null);
  const setViewer = useMapStore((s) => s.setViewer);
  const [engineReady, setEngineReady] = useState(false);

  useEffect(() => {
    if (!containerRef.current || engineRef.current) return;

    const engine = new CesiumEngine(containerRef.current, {
      defaultView: [116.397, 39.908],
      defaultZoom: 12
    });

    engineRef.current = engine;
    setViewer(engine.getViewer());
    setEngineReady(true);

    return () => {
      engine.destroy();
      engineRef.current = null;
      setViewer(null);
      setEngineReady(false);
    };
  }, []);

  const handleResetView = () => {
    engineRef.current?.flyTo([116.397, 39.908], 12);
  };

  return (
    <div style={{ width: '100%', height: '100%', position: 'relative' }}>
      <div ref={containerRef} style={{ width: '100%', height: '100%' }} />
      {engineReady && (
        <Button
          type="default"
          icon={<HomeOutlined />}
          onClick={handleResetView}
          style={{
            position: 'absolute',
            bottom: 40,
            right: 20,
            zIndex: 10,
            boxShadow: '0 2px 6px rgba(0,0,0,0.2)'
          }}
        >
          返回初始视图
        </Button>
      )}
    </div>
  );
};

export default MapContainer;
