# Microservices Architecture - Serviço de Vendas

Este projeto implementa uma arquitetura de microserviços para o sistema de vendas com um frontend React moderno.

## Componentes

### Backend (Microserviços)
1. Eureka Server (Porta 8761)
2. API Gateway (Porta 8080)
3. Sales Service (Porta 8081)
4. Tax Service (Porta 8082)
5. Sales Registry Service (Porta 8083)
6. Neon Database
7. RabbitMQ (Porta 5672)

### Frontend
8. React Frontend (Porta 3000)

## Configuração

Configure as variáveis de ambiente no arquivo `.env`:

```env
NEON_HOST=your-neon-host
NEON_DATABASE=your-database-name
NEON_USERNAME=your-username
NEON_PASSWORD=your-password
NEON_SSL_MODE=require
DATABASE_URL=jdbc:postgresql://your-neon-host/your-database-name?sslmode=require
```

## Execução

### 1. Backend (Microserviços)

```bash
# Iniciar RabbitMQ
docker-compose up -d rabbitmq

# Iniciar microserviços (em terminais separados)
cd eureka-server
.\mvnw.cmd spring-boot:run

cd sales-service
.\mvnw.cmd spring-boot:run

cd tax-service
.\mvnw.cmd spring-boot:run

cd sales-registry-service
.\mvnw.cmd spring-boot:run

cd api-gateway
.\mvnw.cmd spring-boot:run
```

### 2. Frontend (React)

```bash
# Navegar para o diretório do frontend
cd frontend

# Instalar dependências
npm install

# Executar em modo de desenvolvimento
npm start
```

### 3. Acessar a Aplicação

- **Frontend React**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **RabbitMQ Management**: http://localhost:15672 (admin/admin)

## Funcionalidades do Frontend

### 📊 Dashboard
- Visão geral do sistema
- Status dos microserviços em tempo real
- Ações rápidas para navegação

### 📦 Gerenciamento de Produtos
- Listagem, criação e edição de produtos
- Controle de estoque
- Status visual do estoque

### 🛒 Gerenciamento de Pedidos
- Criação de novos pedidos
- Seleção de produtos e quantidades
- Confirmação de pedidos
- Histórico de pedidos

### 🧮 Calculadora de Impostos
- Cálculo de impostos por estado (RS, SP, PE)
- Visualização detalhada dos cálculos
- Informações sobre regras de impostos

### 📈 Relatórios
- Geração de relatórios mensais
- Vendas por estado
- Produtos mais vendidos
- Vendas diárias

### 🔍 Monitoramento
- Status em tempo real de todos os microserviços
- Informações sobre a arquitetura
- Links diretos para dashboards

## Endpoints

### Via API Gateway (http://localhost:8080)

#### Produtos
- `GET /api/products`
- `GET /api/products/{code}`
- `POST /api/products`
- `POST /api/products/{code}/restock`

#### Pedidos
- `GET /api/orders`
- `GET /api/orders/{id}`
- `POST /api/orders`
- `POST /api/orders/{id}/confirm`

#### Impostos
- `POST /api/tax/calculate`
- `GET /api/tax/health`

#### Relatórios
- `GET /api/sales-registry/report/{year}/{month}`
- `GET /api/sales-registry/health`

## Tecnologias Utilizadas

### Backend
- Spring Boot
- Spring Cloud (Eureka, Gateway)
- Spring Data JPA
- RabbitMQ
- PostgreSQL (Neon)

### Frontend
- React 18
- React Router DOM
- React Hook Form
- Axios
- React Hot Toast
- Lucide React
- Tailwind CSS

## Monitoramento

- **Frontend**: http://localhost:3000
- **Eureka Dashboard**: http://localhost:8761
- **RabbitMQ Management**: http://localhost:15672 (admin/admin)

## Estados Suportados

- **RS**: Imposto de 10% sobre valor acima de R$ 100,00
- **SP**: Imposto de 12% sobre o total
- **PE**: Imposto diferenciado por produto (5% para essenciais, 15% para outros)

## Scripts Úteis

### Testar API
```bash
./test-api.sh
```

### Configurar Frontend
```bash
cd frontend
./setup.sh
```

## Estrutura do Projeto

```
microservices/
├── api-gateway/           # API Gateway
├── eureka-server/         # Service Discovery
├── sales-service/         # Gerenciamento de produtos e pedidos
├── tax-service/           # Cálculo de impostos
├── sales-registry-service/ # Relatórios de vendas
├── frontend/              # React Frontend
├── docker-compose.yml     # Configuração do RabbitMQ
├── test-api.sh           # Script de teste da API
└── README.md             # Este arquivo
``` 