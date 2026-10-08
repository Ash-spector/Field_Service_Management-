import React, { useState, useEffect } from 'react';
import { WorkOrder, WorkOrderStatus, User, Part } from '../types';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { X, Clock, Wrench, ShieldAlert, CheckCircle, Package, UserCheck, AlertTriangle } from 'lucide-react';

interface WorkOrderDetailModalProps {
  workOrderId: number | null;
  onClose: () => void;
  onRefresh: () => void;
}

export const WorkOrderDetailModal: React.FC<WorkOrderDetailModalProps> = ({ workOrderId, onClose, onRefresh }) => {
  const { user } = useAuth();
  const [workOrder, setWorkOrder] = useState<WorkOrder | null>(null);
  const [technicians, setTechnicians] = useState<User[]>([]);
  const [parts, setParts] = useState<Part[]>([]);

  const [selectedTechId, setSelectedTechId] = useState<number | ''>('');
  const [statusNote, setStatusNote] = useState<string>('');

  // Part Usage Form State
  const [selectedPartId, setSelectedPartId] = useState<number | ''>('');
  const [partQty, setPartQty] = useState<number>(1);

  // Time Log Form State
  const [timeMinutes, setTimeMinutes] = useState<number>(30);
  const [timeNote, setTimeNote] = useState<string>('');

  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  useEffect(() => {
    if (workOrderId) {
      loadDetails();
      loadDropdowns();
    }
  }, [workOrderId]);

  const loadDetails = async () => {
    if (!workOrderId) return;
    try {
      const data = await api.getWorkOrderById(workOrderId);
      setWorkOrder(data);
      if (data.assignedToId) {
        setSelectedTechId(data.assignedToId);
      }
    } catch (err: any) {
      setErrorMsg(err.message);
    }
  };

  const loadDropdowns = async () => {
    try {
      const [techs, partsData] = await Promise.all([
        api.getTechnicians(),
        api.getParts(),
      ]);
      setTechnicians(techs);
      setParts(partsData.content);
    } catch (e) {
      console.error(e);
    }
  };

  if (!workOrderId || !workOrder) return null;

  const isManager = user?.role === 'MANAGER' || user?.role === 'ADMIN';
  const isDispatcher = user?.role === 'DISPATCHER' || isManager;
  const isAssignedTech = user?.role === 'TECHNICIAN' && workOrder.assignedToId === user.id;

  const handleAssign = async () => {
    if (!selectedTechId) return;
    setIsSubmitting(true);
    setErrorMsg(null);
    try {
      await api.assignTechnician(workOrder.id, Number(selectedTechId), statusNote || 'Technician assigned');
      setSuccessMsg('Technician assigned successfully');
      loadDetails();
      onRefresh();
    } catch (err: any) {
      setErrorMsg(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleStatusChange = async (targetStatus: WorkOrderStatus) => {
    setIsSubmitting(true);
    setErrorMsg(null);
    try {
      await api.updateWorkOrderStatus(workOrder.id, targetStatus, statusNote || `Status updated to ${targetStatus}`);
      setSuccessMsg(`Status updated to ${targetStatus}`);
      setStatusNote('');
      loadDetails();
      onRefresh();
    } catch (err: any) {
      setErrorMsg(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleLogPart = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedPartId || partQty <= 0) return;
    setIsSubmitting(true);
    setErrorMsg(null);
    try {
      await api.logPartUsage(workOrder.id, Number(selectedPartId), partQty);
      setSuccessMsg('Part usage logged successfully (stock decremented)');
      setSelectedPartId('');
      setPartQty(1);
      loadDetails();
      onRefresh();
    } catch (err: any) {
      setErrorMsg(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleLogTime = async (e: React.FormEvent) => {
    e.preventDefault();
    if (timeMinutes <= 0) return;
    setIsSubmitting(true);
    setErrorMsg(null);
    try {
      await api.logTime(workOrder.id, timeMinutes, timeNote);
      setSuccessMsg('Time entry recorded');
      setTimeNote('');
      loadDetails();
      onRefresh();
    } catch (err: any) {
      setErrorMsg(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content glass-card" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 16 }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <span className="card-code">{workOrder.code}</span>
              <span className={`badge badge-${workOrder.status.toLowerCase()}`}>{workOrder.status}</span>
              <span className={`badge badge-${workOrder.priority.toLowerCase()}`}>{workOrder.priority} Priority</span>
              <span className={`badge sla-badge-${workOrder.slaStatus === 'IN_BREACH' ? 'breach' : workOrder.slaStatus === 'AT_RISK' ? 'risk' : 'ok'}`}>
                SLA: {workOrder.slaStatus}
              </span>
            </div>
            <h2 style={{ fontSize: '1.4rem', fontWeight: 700, marginTop: 6 }}>{workOrder.title}</h2>
          </div>
          <button className="nav-btn" onClick={onClose}><X size={20} /></button>
        </div>

        {errorMsg && (
          <div style={{ background: 'var(--danger-bg)', color: '#fca5a5', padding: '10px 14px', borderRadius: 8, marginBottom: 16, fontSize: '0.85rem' }}>
            <AlertTriangle size={16} style={{ display: 'inline', marginRight: 6 }} />
            {errorMsg}
          </div>
        )}

        {successMsg && (
          <div style={{ background: 'var(--success-bg)', color: '#6ee7b7', padding: '10px 14px', borderRadius: 8, marginBottom: 16, fontSize: '0.85rem' }}>
            <CheckCircle size={16} style={{ display: 'inline', marginRight: 6 }} />
            {successMsg}
          </div>
        )}

        {/* Location & Assignee */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, background: 'rgba(15,23,42,0.6)', padding: 14, borderRadius: 10, marginBottom: 20, fontSize: '0.85rem' }}>
          <div>
            <div style={{ color: 'var(--text-muted)' }}>Customer & Site</div>
            <div style={{ fontWeight: 600, color: '#fff' }}>{workOrder.customerName}</div>
            <div style={{ color: 'var(--text-secondary)' }}>📍 {workOrder.siteName}</div>
          </div>
          <div>
            <div style={{ color: 'var(--text-muted)' }}>Assigned Technician</div>
            <div style={{ fontWeight: 600, color: '#fff' }}>👤 {workOrder.assignedToName}</div>
            <div style={{ color: 'var(--text-secondary)' }}>
              ⏰ SLA Due: {workOrder.slaDueDate ? new Date(workOrder.slaDueDate).toLocaleString() : 'N/A'}
            </div>
          </div>
        </div>

        {/* Description */}
        {workOrder.description && (
          <div style={{ marginBottom: 20 }}>
            <h4 style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: 4 }}>Job Description</h4>
            <p style={{ background: 'var(--bg-primary)', padding: 12, borderRadius: 8, fontSize: '0.9rem', color: 'var(--text-primary)' }}>
              {workOrder.description}
            </p>
          </div>
        )}

        {/* State Machine Transition Actions */}
        <div style={{ background: 'var(--bg-surface-hover)', padding: 16, borderRadius: 12, marginBottom: 24 }}>
          <h4 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: 10, textTransform: 'uppercase', letterSpacing: 0.5 }}>
            ⚡ Workflow State Machine Actions
          </h4>

          {/* Dispatcher/Manager Assign Section */}
          {isDispatcher && workOrder.status !== 'CLOSED' && workOrder.status !== 'CANCELLED' && (
            <div style={{ display: 'flex', gap: 10, marginBottom: 12, flexWrap: 'wrap' }}>
              <select
                className="form-control"
                style={{ width: 220 }}
                value={selectedTechId}
                onChange={(e) => setSelectedTechId(e.target.value ? Number(e.target.value) : '')}
              >
                <option value="">Select Technician...</option>
                {technicians.map((tech) => (
                  <option key={tech.id} value={tech.id}>{tech.firstName} {tech.lastName}</option>
                ))}
              </select>
              <button className="btn-secondary btn-sm" onClick={handleAssign} disabled={isSubmitting || !selectedTechId}>
                <UserCheck size={14} /> {workOrder.assignedToId ? 'Reassign Tech' : 'Assign Tech'}
              </button>
            </div>
          )}

          {/* Action Buttons based on state */}
          <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
            {workOrder.status === 'ASSIGNED' && (isAssignedTech || isManager || isDispatcher) && (
              <button className="btn-primary btn-sm" onClick={() => handleStatusChange('IN_PROGRESS')} disabled={isSubmitting}>
                ▶ Start Work
              </button>
            )}

            {workOrder.status === 'IN_PROGRESS' && (isAssignedTech || isManager || isDispatcher) && (
              <>
                <button className="btn-secondary btn-sm" onClick={() => handleStatusChange('ON_HOLD')} disabled={isSubmitting}>
                  ⏸ Put On Hold
                </button>
                <button className="btn-success btn-sm" onClick={() => handleStatusChange('COMPLETED')} disabled={isSubmitting}>
                  ✅ Mark Work Completed
                </button>
              </>
            )}

            {workOrder.status === 'ON_HOLD' && (isAssignedTech || isManager || isDispatcher) && (
              <button className="btn-primary btn-sm" onClick={() => handleStatusChange('IN_PROGRESS')} disabled={isSubmitting}>
                ▶ Resume Work
              </button>
            )}

            {workOrder.status === 'COMPLETED' && isManager && (
              <button className="btn-success btn-sm" onClick={() => handleStatusChange('CLOSED')} disabled={isSubmitting}>
                🔒 Manager Sign-Off & Close Order
              </button>
            )}

            {workOrder.status !== 'CLOSED' && workOrder.status !== 'CANCELLED' && (isDispatcher || isManager) && (
              <button className="btn-danger btn-sm" onClick={() => handleStatusChange('CANCELLED')} disabled={isSubmitting}>
                🚫 Cancel Work Order
              </button>
            )}
          </div>
        </div>

        {/* Tabbed Info: Parts, Time, Audit History */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
          {/* Parts Used & Time Logging */}
          <div>
            <h4 style={{ fontSize: '0.9rem', marginBottom: 10, display: 'flex', alignItems: 'center', gap: 6 }}>
              <Package size={16} /> Parts Used (Stock Tracked)
            </h4>

            {workOrder.status !== 'CLOSED' && workOrder.status !== 'CANCELLED' && (
              <form onSubmit={handleLogPart} style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                <select
                  className="form-control"
                  style={{ flex: 1, fontSize: '0.8rem' }}
                  value={selectedPartId}
                  onChange={(e) => setSelectedPartId(e.target.value ? Number(e.target.value) : '')}
                  required
                >
                  <option value="">Select Inventory Part...</option>
                  {parts.map((p) => (
                    <option key={p.id} value={p.id}>{p.name} (${p.unitCost}) - Stock: {p.stockQty}</option>
                  ))}
                </select>
                <input
                  type="number"
                  className="form-control"
                  style={{ width: 70, fontSize: '0.8rem' }}
                  min="1"
                  value={partQty}
                  onChange={(e) => setPartQty(Number(e.target.value))}
                  required
                />
                <button type="submit" className="btn-primary btn-sm" disabled={isSubmitting}>Add</button>
              </form>
            )}

            <div style={{ background: 'var(--bg-primary)', borderRadius: 8, padding: 10, maxHeight: 150, overflowY: 'auto', fontSize: '0.8rem' }}>
              {workOrder.partsUsed && workOrder.partsUsed.length > 0 ? (
                workOrder.partsUsed.map((p) => (
                  <div key={p.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--border-glass)' }}>
                    <span>{p.qtyUsed}x {p.partName}</span>
                    <span style={{ fontWeight: 600 }}>${p.totalCost.toFixed(2)}</span>
                  </div>
                ))
              ) : (
                <div style={{ color: 'var(--text-muted)' }}>No parts logged yet.</div>
              )}
            </div>

            {/* Time Logging */}
            <h4 style={{ fontSize: '0.9rem', marginTop: 16, marginBottom: 10, display: 'flex', alignItems: 'center', gap: 6 }}>
              <Clock size={16} /> Labor Time Logs
            </h4>

            {workOrder.status !== 'CLOSED' && workOrder.status !== 'CANCELLED' && (
              <form onSubmit={handleLogTime} style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                <input
                  type="number"
                  className="form-control"
                  style={{ width: 90, fontSize: '0.8rem' }}
                  placeholder="Minutes"
                  min="1"
                  value={timeMinutes}
                  onChange={(e) => setTimeMinutes(Number(e.target.value))}
                  required
                />
                <input
                  type="text"
                  className="form-control"
                  style={{ flex: 1, fontSize: '0.8rem' }}
                  placeholder="Optional labor note..."
                  value={timeNote}
                  onChange={(e) => setTimeNote(e.target.value)}
                />
                <button type="submit" className="btn-primary btn-sm" disabled={isSubmitting}>Log</button>
              </form>
            )}

            <div style={{ background: 'var(--bg-primary)', borderRadius: 8, padding: 10, maxHeight: 150, overflowY: 'auto', fontSize: '0.8rem' }}>
              {workOrder.timeLogs && workOrder.timeLogs.length > 0 ? (
                workOrder.timeLogs.map((t) => (
                  <div key={t.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--border-glass)' }}>
                    <span>{t.technicianName} ({t.minutes} mins)</span>
                    <span style={{ color: 'var(--text-muted)' }}>{t.note || 'Labor logged'}</span>
                  </div>
                ))
              ) : (
                <div style={{ color: 'var(--text-muted)' }}>No labor logged yet.</div>
              )}
            </div>
          </div>

          {/* Append-Only Status Audit History */}
          <div>
            <h4 style={{ fontSize: '0.9rem', marginBottom: 10, display: 'flex', alignItems: 'center', gap: 6 }}>
              📜 Status Audit History Trail
            </h4>

            <div style={{ background: 'var(--bg-primary)', borderRadius: 8, padding: 12, maxHeight: 350, overflowY: 'auto' }}>
              <div className="timeline">
                {workOrder.history && workOrder.history.map((h) => (
                  <div key={h.id} className="timeline-item">
                    <div className="timeline-dot" />
                    <div style={{ fontWeight: 600, color: '#fff' }}>
                      {h.fromStatus ? `${h.fromStatus} ➔ ` : ''}{h.toStatus}
                    </div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                      By {h.changedBy} at {new Date(h.changedAt).toLocaleString()}
                    </div>
                    {h.note && (
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginTop: 2 }}>
                        "{h.note}"
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>

            {/* Total Cost Rollup */}
            <div style={{ marginTop: 16, background: 'rgba(99,102,241,0.1)', border: '1px solid var(--primary)', borderRadius: 8, padding: 12, textAlign: 'right' }}>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>Parts Cost Rollup: ${workOrder.totalPartsCost?.toFixed(2) || '0.00'}</div>
              <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>Total Labor Time: {workOrder.totalTimeMinutes || 0} mins ({( (workOrder.totalTimeMinutes || 0) / 60 ).toFixed(1)} hrs)</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
