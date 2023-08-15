package estm.dsic.jee.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import estm.dsic.jee.controllers.Employee;
import estm.dsic.jee.controllers.Login;
import jakarta.annotation.Resource;
import jakarta.faces.context.FacesContext;

public class EmployeeDao {
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();  ;
    @Resource(name = "jdbc/mydb")
    private DataSource mydb;
    private Connection cnx;
    private Statement stm;
    private ResultSet rs;

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        try {
            cnx = mydb.getConnection();
            stm = cnx.createStatement();
            rs = stm.executeQuery("SELECT * FROM Employees e JOIN Users u ON e.emp_user_id = u.user_id");
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("e.emp_id"));

                Login user = new Login();

                user.setId(rs.getInt("u.user_id"));
                user.setFirstName(rs.getString("u.user_first_name"));
                user.setLastName(rs.getString("u.user_last_name"));
                user.setEmail(rs.getString("u.user_email"));
                user.setPasswd(rs.getString("u.user_password"));
                user.setUserType(rs.getString("u.user_type"));
                employee.setUser(user);
                employee.setDepartment(rs.getString("e.emp_department"));
                employees.add(employee);
            }
            

        } catch (Exception e) {
            // TODO: handle exception
        }
        for (Employee employee : employees) {
            System.out.println(" ayman "+employee.getUser().getLastName());
        }
        return employees;
    }

    public String createEmployee(Employee employee) throws SQLException {
        cnx = mydb.getConnection();
        String userQuery = "INSERT INTO Users (user_first_name, user_last_name, user_email, user_password, user_type) VALUES (?, ?, ?, ?, ?)";
        String empQuery = "INSERT INTO Employees (emp_user_id, emp_department) VALUES (?, ?)";
        try (PreparedStatement userStatement = cnx.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement empStatement = cnx.prepareStatement(empQuery)) {
            // Insert user
            Login user = employee.getUser();
            userStatement.setString(1, user.getFirstName());
            userStatement.setString(2, user.getLastName());
            userStatement.setString(3, user.getEmail());
            userStatement.setString(4, user.getPasswd());
            userStatement.setString(5, "employee");
            userStatement.executeUpdate();
    
            // Get the generated user ID
            ResultSet generatedKeys = userStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                // Insert employee
                empStatement.setInt(1, userId);
                empStatement.setString(2, employee.getDepartment());
                empStatement.executeUpdate();
            } else {
                throw new SQLException("Failed to create employee, no ID obtained.");
            }
        }
        return "manager/listEmployees";
        }

        public Employee getEmployeeByUser(Login user) {
            Employee employee = null;
            PreparedStatement stm = null;
            try {
                stm = cnx.prepareStatement("SELECT * FROM Employees WHERE user_id = ?");
                stm.setInt(1, user.getId());
                rs = stm.executeQuery();
                if (rs.next()) {
                    employee = new Employee();
                    employee.setId(rs.getInt("emp_id"));
                    employee.setUser(user);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return employee;
        }
        public Boolean updateEmployee(Employee employee) throws SQLException {
            try {
                cnx = mydb.getConnection();
                PreparedStatement userStm = cnx.prepareStatement("UPDATE Users SET user_first_name=?, user_last_name=?, user_email=?, user_password=? WHERE user_id=?");
                userStm.setString(1, employee.getUser().getFirstName());
                userStm.setString(2, employee.getUser().getLastName());
                userStm.setString(3, employee.getUser().getEmail());
                userStm.setString(4, employee.getUser().getPasswd());
                userStm.setInt(5, employee.getUser().getId());
                userStm.executeUpdate();
        
                PreparedStatement empStm = cnx.prepareStatement("UPDATE Employees SET emp_department=? WHERE emp_user_id=?");
                empStm.setString(1, employee.getDepartment());
                empStm.setInt(2, employee.getUser().getId());
                empStm.executeUpdate();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return true;
        }
        

        public List<Employee> getEmployeesWithoutAssignment() throws SQLException {
            List<Employee> employees = new ArrayList<>();
        
            try {
                cnx = mydb.getConnection();
                stm = cnx.createStatement();
                rs = stm.executeQuery("SELECT DISTINCT u.user_id, u.user_first_name, u.user_last_name, u.user_email, u.user_password, u.user_type, e.emp_id, e.emp_department FROM Employees e JOIN Users u ON e.emp_user_id = u.user_id LEFT JOIN Assignments a ON e.emp_id = a.emp_id LEFT JOIN Tasks t ON t.task_id = a.task_id WHERE a.emp_id IS NULL OR t.task_status = 'complete'");
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("emp_id"));
                    Login user = new Login();
                    user.setId(rs.getInt("user_id"));
                    user.setFirstName(rs.getString("user_first_name"));
                    user.setLastName(rs.getString("user_last_name"));
                    user.setEmail(rs.getString("user_email"));
                    user.setPasswd(rs.getString("user_password"));
                    user.setUserType(rs.getString("user_type"));
                    employee.setUser(user);
                    employee.setDepartment(rs.getString("emp_department"));
                    employees.add(employee);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            for (Employee employee : employees) {
                System.out.println("ayman " + employee.getUser().getLastName());
            }
            return employees;
        }
        

        public String delete(int employeeId) throws SQLException {
            try {
                cnx = mydb.getConnection();
                PreparedStatement ps = cnx.prepareStatement("DELETE FROM Employees WHERE emp_id = ?");
                ps.setInt(1, employeeId);
                ps.executeUpdate();
            } catch (Exception e) {
                // Handle exception
            }
            return "listEmployees.xhtml";
        }
        public boolean editEmployee(int id) {  
            try {
                System.out.println("Employee ID: " + id);
                cnx = mydb.getConnection();
                PreparedStatement ps = cnx.prepareStatement("SELECT u.user_type,u.user_password,u.user_id,e.emp_id, e.emp_department, u.user_first_name, u.user_last_name, u.user_email FROM Employees e JOIN Users u ON e.emp_user_id = u.user_id WHERE e.emp_id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("e.emp_id"));
                    Login user = new Login();
                    user.setId(rs.getInt("u.user_id"));
                    user.setFirstName(rs.getString("u.user_first_name"));
                    user.setLastName(rs.getString("u.user_last_name"));
                    user.setEmail(rs.getString("u.user_email"));
                    user.setPasswd(rs.getString("u.user_password"));
                    user.setUserType(rs.getString("u.user_type"));
                    employee.setUser(user);
                    employee.setDepartment(rs.getString("e.emp_department"));
                    sessionMap.put("editEmployee", employee); 
                    return true;
                }
            } catch(Exception e) {  
                System.out.println(e);  
            }         
            return false;  
        }
        
        

    
}
