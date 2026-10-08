import React, { useState, useEffect } from 'react';
import { DashboardReport } from '../types';
import { api } from '../services/api';
import { CheckCircle2, AlertTriangle, Clock, TrendingUp, Users, MapPin, Wrench } from 'lucide-react';

export const ManagerDashboard: React.FC = () => {
  const [metrics, setMetrics] = useState<DashboardReport | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    loadMetrics();
  }, []);

  const loadMetrics = async () => {
    setLoading(true);
    try {
      const data = await api.getDashboardMetrics();
      setMetrics(data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  if (loading || !metrics) {
    return <div style={{ textAlign: 'center', padding: 60, color: 'var(--text-muted)' }}>Loading manager operational metrics...</div>;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      {/* KPI Stat Cards (F8) */}
      <div className="kpi-grid">
        <div className="kpi-card">
          <div className="kpi-label">Total Work Orders</div>
          <div className="kpi-val" style={{ color: 'var(--primary)' }}>{metrics.totalWorkOrders}</div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Lifetime logged in platform</div>
        </div>

        <div className="kpi-card">
          <div className="kpi-label">Active Field Jobs</div>
          <div className="kpi-val" style={{ color: 'var(--secondary)' }}>{metrics.activeWorkOrders}</div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Open or in-progress</div>
        </div>

        <div className="kpi-card">
          <div className="kpi-label">SLA Compliance Rate</div>
          <div className="kpi-val" style={{ color: metrics.slaCompliancePercentage >= 85 ? 'var(--success)' : 'var(--warning)' }}>
            {metrics.slaCompliancePercentage}%
          </div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>Target: &gt; 90% resolution SLA</div>
        </div>

        <div className="kpi-card">
          <div className="kpi-label">SLA Breaches / Overdue</div>
          <div className="kpi-val" style={{ color: 'var(--danger)' }}>{metrics.slaBreachedCount}</div>
          <div style={{ fontSize: '0.75rem', color: '#f87171' }}>Requires immediate manager escalation</div>
        </div>
      </div>

      {/* Analytics Charts & Breakdown Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: 20 }}>
        {/* Work Orders by Status Breakdown */}
        <div className="glass-card" style={{ padding: 20 }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 16, display: 'flex', alignItems: 'center', gap: 8 }}>
            <TrendingUp size={18} style={{ color: 'var(--primary)' }} /> Work Orders by Lifecycle Status
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            {Object.entries(metrics.statusCounts || {}).map(([status, count]) => {
              const pct = metrics.totalWorkOrders > 0 ? Math.round((count / metrics.totalWorkOrders) * 100) : 0;
              return (
                <div key={status}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', marginBottom: 4 }}>
                    <span style={{ fontWeight: 600 }}>{status}</span>
                    <span style={{ color: 'var(--text-secondary)' }}>{count} ({pct}%)</span>
                  </div>
                  <div style={{ width: '100%', height: 8, background: 'var(--bg-primary)', borderRadius: 4, overflow: 'hidden' }}>
                    <div
                      style={{
                        width: `${pct}%`,
                        height: '100%',
                        background: status === 'COMPLETED' || status === 'CLOSED' ? 'var(--success)' : status === 'ON_HOLD' ? 'var(--warning)' : 'var(--primary)',
                        transition: 'width 0.4s ease',
                      }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Technician Workload Breakdown */}
        <div className="glass-card" style={{ padding: 20 }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 16, display: 'flex', alignItems: 'center', gap: 8 }}>
            <Users size={18} style={{ color: 'var(--secondary)' }} /> Technician Active Workload
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
            {Object.entries(metrics.technicianWorkload || {}).map(([tech, count]) => (
              <div key={tech} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 14px', background: 'var(--bg-surface)', borderRadius: 8, border: '1px solid var(--border-glass)' }}>
                <span style={{ fontWeight: 600, fontSize: '0.9rem' }}>👤 {tech}</span>
                <span className="badge badge-assigned">{count} Assigned Jobs</span>
              </div>
            ))}
          </div>
        </div>

        {/* Site Activity Breakdown */}
        <div className="glass-card" style={{ padding: 20 }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 16, display: 'flex', alignItems: 'center', gap: 8 }}>
            <MapPin size={18} style={{ color: 'var(--accent)' }} /> Customer Site Job Mix
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
            {Object.entries(metrics.siteBreakdown || {}).map(([site, count]) => (
              <div key={site} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 14px', background: 'var(--bg-surface)', borderRadius: 8, border: '1px solid var(--border-glass)' }}>
                <span style={{ fontSize: '0.85rem' }}>📍 {site}</span>
                <span style={{ fontWeight: 700, color: 'var(--primary)' }}>{count} jobs</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
