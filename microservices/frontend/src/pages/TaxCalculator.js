import React, { useState, useEffect } from 'react';
import { Calculator, FileText, RefreshCw } from 'lucide-react';
import { taxAPI, productsAPI } from '../services/api';
import toast from 'react-hot-toast';

const TaxCalculator = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [calculationResult, setCalculationResult] = useState(null);
  const [orderItems, setOrderItems] = useState([{ productCode: '', quantity: 1 }]);
  const [selectedState, setSelectedState] = useState('');

  useEffect(() => {
    loadProducts();
  }, []);

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

  const calculateTaxes = async () => {
    if (!selectedState || orderItems.some(item => !item.productCode || item.quantity <= 0)) {
      toast.error('Por favor, preencha todos os campos obrigatórios');
      return;
    }

    try {
      setLoading(true);
      const request = {
        state: selectedState,
        items: orderItems.filter(item => item.productCode && item.quantity > 0)
      };

      const response = await taxAPI.calculate(request);
      setCalculationResult(response.data);
      toast.success('Cálculo realizado com sucesso!');
    } catch (error) {
      toast.error('Erro ao calcular impostos');
      console.error('Error calculating taxes:', error);
    } finally {
      setLoading(false);
    }
  };

  const getProductName = (productCode) => {
    const product = products.find(p => p.code === productCode);
    return product ? product.name : productCode;
  };

  const getProductPrice = (productCode) => {
    const product = products.find(p => p.code === productCode);
    return product ? product.price : 0;
  };

  const calculateSubtotal = () => {
    return orderItems.reduce((total, item) => {
      if (item.productCode && item.quantity > 0) {
        const price = getProductPrice(item.productCode);
        return total + (price * item.quantity);
      }
      return total;
    }, 0);
  };

  const getStateInfo = (state) => {
    const stateInfo = {
      'RS': {
        name: 'Rio Grande do Sul',
        description: 'Imposto de 10% sobre valor acima de R$ 100,00',
        rate: '10%'
      },
      'SP': {
        name: 'São Paulo',
        description: 'Imposto de 12% sobre o total',
        rate: '12%'
      },
      'PE': {
        name: 'Pernambuco',
        description: 'Imposto diferenciado por produto (5% para essenciais, 15% para outros)',
        rate: '5% - 15%'
      }
    };
    return stateInfo[state] || null;
  };

  const resetCalculator = () => {
    setOrderItems([{ productCode: '', quantity: 1 }]);
    setSelectedState('');
    setCalculationResult(null);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Calculadora de Impostos</h1>
          <p className="mt-2 text-gray-600">
            Calcule impostos por estado e visualize os detalhes
          </p>
        </div>
        <div className="flex space-x-3">
          <button
            onClick={resetCalculator}
            className="flex items-center px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
          >
            <RefreshCw className="h-5 w-5 mr-2" />
            Limpar
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow-sm border p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Dados para Cálculo</h2>
          
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Estado
              </label>
              <select
                value={selectedState}
                onChange={(e) => setSelectedState(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500"
              >
                <option value="">Selecione um estado</option>
                <option value="RS">Rio Grande do Sul</option>
                <option value="SP">São Paulo</option>
                <option value="PE">Pernambuco</option>
              </select>
            </div>

            {selectedState && getStateInfo(selectedState) && (
              <div className="p-3 bg-blue-50 border border-blue-200 rounded-lg">
                <h3 className="text-sm font-medium text-blue-900 mb-1">
                  {getStateInfo(selectedState).name}
                </h3>
                <p className="text-sm text-blue-700">
                  {getStateInfo(selectedState).description}
                </p>
                <p className="text-sm font-medium text-blue-900 mt-1">
                  Taxa: {getStateInfo(selectedState).rate}
                </p>
              </div>
            )}

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

            <div className="border-t pt-4">
              <div className="flex justify-between items-center">
                <span className="text-lg font-semibold text-gray-900">Subtotal</span>
                <span className="text-lg font-semibold text-gray-900">
                  R$ {calculateSubtotal().toFixed(2)}
                </span>
              </div>
            </div>

            <button
              onClick={calculateTaxes}
              disabled={loading || !selectedState || orderItems.some(item => !item.productCode || item.quantity <= 0)}
              className="w-full flex items-center justify-center px-4 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
            >
              {loading ? (
                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
              ) : (
                <>
                  <Calculator className="h-5 w-5 mr-2" />
                  Calcular Impostos
                </>
              )}
            </button>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm border p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Resultado do Cálculo</h2>
          
          {calculationResult ? (
            <div className="space-y-4">
              <div className="p-4 bg-green-50 border border-green-200 rounded-lg">
                <h3 className="text-lg font-semibold text-green-900 mb-2">
                  Cálculo Realizado
                </h3>
                <p className="text-sm text-green-700">
                  Estado: {selectedState} - {getStateInfo(selectedState)?.name}
                </p>
              </div>

              <div className="space-y-3">
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Subtotal:</span>
                  <span className="font-medium">R$ {calculationResult.subtotal?.toFixed(2)}</span>
                </div>
                
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Imposto:</span>
                  <span className="font-medium text-red-600">
                    R$ {calculationResult.taxAmount?.toFixed(2)}
                  </span>
                </div>
                
                <div className="border-t pt-3">
                  <div className="flex justify-between items-center">
                    <span className="text-lg font-semibold text-gray-900">Total:</span>
                    <span className="text-lg font-semibold text-gray-900">
                      R$ {calculationResult.total?.toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>

              {calculationResult.taxDetails && (
                <div className="mt-4 p-3 bg-gray-50 rounded-lg">
                  <h4 className="text-sm font-medium text-gray-900 mb-2">Detalhes dos Impostos</h4>
                  <div className="space-y-1">
                    {calculationResult.taxDetails.map((detail, index) => (
                      <div key={index} className="flex justify-between text-sm">
                        <span className="text-gray-600">{detail.description}</span>
                        <span className="font-medium">R$ {detail.amount?.toFixed(2)}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="text-center py-8">
              <FileText className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">
                Preencha os dados e clique em "Calcular Impostos" para ver o resultado
              </p>
            </div>
          )}
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Informações sobre Impostos</h2>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="p-4 border border-gray-200 rounded-lg">
            <h3 className="font-medium text-gray-900 mb-2">Rio Grande do Sul (RS)</h3>
            <p className="text-sm text-gray-600">
              Imposto de 10% sobre valor acima de R$ 100,00
            </p>
          </div>
          
          <div className="p-4 border border-gray-200 rounded-lg">
            <h3 className="font-medium text-gray-900 mb-2">São Paulo (SP)</h3>
            <p className="text-sm text-gray-600">
              Imposto de 12% sobre o total da compra
            </p>
          </div>
          
          <div className="p-4 border border-gray-200 rounded-lg">
            <h3 className="font-medium text-gray-900 mb-2">Pernambuco (PE)</h3>
            <p className="text-sm text-gray-600">
              Imposto diferenciado por produto: 5% para essenciais, 15% para outros
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TaxCalculator; 