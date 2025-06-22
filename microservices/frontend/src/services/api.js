import axios from 'axios';

// Use relative URL to work with React proxy
const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Products API
export const productsAPI = {
  getAll: () => api.get('/products'),
  getByCode: (code) => api.get(`/products/${code}`),
  create: (product) => api.post('/products', product),
  restock: (code, quantity) => api.post(`/products/${code}/restock?quantity=${quantity}`),
  health: () => api.get('/products/health'),
};

// Orders API
export const ordersAPI = {
  getAll: (startDate, endDate) => api.get('/orders', {
    params: { startDate, endDate }
  }),
  getById: (id) => api.get(`/orders/${id}`),
  create: (order) => api.post('/orders', order),
  confirm: (id) => api.post(`/orders/${id}/confirm`),
};

// Tax API
export const taxAPI = {
  calculate: (request) => api.post('/tax/calculate', request),
  health: () => api.get('/tax/health'),
};

// Sales Registry API
export const salesRegistryAPI = {
  getMonthlyReport: (year, month) => api.get(`/sales-registry/report/${year}/${month}`),
  health: () => api.get('/sales-registry/health'),
};

// Health check for all services
export const healthCheck = async () => {
  try {
    const [products, tax, salesRegistry] = await Promise.all([
      productsAPI.health(),
      taxAPI.health(),
      salesRegistryAPI.health(),
    ]);
    
    return {
      products: products.data,
      tax: tax.data,
      salesRegistry: salesRegistry.data,
      allHealthy: true,
    };
  } catch (error) {
    return {
      allHealthy: false,
      error: error.message,
    };
  }
};

export default api; 