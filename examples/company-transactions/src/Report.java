import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Report {
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
            String query="select distinct essn from dependent";
            ResultSet rs=stmt.executeQuery(query);
            ArrayList<String> ssn=new ArrayList<>();
            while(rs.next()){
                ssn.add(rs.getString("essn"));
            }
            int i=1;
            for(String s : ssn){
                query="select fname, lname from employee   where ssn='"+s+"'";
                rs=stmt.executeQuery(query);
                rs.next();
                String fname=rs.getString("fname");
                String lname=rs.getString("lname");
                System.out.println("Employee#"+i++ +" full name: "+fname+" "+lname);

                query="select dependent_name, relationship "+
                "from dependent "+
                "where essn='"+s+"'";
                rs=stmt.executeQuery(query);
                int j=1;
                while(rs.next()){
                    String name=rs.getString("dependent_name");
                    String rel=rs.getString("relationship");
                    System.out.println("Dependent#"+ j++ +" : "+ name+"("+rel+")" );

                }
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
