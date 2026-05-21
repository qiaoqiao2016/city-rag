import { create } from 'zustand';
import * as Cesium from 'cesium';

export interface MapStore {
  viewer: any | null;
  activeLayerIds: string[];
  center: [number, number];
  zoom: number;
  selectedFeature: any | null;
  setViewer: (v: any) => void;
  setSelectedFeature: (f: any | null) => void;
  toggleLayer: (id: string) => void;
  flyTo: (coords: [number, number], zoom: number) => void;
}

export const useMapStore = create<MapStore>((set, get) => ({
  viewer: null,
  activeLayerIds: ['building', 'grid'],
  center: [116.397, 39.908],
  zoom: 12,
  selectedFeature: null,
  setViewer: (v) => set({ viewer: v }),
  setSelectedFeature: (f) => set({ selectedFeature: f }),
  toggleLayer: (id) =>
    set((state) => ({
      activeLayerIds: state.activeLayerIds.includes(id)
        ? state.activeLayerIds.filter((i) => i !== id)
        : [...state.activeLayerIds, id]
    })),
  flyTo: (coords, zoom) => {
    const { viewer } = get();
    if (viewer) {
      const [lng, lat] = coords;
      const height = 10000000 / Math.pow(2, zoom);
      viewer.camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(lng, lat, Math.max(height, 500))
      });
    }
  }
}));
