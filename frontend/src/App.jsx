import { useState, useEffect, useCallback, useMemo } from 'react'
import { policyApi } from './services/api'
import PolicyModal from './components/PolicyModal'
import { Toast } from './components/Toast'

let toastIdCounter = 0

function formatDate(d) {
  if (!d) return '—'
  return new Date(d).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' })
}

export default function App() {
  const [policies,   setPolicies]   = useState([])
  const [loading,    setLoading]    = useState(true)
  const [saving,     setSaving]     = useState(false)
  const [modal,      setModal]      = useState(null)   // null | 'create' | policy object
  const [search,     setSearch]     = useState('')
  const [statusFilter, setFilter]  = useState('ALL')
  const [toasts,     setToasts]     = useState([])

  const addToast = (message, type = 'success') => {
    const id = ++toastIdCounter
    setToasts(t => [...t, { id, message, type }])
  }
  const removeToast = (id) => setToasts(t => t.filter(x => x.id !== id))

  const fetchPolicies = useCallback(async () => {
    try {
      setLoading(true)
      const data = await policyApi.getAll()
      setPolicies(data)
    } catch {
      addToast('Failed to load policies', 'error')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetchPolicies() }, [fetchPolicies])

  const filtered = useMemo(() => {
    return policies.filter(p => {
      const matchSearch = p.name.toLowerCase().includes(search.toLowerCase())
      const matchStatus = statusFilter === 'ALL' || p.status === statusFilter
      return matchSearch && matchStatus
    })
  }, [policies, search, statusFilter])

  const stats = useMemo(() => ({
    total:    policies.length,
    active:   policies.filter(p => p.status === 'ACTIVE').length,
    inactive: policies.filter(p => p.status === 'INACTIVE').length,
  }), [policies])

    const handleSave = async (form) => {
        setSaving(true)
        try {
            if (modal && typeof modal === 'object') {
                await policyApi.update(modal.id, form)
                addToast('Policy updated successfully')
            } else {
                await policyApi.create(form)
                addToast('Policy created successfully')
            }
            setModal(null)
            // Wait for Axon's event handler to flush the projection to H2
            // before re-querying (applies to both create and update)
            await new Promise(r => setTimeout(r, 500))
            await fetchPolicies()
        } catch (err) {
            const msg = err?.response?.data?.message || 'An error occurred'
            addToast(msg, 'error')
        } finally {
            setSaving(false)
        }
    }

  const showModal = modal !== null

  return (
    <div className="app-shell">
      {/* ── Header ── */}
      <header className="header">
        <div className="header-brand">
          <div className="header-icon">🛡️</div>
          <div>
            <h1>PolicyHub</h1>
            <div className="header-subtitle">Insurance Management</div>
          </div>
        </div>
        <button className="btn btn-primary" onClick={() => setModal('create')}>
          <span>+</span> New Policy
        </button>
      </header>

      {/* ── Main ── */}
      <main className="main">
        {/* Stats */}
        <div className="stats-bar">
          <div className="stat-card">
            <div className="stat-label">Total Policies</div>
            <div className="stat-value gold">{loading ? '…' : stats.total}</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Active</div>
            <div className="stat-value success">{loading ? '…' : stats.active}</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Inactive</div>
            <div className="stat-value">{loading ? '…' : stats.inactive}</div>
          </div>
        </div>

        {/* Toolbar */}
        <div className="toolbar">
          <div className="toolbar-left">
            <div className="search-wrap">
              <span className="search-icon">🔍</span>
              <input
                className="search-input"
                placeholder="Search policies…"
                value={search}
                onChange={e => setSearch(e.target.value)}
              />
            </div>
            <select
              className="filter-select"
              value={statusFilter}
              onChange={e => setFilter(e.target.value)}
            >
              <option value="ALL">All Statuses</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
            </select>
          </div>
          <div style={{ color: 'var(--text-muted)', fontSize: '0.82rem' }}>
            {filtered.length} {filtered.length === 1 ? 'policy' : 'policies'} found
          </div>
        </div>

        {/* Table */}
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Policy</th>
                <th>Status</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Created</th>
                <th style={{ textAlign: 'right' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                Array.from({ length: 4 }).map((_, i) => (
                  <tr key={i}>
                    {Array.from({ length: 6 }).map((_, j) => (
                      <td key={j}><div className="skeleton" style={{ width: j === 5 ? 80 : '70%', opacity: 1 - i * 0.15 }} /></td>
                    ))}
                  </tr>
                ))
              ) : filtered.length === 0 ? (
                <tr>
                  <td colSpan={6}>
                    <div className="empty-state">
                      <div className="empty-state-icon">📋</div>
                      <h3>No policies found</h3>
                      <p>{search || statusFilter !== 'ALL' ? 'Try adjusting your filters' : 'Create your first policy to get started'}</p>
                    </div>
                  </td>
                </tr>
              ) : (
                filtered.map((p, i) => (
                  <tr key={p.id} style={{ animationDelay: `${i * 0.03}s` }} className="animate-up">
                    <td>
                      <div className="policy-name">{p.name}</div>
                      <div className="policy-id">#{p.id}</div>
                    </td>
                    <td>
                      <span className={`badge badge-${p.status.toLowerCase()}`}>
                        {p.status}
                      </span>
                    </td>
                    <td>{formatDate(p.startDate)}</td>
                    <td>{formatDate(p.endDate)}</td>
                    <td>{formatDate(p.createdAt)}</td>
                    <td>
                      <div className="actions" style={{ justifyContent: 'flex-end' }}>
                        <button
                          className="btn btn-ghost btn-sm"
                          onClick={() => setModal(p)}
                          title="Edit policy"
                        >
                          ✏️ Edit
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </main>

      {/* Modal */}
      {showModal && (
        <PolicyModal
          policy={typeof modal === 'object' ? modal : null}
          onClose={() => setModal(null)}
          onSave={handleSave}
          loading={saving}
        />
      )}

      {/* Toasts */}
      <Toast toasts={toasts} removeToast={removeToast} />
    </div>
  )
}
