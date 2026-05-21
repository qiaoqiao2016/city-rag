export interface ApiResult<T> {
  code: number;
  message: string;
  data: T;
  timestamp: number;
  requestId: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

export interface BuildingVO {
  id: string;
  buildingName: string;
  address: string;
  district: string;
  street: string;
  community: string;
  totalArea: number;
  floorCount: number;
  useType: string;
  occupancyRate: number;
  coordinates: [number, number];
}

export interface AlertEventVO {
  id: string;
  ruleName: string;
  resourceType: string;
  resourceName: string;
  priority: string;
  status: string;
  triggerValue: string;
  threshold: string;
  createdAt: string;
}

export interface GridVO {
  id: string;
  gridCode: string;
  gridType: string;
  gridMaster: string;
  populationCount: number;
  buildingCount: number;
  enterpriseCount: number;
}
