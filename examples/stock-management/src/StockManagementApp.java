import java.sql.*;
import java.util.Scanner;

public class StockManagementApp {
    public static void main(String[] args) {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/store_stock_management");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        if (user == null || password == null) {
            System.err.println("Set DB_USER and DB_PASSWORD before running this example.");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Scanner sc = new Scanner(System.in)) {

            conn.setAutoCommit(false); // We will manually control transaction commits

            while (true) {
                System.out.println("\n--- Stock Management Menu ---");
                System.out.println("0. View all suppliers");
                System.out.println("1. View all products");
                System.out.println("2. Check stock for a product");
                System.out.println("3. Decrease stock (simulate purchase)");
                System.out.println("4. Trigger reorder to suppliers");
                System.out.println("5. View purchase history");
                System.out.println("6. View supplier order history");
                System.out.println("7. Change reorder quantity of a product");
                System.out.println("8. Change active status of a supplier");
                System.out.println("9. Add a product");
                System.out.println("10. Add a supplier");
                System.out.println("11. Change the supplier for a product");
                System.out.println("12. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();

                Statement stmt = conn.createStatement();

                switch (choice) {
                    case 0:
                        ResultSet rsSuppliers = stmt.executeQuery("SELECT * FROM suppliers");
                        System.out.println("\n--- Supplier List ---");
                        if(!rsSuppliers.isBeforeFirst()){
                            System.out.println("No suppliers added.");
                        }
                        while (rsSuppliers.next()) {
                            System.out.println("Supplier ID: " + rsSuppliers.getInt("supplier_id") +
                                            ", Name: " + rsSuppliers.getString("name") +
                                            ", Contact: " + rsSuppliers.getString("contact_number") +
                                            ", Active: " + (rsSuppliers.getBoolean("is_active") ? "Yes" : "No"));
                        }
                        break;

                    case 1:
                        ResultSet rs1 = stmt.executeQuery("SELECT * FROM products");
                        if (!rs1.isBeforeFirst()) {  // <- this checks if the ResultSet is empty
                            System.out.println("No products added.");
                        }
                        while (rs1.next()) {
                            System.out.println("Product ID: " + rs1.getInt("product_id") +
                                               ", Name: " + rs1.getString("name") +
                                               ", Stock: " + rs1.getInt("quantity_in_stock") +
                                               ", Reorder quantity: "+ rs1.getInt("reorder_quantity") +
                                               ", Reorder threshold: "+rs1.getInt("reorder_threshold") +
                                               ", Supplier id: "+rs1.getInt("supplier_id"));
                        }
                        break;

                    case 2:
                        System.out.print("Enter product ID to check stock: ");
                        int pid = sc.nextInt();
                        ResultSet rs2 = stmt.executeQuery("SELECT quantity_in_stock FROM products WHERE product_id = " + pid);
                        if (rs2.next()) {
                            System.out.println("Available stock: " + rs2.getInt("quantity_in_stock"));
                        } else {
                            System.out.println("Product not found!");
                        }
                        break;

                    case 3:
                        try {
                            System.out.print("Enter product ID: ");
                            int purchaseProductId = sc.nextInt();
                            System.out.print("Enter quantity purchased: ");
                            int qtyPurchased = sc.nextInt();

                            ResultSet stockCheck = stmt.executeQuery("SELECT quantity_in_stock FROM products WHERE product_id = " + purchaseProductId);
                            if (stockCheck.next()) {
                                int availableStock = stockCheck.getInt("quantity_in_stock");
                                if (availableStock >= qtyPurchased) {
                                    stmt.executeUpdate("UPDATE products SET quantity_in_stock = quantity_in_stock - " + qtyPurchased +
                                                    " WHERE product_id = " + purchaseProductId);

                                    stmt.executeUpdate("INSERT INTO customer_purchases (product_id, quantity_purchased, purchase_date, purchase_time) VALUES (" +
                                                        purchaseProductId + ", " + qtyPurchased + ", CURDATE(), CURTIME())");

                                    conn.commit();
                                    System.out.println("Purchase recorded and stock updated.");
                                } else {
                                    System.out.println("Not enough stock available! Available: " + availableStock);
                                }
                            } else {
                                System.out.println("Product not found!");
                            }
                        } catch (Exception e) {
                            conn.rollback();
                            System.out.println("Error occurred while processing purchase. Rolled back.");
                            e.printStackTrace();
                        }
                        break;


                    case 4:
                        try (Statement stmt1 = conn.createStatement();
                            ResultSet rs3 = stmt1.executeQuery("SELECT * FROM products WHERE quantity_in_stock <= reorder_threshold")) {

                            if(!rs3.isBeforeFirst()){
                                System.out.println("All products have sufficient quantities. No orders made.");
                            }
                            while (rs3.next()) {
                                int prodId = rs3.getInt("product_id");
                                int supplierId = rs3.getInt("supplier_id");
                                int reorderQty = rs3.getInt("reorder_quantity");

                                try (Statement stmt2 = conn.createStatement();
                                    ResultSet rsa = stmt2.executeQuery("SELECT is_active FROM suppliers WHERE supplier_id = " + supplierId)) {

                                    if (rsa.next()) {
                                        boolean isActive = rsa.getBoolean("is_active");
                                        if (!isActive) {
                                            System.out.println("Supplier (ID: " + supplierId + ") is inactive. Cannot reorder Product ID: " + prodId);
                                            continue;
                                        } else {
                                            try (Statement stmt3 = conn.createStatement()) {
                                                stmt3.executeUpdate("UPDATE products SET quantity_in_stock = quantity_in_stock + " + reorderQty +
                                                                    " WHERE product_id = " + prodId);

                                                stmt3.executeUpdate("INSERT INTO supplier_orders (supplier_id, product_id, quantity_ordered ,order_date, order_time) VALUES (" +
                                                                    supplierId + ", " + prodId +", " +reorderQty +", CURDATE(), CURTIME())");

                                                System.out.println("Reorder placed for Product ID: " + prodId);
                                            }
                                        }
                                    } else {
                                        System.out.println("Supplier (ID: " + supplierId + ") not found. Cannot reorder Product ID: " + prodId);
                                    }
                                }
                            }
                            conn.commit();
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Error triggering reorder. Rolled back.");
                            e.printStackTrace();
                        }
                        break;


                    case 5:
                        ResultSet rs4 = stmt.executeQuery("SELECT * FROM customer_purchases");
                        if(!rs4.isBeforeFirst()){
                            System.out.println("No customer purchases made till now");
                        }
                        while (rs4.next()) {
                            System.out.println("Purchase ID: " + rs4.getInt("purchase_id") +
                                               ", Product ID: " + rs4.getInt("product_id") +
                                               ", Quantity: " + rs4.getInt("quantity_purchased") +
                                               ", Date: " + rs4.getDate("purchase_date")+
                                               ", Time: "+ rs4.getTime("purchase_time"));
                        }
                        break;

                    case 6:
                        ResultSet rs5 = stmt.executeQuery("SELECT * FROM supplier_orders");
                        if(!rs5.isBeforeFirst()){
                            System.out.println("No orders to suppliers made till now");
                        }
                        while (rs5.next()) {
                            System.out.println("Order ID: " + rs5.getInt("order_id") +
                                               ", Supplier ID: " + rs5.getInt("supplier_id") +
                                               ", Product ID: " + rs5.getInt("product_id") +
                                               ", Date: " + rs5.getDate("order_date")+
                                               ", Time: "+ rs5.getTime("order_time")+
                                               ", Quantity ordered: "+rs5.getInt("quantity_ordered"));
                        }
                        break;


                    case 7:
                        try {
                            System.out.print("Enter product ID to change reorder quantity: ");
                            int prodIdToUpdate = sc.nextInt();
                            System.out.print("Enter new reorder quantity (if 0 is entered, then the product will be discontinued): ");
                            int newReorderQty = sc.nextInt();

                            ResultSet rsCheckProduct = stmt.executeQuery("SELECT product_id FROM products WHERE product_id = " + prodIdToUpdate);
                            if (rsCheckProduct.next()) {
                                stmt.executeUpdate("UPDATE products SET reorder_quantity = " + newReorderQty +
                                                " WHERE product_id = " + prodIdToUpdate);
                                conn.commit();
                                System.out.println("Reorder quantity updated.");
                            } else {
                                System.out.println("Product not found!");
                            }
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Error updating reorder quantity. Rolled back.");
                            e.printStackTrace();
                        }
                        break;

                    case 8:
                        try {
                            System.out.print("Enter supplier ID to change status: ");
                            int supplierIdToUpdate = sc.nextInt();
                            System.out.print("Enter new status (1 for Active, 0 for Inactive): ");
                            int newStatus = sc.nextInt();

                            ResultSet rs8=stmt.executeQuery("SELECT * from suppliers where supplier_id="+supplierIdToUpdate);
                            if(rs8.next()){
                                stmt.executeUpdate("UPDATE suppliers SET is_active="+newStatus+" where supplier_id="+supplierIdToUpdate);
                                conn.commit();
                                System.out.println("Supplier status updated.");
                            }
                            else{
                                System.out.println("Supplier not found");
                            }
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Error updating supplier status. Rolled back.");
                            e.printStackTrace();
                        }
                        break;


                    case 9:
                        try {
                            sc.nextLine();
                            System.out.println("Enter the product name: ");
                            String name = sc.nextLine();
                            System.out.println("Enter the reorder_threshold:");
                            int reorder_threshold = sc.nextInt();
                            System.out.println("Enter the reorder_quantity:");
                            int reorder_quantity = sc.nextInt();
                            System.out.println("Enter the supplier id:");
                            int supplier_id = sc.nextInt();

                            if (reorder_threshold <= 0 || reorder_quantity < 0) {
                                System.out.println("Invalid input.");
                                break;
                            }

                            ResultSet rs9 = stmt.executeQuery("select * from suppliers where supplier_id=" + supplier_id);
                            if (rs9.next()) {
                                stmt.executeUpdate(String.format(
                                    "insert into products(name, reorder_threshold, reorder_quantity, supplier_id) values ('%s',%d,%d,%d)",
                                    name, reorder_threshold, reorder_quantity, supplier_id));
                                conn.commit();
                                System.out.println("New product added.");
                            } else {
                                System.out.println("Supplier id doesn't exist");
                            }
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Error adding product. Rolled back.");
                            e.printStackTrace();
                        }
                        break;

                    case 10:
                        try {
                            sc.nextLine(); // flush buffer
                            System.out.println("Enter supplier name:");
                            String supplierName = sc.nextLine();
                            System.out.println("Enter supplier contact number (10 digits):");
                            String contactNumber = sc.nextLine();

                            if (contactNumber.length() != 10) {
                                System.out.println("Invalid contact number. Must be 10 digits.");
                                break;
                            }

                            stmt.executeUpdate(String.format(
                                "INSERT INTO suppliers (name, contact_number) VALUES ('%s', '%s')",
                                supplierName, contactNumber
                            ));
                            conn.commit();
                            System.out.println("Supplier added successfully.");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Error adding supplier. Rolled back.");
                            e.printStackTrace();
                        }
                        break;

                    case 11:
                        try {
                            System.out.print("Enter product ID to change supplier: ");
                            int productIdChange = sc.nextInt();
                            System.out.print("Enter new supplier ID: ");
                            int newSupplierId = sc.nextInt();

                            ResultSet rsProduct = stmt.executeQuery("SELECT product_id FROM products WHERE product_id = " + productIdChange);
                            if (!rsProduct.next()) {
                                System.out.println("Product not found.");
                                break;
                            }

                            ResultSet rsSupplier = stmt.executeQuery("SELECT supplier_id FROM suppliers WHERE supplier_id = " + newSupplierId);
                            if (!rsSupplier.next()) {
                                System.out.println("Supplier not found.");
                                break;
                            }

                            stmt.executeUpdate("UPDATE products SET supplier_id = " + newSupplierId + " WHERE product_id = " + productIdChange);
                            conn.commit();
                            System.out.println("Supplier updated for the product.");
                        } catch (SQLException e) {
                            conn.rollback();
                            System.out.println("Error changing supplier. Rolled back.");
                            e.printStackTrace();
                        }
                        break;


                    case 12:
                        System.out.println("Exiting...");
                        conn.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
