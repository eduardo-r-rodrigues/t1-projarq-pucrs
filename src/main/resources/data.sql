-- Sample products for testing
INSERT INTO products (code, description, unit_price, max_stock_quantity, stock_quantity, available_quantity, essential) 
VALUES 
('PROD001', 'Arroz Integral', 8.50, 100, 50, 50, true),
('PROD002', 'Feijão Preto', 6.80, 80, 40, 40, true),
('PROD003', 'Óleo de Soja', 12.90, 60, 30, 30, false),
('PROD004', 'Macarrão', 4.20, 120, 60, 60, false),
('PROD005', 'Leite Integral', 5.50, 90, 45, 45, true)
ON CONFLICT (code) DO NOTHING;
