import React from 'react';
import AppLayout from '@/components/Layout';
import MapContainer from '@/components/MapContainer';
import InfoCard from '@/components/InfoCard';
import DetailPanel from '@/components/DetailPanel';

const MapPage: React.FC = () => {
  return (
    <AppLayout>
      <MapContainer />
      <InfoCard />
      <DetailPanel />
    </AppLayout>
  );
};

export default MapPage;
