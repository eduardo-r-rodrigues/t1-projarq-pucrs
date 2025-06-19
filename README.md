# Serviço de Vendas - DDD Architecture

Este projeto implementa um sistema de vendas usando Domain Driven Design (DDD) com Spring Boot e PostgreSQL.

## Arquitetura DDD

O projeto está organizado nas seguintes camadas:

### Domain Layer
- **Entities**: `Product`, `Order` (Aggregate Root)
- **Value Objects**: `OrderItem`
- **Domain Services**: `TaxCalculationService`
- **Repository Interfaces**: `ProductRepository`, `OrderRepository`

### Application Layer
- **Application Services**: `ProductApplicationService`, `OrderApplicationService`
- **DTOs**: `CreateProductRequest`, `CreateOrderRequest`, `OrderItemRequest`

### Infrastructure Layer
- **Repository Implementations**: `JpaProductRepository`, `JpaOrderRepository`
- **Spring Data Repositories**: `SpringDataProductRepository`, `SpringDataOrderRepository`

### Presentation Layer
- **Controllers**: `ProductController`, `OrderController`

## Pré-requisitos

- Java 21
- Maven
- Docker e Docker Compose

## Como executar

1. **Iniciar o PostgreSQL:**
```bash
docker-compose up -d postgres
```

2. **Executar a aplicação:**
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080

## Endpoints da API

### Produtos
- `GET /api/products` - Listar todos os produtos
- `GET /api/products/{code}` - Buscar produto por código
- `POST /api/products` - Criar novo produto
- `POST /api/products/{code}/restock` - Adicionar estoque
- `GET /api/products/stock` - Verificar estoque de todos os produtos
- `DELETE /api/products/{code}` - Deletar produto

### Pedidos
- `GET /api/orders?start={start}&end={end}` - Listar pedidos por período
- `GET /api/orders/{id}` - Buscar pedido por ID
- `GET /api/orders/all` - Listar todos os pedidos
- `POST /api/orders` - Criar novo pedido
- `POST /api/orders/{orderId}/confirm` - Confirmar pedido

## Exemplo de uso

### Criar um produto:
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PROD006",
    "description": "Café em Grãos",
    "unitPrice": 15.90,
    "maxStockQuantity": 50,
    "essential": false
  }'
```

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
      },
      {
        "productCode": "PROD002",
        "quantity": 1
      }
    ]
  }'
```

## Estados Suportados

- **RS**: Imposto de 10% sobre valor acima de R$ 100,00
- **SP**: Imposto de 12% sobre o total
- **PE**: Imposto diferenciado por produto (5% para essenciais, 15% para outros)

## Próximos Passos

Este é o primeiro passo da migração. Os próximos passos incluem:

1. Quebrar o sistema em microserviços
2. Implementar API Gateway
3. Adicionar Service Discovery
4. Implementar comunicação assíncrona com filas
5. Criar microserviço de impostos
6. Criar microserviço de registro de vendas 