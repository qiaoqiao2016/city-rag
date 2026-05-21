import { create } from 'zustand';
import type { AlertEventVO } from '@/api/types';

export interface AlertStore {
  alerts: AlertEventVO[];
  unreadCount: number;
  pushAlert: (alert: AlertEventVO) => void;
  confirmAlert: (id: string) => void;
  batchConfirm: (ids: string[]) => void;
  setAlerts: (alerts: AlertEventVO[]) => void;
}

export const useAlertStore = create<AlertStore>((set) => ({
  alerts: [],
  unreadCount: 0,
  pushAlert: (alert) =>
    set((state) => ({
      alerts: [alert, ...state.alerts],
      unreadCount: state.unreadCount + 1
    })),
  confirmAlert: (id) =>
    set((state) => ({
      alerts: state.alerts.map((a) =>
        a.id === id ? { ...a, status: 'CONFIRMED' } : a
      ),
      unreadCount: Math.max(0, state.unreadCount - 1)
    })),
  batchConfirm: (ids) =>
    set((state) => ({
      alerts: state.alerts.map((a) =>
        ids.includes(a.id) ? { ...a, status: 'CONFIRMED' } : a
      ),
      unreadCount: Math.max(0, state.unreadCount - ids.length)
    })),
  setAlerts: (alerts) =>
    set({
      alerts,
      unreadCount: alerts.filter((a) => a.status === 'PENDING').length
    })
}));
