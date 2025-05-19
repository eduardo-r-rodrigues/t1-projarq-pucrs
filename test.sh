#!/bin/bash

# This script provides a comprehensive test of all endpoints in the Sales Service API
# It executes all endpoints with direct URLs for maximum efficiency

echo "Starting tests for Sales Service API"
echo "----------------------------------------------"

# ==========================================
# PRODUCT ENDPOINTS
# ==========================================
echo -e "\n=== TESTING PRODUCT ENDPOINTS ===\n"

echo "1. Creating test products..."
# Create a non-essential product
echo "Creating non-essential product (PROD001)..."
curl -s -X POST "localhost:8080/api/sales/products" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PROD001",
    "description": "Smartphone XYZ",
    "unitPrice": 1200.00,
    "maxStockQuantity": 100,
    "stockQuantity": 50,
    "availableQuantity": 50,
    "essential": false
  }'
echo -e "\n"

# Create an essential product (for PE tax rule)
echo "Creating essential product (PROD002)..."
curl -s -X POST "localhost:8080/api/sales/products" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PROD002",
    "description": "Basic Food Pack",
    "unitPrice": 45.99,
    "maxStockQuantity": 200,
    "stockQuantity": 150,
    "availableQuantity": 150,
    "essential": true
  }'
echo -e "\n"

# Create a third product for testing
echo "Creating third product (PROD003)..."
curl -s -X POST "localhost:8080/api/sales/products" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "PROD003",
    "description": "Headphones",
    "unitPrice": 250.00,
    "maxStockQuantity": 80,
    "stockQuantity": 40,
    "availableQuantity": 40,
    "essential": false
  }'
echo -e "\n"

echo "2. Getting all products..."
curl -s -X GET "localhost:8080/api/sales/products"
echo -e "\n"

echo "3. Getting product by code (PROD001)..."
curl -s -X GET "localhost:8080/api/sales/products/PROD001"
echo -e "\n"

echo "4. Restocking product (PROD001 with 20 units)..."
curl -s -X POST "localhost:8080/api/sales/products/PROD001/restock?quantity=20"
echo -e "\n"

echo "5. Getting stock for all products..."
curl -s -X GET "localhost:8080/api/sales/products/stock"
echo -e "\n"

echo "6. Getting stock for specific products (PROD001, PROD002)..."
curl -s -X POST "localhost:8080/api/sales/products/stock" \
  -H "Content-Type: application/json" \
  -d '["PROD001", "PROD002"]'
echo -e "\n"

# ==========================================
# ORDER ENDPOINTS
# ==========================================
echo -e "\n=== TESTING ORDER ENDPOINTS ===\n"

echo "1. Creating a quote for RS state..."
RS_ORDER_RESPONSE=$(curl -s -X POST "localhost:8080/api/sales/orders/quote" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST001",
    "state": "RS",
    "country": "Brasil",
    "items": [
      {
        "productCode": "PROD001",
        "quantity": 2
      },
      {
        "productCode": "PROD003",
        "quantity": 1
      }
    ]
  }')
echo "$RS_ORDER_RESPONSE"
# Extract the order ID using a simple grep approach
RS_ORDER_ID=$(echo "$RS_ORDER_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d':' -f2 | tr -d '"')
echo "Created order with ID: $RS_ORDER_ID"
echo -e "\n"

echo "2. Creating a quote for SP state..."
curl -s -X POST "localhost:8080/api/sales/orders/quote" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST002",
    "state": "SP",
    "country": "Brazil",
    "items": [
      {
        "productCode": "PROD002",
        "quantity": 5
      }
    ]
  }'
echo -e "\n"

echo "3. Creating a quote for PE state with mixed essential/non-essential products..."
curl -s -X POST "localhost:8080/api/sales/orders/quote" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST003",
    "state": "PE",
    "country": "Brasil",
    "items": [
      {
        "productCode": "PROD001",
        "quantity": 1
      },
      {
        "productCode": "PROD002",
        "quantity": 3
      }
    ]
  }'
echo -e "\n"

echo "4. Creating a quote with >10 items (to test 10% discount)..."
curl -s -X POST "localhost:8080/api/sales/orders/quote" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST004",
    "state": "RS",
    "country": "Brasil",
    "items": [
      {
        "productCode": "PROD002",
        "quantity": 11
      }
    ]
  }'
echo -e "\n"

echo "5. Getting order by ID..."
if [ ! -z "$RS_ORDER_ID" ]; then
  echo "Getting order $RS_ORDER_ID..."
  curl -s -X GET "localhost:8080/api/sales/orders/$RS_ORDER_ID"
else
  echo "Getting order with example ID..."
  curl -s -X GET "localhost:8080/api/sales/orders/example-id"
fi
echo -e "\n"

echo "6. Confirming order..."
if [ ! -z "$RS_ORDER_ID" ]; then
  echo "Confirming order $RS_ORDER_ID..."
  curl -s -X POST "localhost:8080/api/sales/orders/$RS_ORDER_ID/confirm"
else
  echo "Confirming order with example ID..."
  curl -s -X POST "localhost:8080/api/sales/orders/example-id/confirm"
fi
echo -e "\n"

echo "7. Getting orders by period (2025 year range)..."
curl -s -X GET "localhost:8080/api/sales/orders?start=2025-01-01T00:00:00&end=2025-12-31T23:59:59"
echo -e "\n"


echo "======================================"
echo "Test execution completed!"
echo "======================================"