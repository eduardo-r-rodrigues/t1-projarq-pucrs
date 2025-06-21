import React, { useState, useEffect } from 'react';
import { Activity, CheckCircle, XCircle, RefreshCw, Server } from 'lucide-react';
import { healthCheck } from '../services/api';
import toast from 'react-hot-toast';

const Health = () => {
  const [healthStatus, setHealthStatus] = useState(null);
  const [loading, setLoading] = useState(true);
  const [lastChecked, setLastChecked] = useState(null);

  useEffect(() => {
    checkHealth();
  }, []);

  const checkHealth = async () => {
    try {
      setLoading(true);
      const status = await healthCheck();
      setHealthStatus(status);
      setLastChecked(new Date());
    } catch (error) {
      setHealthStatus({ allHealthy: false, error: error.message });
      setLastChecked(new Date());
    } finally {
      setLoading(false);
    }
  };

  const services = [
    {
      name: 'API Gateway',
      description: 'Serviço de gateway que roteia requisições',
      port: 8080,
      status: healthStatus?.allHealthy ? 'online' : 'offline',
      icon: Server
    },
    {
      name: 'Sales Service',
      description: 'Serviço de gerenciamento de produtos e pedidos',
      port: 8081,
      status: healthStatus?.allHealthy ? 'online' : 'offline',
      icon: Server
    },
    {
      name: 'Tax Service',
      description: 'Serviço de cálculo de impostos',
      port: 8082,
      status: healthStatus?.allHealthy ? 'online' : 'offline',
      icon: Server
    },
    {
      name: 'Sales Registry Service',
      description: 'Serviço de registro e relatórios de vendas',
      port: 8083,
      status: healthStatus?.allHealthy ? 'online' : 'offline',
      icon: Server
    },
    {
      name: 'Eureka Server',
      description: 'Servidor de descoberta de serviços',
      port: 8761,
      status: healthStatus?.allHealthy ? 'online' : 'offline',
      icon: Server
    },
    {
      name: 'RabbitMQ',
      description: 'Message broker para comunicação entre serviços',
      port: 5672,
      status: healthStatus?.allHealthy ? 'online' : 'offline',
      icon: Server
    }
  ];

  const getStatusColor = (status) => {
    return status === 'online' ? 'text-green-600' : 'text-red-600';
  };

  const getStatusBgColor = (status) => {
    return status === 'online' ? 'bg-green-100' : 'bg-red-100';
  };

  const getStatusTextColor = (status) => {
    return status === 'online' ? 'text-green-800' : 'text-red-800';
  };

  const formatLastChecked = () => {
    if (!lastChecked) return 'Nunca';
    return lastChecked.toLocaleString('pt-BR');
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Status dos Serviços</h1>
          <p className="mt-2 text-gray-600">
            Monitoramento em tempo real dos microserviços
          </p>
        </div>
        <div className="flex space-x-3">
          <button
            onClick={checkHealth}
            disabled={loading}
            className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
          >
            {loading ? (
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
            ) : (
              <>
                <RefreshCw className="h-4 w-4 mr-2" />
                Verificar Status
              </>
            )}
          </button>
        </div>
      </div>

      {/* Overall Status */}
      <div className="bg-white rounded-lg shadow-sm border p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-900">Status Geral</h2>
          <div className="flex items-center space-x-2">
            <Activity className="h-5 w-5 text-gray-400" />
            <span className="text-sm text-gray-500">
              Última verificação: {formatLastChecked()}
            </span>
          </div>
        </div>
        
        <div className="flex items-center space-x-4">
          {healthStatus?.allHealthy ? (
            <>
              <CheckCircle className="h-8 w-8 text-green-600" />
              <div>
                <p className="text-lg font-semibold text-green-900">Todos os serviços estão online</p>
                <p className="text-sm text-green-700">Sistema funcionando normalmente</p>
              </div>
            </>
          ) : (
            <>
              <XCircle className="h-8 w-8 text-red-600" />
              <div>
                <p className="text-lg font-semibold text-red-900">Alguns serviços estão offline</p>
                <p className="text-sm text-red-700">
                  {healthStatus?.error || 'Erro na verificação de status'}
                </p>
              </div>
            </>
          )}
        </div>
      </div>

      {/* Services Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {services.map((service) => {
          const Icon = service.icon;
          return (
            <div key={service.name} className="bg-white rounded-lg shadow-sm border p-6">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-3">
                  <div className={`p-2 rounded-lg ${getStatusBgColor(service.status)}`}>
                    <Icon className={`h-5 w-5 ${getStatusColor(service.status)}`} />
                  </div>
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900">{service.name}</h3>
                    <p className="text-sm text-gray-500">Porta {service.port}</p>
                  </div>
                </div>
                <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusBgColor(service.status)} ${getStatusTextColor(service.status)}`}>
                  {service.status === 'online' ? 'Online' : 'Offline'}
                </span>
              </div>
              
              <p className="text-sm text-gray-600 mb-4">{service.description}</p>
              
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-2">
                  <div className={`w-2 h-2 rounded-full ${service.status === 'online' ? 'bg-green-500' : 'bg-red-500'}`}></div>
                  <span className="text-xs text-gray-500">
                    {service.status === 'online' ? 'Respondendo' : 'Não respondendo'}
                  </span>
                </div>
                
                <a
                  href={`http://localhost:${service.port}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-xs text-primary-600 hover:text-primary-700 font-medium"
                >
                  Acessar
                </a>
              </div>
            </div>
          );
        })}
      </div>

      {/* System Information */}
      <div className="bg-white rounded-lg shadow-sm border p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Informações do Sistema</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h3 className="text-md font-medium text-gray-900 mb-3">Arquitetura</h3>
            <div className="space-y-2">
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">Padrão:</span>
                <span className="text-sm font-medium text-gray-900">Microserviços</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">Service Discovery:</span>
                <span className="text-sm font-medium text-gray-900">Eureka</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">API Gateway:</span>
                <span className="text-sm font-medium text-gray-900">Spring Cloud Gateway</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">Message Broker:</span>
                <span className="text-sm font-medium text-gray-900">RabbitMQ</span>
              </div>
            </div>
          </div>
          
          <div>
            <h3 className="text-md font-medium text-gray-900 mb-3">Endpoints</h3>
            <div className="space-y-2">
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">API Gateway:</span>
                <span className="text-sm font-medium text-gray-900">http://localhost:8080</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">Eureka Dashboard:</span>
                <span className="text-sm font-medium text-gray-900">http://localhost:8761</span>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-gray-600">RabbitMQ Management:</span>
                <span className="text-sm font-medium text-gray-900">http://localhost:15672</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Troubleshooting */}
      {!healthStatus?.allHealthy && (
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-yellow-900 mb-3">Solução de Problemas</h3>
          <div className="space-y-2 text-sm text-yellow-800">
            <p>• Verifique se todos os microserviços estão rodando</p>
            <p>• Confirme se o Eureka Server está ativo na porta 8761</p>
            <p>• Verifique se o RabbitMQ está rodando na porta 5672</p>
            <p>• Certifique-se de que o API Gateway está acessível na porta 8080</p>
            <p>• Verifique os logs de cada serviço para identificar erros</p>
          </div>
        </div>
      )}
    </div>
  );
};

export default Health; 