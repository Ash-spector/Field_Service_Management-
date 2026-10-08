import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { KanbanBoard } from './components/KanbanBoard';
import { WorkOrderDetailModal } from './components/WorkOrderDetailModal';
import { CreateWorkOrderModal } from './components/CreateWorkOrderModal';
import { ManagerDashboard } from './components/ManagerDashboard';
import { TechnicianFieldView } from './components/TechnicianFieldView';
import { CustomerPortal } from './components/CustomerPortal';
import { CustomerSiteManagement } from './components/CustomerSiteManagement';
import { PartsInventoryView } from './components/PartsInventoryView';
import { Wrench, Lock } from 'lucide-react';

const MainAppContent: React.FC = () => {
  const { user, isLoading } = useAuth();
  const [activeTab, setActiveTab] = useState<string>('kanban');
  const [selectedWorkOrderId, setSelectedWorkOrderId] = useState<number | null>(null);
  const [showCreateModal, setShowCreateModal] = useState<boolean>(false);
  const [refreshTrigger, setRefreshTrigger] = useState<number>(0);

  if (isLoading) {
    return (
      <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--text-muted)' }}>
        Loading Project KEYSTONE...
      </div>
    );
  }

  const triggerRefresh = () => {
    setRefreshTrigger((prev) => prev + 1);
  };

  return (
    <div className="app-layout">
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onOpenNewWorkOrder={() => setShowCreateModal(true)}
      />

      <main className="main-container">
        {activeTab === 'kanban' && (
          <KanbanBoard
            onSelectWorkOrder={(id) => setSelectedWorkOrderId(id)}
            refreshTrigger={refreshTrigger}
          />
        )}

        {activeTab === 'dashboard' && (
          <ManagerDashboard />
        )}

        {activeTab === 'tech-field' && (
          <TechnicianFieldView
            onSelectWorkOrder={(id) => setSelectedWorkOrderId(id)}
            refreshTrigger={refreshTrigger}
          />
        )}

        {activeTab === 'customer-portal' && (
          <CustomerPortal
            onSelectWorkOrder={(id) => setSelectedWorkOrderId(id)}
            refreshTrigger={refreshTrigger}
          />
        )}

        {activeTab === 'customers-sites' && (
          <CustomerSiteManagement />
        )}

        {activeTab === 'inventory' && (
          <PartsInventoryView />
        )}
      </main>

      {/* Detail Modal */}
      {selectedWorkOrderId && (
        <WorkOrderDetailModal
          workOrderId={selectedWorkOrderId}
          onClose={() => setSelectedWorkOrderId(null)}
          onRefresh={triggerRefresh}
        />
      )}

      {/* Create Modal */}
      {showCreateModal && (
        <CreateWorkOrderModal
          onClose={() => setShowCreateModal(false)}
          onSuccess={triggerRefresh}
        />
      )}
    </div>
  );
};

export const App: React.FC = () => {
  return (
    <AuthProvider>
      <MainAppContent />
    </AuthProvider>
  );
};

export default App;
