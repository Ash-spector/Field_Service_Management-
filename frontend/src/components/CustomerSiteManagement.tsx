import React, { useState, useEffect } from 'react';
import { Customer, Site } from '../types';
import { api } from '../services/api';
import { Plus, Building, MapPin, Search } from 'lucide-react';

export const CustomerSiteManagement: React.FC = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [sites, setSites] = useState<Site[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  // Forms
  const [showCustForm, setShowCustForm] = useState<boolean>(false);
  const [showSiteForm, setShowSiteForm] = useState<boolean>(false);

  const [custName, setCustName] = useState('');
  const [custEmail, setCustEmail] = useState('');
  const [custPhone, setCustPhone] = useState('');
  const [custAddress, setCustAddress] = useState('');

  const [siteCustId, setSiteCustId] = useState<number | ''>('');
  const [siteName, setSiteName] = useState('');
  const [siteAddress, setSiteAddress] = useState('');
  const [siteContact, setSiteContact] = useState('');
  const [sitePhone, setSitePhone] = useState('');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [custData, sitesData] = await Promise.all([
        api.getCustomers(),
        api.getAllSites(),
      ]);
      setCustomers(custData.content);
      setSites(sitesData);
      if (custData.content.length > 0) {
        setSiteCustId(custData.content[0].id);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateCustomer = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.createCustomer({
        name: custName,
        contactEmail: custEmail,
        phone: custPhone,
        address: custAddress,
      });
      setCustName('');
      setCustEmail('');
      setCustPhone('');
      setCustAddress('');
      setShowCustForm(false);
      fetchData();
    } catch (err: any) {
      alert(err.message);
    }
  };

  const handleCreateSite = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!siteCustId) return;
    try {
      await api.createSite({
        customerId: Number(siteCustId),
        name: siteName,
        address: siteAddress,
        contactName: siteContact,
        contactPhone: sitePhone,
      });
      setSiteName('');
      setSiteAddress('');
      setSiteContact('');
      setSitePhone('');
      setShowSiteForm(false);
      fetchData();
    } catch (err: any) {
      alert(err.message);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h2 style={{ fontSize: '1.4rem', fontWeight: 800 }}>🏢 Customer &amp; Site Directory</h2>
          <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Manage client organizations and facility building locations</p>
        </div>

        <div style={{ display: 'flex', gap: 10 }}>
          <button className="btn-secondary" onClick={() => setShowCustForm(!showCustForm)}>
            <Plus size={16} /> Add Customer Org
          </button>
          <button className="btn-primary" onClick={() => setShowSiteForm(!showSiteForm)}>
            <Plus size={16} /> Add Site Location
          </button>
        </div>
      </div>

      {/* New Customer Form */}
      {showCustForm && (
        <div className="glass-card" style={{ padding: 20 }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 12 }}>New Customer Organization</h3>
          <form onSubmit={handleCreateCustomer} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <input type="text" className="form-control" placeholder="Company Name *" value={custName} onChange={(e) => setCustName(e.target.value)} required />
            <input type="email" className="form-control" placeholder="Contact Email" value={custEmail} onChange={(e) => setCustEmail(e.target.value)} />
            <input type="text" className="form-control" placeholder="Phone Number" value={custPhone} onChange={(e) => setCustPhone(e.target.value)} />
            <input type="text" className="form-control" placeholder="Address" value={custAddress} onChange={(e) => setCustAddress(e.target.value)} />
            <div style={{ gridColumn: '1 / -1', display: 'flex', justifyContent: 'flex-end', gap: 8 }}>
              <button type="button" className="btn-secondary btn-sm" onClick={() => setShowCustForm(false)}>Cancel</button>
              <button type="submit" className="btn-primary btn-sm">Save Customer</button>
            </div>
          </form>
        </div>
      )}

      {/* New Site Form */}
      {showSiteForm && (
        <div className="glass-card" style={{ padding: 20 }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: 12 }}>New Facility Site</h3>
          <form onSubmit={handleCreateSite} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <select className="form-control" value={siteCustId} onChange={(e) => setSiteCustId(Number(e.target.value))} required>
              <option value="">Select Owner Customer...</option>
              {customers.map((c) => (
                <option key={c.id} value={c.id}>{c.name}</option>
              ))}
            </select>
            <input type="text" className="form-control" placeholder="Site / Building Name *" value={siteName} onChange={(e) => setSiteName(e.target.value)} required />
            <input type="text" className="form-control" placeholder="Physical Address" value={siteAddress} onChange={(e) => setSiteAddress(e.target.value)} />
            <input type="text" className="form-control" placeholder="Facility Manager Contact" value={siteContact} onChange={(e) => setSiteContact(e.target.value)} />
            <div style={{ gridColumn: '1 / -1', display: 'flex', justifyContent: 'flex-end', gap: 8 }}>
              <button type="button" className="btn-secondary btn-sm" onClick={() => setShowSiteForm(false)}>Cancel</button>
              <button type="submit" className="btn-primary btn-sm">Save Site</button>
            </div>
          </form>
        </div>
      )}

      {/* Customers Table */}
      <div className="glass-card" style={{ padding: 20 }}>
        <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: 14, display: 'flex', alignItems: 'center', gap: 8 }}>
          <Building size={18} style={{ color: 'var(--primary)' }} /> Registered Client Organizations ({customers.length})
        </h3>
        <div className="table-container">
          <table className="custom-table">
            <thead>
              <tr>
                <th>Customer ID</th>
                <th>Name</th>
                <th>Contact Email</th>
                <th>Phone</th>
                <th>Sites Count</th>
              </tr>
            </thead>
            <tbody>
              {customers.map((c) => (
                <tr key={c.id}>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>CUST-{c.id}</td>
                  <td style={{ fontWeight: 600 }}>{c.name}</td>
                  <td>{c.contactEmail || 'N/A'}</td>
                  <td>{c.phone || 'N/A'}</td>
                  <td><span className="badge badge-new">{c.siteCount || 0} sites</span></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Sites Table */}
      <div className="glass-card" style={{ padding: 20 }}>
        <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: 14, display: 'flex', alignItems: 'center', gap: 8 }}>
          <MapPin size={18} style={{ color: 'var(--secondary)' }} /> Building &amp; Facility Sites ({sites.length})
        </h3>
        <div className="table-container">
          <table className="custom-table">
            <thead>
              <tr>
                <th>Site ID</th>
                <th>Site Name</th>
                <th>Customer Org</th>
                <th>Address</th>
                <th>Contact Person</th>
              </tr>
            </thead>
            <tbody>
              {sites.map((s) => (
                <tr key={s.id}>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>SITE-{s.id}</td>
                  <td style={{ fontWeight: 600 }}>{s.name}</td>
                  <td>{s.customerName}</td>
                  <td>{s.address || 'N/A'}</td>
                  <td>{s.contactName} ({s.contactPhone || 'N/A'})</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
