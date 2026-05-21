import * as Cesium from 'cesium';

Cesium.Ion.defaultAccessToken = '';

export interface CesiumEngineOptions {
  cesiumIonToken?: string;
  defaultView?: [number, number];
  defaultZoom?: number;
  terrainProvider?: boolean;
}

export class CesiumEngine {
  private viewer: Cesium.Viewer | null = null;
  private entityMap: Map<string, Cesium.Entity> = new Map();
  private clickHandlers: Array<(feature: any) => void> = [];

  constructor(container: HTMLElement, options: CesiumEngineOptions = {}) {
    if (options.cesiumIonToken) {
      Cesium.Ion.defaultAccessToken = options.cesiumIonToken;
    }

    const [lng, lat] = options.defaultView ?? [116.397, 39.908];
    const zoom = options.defaultZoom ?? 12;

    this.viewer = new Cesium.Viewer(container, {
      animation: false,
      timeline: false,
      baseLayerPicker: false,
      fullscreenButton: false,
      vrButton: false,
      geocoder: false,
      homeButton: false,
      infoBox: false,
      sceneModePicker: false,
      navigationHelpButton: false,
      selectionIndicator: false,
      terrainProvider: undefined // terrain loaded on demand via setTerrain()
    });

    this.viewer.camera.setView({
      destination: Cesium.Cartesian3.fromDegrees(lng, lat, 10000),
      orientation: {
        heading: Cesium.Math.toRadians(0),
        pitch: Cesium.Math.toRadians(-45),
        roll: 0
      }
    });

    this.viewer.screenSpaceEventHandler.setInputAction(
      (click: Cesium.ScreenSpaceEventType) => {
        const picked = this.viewer?.scene.pick(click.position as any);
        if (picked && picked.id) {
          const entity = this.entityMap.get(picked.id.id);
          if (entity) {
            this.clickHandlers.forEach((fn) => fn(entity));
          }
        }
      },
      Cesium.ScreenSpaceEventType.LEFT_CLICK
    );
  }

  flyTo(coords: [number, number], zoom: number): void {
    if (!this.viewer) return;
    const [lng, lat] = coords;
    const height = 20000 / Math.pow(2, zoom - 10);
    this.viewer.camera.flyTo({
      destination: Cesium.Cartesian3.fromDegrees(lng, lat, Math.max(height, 500))
    });
  }

  addEntity(entity: Cesium.Entity): string {
    if (!this.viewer) return '';
    const id = entity.id ?? `entity_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`;
    entity.id = id;
    this.viewer.entities.add(entity);
    this.entityMap.set(id, entity);
    return id;
  }

  removeEntity(id: string): void {
    if (!this.viewer) return;
    this.viewer.entities.removeById(id);
    this.entityMap.delete(id);
  }

  onClick(callback: (feature: any) => void): void {
    this.clickHandlers.push(callback);
  }

  setViewMode(mode: '3D' | 'Columbus' | '2D'): void {
    if (!this.viewer) return;
    switch (mode) {
      case '3D':
        this.viewer.scene.mode = Cesium.SceneMode.SCENE3D;
        break;
      case 'Columbus':
        this.viewer.scene.mode = Cesium.SceneMode.COLUMBUS_VIEW;
        break;
      case '2D':
        this.viewer.scene.mode = Cesium.SceneMode.SCENE2D;
        break;
    }
  }

  getViewer(): Cesium.Viewer | null {
    return this.viewer;
  }

  destroy(): void {
    if (this.viewer) {
      this.viewer.destroy();
      this.viewer = null;
    }
    this.entityMap.clear();
    this.clickHandlers = [];
  }
}
