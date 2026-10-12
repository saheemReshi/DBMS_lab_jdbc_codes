# Basic JDBC query

**Schema:** [company.sql](../../schemas/company.sql). It includes six tables and sample employees, departments, projects and dependents.

**Source:** [src/Demo.java](src/Demo.java). **Main class:** `Demo`.

Follow the [root setup guide](../../README.md), then run from the repository root:

```sh
bash scripts/run.sh basics
```

The program prints the first name, last name, birth date and department number for each seeded employee (eight rows). Row order is unspecified because the query has no `ORDER BY`.

Trace the `Connection`, `Statement`, query execution and `ResultSet.next()` loop. The loop advances one row at a time; getters read values from the current row. Discuss how try-with-resources could simplify cleanup.

For manual compilation, compile `src/Demo.java` and run `Demo` with Connector/J on the classpath. The default URL is `jdbc:mysql://localhost:3306/companydb`.
