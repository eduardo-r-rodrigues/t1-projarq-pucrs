#!/bin/bash

echo "Testing Microservices API"
echo "========================="

echo "Waiting for services to start..."
sleep 10

echo "1. Testing API Gateway..."
curl -s http://localhost:8080/api/products/health || echo "API Gateway not responding"

echo -e "\n2. Testing Products endpoint..."
curl -s http://localhost:8080/api/products | jq '.' || echo "Products endpoint not responding"

echo -e "\n3. Testing Order creation..."
ORDER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/orders \
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
  }')

echo "Order created:"
echo $ORDER_RESPONSE | jq '.'

ORDER_ID=$(echo $ORDER_RESPONSE | jq -r '.id')

if [ "$ORDER_ID" != "null" ] && [ "$ORDER_ID" != "" ]; then
    echo -e "\n4. Testing Order confirmation..."
    curl -s -X POST http://localhost:8080/api/orders/$ORDER_ID/confirm | jq '.'
    
    echo -e "\n5. Testing Sales Registry report..."
    curl -s http://localhost:8080/api/sales-registry/report/2024/1 | jq '.'
else
    echo "Failed to create order or extract order ID"
fi

echo -e "\nTest completed!" 