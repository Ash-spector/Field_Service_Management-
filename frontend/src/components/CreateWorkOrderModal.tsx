import React, { useState, useEffect } from 'react';
import { Customer, Site, User, Priority } from '../types';
import { api } from '../services/api';
import { X, Plus, CheckCircle } from 'lucide-react';

interface CreateWorkOrderModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

export const CreateWorkOrderModal: React.FC<CreateWorkOrderModalProps> = ({ onClose, onSuccess }) => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [sites, setSites] = useState<Site[]>([]);
  const [technicians, setTechnicians] = useState<User[]>([]);

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState<Priority>('MEDIUM');
  const [selectedCustomerId, setSelectedCustomerId] = useState<number | ''>('');
  const [selectedSiteId, setSelectedSiteId] = useState<number | ''>('');
  const [selectedTechId, setSelectedTechId] = useState<number | ''>('');

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [custData, techData] = await Promise.all([
        api.getCustomers(),
        api.getTechnicians(),
      ]);
      setCustomers(custData.content);
      setTechnicians(techData);
      if (custData.content.length > 0) {
        const firstCustId = custData.content[0].id;
        setSelectedCustomerId(firstCustId);
        loadSitesForCustomer(firstCustId);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const loadSitesForCustomer = async (custId: number) => {
    try {
      const sitesData = await api.getSitesByCustomer(custId);
      setSites(sitesData);
      if (sitesData.length > 0) {
        setSelectedSiteId(sitesData[0].id);
      } else {
        setSelectedSiteId('');
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleCustomerChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const val = Number(e.target.value);
    setSelectedCustomerId(val);
    loadSitesForCustomer(val);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedCustomerId || !selectedSiteId) {
      setErrorMsg('Customer and site selections are required');
      return;
    }
    setIsSubmitting(true);
    setErrorMsg(null);
    try {
      await api.createWorkOrder({
        title,
        description,
        priority,
        customerId: Number(selectedCustomerId),
        siteId: Number(selectedSiteId),
        assignedToId: selectedTechId ? Number(selectedTechId) : undefined,
      });
      onSuccess();
      onClose();
    } catch (err: any) {
      setErrorMsg(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content glass-card" onClick={(e) => e.stopPropagation()} style={{ maxWidth: 600 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 800 }}>➕ Raise New Work Order</h2>
          <button className="nav-btn" onClick={onClose}><X size={20} /></button>
        </div>

        {errorMsg && (
          <div style={{ background: 'var(--danger-bg)', color: '#fca5a5', padding: 10, borderRadius: 8, marginBottom: 16, fontSize: '0.85rem' }}>
            {errorMsg}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Work Order Title *</label>
            <input
              type="text"
              className="form-control"
              placeholder="e.g. HVAC Air Handler Overheating error on 2nd floor"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <div className="form-group">
              <label>Customer Organization *</label>
              <select className="form-control" value={selectedCustomerId} onChange={handleCustomerChange} required>
                {customers.map((c) => (
                  <option key={c.id} value={c.id}>{c.name}</option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Facility Site *</label>
              <select className="form-control" value={selectedSiteId} onChange={(e) => setSelectedSiteId(Number(e.target.value))} required>
                {sites.map((s) => (
                  <option key={s.id} value={s.id}>{s.name}</option>
                ))}
              </select>
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <div className="form-group">
              <label>Priority Level *</label>
              <select className="form-control" value={priority} onChange={(e) => setPriority(e.target.value as Priority)} required>
                <option value="LOW">LOW (72hr SLA)</option>
                <option value="MEDIUM">MEDIUM (48hr SLA)</option>
                <option value="HIGH">HIGH (24hr SLA)</option>
                <option value="URGENT">URGENT (4hr SLA)</option>
              </select>
            </div>

            <div className="form-group">
              <label>Assign Technician (Optional)</label>
              <select className="form-control" value={selectedTechId} onChange={(e) => setSelectedTechId(e.target.value ? Number(e.target.value) : '')}>
                <option value="">Leave Unassigned (NEW)</option>
                {technicians.map((t) => (
                  <option key={t.id} value={t.id}>{t.firstName} {t.lastName}</option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-group">
            <label>Detailed Problem Description</label>
            <textarea
              className="form-control"
              rows={3}
              placeholder="Enter symptoms, access instructions, model numbers..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>

          <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end', marginTop: 20 }}>
            <button type="button" className="btn-secondary" onClick={onClose}>Cancel</button>
            <button type="submit" className="btn-primary" disabled={isSubmitting}>
              Raise Ticket
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
