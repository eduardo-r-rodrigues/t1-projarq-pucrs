import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { 
  Package, 
  ShoppingCart, 
  FileText, 
  BarChart3, 
  Activity,
  TrendingUp,
  DollarSign,
  Users
} from 'lucide-react';
import { healthCheck } from '../services/api';

const Dashboard = () => {
  const [healthStatus, setHealthStatus] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkHealth = async () => {
      try {
        const status = await healthCheck();
        setHealthStatus(status);
      } catch (error) {
        setHealthStatus({ allHealthy: false, error: error.message });
      } finally {
        setLoading(false);
      }
    };

    checkHealth();
  }, []);

  const stats = [
    {
      title: 'Produtos',
      value: 'Gerenciar',
      description: 'Cadastro e controle de produtos',
      icon: Package,
      color: 'bg-blue-500',
      link: '/products'
    },
    {
      title: 'Pedidos',
      value: 'Criar',
      description: 'Criação e acompanhamento de pedidos',
      icon: ShoppingCart,
      color: 'bg-green-500',
      link: '/orders'
    },
    {
      title: 'Impostos',
      value: 'Calcular',
      description: 'Calculadora de impostos por estado',
      icon: FileText,
      color: 'bg-yellow-500',
      link: '/tax-calculator'
    },
    {
      title: 'Relatórios',
      value: 'Visualizar',
      description: 'Relatórios mensais de vendas',
      icon: BarChart3,
      color: 'bg-purple-500',
      link: '/reports'
    }
  ];

  const serviceStatus = [
    {
      name: 'Sales Service',
      status: healthStatus?.allHealthy ? 'Online' : 'Offline',
      color: healthStatus?.allHealthy ? 'text-green-600' : 'text-red-600'
    },
    {
      name: 'Tax Service',
      status: healthStatus?.allHealthy ? 'Online' : 'Offline',
      color: healthStatus?.allHealthy ? 'text-green-600' : 'text-red-600'
    },
    {
      name: 'Sales Registry Service',
      status: healthStatus?.allHealthy ? 'Online' : 'Offline',
      color: healthStatus?.allHealthy ? 'text-green-600' : 'text-red-600'
    },
    {
      name: 'API Gateway',
      status: healthStatus?.allHealthy ? 'Online' : 'Offline',
      color: healthStatus?.allHealthy ? 'text-green-600' : 'text-red-600'
    }
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="mt-2 text-gray-600">
          Visão geral do sistema de vendas com microserviços
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <Link
              key={stat.title}
              to={stat.link}
              className="bg-white rounded-lg shadow-sm border p-6 hover:shadow-md transition-shadow"
            >
              <div className="flex items-center">
                <div className={`p-3 rounded-lg ${stat.color}`}>
                  <Icon className="h-6 w-6 text-white" />
                </div>
                <div className="ml-4">
                  <p className="text-sm font-medium text-gray-600">{stat.title}</p>
                  <p className="text-2xl font-semibold text-gray-900">{stat.value}</p>
                </div>
              </div>
              <p className="mt-4 text-sm text-gray-500">{stat.description}</p>
            </Link>
          );
        })}
      </div>

      {/* Service Status */}
      <div className="bg-white rounded-lg shadow-sm border p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-900">Status dos Serviços</h2>
          <Activity className="h-5 w-5 text-gray-400" />
        </div>
        
        {loading ? (
          <div className="text-center py-4">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-500 mx-auto"></div>
            <p className="mt-2 text-sm text-gray-500">Verificando status...</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {serviceStatus.map((service) => (
              <div key={service.name} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <span className="text-sm font-medium text-gray-700">{service.name}</span>
                <span className={`text-sm font-medium ${service.color}`}>
                  {service.status}
                </span>
              </div>
            ))}
          </div>
        )}

        {healthStatus?.error && (
          <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <p className="text-sm text-red-600">
              Erro ao verificar status: {healthStatus.error}
            </p>
          </div>
        )}
      </div>

      {/* Quick Actions */}
      <div className="bg-white rounded-lg shadow-sm border p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Ações Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Link
            to="/orders"
            className="flex items-center p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <ShoppingCart className="h-5 w-5 text-primary-500 mr-3" />
            <div>
              <p className="font-medium text-gray-900">Criar Novo Pedido</p>
              <p className="text-sm text-gray-500">Iniciar processo de venda</p>
            </div>
          </Link>
          
          <Link
            to="/products"
            className="flex items-center p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <Package className="h-5 w-5 text-primary-500 mr-3" />
            <div>
              <p className="font-medium text-gray-900">Gerenciar Produtos</p>
              <p className="text-sm text-gray-500">Adicionar ou editar produtos</p>
            </div>
          </Link>
          
          <Link
            to="/reports"
            className="flex items-center p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
          >
            <BarChart3 className="h-5 w-5 text-primary-500 mr-3" />
            <div>
              <p className="font-medium text-gray-900">Ver Relatórios</p>
              <p className="text-sm text-gray-500">Análise de vendas</p>
            </div>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Dashboard; 