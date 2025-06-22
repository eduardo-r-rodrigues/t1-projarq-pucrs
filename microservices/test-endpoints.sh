#!/bin/bash

# =====================================================
# MICROSERVICES ENDPOINTS TEST SCRIPT
# =====================================================
# This script tests all endpoints across all microservices
# 
# Services:
# - API Gateway (port 8080)
# - Sales Service (port 8081)
# - Tax Service (port 8082)
# - Sales Registry Service (port 8083)
# - Eureka Server (port 8761)
# =====================================================

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Base URLs
API_GATEWAY_URL="http://localhost:8080"
SALES_SERVICE_URL="http://localhost:8081"
TAX_SERVICE_URL="http://localhost:8082"
SALES_REGISTRY_URL="http://localhost:8083"
EUREKA_URL="http://localhost:8761"

# Test data
PRODUCT_CODE="TEST001"
CUSTOMER_ID="CUST123"
ORDER_ID="ORDER123"
STATE="CA"
COUNTRY="USA"

# Function to print section headers
print_section() {
    echo -e "\n${BLUE}=====================================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}=====================================================${NC}"
}

# Function to test endpoint
test_endpoint() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    echo -e "\n${YELLOW}Testing: $description${NC}"
    echo -e "URL: $url"
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" "$url")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST -H "Content-Type: application/json" -d "$data" "$url")
    fi
    
    # Extract status code
    status_code=$(echo "$response" | grep "HTTP_STATUS:" | cut -d: -f2)
    # Extract response body
    body=$(echo "$response" | sed '/HTTP_STATUS:/d')
    
    if [ "$status_code" -ge 200 ] && [ "$status_code" -lt 300 ]; then
        echo -e "${GREEN}✓ SUCCESS (Status: $status_code)${NC}"
    else
        echo -e "${RED}✗ FAILED (Status: $status_code)${NC}"
    fi
    echo -e "Response: $body"
}

# Function to wait for service to be ready
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    echo -e "\n${YELLOW}Waiting for $service_name to be ready...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✓ $service_name is ready!${NC}"
            return 0
        fi
        
        echo -e "Attempt $attempt/$max_attempts - $service_name not ready yet..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}✗ $service_name failed to start within expected time${NC}"
    return 1
}

