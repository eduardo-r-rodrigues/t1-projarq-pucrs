import React, { useState, useEffect } from 'react';
import { BarChart3, Calendar, Download, RefreshCw } from 'lucide-react';
import { salesRegistryAPI } from '../services/api';
import toast from 'react-hot-toast';

const Reports = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
  const [currentReport, setCurrentReport] = useState(null);

  useEffect(() => {
    loadReports();
  }, []);

  const loadReports = async () => {
    try {
      setLoading(true);
      const response = await salesRegistryAPI.getMonthlyReport(selectedYear, selectedMonth);
      setCurrentReport(response.data);
      setReports(prev => [...prev, response.data]);
    } catch (error) {
      toast.error('Erro ao carregar relatório');
      console.error('Error loading report:', error);
    } finally {
      setLoading(false);
    }
  };

  const generateReport = async () => {
    try {
      setLoading(true);
      const response = await salesRegistryAPI.getMonthlyReport(selectedYear, selectedMonth);
      setCurrentReport(response.data);
      
      // Add to reports history if not already present
      const reportExists = reports.some(report => 
        report.year === selectedYear && report.month === selectedMonth
      );
      
      if (!reportExists) {
        setReports(prev => [...prev, response.data]);
      }
      
      toast.success('Relatório gerado com sucesso!');
    } catch (error) {
      toast.error('Erro ao gerar relatório');
      console.error('Error generating report:', error);
    } finally {
      setLoading(false);
    }
  };

  const getMonthName = (month) => {
    const months = [
      'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
      'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
    ];
    return months[month - 1];
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  };

  const getYears = () => {
    const currentYear = new Date().getFullYear();
    const years = [];
    for (let year = currentYear; year >= currentYear - 5; year--) {
      years.push(year);
    }
    return years;
  };

  const getMonths = () => {
    return Array.from({ length: 12 }, (_, i) => i + 1);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Relatórios</h1>
          <p className="mt-2 text-gray-600">
            Visualize relatórios mensais de vendas
          </p>
        </div>
        <div className="flex space-x-3">
          <button
            onClick={loadReports}
            className="flex items-center px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
          >
            <RefreshCw className="h-5 w-5 mr-2" />
            Atualizar
          </button>
        </div>
      </div>

      {/* Report Generator */}
      <div className="bg-white rounded-lg shadow-sm border p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Gerar Relatório</h2>
        
        <div className="flex space-x-4 items-end">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Ano
            </label>
            <select
              value={selectedYear}
              onChange={(e) => setSelectedYear(parseInt(e.target.value))}
              className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
              {getYears().map(year => (
                <option key={year} value={year}>{year}</option>
              ))}
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Mês
            </label>
            <select
              value={selectedMonth}
              onChange={(e) => setSelectedMonth(parseInt(e.target.value))}
              className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
              {getMonths().map(month => (
                <option key={month} value={month}>{getMonthName(month)}</option>
              ))}
            </select>
          </div>
          
          <button
            onClick={generateReport}
            disabled={loading}
            className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
          >
            {loading ? (
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
            ) : (
              <>
                <BarChart3 className="h-4 w-4 mr-2" />
                Gerar Relatório
              </>
            )}
          </button>
        </div>
      </div>

      {/* Current Report */}
      {currentReport && (
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold text-gray-900">
              Relatório - {getMonthName(currentReport.month)}/{currentReport.year}
            </h2>
            <button className="flex items-center px-3 py-1 text-primary-600 hover:text-primary-700 text-sm">
              <Download className="h-4 w-4 mr-1" />
              Exportar
            </button>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="p-4 bg-blue-50 rounded-lg">
              <h3 className="text-sm font-medium text-blue-900 mb-1">Total de Vendas</h3>
              <p className="text-2xl font-bold text-blue-900">
                {formatCurrency(currentReport.totalSales || 0)}
              </p>
            </div>
            
            <div className="p-4 bg-green-50 rounded-lg">
              <h3 className="text-sm font-medium text-green-900 mb-1">Total de Pedidos</h3>
              <p className="text-2xl font-bold text-green-900">
                {currentReport.totalOrders || 0}
              </p>
            </div>
            
            <div className="p-4 bg-purple-50 rounded-lg">
              <h3 className="text-sm font-medium text-purple-900 mb-1">Ticket Médio</h3>
              <p className="text-2xl font-bold text-purple-900">
                {formatCurrency((currentReport.totalSales || 0) / (currentReport.totalOrders || 1))}
              </p>
            </div>
          </div>

          {/* Sales by State */}
          {currentReport.salesByState && (
            <div className="mt-6">
              <h3 className="text-md font-medium text-gray-900 mb-3">Vendas por Estado</h3>
              <div className="space-y-2">
                {Object.entries(currentReport.salesByState).map(([state, amount]) => (
                  <div key={state} className="flex justify-between items-center p-3 bg-gray-50 rounded">
                    <span className="text-sm font-medium text-gray-700">{state}</span>
                    <span className="text-sm font-semibold text-gray-900">
                      {formatCurrency(amount)}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Top Products */}
          {currentReport.topProducts && (
            <div className="mt-6">
              <h3 className="text-md font-medium text-gray-900 mb-3">Produtos Mais Vendidos</h3>
              <div className="space-y-2">
                {currentReport.topProducts.map((product, index) => (
                  <div key={index} className="flex justify-between items-center p-3 bg-gray-50 rounded">
                    <div>
                      <span className="text-sm font-medium text-gray-700">{product.name}</span>
                      <p className="text-xs text-gray-500">Código: {product.code}</p>
                    </div>
                    <div className="text-right">
                      <span className="text-sm font-semibold text-gray-900">
                        {product.quantity} unidades
                      </span>
                      <p className="text-xs text-gray-500">
                        {formatCurrency(product.totalValue)}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Daily Sales */}
          {currentReport.dailySales && (
            <div className="mt-6">
              <h3 className="text-md font-medium text-gray-900 mb-3">Vendas Diárias</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
                {Object.entries(currentReport.dailySales).map(([date, amount]) => (
                  <div key={date} className="p-3 bg-gray-50 rounded">
                    <p className="text-sm font-medium text-gray-700">{date}</p>
                    <p className="text-lg font-semibold text-gray-900">
                      {formatCurrency(amount)}
                    </p>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}

      {/* Reports History */}
      {reports.length > 0 && (
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Histórico de Relatórios</h2>
          
          <div className="space-y-3">
            {reports.map((report, index) => (
              <div key={index} className="flex justify-between items-center p-3 border border-gray-200 rounded-lg hover:bg-gray-50">
                <div className="flex items-center">
                  <Calendar className="h-5 w-5 text-gray-400 mr-3" />
                  <div>
                    <p className="text-sm font-medium text-gray-900">
                      {getMonthName(report.month)}/{report.year}
                    </p>
                    <p className="text-xs text-gray-500">
                      Total: {formatCurrency(report.totalSales || 0)} | Pedidos: {report.totalOrders || 0}
                    </p>
                  </div>
                </div>
                <button
                  onClick={() => setCurrentReport(report)}
                  className="text-primary-600 hover:text-primary-700 text-sm font-medium"
                >
                  Visualizar
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Empty State */}
      {!currentReport && reports.length === 0 && (
        <div className="text-center py-12">
          <BarChart3 className="h-12 w-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 mb-2">Nenhum relatório gerado</h3>
          <p className="text-gray-500">
            Selecione um período e clique em "Gerar Relatório" para começar
          </p>
        </div>
      )}
    </div>
  );
};

export default Reports; 