package estm.dsic.jee.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import estm.dsic.jee.controllers.Assign;
import estm.dsic.jee.controllers.Employee;
import estm.dsic.jee.controllers.Login;
import estm.dsic.jee.controllers.Task;
import estm.dsic.jee.services.EmployeeService;
import jakarta.annotation.Resource;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

public class AssignDao {
    @Resource(name = "jdbc/mydb")
    private DataSource mydb;
    private Connection cnx;
    private Statement stm;
    private ResultSet rs;
    EmployeeService employeeService;
    public List<Assign> getAllAssignments() throws SQLException {
        String query = "SELECT a.assign_id, t.task_name , u.user_first_name , u.user_last_name, t.task_status FROM Assignments a JOIN Tasks t ON a.task_id = t.task_id JOIN Employees e ON a.emp_id = e.emp_id JOIN Users u ON e.emp_user_id = u.user_id";
        List<Assign> assignments = new ArrayList<>();
            cnx=mydb.getConnection();
            stm=cnx.createStatement();
            rs=stm.executeQuery(query);
            while (rs.next()) {
                Assign assign = new Assign();
                assign.setId(rs.getInt("a.assign_id"));
                Task task = new Task();
                task.setTaskName(rs.getString("t.task_name"));
                task.setTaskStatus(rs.getString("t.task_status"));
                assign.setTask(task);
                Employee employee = new Employee();
                Login user = new Login();
                user.setFirstName(rs.getString("u.user_first_name"));
                user.setLastName(rs.getString("u.user_last_name"));
                employee.setUser(user);
                assign.setEmployee(employee);
                assignments.add(assign);
            }
        
        return assignments;
    }

    public boolean save(Assign assign) {
        PreparedStatement stm = null;
        try {
            cnx = mydb.getConnection();
            stm = cnx.prepareStatement("INSERT INTO Assignments (task_id, emp_id) VALUES (?, ?)");
            stm.setInt(1, assign.getTask().getTaskId());
            stm.setInt(2, assign.getEmployee().getId());
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error executing SQL statement: " + ex.getMessage());
        }
        return true;
    }

    public List<Assign> getAssignmentsByLoggedInUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String email = request.getUserPrincipal().getName();
        Employee loggedInEmployee = employeeService.getEmployeeByEmail(email);

        System.out.println(" dkhlna ");

        List<Assign> assignments = new ArrayList<>();
        try {
            PreparedStatement stm = cnx.prepareStatement("SELECT * FROM Assignments WHERE emp_id = ?");
            stm.setInt(1, loggedInEmployee.getId());
            rs = stm.executeQuery();
            while (rs.next()) {
                Assign assign = new Assign();
                assign.setId(rs.getInt("assign_id"));
                assignments.add(assign);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return assignments;
    }

    public List<Assign> getAssignByEmail(String email) {
        List<Assign> assignments = new ArrayList<>();
        try {
        
        System.out.println(" rni hna ");
            cnx=mydb.getConnection();
            String sql = "SELECT Assignments.*, Tasks.task_id,Tasks.task_name, Tasks.task_description, Tasks.task_start_date, Tasks.task_end_date, Tasks.task_status, Users.user_email "
                       + "FROM Assignments "
                       + "INNER JOIN Employees ON Assignments.emp_id = Employees.emp_id "
                       +"INNER JOIN Users ON Employees.emp_user_id = Users.user_id "
                       +"INNER JOIN Tasks ON Assignments.task_id = Tasks.task_id "
                       + "WHERE Users.user_email ='"+email+"'";
                       System.out.println(" rni hna 2" + email);
                       stm=cnx.createStatement();
                       rs=stm.executeQuery(sql);
            System.out.println(" rni hna 55");
            while (rs.next()) {
                Assign assign = new Assign();
                assign.setId(rs.getInt("assign_id"));
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskStatus(rs.getString("task_status"));
                task.setTaskDescription(rs.getString("task_description"));
                task.setTaskStartDate(rs.getString("task_start_date"));
                task.setTaskEndDate(rs.getString("task_end_date"));
                assign.setTask(task);
                Login user = new Login();
                user.setEmail(rs.getString("user_email"));
                Employee employee = new Employee();
                employee.setUser(user);
                assign.setEmployee(employee);
                assignments.add(assign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Assign assign1 : assignments) {
            System.out.println(assign1.getTask().getTaskStatus() + " end ");
        }
        return assignments;
    }

    public boolean delete(int assignId) {
        PreparedStatement stm = null;
        try {
            cnx = mydb.getConnection();
            stm = cnx.prepareStatement("DELETE FROM Assignments WHERE assign_id = ?");
            stm.setInt(1, assignId);
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error executing SQL statement: " + ex.getMessage());
        }
        return true;
    }
    
}
