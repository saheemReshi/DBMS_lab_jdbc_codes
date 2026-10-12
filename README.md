# Database programming examples with Java and MySQL

These examples come from a DBMS course project. They show how a console application connects to a relational database, reads rows, changes related tables, and controls transactions. Start with the basic query, then the company transaction example, then the store project.

## Examples and required schemas

| Example | Java entry point | Complete setup script | Run command |
| --- | --- | --- | --- |
| [Basic JDBC query](examples/jdbc-basics/README.md) | `Demo` | [Company schema and sample data](schemas/company.sql) | `bash scripts/run.sh basics` |
| [Employee/project transaction](examples/company-transactions/README.md) | `App` | [Company schema and sample data](schemas/company.sql) | `bash scripts/run.sh transactions` |
| [Employee/dependent report](examples/company-transactions/README.md) | `Report` | [Company schema and sample data](schemas/company.sql) | `bash scripts/run.sh report` |
| [Store stock management](examples/stock-management/README.md) | `StockManagementApp` | [Store schema and sample data](schemas/store.sql) | `bash scripts/run.sh stock` |

## Quick start (Linux / macOS)

Run the commands below in Bash from the repository root. Requirements are a JDK (Java 8 or newer), MySQL Server, the `mysql` command-line client, and a MySQL Connector/J JAR.

The original project used Connector/J 8.0.32. Obtain a Connector/J version compatible with your MySQL server, put the JAR on your machine, and set `JDBC_JAR` to its **absolute path**. Driver binaries are installed separately.

```sh
export JDBC_JAR='/absolute/path/to/mysql-connector-j.jar'
export DB_USER='your_mysql_user'
read -r -s -p 'MySQL password: ' DB_PASSWORD
echo
export DB_PASSWORD
unset DB_URL
```

Use a local teaching account with access to the databases. For initial setup, that account must also be allowed to create databases/tables and add foreign keys (or run the imports using your database administrator account).

### Load the schemas

These scripts create their named database, all tables, foreign keys, and sample data. Run each script **once on a fresh database**. They do not drop existing tables; importing again into a populated database will fail rather than reset it. You only need the company script once for all three company programs.

```sh
mysql -u "$DB_USER" -p < schemas/company.sql
mysql -u "$DB_USER" -p < schemas/store.sql
```

The MySQL client prompts for its password independently of the Java environment variable. No other SQL files are required. If your MySQL server is on another host or port, also pass `-h` and `-P` to the import commands.

### Run an example

```sh
bash scripts/run.sh basics
bash scripts/run.sh report
bash scripts/run.sh transactions
bash scripts/run.sh stock
```

Each app defaults to its correct database on `localhost:3306`. Leave `DB_URL` unset to use those defaults. For a different host/port, set it explicitly to `jdbc:mysql://HOST:PORT/companydb` or `jdbc:mysql://HOST:PORT/store_stock_management` as appropriate. An exported URL overrides the default for every app.

The launcher compiles each example into a separate directory under `out/`, then runs its main class with the JDBC driver on the classpath. It does not create or reset databases.

### Manual compilation (including Windows)

You can also compile and run directly; no build tool or IDE is required. For example:

```sh
mkdir -p out/jdbc-basics
javac -d out/jdbc-basics examples/jdbc-basics/src/Demo.java
java -cp "out/jdbc-basics:$JDBC_JAR" Demo
```

On Windows, use `;` as the classpath separator, replace `$JDBC_JAR` with the actual JAR path, and set the database environment variables in your shell. See each example's README for its source files and main class.

## What is included

```text
examples/
  jdbc-basics/           # Connection, SELECT, ResultSet
  company-transactions/ # Prepared INSERTs, commit/rollback, reports
  stock-management/     # Suppliers, products, purchases, reorders
schemas/
  company.sql           # Six related tables and sample data
  store.sql             # Four related tables, foreign keys and sample data
scripts/run.sh          # Compile and run any example
docs/                   # Original course notes and assignment PDFs
```

The [course notes](docs/README.md) provide the original assignment context. The repository uses one canonical copy of each schema/source example so setup commands and teaching material stay together.

## Teaching notes and limitations

The transaction example uses `PreparedStatement` parameters for console input and rolls back when a step fails. Trace the employee insert and `works_on` insert together to explain atomicity: both changes persist, or both are undone. Its README includes inputs for both a commit and a rollback demonstration.

The store app is original student coursework. Several operations still construct SQL strings from input, lack complete input validation/resource cleanup, and do not handle concurrent purchases safely. It assumes a reorder immediately increases available stock. Use these behaviors as code-review exercises before using it as a design reference.

A password appeared in earlier Git commits. Current tutorial sources use environment variables, but Git history still contains the old value. Rotate it if it was real or reused.
