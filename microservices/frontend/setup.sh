#!/bin/bash

echo "🚀 Configurando Frontend React para Sistema de Vendas"
echo "=================================================="

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js não está instalado. Por favor, instale o Node.js 16+ primeiro."
    exit 1
fi

# Check Node.js version
NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 16 ]; then
    echo "❌ Node.js versão 16+ é necessária. Versão atual: $(node -v)"
    exit 1
fi

echo "✅ Node.js $(node -v) detectado"

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "❌ npm não está instalado. Por favor, instale o npm primeiro."
    exit 1
fi

echo "✅ npm $(npm -v) detectado"

# Install dependencies
echo "📦 Instalando dependências..."
npm install

if [ $? -eq 0 ]; then
    echo "✅ Dependências instaladas com sucesso!"
else
    echo "❌ Erro ao instalar dependências"
    exit 1
fi

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo "🔧 Criando arquivo .env..."
    cat > .env << EOF
# Frontend Configuration
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_ENVIRONMENT=development
EOF
    echo "✅ Arquivo .env criado"
fi

echo ""
echo "🎉 Configuração concluída!"
echo ""
echo "📋 Próximos passos:"
echo "1. Certifique-se de que os microserviços estão rodando:"
echo "   - Eureka Server (porta 8761)"
echo "   - API Gateway (porta 8080)"
echo "   - Sales Service (porta 8081)"
echo "   - Tax Service (porta 8082)"
echo "   - Sales Registry Service (porta 8083)"
echo "   - RabbitMQ (porta 5672)"
echo ""
echo "2. Execute o frontend:"
echo "   npm start"
echo ""
echo "3. Acesse a aplicação:"
echo "   http://localhost:3000"
echo ""
echo "🔗 Links úteis:"
echo "   - API Gateway: http://localhost:8080"
echo "   - Eureka Dashboard: http://localhost:8761"
echo "   - RabbitMQ Management: http://localhost:15672 (admin/admin)"
echo ""
echo "📚 Documentação: README.md" 