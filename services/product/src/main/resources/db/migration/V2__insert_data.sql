-- Insert data for categories
INSERT INTO categories (category_id, name, description, created_date, last_modified_date)
VALUES
    (gen_random_uuid(), 'Electronics', 'Devices and gadgets', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Home Appliances', 'Appliances for household use', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'Furniture', 'Furniture for home and office', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert data for products
INSERT INTO products (product_id, vendor_id, name, description, price, category_id, stock_quantity, threshold_quantity, sku, created_date, last_modified_date)
VALUES
    (gen_random_uuid(), 1, 'Smartphone', 'Latest model smartphone with high-res camera', 699.99,
     (SELECT category_id FROM categories WHERE name = 'Electronics'), 50, 5, 'SPHONE-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 1, 'Microwave Oven', '800W microwave with grill function', 120.00,
     (SELECT category_id FROM categories WHERE name = 'Home Appliances'), 30, 3, 'MICRO-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 2, 'Office Chair', 'Ergonomic chair with lumbar support', 150.00,
     (SELECT category_id FROM categories WHERE name = 'Furniture'), 20, 2, 'CHAIR-003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