# Main test execution
main() {
    echo -e "${BLUE}=====================================================${NC}"
    echo -e "${BLUE}MICROSERVICES ENDPOINTS TEST SCRIPT${NC}"
    echo -e "${BLUE}=====================================================${NC}"
    
    # Wait for services to be ready
    wait_for_service "$EUREKA_URL" "Eureka Server"
    wait_for_service "$API_GATEWAY_URL/api/products/health" "API Gateway"
    wait_for_service "$SALES_SERVICE_URL/api/products/health" "Sales Service"
    wait_for_service "$TAX_SERVICE_URL/api/tax/health" "Tax Service"
    wait_for_service "$SALES_REGISTRY_URL/api/sales-registry/health" "Sales Registry Service"
    
    # =====================================================
    # 1. HEALTH CHECKS
    # =====================================================
    print_section "1. HEALTH CHECKS"
    
    test_endpoint "GET" "$API_GATEWAY_URL/api/products/health" "" "API Gateway Health"
    test_endpoint "GET" "$SALES_SERVICE_URL/api/products/health" "" "Sales Service Health"
    test_endpoint "GET" "$TAX_SERVICE_URL/api/tax/health" "" "Tax Service Health"
    test_endpoint "GET" "$SALES_REGISTRY_URL/api/sales-registry/health" "" "Sales Registry Health"
    
    # =====================================================
    # 2. PRODUCTS ENDPOINTS
    # =====================================================
    print_section "2. PRODUCTS ENDPOINTS"
    
    # Create a test product
    product_data='{
        "code": "'$PRODUCT_CODE'",
        "name": "Test Product",
        "description": "Test product",
        "price": 29.99,
        "stock": 100,
        "essential": false
    }'
    
    test_endpoint "POST" "$API_GATEWAY_URL/api/products" "$product_data" "Create Product (Gateway)"
    test_endpoint "GET" "$API_GATEWAY_URL/api/products" "" "Get All Products (Gateway)"
    test_endpoint "GET" "$API_GATEWAY_URL/api/products/$PRODUCT_CODE" "" "Get Product by Code (Gateway)"
    test_endpoint "POST" "$API_GATEWAY_URL/api/products/$PRODUCT_CODE/restock?quantity=50" "" "Restock Product (Gateway)"
    
    # =====================================================
    # 3. TAX SERVICE ENDPOINTS
    # =====================================================
    print_section "3. TAX SERVICE ENDPOINTS"
    
    # Calculate taxes
    tax_request='{
        "state": "'$STATE'",
        "baseAmount": 100.0,
        "items": [
            {
                "productCode": "'$PRODUCT_CODE'",
                "quantity": 2,
                "unitPrice": 29.99,
                "totalPrice": 59.98,
                "essential": false
            }
        ]
    }'
    
    test_endpoint "POST" "$API_GATEWAY_URL/api/tax/calculate" "$tax_request" "Calculate Taxes (Gateway)"
    
    # =====================================================
    # 4. ORDERS ENDPOINTS
    # =====================================================
    print_section "4. ORDERS ENDPOINTS"
    
    # Create an order
    order_data='{
        "customerId": "'$CUSTOMER_ID'",
        "state": "'$STATE'",
        "country": "'$COUNTRY'",
        "items": [
            {
                "productCode": "'$PRODUCT_CODE'",
                "quantity": 2,
                "unitPrice": 29.99,
                "totalPrice": 59.98,
                "essential": false
            }
        ]
    }'
    
    test_endpoint "POST" "$API_GATEWAY_URL/api/orders" "$order_data" "Create Order (Gateway)"
    
    # Get orders by period (last 30 days)
    start_date=$(date -u -d '30 days ago' +%Y-%m-%dT%H:%M:%S.%3NZ)
    end_date=$(date -u +%Y-%m-%dT%H:%M:%S.%3NZ)
    
    test_endpoint "GET" "$API_GATEWAY_URL/api/orders?startDate=$start_date&endDate=$end_date" "" "Get Orders by Period (Gateway)"
    
    # Get order by ID (using the first order created)
    test_endpoint "GET" "$API_GATEWAY_URL/api/orders/$ORDER_ID" "" "Get Order by ID (Gateway)"
    
    # Confirm order
    test_endpoint "POST" "$API_GATEWAY_URL/api/orders/$ORDER_ID/confirm" "" "Confirm Order (Gateway)"
    
    # =====================================================
    # 5. SALES REGISTRY ENDPOINTS
    # =====================================================
    print_section "5. SALES REGISTRY ENDPOINTS"
    
    # Get monthly report (current year and month)
    current_year=$(date +%Y)
    current_month=$(date +%m)
    
    test_endpoint "GET" "$API_GATEWAY_URL/api/sales-registry/report/$current_year/$current_month" "" "Get Monthly Report (Gateway)"
    
    # =====================================================
    # 6. EUREKA SERVICE DISCOVERY
    # =====================================================
    print_section "6. EUREKA SERVICE DISCOVERY"
    
    test_endpoint "GET" "$EUREKA_URL" "" "Eureka Dashboard"
    test_endpoint "GET" "$EUREKA_URL/eureka/apps" "" "Eureka Applications"
    
    # =====================================================
    # 7. ERROR CASES TESTING
    # =====================================================
    print_section "7. ERROR CASES TESTING"
    
    # Test non-existent product
    test_endpoint "GET" "$API_GATEWAY_URL/api/products/NONEXISTENT" "" "Get Non-existent Product (404)"
    
    # Test non-existent order
    test_endpoint "GET" "$API_GATEWAY_URL/api/orders/NONEXISTENT" "" "Get Non-existent Order (404)"
    
    # =====================================================
    # SUMMARY
    # =====================================================
    print_section "TEST SUMMARY"
    
    echo -e "${GREEN}All endpoint tests completed!${NC}"
    echo -e "\n${YELLOW}Services tested:${NC}"
    echo -e "  • API Gateway (port 8080)"
    echo -e "  • Sales Service (port 8081)"
    echo -e "  • Tax Service (port 8082)"
    echo -e "  • Sales Registry Service (port 8083)"
    echo -e "  • Eureka Server (port 8761)"
    
    echo -e "\n${BLUE}=====================================================${NC}"
    echo -e "${BLUE}TEST SCRIPT COMPLETED${NC}"
    echo -e "${BLUE}=====================================================${NC}"
}

# Run the main function
main "$@" 