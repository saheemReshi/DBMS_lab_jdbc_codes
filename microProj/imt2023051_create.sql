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
