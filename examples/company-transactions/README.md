# Company transactions and reports

**Schema:** [company.sql](../../schemas/company.sql), shared with the basic JDBC example. Import it once using the [root setup guide](../../README.md).

| Source | Main class | Purpose |
| --- | --- | --- |
| [src/App.java](src/App.java) | `App` | Insert an employee and project assignment in one transaction |
| [src/Report.java](src/Report.java) | `Report` | Display employees and their dependents |

Run from the repository root:

```sh
bash scripts/run.sh report
bash scripts/run.sh transactions
```

The report should list three employees with dependents in the initial sample data. Output order is unspecified.

## Transaction demonstration

The insert app prompts for these fields in order. Use a fresh SSN each time; supervisor, department and project IDs must already exist. A blank supervisor is allowed and stored as SQL `NULL`.

| Prompt | Sample commit input |
| --- | --- |
| SSN | `111223333` |
| First name | `Sample` |
| Middle initial | `A` |
| Last name | `Student` |
| Birth date | `2000-01-01` |
| Address | `Campus` |
| Sex | `F` |
| Salary | `25000` |
| Supervisor SSN | `333445555` |
| Department number | `5` |
| Project number | `1` |
| Hours | `5` |

This should commit both rows. Repeat with SSN `111223334` and hours `0` to demonstrate rollback. The program prints intermediate insert messages before the transaction outcome; an insert message alone does not mean a row was committed.

To inspect the outcome, run these queries in `companydb`:

```sql
SELECT ssn, fname FROM employee WHERE ssn IN ('111223333', '111223334');
SELECT essn, pno, hours FROM works_on WHERE essn IN ('111223333', '111223334');
```

Only the committed SSN should appear after those two demonstrations. Using a nonexistent department or project demonstrates the foreign-key error path and rollback.

For manual compilation, compile `src/App.java` and `src/Report.java`, then run the selected main class with Connector/J on the classpath. The default URL is `jdbc:mysql://localhost:3306/companydb`.
