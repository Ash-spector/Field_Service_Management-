import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Wrench, LayoutDashboard, Kanban, Users, Package, FileText, UserCheck, LogOut, PhoneCall } from 'lucide-react';

interface NavbarProps {
  activeTab: string;
  setActiveTab: (tab: string) => void;
  onOpenNewWorkOrder: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ activeTab, setActiveTab, onOpenNewWorkOrder }) => {
  const { user, logout, switchRoleDemo } = useAuth();

  const isManager = user?.role === 'MANAGER' || user?.role === 'ADMIN';
  const isDispatcher = user?.role === 'DISPATCHER' || isManager;
  const isTechnician = user?.role === 'TECHNICIAN';
  const isCustomer = user?.role === 'CUSTOMER';

  return (
    <header>
      {/* Demo Role Fast Switcher Bar */}
      <div className="demo-role-bar">
        <span style={{ fontWeight: 600, color: 'var(--secondary)', display: 'flex', alignItems: 'center', gap: 4 }}>
          <UserCheck size={14} /> Demo Fast Role Switcher:
        </span>
        <button
          className={`demo-chip ${user?.role === 'MANAGER' ? 'active-chip' : ''}`}
          onClick={() => switchRoleDemo('manager@meridian.com')}
        >
          👑 Manager (Robert)
        </button>
        <button
          className={`demo-chip ${user?.role === 'DISPATCHER' ? 'active-chip' : ''}`}
          onClick={() => switchRoleDemo('dispatcher@meridian.com')}
        >
          🎧 Dispatcher (Elena)
        </button>
        <button
          className={`demo-chip ${user?.email === 'tech.alex@meridian.com' ? 'active-chip' : ''}`}
          onClick={() => switchRoleDemo('tech.alex@meridian.com')}
        >
          🔧 Tech Alex
        </button>
        <button
          className={`demo-chip ${user?.email === 'tech.sarah@meridian.com' ? 'active-chip' : ''}`}
          onClick={() => switchRoleDemo('tech.sarah@meridian.com')}
        >
          🔩 Tech Sarah
        </button>
        <button
          className={`demo-chip ${user?.email === 'customer@acme.com' ? 'active-chip' : ''}`}
          onClick={() => switchRoleDemo('customer@acme.com')}
        >
          🏢 Customer (Acme Corp)
        </button>
      </div>

      {/* Main Navigation */}
      <div className="app-header">
        <div className="nav-container">
          <div className="brand-logo" style={{ cursor: 'pointer' }} onClick={() => setActiveTab('kanban')}>
            <Wrench size={26} />
            <span>KEYSTONE <span style={{ fontSize: '0.7rem', opacity: 0.6, fontWeight: 400 }}>FIELD SERVICE</span></span>
          </div>

          <nav className="nav-links">
            {isManager && (
              <button
                className={`nav-btn ${activeTab === 'dashboard' ? 'active' : ''}`}
                onClick={() => setActiveTab('dashboard')}
              >
                <LayoutDashboard size={18} /> Dashboard & SLA
              </button>
            )}

            {!isCustomer && (
              <button
                className={`nav-btn ${activeTab === 'kanban' ? 'active' : ''}`}
                onClick={() => setActiveTab('kanban')}
              >
                <Kanban size={18} /> Work Orders Board
              </button>
            )}

            {isTechnician && (
              <button
                className={`nav-btn ${activeTab === 'tech-field' ? 'active' : ''}`}
                onClick={() => setActiveTab('tech-field')}
              >
                <Wrench size={18} /> My Assigned Jobs
              </button>
            )}

            {isCustomer && (
              <button
                className={`nav-btn ${activeTab === 'customer-portal' ? 'active' : ''}`}
                onClick={() => setActiveTab('customer-portal')}
              >
                <PhoneCall size={18} /> Customer Portal
              </button>
            )}

            {isDispatcher && (
              <>
                <button
                  className={`nav-btn ${activeTab === 'customers-sites' ? 'active' : ''}`}
                  onClick={() => setActiveTab('customers-sites')}
                >
                  <Users size={18} /> Customers & Sites
                </button>

                <button
                  className={`nav-btn ${activeTab === 'inventory' ? 'active' : ''}`}
                  onClick={() => setActiveTab('inventory')}
                >
                  <Package size={18} /> Spare Parts Stock
                </button>
              </>
            )}
          </nav>

          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            {!isCustomer && (
              <button className="btn-primary btn-sm" onClick={onOpenNewWorkOrder}>
                + Raise Work Order
              </button>
            )}

            <div style={{ fontSize: '0.8rem', textAlign: 'right' }}>
              <div style={{ fontWeight: 600 }}>{user?.firstName} {user?.lastName}</div>
              <div style={{ color: 'var(--text-muted)', fontSize: '0.7rem' }}>{user?.role}</div>
            </div>

            <button className="nav-btn" onClick={logout} title="Sign Out">
              <LogOut size={16} />
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};
