import { useState, useEffect } from 'react'

const EMPTY = { name: '', status: 'ACTIVE', startDate: '', endDate: '' }

export default function PolicyModal({ policy, onClose, onSave, loading }) {
  const [form, setForm]   = useState(EMPTY)
  const [errors, setErrors] = useState({})
  const isEdit = Boolean(policy)

  useEffect(() => {
    if (policy) {
      setForm({
        name:      policy.name,
        status:    policy.status,
        startDate: policy.startDate,
        endDate:   policy.endDate,
      })
    } else {
      setForm(EMPTY)
    }
    setErrors({})
  }, [policy])

  const change = (e) => {
    const { name, value } = e.target
    setForm(f => ({ ...f, [name]: value }))
    if (errors[name]) setErrors(e => ({ ...e, [name]: '' }))
  }

  const validate = () => {
    const e = {}
    if (!form.name.trim())  e.name      = 'Name is required'
    if (!form.startDate)    e.startDate = 'Start date required'
    if (!form.endDate)      e.endDate   = 'End date required'
    if (form.startDate && form.endDate && form.endDate < form.startDate)
      e.endDate = 'End date must be after start date'
    setErrors(e)
    return Object.keys(e).length === 0
  }

  const submit = () => {
    if (!validate()) return
    onSave(form)
  }

  const handleBackdrop = (e) => {
    if (e.target === e.currentTarget) onClose()
  }

  return (
    <div className="modal-backdrop" onClick={handleBackdrop}>
      <div className="modal" role="dialog" aria-modal="true">
        <div className="modal-header">
          <span className="modal-title">
            {isEdit ? '✏️ Edit Policy' : '+ New Policy'}
          </span>
          <button className="modal-close" onClick={onClose} aria-label="Close">✕</button>
        </div>

        <div className="modal-body">
          <div className="form-group">
            <label htmlFor="name">Policy Name</label>
            <input
              id="name" name="name" className="form-control"
              placeholder="e.g. Home Insurance Premium"
              value={form.name} onChange={change}
            />
            {errors.name && <p className="form-error">{errors.name}</p>}
          </div>

          <div className="form-group">
            <label htmlFor="status">Status</label>
            <select id="status" name="status" className="form-control" value={form.status} onChange={change}>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
            </select>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="startDate">Start Date</label>
              <input
                id="startDate" name="startDate" type="date" className="form-control"
                value={form.startDate} onChange={change}
              />
              {errors.startDate && <p className="form-error">{errors.startDate}</p>}
            </div>
            <div className="form-group">
              <label htmlFor="endDate">End Date</label>
              <input
                id="endDate" name="endDate" type="date" className="form-control"
                value={form.endDate} onChange={change}
              />
              {errors.endDate && <p className="form-error">{errors.endDate}</p>}
            </div>
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn btn-ghost" onClick={onClose} disabled={loading}>
            Cancel
          </button>
          <button className="btn btn-primary" onClick={submit} disabled={loading}>
            {loading ? <><span className="spinner" /> Saving…</> : isEdit ? 'Update Policy' : 'Create Policy'}
          </button>
        </div>
      </div>
    </div>
  )
}
