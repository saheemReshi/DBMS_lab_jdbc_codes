ALTER TABLE products
ADD FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id);

ALTER TABLE customer_purchases
ADD FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE supplier_orders
ADD FOREIGN KEY (product_id) REFERENCES products(product_id);

ALTER TABLE supplier_orders
ADD FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id);
