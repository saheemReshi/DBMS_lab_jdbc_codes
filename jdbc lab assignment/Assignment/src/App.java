import java.sql.*;
import java.util.Scanner;

public class App {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/companydb";

    static final String USER = "root";// add your user
   static final String PASSWORD = "naveedBoy";// add password
    
    public static void main(String[] args) {
        Connection conn=null;
        Statement stmt=null;
        Scanner sc = new Scanner(System.in);
        try {
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

            String insertQuery = "INSERT INTO employee (ssn, fname, minit, lname, bdate, address, sex, salary, super_ssn, dno) " +"VALUES ('" + ssn + "', '" + fname + "', '" + minit + "', '" + lname + "', '" + bdate + "', '" + address + "', '" + sex + "', " + salary + ", '" + super_ssn + "', " + dno + ")";

            int result = stmt.executeUpdate(insertQuery);
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
            
            String insertWork="INSERT INTO works_on values('"+ssn+"',"+pno+","+hours+")";
            int res=stmt.executeUpdate(insertWork);

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
