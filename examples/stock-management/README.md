# Store stock management

**Schema:** [store.sql](../../schemas/store.sql). This single file contains the database, all four tables, all four foreign keys, and fictional sample suppliers/products. No separate ALTER script is needed.

**Source:** [src/StockManagementApp.java](src/StockManagementApp.java). **Main class:** `StockManagementApp`.

Follow the [root setup guide](../../README.md), then run from the repository root:

```sh
bash scripts/run.sh stock
```

The app defaults to `jdbc:mysql://localhost:3306/store_stock_management`. If you set `DB_URL` for a company example, unset it or change it before running this app.

## Suggested walkthrough

1. Choose `0` to view the two suppliers, then `1` to view the two products.
2. Choose `2` and product ID `1`: Notebook initially has stock `5`.
3. Choose `3`, product ID `1`, quantity `2`. Stock becomes `3`; choose `5` to view the purchase record.
4. Choose `4` to reorder low-stock products. Notebook receives `25` units; choose `6` to view the supplier order. Its stock should now be `28`.
5. Choose `12` to exit. These operations persist changes, so repeat runs start from the updated database state.

Purchase and supplier-order histories are initially empty. Supplier IDs and product IDs in the seeded dataset are `1` and `2`.

## Database model

- `suppliers` records supplier contact details and active status.
- `products` stores current quantities, thresholds, reorder quantities and the current supplier.
- `customer_purchases` records a purchase against a product.
- `supplier_orders` records both the product and supplier so historical orders still identify the supplier when a product's current supplier changes.

The app is a teaching exercise. Reorders immediately increase stock rather than model delivery. Discuss replacing SQL string construction with `PreparedStatement`, rejecting zero/negative purchase quantities, closing resources, making transaction boundaries explicit, and protecting stock updates against concurrent buyers.

For manual compilation, compile `src/StockManagementApp.java` and run `StockManagementApp` with Connector/J on the classpath.
