import React, { useState, useEffect } from 'react';
import { Part } from '../types';
import { api } from '../services/api';
import { Plus, Package, AlertCircle } from 'lucide-react';

export const PartsInventoryView: React.FC = () => {
  const [parts, setParts] = useState<Part[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  const [showAddForm, setShowAddForm] = useState<boolean>(false);
  const [name, setName] = useState('');
  const [sku, setSku] = useState('');
  const [unitCost, setUnitCost] = useState<number>(0);
  const [stockQty, setStockQty] = useState<number>(0);

  useEffect(() => {
    fetchParts();
  }, []);

  const fetchParts = async () => {
    setLoading(true);
    try {
      const data = await api.getParts();
      setParts(data.content);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreatePart = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.createPart({ name, sku, unitCost, stockQty });
      setName('');
      setSku('');
      setUnitCost(0);
      setStockQty(0);
      setShowAddForm(false);
      fetchParts();
    } catch (err: any) {
      alert(err.message);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h2 style={{ fontSize: '1.4rem', fontWeight: 800 }}>📦 Spare Parts Inventory Control</h2>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
            Transactional inventory management (Stock decrements automatically upon field usage)
          </p>
        </div>

        <button className="btn-primary" onClick={() => setShowAddForm(!showAddForm)}>
          <Plus size={16} /> Add Inventory Item
        </button>
      </div>

      {showAddForm && (
        <div className="glass-card" style={{ padding: 20 }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 12 }}>New Inventory Part</h3>
          <form onSubmit={handleCreatePart} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <input type="text" className="form-control" placeholder="Part Name *" value={name} onChange={(e) => setName(e.target.value)} required />
            <input type="text" className="form-control" placeholder="SKU Code (Unique) *" value={sku} onChange={(e) => setSku(e.target.value)} required />
            <input type="number" className="form-control" placeholder="Unit Cost ($) *" step="0.01" min="0" value={unitCost} onChange={(e) => setUnitCost(Number(e.target.value))} required />
            <input type="number" className="form-control" placeholder="Stock Qty *" min="0" value={stockQty} onChange={(e) => setStockQty(Number(e.target.value))} required />
            <div style={{ gridColumn: '1 / -1', display: 'flex', justifyContent: 'flex-end', gap: 8 }}>
              <button type="button" className="btn-secondary btn-sm" onClick={() => setShowAddForm(false)}>Cancel</button>
              <button type="submit" className="btn-primary btn-sm">Save Part</button>
            </div>
          </form>
        </div>
      )}

      <div className="glass-card" style={{ padding: 20 }}>
        {loading ? (
          <div style={{ textAlign: 'center', padding: 40, color: 'var(--text-muted)' }}>Loading inventory...</div>
        ) : (
          <div className="table-container">
            <table className="custom-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Part Name</th>
                  <th>SKU</th>
                  <th>Unit Cost ($)</th>
                  <th>Stock Quantity</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {parts.map((p) => (
                  <tr key={p.id}>
                    <td>#{p.id}</td>
                    <td style={{ fontWeight: 600 }}>{p.name}</td>
                    <td style={{ fontFamily: 'var(--font-mono)' }}>{p.sku}</td>
                    <td>${p.unitCost.toFixed(2)}</td>
                    <td style={{ fontWeight: 700, fontSize: '1rem' }}>{p.stockQty}</td>
                    <td>
                      {p.stockQty <= 5 ? (
                        <span className="badge badge-urgent" style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
                          <AlertCircle size={12} /> LOW STOCK
                        </span>
                      ) : (
                        <span className="badge badge-completed">IN STOCK</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};
