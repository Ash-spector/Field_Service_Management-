import React, { useState, useEffect } from 'react';
import { WorkOrder } from '../types';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Wrench, Clock, CheckCircle2, PauseCircle, PlayCircle, MapPin, AlertCircle } from 'lucide-react';

interface TechnicianFieldViewProps {
  onSelectWorkOrder: (id: number) => void;
  refreshTrigger: number;
}

export const TechnicianFieldView: React.FC<TechnicianFieldViewProps> = ({ onSelectWorkOrder, refreshTrigger }) => {
  const { user } = useAuth();
  const [assignedJobs, setAssignedJobs] = useState<WorkOrder[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    fetchMyJobs();
  }, [user, refreshTrigger]);

  const fetchMyJobs = async () => {
    if (!user) return;
    setLoading(true);
    try {
      const data = await api.getWorkOrders({
        technicianId: user.id,
        size: 100,
      });
      setAssignedJobs(data.content);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleQuickStatus = async (e: React.MouseEvent, woId: number, targetStatus: any) => {
    e.stopPropagation();
    try {
      await api.updateWorkOrderStatus(woId, targetStatus, `Field update by ${user?.firstName}`);
      fetchMyJobs();
    } catch (err: any) {
      alert(err.message);
    }
  };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: 60, color: 'var(--text-muted)' }}>Loading technician field jobs...</div>;
  }

  return (
    <div style={{ maxWidth: 800, margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <div>
          <h2 style={{ fontSize: '1.4rem', fontWeight: 800 }}>📱 Technician Field Service View</h2>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
            Logged in as Tech <strong>{user?.firstName} {user?.lastName}</strong> ({assignedJobs.length} Assigned Jobs)
          </p>
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
        {assignedJobs.map((job) => (
          <div
            key={job.id}
            className="glass-card"
            style={{ padding: 18, cursor: 'pointer' }}
            onClick={() => onSelectWorkOrder(job.id)}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8 }}>
              <div>
                <span className="card-code" style={{ marginRight: 8 }}>{job.code}</span>
                <span className={`badge badge-${job.status.toLowerCase()}`}>{job.status}</span>
                <span className={`badge badge-${job.priority.toLowerCase()}`} style={{ marginLeft: 6 }}>{job.priority}</span>
              </div>
              <span className={`badge sla-badge-${job.slaStatus === 'IN_BREACH' ? 'breach' : job.slaStatus === 'AT_RISK' ? 'risk' : 'ok'}`}>
                SLA: {job.slaStatus}
              </span>
            </div>

            <h3 style={{ fontSize: '1.1rem', fontWeight: 700, color: '#fff', marginBottom: 6 }}>{job.title}</h3>

            <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: 12 }}>
              <div>🏢 <strong>{job.customerName}</strong></div>
              <div><MapPin size={14} style={{ display: 'inline', marginRight: 4 }} /> {job.siteName}</div>
            </div>

            {/* Quick Field Actions */}
            <div style={{ display: 'flex', gap: 10, borderTop: '1px dashed var(--border-glass)', paddingTop: 12, flexWrap: 'wrap' }} onClick={(e) => e.stopPropagation()}>
              {job.status === 'ASSIGNED' && (
                <button className="btn-primary btn-sm" onClick={(e) => handleQuickStatus(e, job.id, 'IN_PROGRESS')}>
                  <PlayCircle size={14} /> Start Job
                </button>
              )}

              {job.status === 'IN_PROGRESS' && (
                <>
                  <button className="btn-secondary btn-sm" onClick={(e) => handleQuickStatus(e, job.id, 'ON_HOLD')}>
                    <PauseCircle size={14} /> Hold Job
                  </button>
                  <button className="btn-success btn-sm" onClick={(e) => handleQuickStatus(e, job.id, 'COMPLETED')}>
                    <CheckCircle2 size={14} /> Finish Job
                  </button>
                </>
              )}

              {job.status === 'ON_HOLD' && (
                <button className="btn-primary btn-sm" onClick={(e) => handleQuickStatus(e, job.id, 'IN_PROGRESS')}>
                  <PlayCircle size={14} /> Resume Job
                </button>
              )}

              <button className="btn-secondary btn-sm" onClick={() => onSelectWorkOrder(job.id)}>
                <Wrench size={14} /> Log Parts &amp; Time
              </button>
            </div>
          </div>
        ))}

        {assignedJobs.length === 0 && (
          <div className="glass-card" style={{ padding: 40, textAlign: 'center', color: 'var(--text-muted)' }}>
            🎉 No active jobs currently assigned to you!
          </div>
        )}
      </div>
    </div>
  );
};
