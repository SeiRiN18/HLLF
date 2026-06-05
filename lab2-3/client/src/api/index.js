import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(cfg => {
  const token = localStorage.getItem('token')
  if (token) cfg.headers.Authorization = `Bearer ${token}`
  return cfg
})

export const getProducts = (params) => api.get('/products', { params })
export const getCategories = () => api.get('/products/categories')
export const getProduct = (id) => api.get(`/products/${id}`)

export const login = (data) => api.post('/auth/login', data)
export const register = (data) => api.post('/auth/register', data)

export const getOrders = () => api.get('/orders')
export const createOrder = (data) => api.post('/orders', data)

export const getRecommendations = () => api.get('/recommendations')
