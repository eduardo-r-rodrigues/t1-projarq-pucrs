import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Plus, ShoppingCart, CheckCircle, Eye, RefreshCw } from 'lucide-react';
import { ordersAPI, productsAPI } from '../services/api';
import toast from 'react-hot-toast';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [orderItems, setOrderItems] = useState([{ productCode: '', quantity: 1 }]);

  const { register, handleSubmit, reset, formState: { errors } } = useForm();

  useEffect(() => {
    loadOrders();
    loadProducts();
  }, []);

  const loadOrders = async () => {
    try {
      setLoading(true);
      const startDate = new Date(new Date().getFullYear(), 0, 1).toISOString();
      const endDate = new Date().toISOString();
      const response = await ordersAPI.getAll(startDate, endDate);
      setOrders(response.data);
    } catch (error) {
      toast.error('Erro ao carregar pedidos');
      console.error('Error loading orders:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadProducts = async () => {
    try {
      const response = await productsAPI.getAll();
      setProducts(response.data);
    } catch (error) {
      console.error('Error loading products:', error);
    }
  };

  const addOrderItem = () => {
    setOrderItems([...orderItems, { productCode: '', quantity: 1 }]);
  };

  const removeOrderItem = (index) => {
    if (orderItems.length > 1) {
      setOrderItems(orderItems.filter((_, i) => i !== index));
    }
  };

  const updateOrderItem = (index, field, value) => {
    const updatedItems = [...orderItems];
    updatedItems[index][field] = value;
    setOrderItems(updatedItems);
  };

  const onSubmit = async (data) => {
    try {
      const orderData = {
        ...data,
        items: orderItems.filter(item => item.productCode && item.quantity > 0)
      };

      await ordersAPI.create(orderData);
      toast.success('Pedido criado com sucesso!');
      setShowForm(false);
      setOrderItems([{ productCode: '', quantity: 1 }]);
      reset();
      loadOrders();
    } catch (error) {
      toast.error('Erro ao criar pedido');
      console.error('Error creating order:', error);
    }
  };

  const handleConfirmOrder = async (orderId) => {
    try {
      await ordersAPI.confirm(orderId);
      toast.success('Pedido confirmado com sucesso!');
      loadOrders();
    } catch (error) {
      toast.error('Erro ao confirmar pedido');
      console.error('Error confirming order:', error);
    }
  };

  const handleViewOrder = async (orderId) => {
    try {
      const response = await ordersAPI.getById(orderId);
      setSelectedOrder(response.data);
    } catch (error) {
      toast.error('Erro ao carregar detalhes do pedido');
      console.error('Error loading order details:', error);
    }
  };

  const handleCancel = () => {
    setShowForm(false);
    setSelectedOrder(null);
    setOrderItems([{ productCode: '', quantity: 1 }]);
    reset();
  };

  const getProductName = (productCode) => {
    const product = products.find(p => p.code === productCode);
    return product ? product.name : productCode;
  };

  const getProductPrice = (productCode) => {
    const product = products.find(p => p.code === productCode);
    return product ? product.price : 0;
  };

  const calculateOrderTotal = (items) => {
    return items.reduce((total, item) => {
      const price = getProductPrice(item.productCode);
      return total + (price * item.quantity);
    }, 0);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-500"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Pedidos</h1>
          <p className="mt-2 text-gray-600">
            Gerencie pedidos e confirmações
          </p>
        </div>
        <div className="flex space-x-3">
          <button
            onClick={() => setShowForm(true)}
            className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors"
          >
            <Plus className="h-5 w-5 mr-2" />
            Novo Pedido
          </button>
          <button
            onClick={loadOrders}
            className="flex items-center px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
          >
            <RefreshCw className="h-5 w-5 mr-2" />
            Atualizar
          </button>
        </div>
      </div>

      {/* Order Form */}
      {showForm && (
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Novo Pedido</h2>
          
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  ID do Cliente
                </label>
                <input
                  type="text"
                  {...register('customerId', { required: 'ID do cliente é obrigatório' })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
                  placeholder="Ex: CUST001"
                />
                {errors.customerId && (
                  <p className="mt-1 text-sm text-red-600">{errors.customerId.message}</p>
                )}
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Estado
                </label>
                <select
                  {...register('state', { required: 'Estado é obrigatório' })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
                >
                  <option value="">Selecione um estado</option>
                  <option value="RS">Rio Grande do Sul</option>
                  <option value="SP">São Paulo</option>
                  <option value="PE">Pernambuco</option>
                </select>
                {errors.state && (
                  <p className="mt-1 text-sm text-red-600">{errors.state.message}</p>
                )}
              </div>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  País
                </label>
                <input
                  type="text"
                  {...register('country', { required: 'País é obrigatório' })}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
                  placeholder="Brasil"
                  defaultValue="Brasil"
                />
                {errors.country && (
                  <p className="mt-1 text-sm text-red-600">{errors.country.message}</p>
                )}
              </div>
            </div>

            {/* Order Items */}
            <div>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-md font-medium text-gray-900">Itens do Pedido</h3>
                <button
                  type="button"
                  onClick={addOrderItem}
                  className="text-primary-600 hover:text-primary-700 text-sm font-medium"
                >
                  + Adicionar Item
                </button>
              </div>
              
              {orderItems.map((item, index) => (
                <div key={index} className="flex space-x-4 mb-3">
                  <div className="flex-1">
                    <select
                      value={item.productCode}
                      onChange={(e) => updateOrderItem(index, 'productCode', e.target.value)}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
                    >
                      <option value="">Selecione um produto</option>
                      {products.map((product) => (
                        <option key={product.code} value={product.code}>
                          {product.name} - R$ {product.price?.toFixed(2)}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="w-24">
                    <input
                      type="number"
                      value={item.quantity}
                      onChange={(e) => updateOrderItem(index, 'quantity', parseInt(e.target.value) || 1)}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
                      placeholder="Qtd"
                      min="1"
                    />
                  </div>
                  <button
                    type="button"
                    onClick={() => removeOrderItem(index)}
                    className="px-3 py-2 text-red-600 hover:text-red-700"
                    disabled={orderItems.length === 1}
                  >
                    Remover
                  </button>
                </div>
              ))}
            </div>
            
            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={handleCancel}
                className="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors"
              >
                Criar Pedido
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Order Details Modal */}
      {selectedOrder && (
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold text-gray-900">
              Detalhes do Pedido - {selectedOrder.id}
            </h2>
            <button
              onClick={() => setSelectedOrder(null)}
              className="text-gray-400 hover:text-gray-600"
            >
              ✕
            </button>
          </div>
          
          <div className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <p className="text-sm font-medium text-gray-500">Cliente</p>
                <p className="text-sm text-gray-900">{selectedOrder.customerId}</p>
              </div>
              <div>
                <p className="text-sm font-medium text-gray-500">Estado</p>
                <p className="text-sm text-gray-900">{selectedOrder.state}</p>
              </div>
              <div>
                <p className="text-sm font-medium text-gray-500">Status</p>
                <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                  selectedOrder.status === 'CONFIRMED' 
                    ? 'bg-green-100 text-green-800' 
                    : 'bg-yellow-100 text-yellow-800'
                }`}>
                  {selectedOrder.status === 'CONFIRMED' ? 'Confirmado' : 'Pendente'}
                </span>
              </div>
            </div>
            
            <div>
              <p className="text-sm font-medium text-gray-500 mb-2">Itens</p>
              <div className="space-y-2">
                {selectedOrder.items?.map((item, index) => (
                  <div key={index} className="flex justify-between items-center p-2 bg-gray-50 rounded">
                    <span className="text-sm text-gray-900">
                      {getProductName(item.productCode)} x{item.quantity}
                    </span>
                    <span className="text-sm text-gray-900">
                      R$ {(getProductPrice(item.productCode) * item.quantity).toFixed(2)}
                    </span>
                  </div>
                ))}
              </div>
            </div>
            
            <div className="border-t pt-4">
              <div className="flex justify-between items-center">
                <span className="text-lg font-semibold text-gray-900">Total</span>
                <span className="text-lg font-semibold text-gray-900">
                  R$ {calculateOrderTotal(selectedOrder.items || []).toFixed(2)}
                </span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Orders List */}
      <div className="bg-white rounded-lg shadow-sm border">
        <div className="px-6 py-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">Lista de Pedidos</h2>
        </div>
        
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  ID
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Cliente
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Estado
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Ações
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {orders.map((order) => (
                <tr key={order.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {order.id}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {order.customerId}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {order.state}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                      order.status === 'CONFIRMED' 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {order.status === 'CONFIRMED' ? 'Confirmado' : 'Pendente'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    R$ {calculateOrderTotal(order.items || []).toFixed(2)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                    <button
                      onClick={() => handleViewOrder(order.id)}
                      className="text-primary-600 hover:text-primary-900"
                    >
                      <Eye className="h-4 w-4" />
                    </button>
                    {order.status !== 'CONFIRMED' && (
                      <button
                        onClick={() => handleConfirmOrder(order.id)}
                        className="text-green-600 hover:text-green-900"
                      >
                        <CheckCircle className="h-4 w-4" />
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        
        {orders.length === 0 && (
          <div className="text-center py-8">
            <ShoppingCart className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500">Nenhum pedido encontrado</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Orders; 