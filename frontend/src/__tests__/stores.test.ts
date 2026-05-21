import { describe, it, expect, beforeEach } from "vitest";
import { useMapStore } from "../stores/mapStore";
import { useAlertStore } from "../stores/alertStore";
import { useUIStore } from "../stores/uiStore";

describe("mapStore", () => {
  beforeEach(() => {
    useMapStore.setState({
      viewer: null,
      activeLayerIds: ["building", "grid"],
      center: [116.397, 39.908],
      zoom: 12,
      selectedFeature: null,
    });
  });

  it("should initialize with defaults", () => {
    const state = useMapStore.getState();
    expect(state.activeLayerIds).toContain("building");
    expect(state.center).toEqual([116.397, 39.908]);
    expect(state.zoom).toBe(12);
  });

  it("should set viewer", () => {
    const mockViewer = { flyTo: () => {} };
    useMapStore.getState().setViewer(mockViewer);
    expect(useMapStore.getState().viewer).toBe(mockViewer);
  });

  it("should toggle layer on", () => {
    useMapStore.getState().toggleLayer("camera");
    expect(useMapStore.getState().activeLayerIds).toContain("camera");
  });

  it("should toggle layer off", () => {
    useMapStore.getState().toggleLayer("building");
    expect(useMapStore.getState().activeLayerIds).not.toContain("building");
  });

  it("should set and clear selected feature", () => {
    const f = { id: "b-001", name: "Test" };
    useMapStore.getState().setSelectedFeature(f);
    expect(useMapStore.getState().selectedFeature).toEqual(f);
    useMapStore.getState().setSelectedFeature(null);
    expect(useMapStore.getState().selectedFeature).toBeNull();
  });
});

describe("alertStore", () => {
  beforeEach(() => {
    useAlertStore.setState({ alerts: [], unreadCount: 0, activeRules: [] });
  });

  it("should push alert and increment unread", () => {
    useAlertStore.getState().pushAlert({ id: "a-001", ruleName: "水位超限", priority: "high" } as any);
    expect(useAlertStore.getState().alerts).toHaveLength(1);
    expect(useAlertStore.getState().unreadCount).toBe(1);
  });

  it("should confirm alert", () => {
    useAlertStore.getState().pushAlert({ id: "a-001", ruleName: "水位超限", priority: "high" } as any);
    useAlertStore.getState().confirmAlert("a-001");
    expect(useAlertStore.getState().unreadCount).toBe(0);
    expect(useAlertStore.getState().alerts[0].status).toBe("confirmed");
  });

  it("should batch confirm", () => {
    useAlertStore.getState().pushAlert({ id: "a-001" } as any);
    useAlertStore.getState().pushAlert({ id: "a-002" } as any);
    useAlertStore.getState().batchConfirm(["a-001", "a-002"]);
    expect(useAlertStore.getState().unreadCount).toBe(0);
  });
});

describe("uiStore", () => {
  beforeEach(() => {
    useUIStore.setState({ activeResourceType: "building", detailPanelOpen: false, terminal: "desktop" });
  });

  it("should set active resource type", () => {
    useUIStore.getState().setActiveResourceType("municipal");
    expect(useUIStore.getState().activeResourceType).toBe("municipal");
  });

  it("should toggle detail panel", () => {
    useUIStore.getState().toggleDetailPanel();
    expect(useUIStore.getState().detailPanelOpen).toBe(true);
    useUIStore.getState().toggleDetailPanel();
    expect(useUIStore.getState().detailPanelOpen).toBe(false);
  });
});
