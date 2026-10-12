-- Run once on a new, empty store_stock_management database.
-- Contains all tables, foreign keys, and sample data. No existing tables are dropped.
CREATE DATABASE IF NOT EXISTS store_stock_management;
USE store_stock_management;

CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    quantity_in_stock INT DEFAULT 0,
    reorder_threshold INT,
    reorder_quantity int,
    supplier_id INT
);
-- we'll keep the reorder quantity fixed everytime

CREATE TABLE suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30),
    contact_number VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE  -- NEW: active flag
);

CREATE TABLE customer_purchases (
    purchase_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    quantity_purchased INT,
    purchase_date DATE,
    purchase_time TIME
);

CREATE TABLE supplier_orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_id int,
    product_id INT,
    quantity_ordered INT,
    order_date DATE,
    order_time TIME
);
-- keeping supplier id here as well as supplier for a product may change in the future, so i want to keep a record of things

ALTER TABLE products
ADD FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id);

ALTER TABLE customer_purchases
ADD FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE supplier_orders
ADD FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE supplier_orders
ADD FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id);

-- Fictional sample data: product 1 is below its reorder threshold.
INSERT INTO suppliers (supplier_id, name, contact_number, is_active) VALUES
    (1, 'Campus Supplies', '9000000001', TRUE),
    (2, 'Paper Warehouse', '9000000002', TRUE);

INSERT INTO products (product_id, name, quantity_in_stock, reorder_threshold, reorder_quantity, supplier_id) VALUES
    (1, 'Notebook', 5, 10, 25, 1),
    (2, 'Pen', 30, 10, 50, 2);
