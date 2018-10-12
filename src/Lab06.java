import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Lab06 {

    private String URL, sql, user, pass;
    private Connection conn = null;

    public Lab06() {
        URL = "jdbc:mysql://localhost:3306/java";
        sql = "";
        user = "test";
        pass = "some_pass";
    }

    public static void main(String[] args) {
        Lab06 o = new Lab06();
        //o.printAllAndTotal_ch1();
        o.printSalaryByDept_ch2();
    }

    /*
     * Write a program to print a listing of all employee names and salaries,
     * and total of all salaries at the end
     */
    private void printAllAndTotal_ch1() {
        Double s, total_salary = 0d;
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             Statement stmt = conn.createStatement();) {

            sql = "SELECT id, firstname, lastname, salary FROM employee";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                s = rs.getDouble(4);
                total_salary += s;
                System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + " " + rs.getString(3) + "\t" + s);
            }
            System.out.println("-----------------------");
            System.out.println("Total salary: " + total_salary);
        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }

    /*
     * Modify the salary report program from ch1 so that after the list of all employee names,
     * it prints a list of the department names with salary total for each
     */
    private void printSalaryByDept_ch2() {
        Double s, totalSalary = 0d;
        HashMap<String, Double> salaryByDept = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             Statement stmt = conn.createStatement();) {

            sql = "SELECT id, firstname, lastname, salary, department_code FROM employee";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                s = rs.getDouble(4);
                String dept = rs.getString(5);
                salaryByDept.put(dept, (salaryByDept.get(dept) == null ? s : salaryByDept.get(dept).doubleValue() + s));

                totalSalary += s;
                System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + " " + rs.getString(3) + "\t" + s);
            }
            System.out.println("-----------------------");
            System.out.println("Total salary: " + totalSalary);
            System.out.println();

            for (Map.Entry<String, Double> h: salaryByDept.entrySet())
                System.out.println("Department: " + h.getKey() + ", Total Salary: " + h.getValue());
        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }

}
