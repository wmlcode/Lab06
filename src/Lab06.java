import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import examples.*;

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
        //o.printSalaryByDept_ch2();
        //o.printUsersByDept_ch4("CS");
        o.printNumRowsOrAffected_ch5("SELECT id, firstname, lastname, salary, department_code, hire_date FROM employee");
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
     * Ch2: Modify the salary report program from ch1 so that after the list of all employee names,
     * it prints a list of the department names with salary total for each
     *
     * Ch3: Modify the salary report program to use rPadTrunc(), found in the provider
     * SQLUtils.java, to format the output
     */
    private void printSalaryByDept_ch2() {
        Double s, totalSalary = 0d;
        HashMap<String, Double> salaryByDept = new HashMap<>();
        examples.SQLUtils sqlUtils = new SQLUtils();
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
            System.out.println("Total salary: " + sqlUtils.rPadTrunc(totalSalary.toString(), 7));
            System.out.println();

            for (Map.Entry<String, Double> h: salaryByDept.entrySet())
                System.out.println("Department: " + h.getKey() + ", Total Salary: " + h.getValue());
        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }

    /*
     * Write a program that uses a PreparedStatement to retrieve the id, name, and title of all employees of a department.
     * The statement should like the department code as its only parameter. Prompt the user for a department code and use
     * the response to run the query and print the results.
     */
    private void printUsersByDept_ch4(String dept) {
        sql = "SELECT id, firstname, lastname, title FROM employee WHERE department_code = ?";
        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             PreparedStatement pst = conn.prepareStatement(sql);) {

            pst.setString(1, dept);
            ResultSet rs = pst.executeQuery();
            while (rs.next())
                System.out.println(rs.getInt(1) + "\t" + rs.getString(4) + ":\t" + rs.getString(2) + " " + rs.getString(3));
        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }

    /*
     * Ch5: Determine if the statement is a query
     *
     * Ch6: Use ResultSetMetaData to determine the number of columns retrieved by the query
     */
    private void printNumRowsOrAffected_ch5(String st) {
        Boolean isQuery = true;
        Boolean isDml = false;

        try (Connection conn = DriverManager.getConnection(URL, user, pass);
             Statement stmt = conn.createStatement();) {

            if (st.trim().indexOf("SELECT") != -1) {
                ResultSet rs = stmt.executeQuery(st);
                ResultSetMetaData rsmd = rs.getMetaData();
                System.out.println("Columns number: " + rsmd.getColumnCount());
                while (rs.next()) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        System.out.print(rsmd.getColumnName(i) + ": " + rs.getObject(i).toString() + " ");
                    }
                    System.out.println();
                }
            }

        }
        catch (SQLException e) {
            System.err.println(e);
        }
    }

}