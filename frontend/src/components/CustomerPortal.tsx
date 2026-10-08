import React, { useState, useEffect } from 'react';
import { WorkOrder, Site, Priority } from '../types';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Plus, CheckCircle, AlertTriangle, Building, MapPin } from 'lucide-react';

interface CustomerPortalProps {
  onSelectWorkOrder: (id: number) => void;
  refreshTrigger: number;
}

export const CustomerPortal: React.FC<CustomerPortalProps> = ({ onSelectWorkOrder, refreshTrigger }) => {
  const { user } = useAuth();
  const [myOrders, setMyOrders] = useState<WorkOrder[]>([]);
  const [sites, setSites] = useState<Site[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  // New Request Form
  const [showForm, setShowForm] = useState<boolean>(false);
  const [title, setTitle] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [priority, setPriority] = useState<Priority>('MEDIUM');
  const [selectedSiteId, setSelectedSiteId] = useState<number | ''>('');

  const [msg, setMsg] = useState<string | null>(null);

  useEffect(() => {
    fetchCustomerData();
  }, [user, refreshTrigger]);

  const fetchCustomerData = async () => {
    if (!user || !user.customerId) return;
    setLoading(true);
    try {
      const [ordersData, sitesData] = await Promise.all([
        api.getWorkOrders({ customerId: user.customerId, size: 100 }),
        api.getSitesByCustomer(user.customerId),
      ]);
      setMyOrders(ordersData.content);
      setSites(sitesData);
      if (sitesData.length > 0) {
        setSelectedSiteId(sitesData[0].id);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateRequest = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user || !user.customerId || !selectedSiteId) return;
    try {
      await api.createWorkOrder({
        title,
        description,
        priority,
        customerId: user.customerId,
        siteId: Number(selectedSiteId),
      });
      setMsg('Request raised successfully!');
      setTitle('');
      setDescription('');
      setShowForm(false);
      fetchCustomerData();
    } catch (err: any) {
      alert(err.message);
    }
  };

  return (
    <div style={{ maxWidth: 900, margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <div>
          <h2 style={{ fontSize: '1.4rem', fontWeight: 800 }}>🏢 Customer Maintenance Portal</h2>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
            Organization: <strong>{user?.customerName || 'Acme Commercial'}</strong>
          </p>
        </div>

        <button className="btn-primary" onClick={() => setShowForm(!showForm)}>
          <Plus size={16} /> Raise Maintenance Request
        </button>
      </div>

      {msg && (
        <div style={{ background: 'var(--success-bg)', color: '#6ee7b7', padding: 12, borderRadius: 8, marginBottom: 20, fontSize: '0.85rem' }}>
          <CheckCircle size={16} style={{ display: 'inline', marginRight: 6 }} /> {msg}
        </div>
      )}

      {/* New Request Form Modal / Collapsible */}
      {showForm && (
        <div className="glass-card" style={{ padding: 24, marginBottom: 24 }}>
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: 16 }}>Raise New Service Ticket</h3>
          <form onSubmit={handleCreateRequest}>
            <div className="form-group">
              <label>Issue Title / Equipment Summary *</label>
              <input
                type="text"
                className="form-control"
                placeholder="e.g. Chiller AC unit non-responsive on 3rd floor"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
              />
            </div>

            <div className="form-group">
              <label>Facility Site *</label>
              <select
                className="form-control"
                value={selectedSiteId}
                onChange={(e) => setSelectedSiteId(Number(e.target.value))}
                required
              >
                {sites.map((s) => (
                  <option key={s.id} value={s.id}>{s.name} ({s.address})</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Priority Level *</label>
              <select
                className="form-control"
                value={priority}
                onChange={(e) => setPriority(e.target.value as Priority)}
                required
              >
                <option value="LOW">LOW (Routine Service - 72hr SLA)</option>
                <option value="MEDIUM">MEDIUM (Normal Operation - 48hr SLA)</option>
                <option value="HIGH">HIGH (Urgent Equipment Fault - 24hr SLA)</option>
                <option value="URGENT">URGENT (Emergency / Facility Down - 4hr SLA)</option>
              </select>
            </div>

            <div className="form-group">
              <label>Detailed Description</label>
              <textarea
                className="form-control"
                rows={3}
                placeholder="Describe symptoms, room numbers, error codes..."
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>

            <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end' }}>
              <button type="button" className="btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
              <button type="submit" className="btn-primary">Submit Ticket</button>
            </div>
          </form>
        </div>
      )}

      {/* Customer's Work Orders */}
      <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 14 }}>My Organization's Maintenance Tickets</h3>

      {loading ? (
        <div style={{ textAlign: 'center', padding: 40, color: 'var(--text-muted)' }}>Loading tickets...</div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          {myOrders.map((wo) => (
            <div key={wo.id} className="glass-card" style={{ padding: 16, cursor: 'pointer' }} onClick={() => onSelectWorkOrder(wo.id)}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 6 }}>
                <div>
                  <span className="card-code" style={{ marginRight: 8 }}>{wo.code}</span>
                  <span className={`badge badge-${wo.status.toLowerCase()}`}>{wo.status}</span>
                </div>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>
                  Logged: {new Date(wo.createdAt).toLocaleDateString()}
                </span>
              </div>
              <h4 style={{ fontSize: '1rem', fontWeight: 600, color: '#fff' }}>{wo.title}</h4>
              <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginTop: 4 }}>
                📍 Site: {wo.siteName}
              </div>
            </div>
          ))}

          {myOrders.length === 0 && (
            <div className="glass-card" style={{ padding: 30, textAlign: 'center', color: 'var(--text-muted)' }}>
              No active tickets logged for your organization.
            </div>
          )}
        </div>
      )}
    </div>
  );
};
