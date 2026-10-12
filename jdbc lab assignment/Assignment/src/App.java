import java.sql.*;
import java.util.Scanner;

public class App {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/companydb");

    static final String USER = System.getenv("DB_USER");
    static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        Connection conn=null;
        Statement stmt=null;
        Scanner sc = new Scanner(System.in);
        try {
            if (USER == null || PASSWORD == null) {
                throw new IllegalStateException("Set DB_USER and DB_PASSWORD before running this example.");
            }
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER, PASSWORD);

            stmt= conn.createStatement();

            conn.setAutoCommit(false);

            // Take input from user
            System.out.print("Enter SSN: ");
            String ssn = sc.nextLine();

            System.out.print("Enter First Name: ");
            String fname = sc.nextLine();

            System.out.print("Enter Middle Initial: ");
            String minit = sc.nextLine();

            System.out.print("Enter Last Name: ");
            String lname = sc.nextLine();

            System.out.print("Enter Birth Date (yyyy-mm-dd): ");
            String bdate = sc.nextLine();

            System.out.print("Enter Address: ");
            String address = sc.nextLine();

            System.out.print("Enter Sex (M/F): ");
            String sex = sc.nextLine();

            System.out.print("Enter Salary: ");
            double salary = sc.nextDouble();
            sc.nextLine(); // consume newline

            System.out.print("Enter Supervisor SSN: ");
            String super_ssn = sc.nextLine();

            System.out.print("Enter Department Number: ");
            int dno = sc.nextInt();

            String insertQuery = "INSERT INTO employee (ssn, fname, minit, lname, bdate, address, sex, salary, super_ssn, dno) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int result;
            try (PreparedStatement employeeInsert = conn.prepareStatement(insertQuery)) {
                employeeInsert.setString(1, ssn);
                employeeInsert.setString(2, fname);
                employeeInsert.setString(3, minit);
                employeeInsert.setString(4, lname);
                employeeInsert.setDate(5, Date.valueOf(bdate));
                employeeInsert.setString(6, address);
                employeeInsert.setString(7, sex);
                employeeInsert.setDouble(8, salary);
                employeeInsert.setString(9, super_ssn.trim().isEmpty() ? null : super_ssn);
                employeeInsert.setInt(10, dno);
                result = employeeInsert.executeUpdate();
            }
            if (result > 0) {
                System.out.println("Employee inserted successfully.");
            } else {
                System.out.println("Failed to insert employee.");
            }
            //now need to make an entry in the works_on table for the new employee

            System.out.println("Enter Project number:");
            int pno=sc.nextInt();
            System.out.println("Enter the number of hours");
            double hours=sc.nextDouble();

            String insertWork = "INSERT INTO works_on (essn, pno, hours) VALUES (?, ?, ?)";
            int res;
            try (PreparedStatement workInsert = conn.prepareStatement(insertWork)) {
                workInsert.setString(1, ssn);
                workInsert.setInt(2, pno);
                workInsert.setDouble(3, hours);
                res = workInsert.executeUpdate();
            }

            if (res > 0) {
                System.out.println("Works_on inserted successfully.");
            } else {
                System.out.println("Failed to insert works_on.");
            }
            if (hours > 0) {
                conn.commit();
                System.out.println("Transaction committed.");
            } else {
                conn.rollback();
                System.out.println("Transaction rolled back (hours <= 0).");
            }

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
            }
            e.printStackTrace();
        } finally{
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
                sc.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
