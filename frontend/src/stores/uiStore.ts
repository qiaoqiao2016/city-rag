import { create } from 'zustand';

export type ResourceType = 'building' | 'municipal' | 'security' | 'ecology' | 'population';

export interface UIStore {
  activeResourceType: ResourceType;
  detailPanelOpen: boolean;
  terminal: string;
  setActiveResourceType: (t: ResourceType) => void;
  setDetailPanelOpen: (open: boolean) => void;
  setTerminal: (t: string) => void;
}

export const useUIStore = create<UIStore>((set) => ({
  activeResourceType: 'building',
  detailPanelOpen: false,
  terminal: '',
  setActiveResourceType: (t) => set({ activeResourceType: t }),
  setDetailPanelOpen: (open) => set({ detailPanelOpen: open }),
  setTerminal: (t) => set({ terminal: t })
}));
