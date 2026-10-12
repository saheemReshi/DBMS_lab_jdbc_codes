# JDBC assignment: insert and report

This assignment contains three console programs using the `companydb` schema:

- `App.java` inserts an employee and a `works_on` row in one transaction. A nonpositive hours value rolls the transaction back.
- `Demo.java` lists selected employee columns.
- `Report.java` prints employees and their dependents.

Load `LAB 11 - SQL DML-20250408/companydb.sql`, set `DB_URL`, `DB_USER`, and `DB_PASSWORD`, and add MySQL Connector/J to the Java classpath. For example, from this directory:

```sh
export DB_URL='jdbc:mysql://localhost:3306/companydb'
export DB_USER='your_mysql_user'
export DB_PASSWORD='your_mysql_password'
javac -cp '/path/to/mysql-connector-j.jar' -d out src/App.java src/Demo.java src/Report.java
java -cp 'out:/path/to/mysql-connector-j.jar' Demo
```

Run `App` only with valid sample values that satisfy the schema constraints. This is course code; use it to discuss transaction behavior, SQL parameterization, input validation, and resource cleanup.
