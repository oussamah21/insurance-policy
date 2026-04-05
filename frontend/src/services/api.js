import axios from 'axios'

const BASE = import.meta.env.VITE_API_URL || '/api'

const http = axios.create({ baseURL: BASE })

export const policyApi = {
  getAll:   ()       => http.get('/policies').then(r => r.data),
  getById:  (id)     => http.get(`/policies/${id}`).then(r => r.data),
  create:   (data)   => http.post('/policies', data).then(r => r.data),
  update:   (id, d)  => http.put(`/policies/${id}`, d).then(r => r.data),
}
