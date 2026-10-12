# DBMS and JDBC teaching examples

This repository contains small Java and MySQL examples from a database systems course. They are intended for classroom discussion and practice. The programs are console applications; they are not web services or production backend systems.

## Suggested teaching path

1. **Company database and SQL** — start with [`LAB 11 - SQL DML-20250408/companydb.sql`](<LAB 11 - SQL DML-20250408/companydb.sql>). It creates and populates a familiar employee, department, project, and dependent schema. Use it to discuss keys, foreign keys, joins, and sample queries.
2. **Basic JDBC query** — use [`JDBC_Demo/Demo`](JDBC_Demo/Demo/README.md) to trace connection setup, query execution, and reading rows from a `ResultSet`.
3. **JDBC writes and transactions** — review [`jdbc lab assignment/Assignment/src/App.java`](<jdbc lab assignment/Assignment/src/App.java>) for inserting an employee and a related `works_on` row as one transaction. [`Report.java`](<jdbc lab assignment/Assignment/src/Report.java>) demonstrates reading related data.
4. **Store stock project** — see [`microProj/store_stock_management`](microProj/store_stock_management/README.md) for a larger schema and menu-driven example involving stock updates, purchase history, suppliers, and reorders.

## Requirements

- Java (JDK 8 or newer)
- MySQL Server
- MySQL Connector/J
- A MySQL account with permission to create and use the example databases

The examples use Connector/J 8.0.32. Install it through your normal Java dependency workflow or provide its JAR on the classpath. Do not commit the driver JAR or compiled `.class` files.

## Configure database access

Set these environment variables in the shell before running an example:

```sh
export DB_URL='jdbc:mysql://localhost:3306/companydb'
export DB_USER='your_mysql_user'
export DB_PASSWORD='your_mysql_password'
```

For the stock project, set `DB_URL` to `jdbc:mysql://localhost:3306/store_stock_management`. The checked-in example code reads these variables; it does not contain a usable database password. Use a dedicated local teaching account instead of a privileged account such as `root`.

## Example setup

Create the company database and load its schema/data:

```sql
CREATE DATABASE companydb;
```

Then, from a terminal, run:

```sh
mysql -u your_mysql_user -p companydb < 'LAB 11 - SQL DML-20250408/companydb.sql'
```

The stock project has its own schema in [`microProj/imt2023051_create.sql`](microProj/imt2023051_create.sql). Create the database first, then run that SQL file against it:

```sql
CREATE DATABASE store_stock_management;
```

```sh
mysql -u your_mysql_user -p store_stock_management < microProj/imt2023051_create.sql
```

Compile and run a Java example from its project directory with Connector/J on the classpath. For example, on Linux:

```sh
javac -cp '/path/to/mysql-connector-j.jar' -d out src/Demo.java
java -cp 'out:/path/to/mysql-connector-j.jar' Demo
```

The classpath separator is `:` on Linux and macOS and `;` on Windows. The stock source file is under `microProj/store_stock_management/src`.

## Discussion notes

These are student exercises and preserve a few rough edges that are useful to review. In particular, discuss why parameterized `PreparedStatement`s are preferred to building SQL strings from input, why every `ResultSet` and statement should be closed, how rollback should work when any part of a multi-table operation fails, and how database constraints protect data. The stock app currently uses SQL string construction in several operations and has inconsistent transaction handling; treat those as review exercises, not recommended patterns.

The project includes compiled files and driver archives in older commits. Current source files should be used for lessons. A password was also present in an earlier commit; removing it from the current files does not erase it from Git history. If it was a real or reused credential, rotate it before sharing the repository and consider sharing a cleaned export instead of the full history.

## Repository contents

- `LAB 11 - SQL DML-20250408/` — company database setup and course SQL material.
- `JDBC_Demo/` — introductory JDBC query examples and accompanying notes.
- `jdbc lab assignment/` — assignment source for inserts, transactions, and reports.
- `microProj/` — store stock management mini project and schema (local working copy).
- `Lab 09 - Database Design - PART B-20250325/` and `Lab 10 - SQL DDL-20250325/` — additional course exercises.
