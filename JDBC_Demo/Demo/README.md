# Introductory JDBC query

`src/Demo.java` connects to the `companydb` database, selects employee columns, and prints each returned row. It demonstrates the JDBC flow: open a `Connection`, create a `Statement`, execute a `SELECT`, iterate through the `ResultSet`, then close resources.

## Before running

Load `LAB 11 - SQL DML-20250408/companydb.sql` into a MySQL database named `companydb`. Set `DB_USER` and `DB_PASSWORD` in the shell. `DB_URL` is optional and defaults to `jdbc:mysql://localhost:3306/companydb`.

Install MySQL Connector/J separately and compile/run from this directory:

```sh
export DB_URL='jdbc:mysql://localhost:3306/companydb'
export DB_USER='your_mysql_user'
export DB_PASSWORD='your_mysql_password'
javac -cp '/path/to/mysql-connector-j.jar' -d out src/Demo.java
java -cp 'out:/path/to/mysql-connector-j.jar' Demo
```

Use the source file as the lesson material. Compiled `.class` files and the JDBC driver are intentionally not included.
