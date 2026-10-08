import React, { useState, useEffect } from 'react';
import { WorkOrder, WorkOrderStatus, Priority } from '../types';
import { api } from '../services/api';
import { Search, Filter, RefreshCw, AlertCircle, Clock, CheckCircle2 } from 'lucide-react';

interface KanbanBoardProps {
  onSelectWorkOrder: (id: number) => void;
  refreshTrigger: number;
}

const COLUMNS: { status: WorkOrderStatus; title: string; color: string }[] = [
  { status: 'NEW', title: 'New Requests', color: '#3b82f6' },
  { status: 'ASSIGNED', title: 'Assigned', color: '#8b5cf6' },
  { status: 'IN_PROGRESS', title: 'In Progress', color: '#06b6d4' },
  { status: 'ON_HOLD', title: 'On Hold', color: '#f59e0b' },
  { status: 'COMPLETED', title: 'Completed', color: '#10b981' },
  { status: 'CLOSED', title: 'Closed / Signed Off', color: '#6b7280' },
];

export const KanbanBoard: React.FC<KanbanBoardProps> = ({ onSelectWorkOrder, refreshTrigger }) => {
  const [workOrders, setWorkOrders] = useState<WorkOrder[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [search, setSearch] = useState<string>('');
  const [priorityFilter, setPriorityFilter] = useState<string>('');

  useEffect(() => {
    fetchOrders();
  }, [refreshTrigger, search, priorityFilter]);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const data = await api.getWorkOrders({
        search: search || undefined,
        priority: (priorityFilter as Priority) || undefined,
        size: 100,
      });
      setWorkOrders(data.content);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* Search & Filter Header */}
      <div className="glass-card" style={{ padding: 16, marginBottom: 20, display: 'flex', gap: 16, alignItems: 'center', flexWrap: 'wrap' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, flex: 1, minWidth: 240, background: 'var(--bg-primary)', padding: '8px 12px', borderRadius: 8, border: '1px solid var(--border-glass)' }}>
          <Search size={16} style={{ color: 'var(--text-muted)' }} />
          <input
            type="text"
            placeholder="Search by code, title, customer, site..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            style={{ background: 'transparent', border: 'none', color: '#fff', outline: 'none', width: '100%', fontSize: '0.875rem' }}
          />
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <Filter size={16} style={{ color: 'var(--text-muted)' }} />
          <select
            className="form-control"
            style={{ fontSize: '0.85rem' }}
            value={priorityFilter}
            onChange={(e) => setPriorityFilter(e.target.value)}
          >
            <option value="">All Priorities</option>
            <option value="URGENT">URGENT</option>
            <option value="HIGH">HIGH</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="LOW">LOW</option>
          </select>
        </div>

        <button className="btn-secondary btn-sm" onClick={fetchOrders} title="Refresh">
          <RefreshCw size={14} className={loading ? 'spin' : ''} /> Refresh
        </button>
      </div>

      {/* Kanban Board Layout */}
      {loading ? (
        <div style={{ textAlign: 'center', padding: 60, color: 'var(--text-muted)' }}>Loading work orders board...</div>
      ) : (
        <div className="kanban-grid">
          {COLUMNS.map((col) => {
            const colOrders = workOrders.filter((wo) => wo.status === col.status);
            return (
              <div key={col.status} className="kanban-column">
                <div className="column-header">
                  <span style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                    <span style={{ width: 8, height: 8, borderRadius: '50%', background: col.color }} />
                    {col.title}
                  </span>
                  <span className="card-count">{colOrders.length}</span>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
                  {colOrders.map((wo) => (
                    <div key={wo.id} className="kanban-card" onClick={() => onSelectWorkOrder(wo.id)}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <span className="card-code">{wo.code}</span>
                        <span className={`badge badge-${wo.priority.toLowerCase()}`}>{wo.priority}</span>
                      </div>

                      <div className="card-title">{wo.title}</div>

                      <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>
                        🏢 {wo.customerName}
                        <div style={{ color: 'var(--text-muted)', marginTop: 2 }}>📍 {wo.siteName}</div>
                      </div>

                      <div className="card-meta">
                        <span>👤 {wo.assignedToName}</span>
                        {wo.slaStatus === 'IN_BREACH' ? (
                          <span style={{ color: '#ef4444', fontWeight: 600, display: 'flex', alignItems: 'center', gap: 2 }}>
                            <AlertCircle size={12} /> BREACHED
                          </span>
                        ) : wo.slaStatus === 'AT_RISK' ? (
                          <span style={{ color: '#f59e0b', fontWeight: 600, display: 'flex', alignItems: 'center', gap: 2 }}>
                            <Clock size={12} /> AT RISK
                          </span>
                        ) : (
                          <span style={{ color: '#10b981', display: 'flex', alignItems: 'center', gap: 2 }}>
                            <CheckCircle2 size={12} /> SLA OK
                          </span>
                        )}
                      </div>
                    </div>
                  ))}

                  {colOrders.length === 0 && (
                    <div style={{ textAlign: 'center', padding: '30px 10px', color: 'var(--text-muted)', fontSize: '0.8rem', border: '1px dashed var(--border-glass)', borderRadius: 8 }}>
                      No jobs in {col.title}
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};
