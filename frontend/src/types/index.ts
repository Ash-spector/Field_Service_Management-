export type Role = 'ADMIN' | 'MANAGER' | 'DISPATCHER' | 'TECHNICIAN' | 'CUSTOMER';

export type WorkOrderStatus = 
  | 'NEW' 
  | 'ASSIGNED' 
  | 'IN_PROGRESS' 
  | 'ON_HOLD' 
  | 'COMPLETED' 
  | 'CLOSED' 
  | 'CANCELLED';

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';

export type SlaStatus = 'PENDING' | 'AT_RISK' | 'IN_BREACH' | 'MET';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  role: Role;
  customerId?: number;
  customerName?: string;
}

export interface Customer {
  id: number;
  name: string;
  contactEmail?: string;
  phone?: string;
  address?: string;
  createdAt: string;
  siteCount?: number;
}

export interface Site {
  id: number;
  customerId: number;
  customerName?: string;
  name: string;
  address?: string;
  contactName?: string;
  contactPhone?: string;
  createdAt: string;
}

export interface Part {
  id: number;
  name: string;
  sku: string;
  unitCost: number;
  stockQty: number;
  createdAt: string;
}

export interface PartUsage {
  id: number;
  workOrderId: number;
  partId: number;
  partName: string;
  sku: string;
  qtyUsed: number;
  unitCost: number;
  totalCost: number;
  usedAt: string;
}

export interface TimeLog {
  id: number;
  workOrderId: number;
  technicianId: number;
  technicianName: string;
  minutes: number;
  note?: string;
  loggedAt: string;
}

export interface WorkOrderStatusHistory {
  id: number;
  workOrderId: number;
  fromStatus?: WorkOrderStatus;
  toStatus: WorkOrderStatus;
  changedBy: string;
  changedAt: string;
  note?: string;
}

export interface WorkOrder {
  id: number;
  code: string;
  title: string;
  description?: string;
  priority: Priority;
  status: WorkOrderStatus;
  customerId: number;
  customerName: string;
  siteId: number;
  siteName: string;
  assignedToId?: number;
  assignedToName?: string;
  slaDueDate?: string;
  slaBreached: boolean;
  slaStatus: SlaStatus;
  createdAt: string;
  updatedAt: string;
  totalPartsCost: number;
  totalTimeMinutes: number;
  history?: WorkOrderStatusHistory[];
  partsUsed?: PartUsage[];
  timeLogs?: TimeLog[];
}

export interface DashboardReport {
  totalWorkOrders: number;
  activeWorkOrders: number;
  overdueWorkOrders: number;
  slaBreachedCount: number;
  slaCompliancePercentage: number;
  statusCounts: Record<string, number>;
  priorityCounts: Record<string, number>;
  technicianWorkload: Record<string, number>;
  siteBreakdown: Record<string, number>;
}

export interface AuthResponse {
  token: string;
  message: string;
  user: User;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
