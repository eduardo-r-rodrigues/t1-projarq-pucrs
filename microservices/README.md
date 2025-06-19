# Microservices Architecture - Serviço de Vendas

Este projeto implementa uma arquitetura de microserviços para o sistema de vendas, seguindo os requisitos do trabalho de Projeto e Arquitetura de Software.

## Arquitetura dos Microserviços

### Componentes da Arquitetura

1. **Eureka Server** (Porta 8761) - Service Discovery
2. **API Gateway** (Porta 8080) - Roteamento e Gateway
3. **Sales Service** (Porta 8081) - Serviço principal (monolito)
4. **Tax Service** (Porta 8082) - Cálculo de impostos (múltiplas instâncias)
5. **Sales Registry Service** (Porta 8083) - Registro de vendas para relatórios fiscais
6. **PostgreSQL** (Porta 5432) - Banco de dados
7. **RabbitMQ** (Porta 5672) - Message Queue

### Comunicação entre Serviços

- **Síncrona**: Sales Service ↔ Tax Service (via Feign Client)
- **Assíncrona**: Sales Service → Sales Registry Service (via RabbitMQ)

## Como Executar

### 1. Iniciar a Infraestrutura
```bash
cd microservices
docker-compose up -d postgres rabbitmq
```

### 2. Iniciar o Eureka Server
```bash
cd microservices/eureka-server
./mvnw spring-boot:run
```

### 3. Iniciar os Microserviços
```bash
# Sales Service (Monolito)
cd microservices/sales-service
./mvnw spring-boot:run

# Tax Service (Múltiplas instâncias)
cd microservices/tax-service
./mvnw spring-boot:run

# Sales Registry Service
cd microservices/sales-registry-service
./mvnw spring-boot:run

# API Gateway
cd microservices/api-gateway
./mvnw spring-boot:run
```

### 4. Executar com Docker Compose (Todos os serviços)
```bash
cd microservices
docker-compose up --scale tax-service=3
```

## Endpoints da API

### Via API Gateway (http://localhost:8080)

#### Produtos (Sales Service)
- `GET /api/products` - Listar produtos
- `GET /api/products/{code}` - Buscar produto
- `POST /api/products` - Criar produto
- `POST /api/products/{code}/restock` - Adicionar estoque

#### Pedidos (Sales Service)
- `GET /api/orders` - Listar pedidos por período
- `GET /api/orders/{id}` - Buscar pedido
- `POST /api/orders` - Criar pedido
- `POST /api/orders/{id}/confirm` - Confirmar pedido

#### Impostos (Tax Service)
- `POST /api/tax/calculate` - Calcular impostos
- `GET /api/tax/health` - Health check

#### Relatórios (Sales Registry Service)
- `GET /api/sales-registry/report/{year}/{month}` - Relatório mensal
- `GET /api/sales-registry/health` - Health check

## Monitoramento

### Eureka Dashboard
- URL: http://localhost:8761
- Visualizar serviços registrados

### RabbitMQ Management
- URL: http://localhost:15672
- Usuário: admin
- Senha: admin

## Fluxo de Funcionamento

1. **Criação de Pedido**:
   - Cliente faz requisição via API Gateway
   - Gateway roteia para Sales Service
   - Sales Service valida produtos e estoque
   - Sales Service chama Tax Service (síncrono) para calcular impostos
   - Pedido é criado com status PENDING

2. **Confirmação de Pedido**:
   - Cliente confirma pedido via API Gateway
   - Sales Service reserva estoque
   - Sales Service envia mensagem para Sales Registry Service (assíncrono)
   - Sales Registry Service salva registro para relatórios fiscais

3. **Relatórios Fiscais**:
   - Receita Federal consulta Sales Registry Service
   - Service retorna totais de vendas e impostos por mês

## Características Técnicas

### Tax Service (Múltiplas Instâncias)
- Comunicação síncrona via Feign Client
- Load balancing automático via Eureka
- Instâncias escaláveis via Docker Compose

### Sales Registry Service (Fila)
- Comunicação assíncrona via RabbitMQ
- Fila com nome aleatório para evitar conflitos
- Persistência em PostgreSQL para relatórios

### API Gateway
- Roteamento baseado em path
- Load balancing automático
- Service discovery integrado

## Estados Suportados

- **RS**: Imposto de 10% sobre valor acima de R$ 100,00
- **SP**: Imposto de 12% sobre o total
- **PE**: Imposto diferenciado por produto (5% para essenciais, 15% para outros)

## Exemplo de Uso

### Criar um pedido:
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST001",
    "state": "SP",
    "country": "Brasil",
    "items": [
      {
        "productCode": "PROD001",
        "quantity": 2
      }
    ]
  }'
```

### Confirmar pedido:
```bash
curl -X POST http://localhost:8080/api/orders/{orderId}/confirm
```

### Consultar relatório mensal:
```bash
curl http://localhost:8080/api/sales-registry/report/2024/1
```

## Próximos Passos

1. Implementar circuit breakers (Hystrix/Resilience4j)
2. Adicionar logging centralizado
3. Implementar métricas e monitoramento
4. Adicionar autenticação e autorização
5. Implementar testes de integração 