import { 
  AuthResponse, 
  Customer, 
  Site, 
  Part, 
  WorkOrder, 
  DashboardReport, 
  User, 
  PageResponse,
  WorkOrderStatus,
  Priority,
  PartUsage,
  TimeLog
} from '../types';

const API_BASE = '';

const getAuthHeaders = (): HeadersInit => {
  const token = localStorage.getItem('keystone_jwt_token');
  return {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
};

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let errorMessage = 'An error occurred';
    try {
      const errJson = await response.json();
      errorMessage = errJson.message || errJson.error || response.statusText;
    } catch {
      errorMessage = response.statusText;
    }
    throw new Error(errorMessage);
  }
  if (response.status === 204) {
    return {} as T;
  }
  return response.json();
}

export const api = {
  // Auth
  login: async (email: string, password: string): Promise<AuthResponse> => {
    const res = await fetch(`${API_BASE}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    return handleResponse<AuthResponse>(res);
  },

  getCurrentUser: async (): Promise<User> => {
    const res = await fetch(`${API_BASE}/api/auth/me`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<User>(res);
  },

  logout: async (): Promise<void> => {
    try {
      await fetch(`${API_BASE}/api/auth/logout`, {
        method: 'POST',
        headers: getAuthHeaders(),
      });
    } catch (e) {
      console.error(e);
    } finally {
      localStorage.removeItem('keystone_jwt_token');
      localStorage.removeItem('keystone_user');
    }
  },

  // Work Orders
  getWorkOrders: async (params?: {
    customerId?: number;
    siteId?: number;
    technicianId?: number;
    status?: WorkOrderStatus;
    priority?: Priority;
    search?: string;
    page?: number;
    size?: number;
  }): Promise<PageResponse<WorkOrder>> => {
    const query = new URLSearchParams();
    if (params?.customerId) query.append('customerId', params.customerId.toString());
    if (params?.siteId) query.append('siteId', params.siteId.toString());
    if (params?.technicianId) query.append('technicianId', params.technicianId.toString());
    if (params?.status) query.append('status', params.status);
    if (params?.priority) query.append('priority', params.priority);
    if (params?.search) query.append('search', params.search);
    query.append('page', (params?.page || 0).toString());
    query.append('size', (params?.size || 50).toString());

    const res = await fetch(`${API_BASE}/api/work-orders?${query.toString()}`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<PageResponse<WorkOrder>>(res);
  },

  getWorkOrderById: async (id: number): Promise<WorkOrder> => {
    const res = await fetch(`${API_BASE}/api/work-orders/${id}`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<WorkOrder>(res);
  },

  createWorkOrder: async (data: {
    title: string;
    description?: string;
    priority: Priority;
    customerId: number;
    siteId: number;
    assignedToId?: number;
  }): Promise<WorkOrder> => {
    const res = await fetch(`${API_BASE}/api/work-orders`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    return handleResponse<WorkOrder>(res);
  },

  updateWorkOrder: async (id: number, data: {
    title?: string;
    description?: string;
    priority?: Priority;
  }): Promise<WorkOrder> => {
    const res = await fetch(`${API_BASE}/api/work-orders/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    return handleResponse<WorkOrder>(res);
  },

  assignTechnician: async (id: number, technicianId: number, note?: string): Promise<WorkOrder> => {
    const res = await fetch(`${API_BASE}/api/work-orders/${id}/assign`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ technicianId, note }),
    });
    return handleResponse<WorkOrder>(res);
  },

  updateWorkOrderStatus: async (id: number, status: WorkOrderStatus, note?: string): Promise<WorkOrder> => {
    const res = await fetch(`${API_BASE}/api/work-orders/${id}/status`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ status, note }),
    });
    return handleResponse<WorkOrder>(res);
  },

  logPartUsage: async (workOrderId: number, partId: number, qtyUsed: number): Promise<PartUsage> => {
    const res = await fetch(`${API_BASE}/api/work-orders/${workOrderId}/parts`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ partId, qtyUsed }),
    });
    return handleResponse<PartUsage>(res);
  },

  logTime: async (workOrderId: number, minutes: number, note?: string): Promise<TimeLog> => {
    const res = await fetch(`${API_BASE}/api/work-orders/${workOrderId}/time`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ minutes, note }),
    });
    return handleResponse<TimeLog>(res);
  },

  // Customers & Sites
  getCustomers: async (search?: string): Promise<PageResponse<Customer>> => {
    const res = await fetch(`${API_BASE}/api/customers?size=100${search ? `&search=${search}` : ''}`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<PageResponse<Customer>>(res);
  },

  createCustomer: async (data: { name: string; contactEmail?: string; phone?: string; address?: string }): Promise<Customer> => {
    const res = await fetch(`${API_BASE}/api/customers`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    return handleResponse<Customer>(res);
  },

  getSitesByCustomer: async (customerId: number): Promise<Site[]> => {
    const res = await fetch(`${API_BASE}/api/customers/${customerId}/sites`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<Site[]>(res);
  },

  getAllSites: async (): Promise<Site[]> => {
    const res = await fetch(`${API_BASE}/api/sites`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<Site[]>(res);
  },

  createSite: async (data: { customerId: number; name: string; address?: string; contactName?: string; contactPhone?: string }): Promise<Site> => {
    const res = await fetch(`${API_BASE}/api/sites`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    return handleResponse<Site>(res);
  },

  // Parts
  getParts: async (search?: string): Promise<PageResponse<Part>> => {
    const res = await fetch(`${API_BASE}/api/parts?size=100${search ? `&search=${search}` : ''}`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<PageResponse<Part>>(res);
  },

  createPart: async (data: { name: string; sku: string; unitCost: number; stockQty: number }): Promise<Part> => {
    const res = await fetch(`${API_BASE}/api/parts`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    return handleResponse<Part>(res);
  },

  updatePart: async (id: number, data: { name: string; sku: string; unitCost: number; stockQty: number }): Promise<Part> => {
    const res = await fetch(`${API_BASE}/api/parts/${id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    return handleResponse<Part>(res);
  },

  // Users & Technicians
  getTechnicians: async (): Promise<User[]> => {
    const res = await fetch(`${API_BASE}/api/users/technicians`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<User[]>(res);
  },

  // Reports
  getDashboardMetrics: async (): Promise<DashboardReport> => {
    const res = await fetch(`${API_BASE}/api/reports/summary`, {
      headers: getAuthHeaders(),
    });
    return handleResponse<DashboardReport>(res);
  },
};
