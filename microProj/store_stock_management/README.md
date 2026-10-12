# Store stock management JDBC example

This console app is a course mini project for practicing relational modeling and JDBC. It tracks products, suppliers, customer purchases, and supplier orders. It can show stock, record a purchase, place a reorder, and edit product or supplier details.

## Database setup

Create the database, then load the schema from the repository root:

```sql
CREATE DATABASE store_stock_management;
```

```sh
mysql -u your_mysql_user -p store_stock_management < microProj/imt2023051_create.sql
mysql -u your_mysql_user -p store_stock_management < microProj/imt2023051_alter.sql
```

The second file adds the foreign keys after table creation. Use a MySQL account with permission to create tables and modify this database.

## Configure and run

Set `DB_URL`, `DB_USER`, and `DB_PASSWORD` in your shell. For example:

```sh
export DB_URL='jdbc:mysql://localhost:3306/store_stock_management'
export DB_USER='your_mysql_user'
export DB_PASSWORD='your_mysql_password'
```

Compile and run with MySQL Connector/J on the classpath (adjust the JAR path):

```sh
javac -cp '/path/to/mysql-connector-j.jar' -d out microProj/store_stock_management/src/StockManagementApp.java
java -cp 'out:/path/to/mysql-connector-j.jar' StockManagementApp
```

On Windows, use `;` instead of `:` between classpath entries.

## Suggested discussion

- Identify the primary and foreign keys and explain why order history stores both product and supplier IDs.
- Trace the purchase operation: check available stock, decrease the quantity, record the purchase, and commit.
- Discuss what should happen when any step fails and whether concurrent purchases can oversell stock.
- Review the SQL built from console input. A follow-up exercise is to replace it with parameterized `PreparedStatement`s, validate all quantities, and close each statement and result set with try-with-resources.

This is student coursework. Several menu operations still build SQL strings from input, and transaction handling is inconsistent. Use those as review exercises; do not present the current app as a production backend.
