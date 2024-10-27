-- Insert data for vendor_profiles in MySQL
INSERT INTO vendor_profiles (username, password, store_name, business_license_number, store_address, store_phone, store_email, description, status)
VALUES
    ('vendor1', 'password123', 'Tech Store', 'BLN-12345', '123 Tech St, Cityville', '123-456-7890', 'techstore@example.com', 'Leading store for electronics', 'ACTIVE'),
    ('vendor2', 'password456', 'Home Store', 'BLN-67890', '456 Home Ave, Townsville', '123-456-7891', 'homestore@example.com', 'Best in home appliances and furniture', 'ACTIVE'),
    ('vendor3', 'password789', 'Furniture World', 'BLN-11223', '789 Furniture Blvd, Villagetown', '123-456-7892', 'furnitureworld@example.com', 'Quality furniture at great prices', 'SUSPENDED');
