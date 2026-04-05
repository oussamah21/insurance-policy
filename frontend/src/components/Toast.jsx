import { useEffect } from 'react'

export function Toast({ toasts, removeToast }) {
  return (
    <div className="toast-container">
      {toasts.map(t => (
        <ToastItem key={t.id} toast={t} onRemove={removeToast} />
      ))}
    </div>
  )
}

function ToastItem({ toast, onRemove }) {
  useEffect(() => {
    const timer = setTimeout(() => onRemove(toast.id), 3500)
    return () => clearTimeout(timer)
  }, [toast.id, onRemove])

  const icon = toast.type === 'success' ? '✓' : '✕'
  return (
    <div className={`toast toast-${toast.type}`}>
      <span style={{ color: toast.type === 'success' ? 'var(--success)' : 'var(--danger)', fontWeight: 700 }}>{icon}</span>
      {toast.message}
    </div>
  )
}
