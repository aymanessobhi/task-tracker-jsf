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

import estm.dsic.jee.controllers.Assign;
import estm.dsic.jee.controllers.Project;
import estm.dsic.jee.controllers.Task;
import jakarta.annotation.Resource;
import jakarta.faces.context.FacesContext;

public class TaskDao {

    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();;
    @Resource(name = "jdbc/mydb")
    private DataSource mydb;
    private Connection cnx;
    private Statement stm;
    private ResultSet rs;

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        try {
            cnx = mydb.getConnection();
            stm = cnx.createStatement();
            rs = stm.executeQuery("SELECT t.task_id, t.task_name, t.task_description, t.task_start_date, " +
            "t.task_end_date, t.task_status, p.proj_id, p.proj_name " +
            "FROM Tasks t JOIN Projects p ON t.proj_id = p.proj_id");
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskDescription(rs.getString("task_description"));
                task.setTaskStartDate(rs.getString("task_start_date"));
                task.setTaskEndDate(rs.getString("task_end_date"));
                task.setTaskStatus(rs.getString("task_status"));
                
                Project project = new Project();
                project.setId(rs.getInt("proj_id"));
                project.setName(rs.getString("proj_name"));
                task.setProject(project);

                tasks.add(task);
                System.out.println(" tasks " + task.getTaskName());
            }
            

        } catch (Exception e) {
            // TODO: handle exception
        }
        return tasks;
    }
    public Boolean deleteTaskById(int taskId) throws SQLException {
        try {
            cnx = mydb.getConnection();
            PreparedStatement ps = cnx.prepareStatement("DELETE FROM Tasks WHERE task_id = ?");
            ps.setInt(1, taskId);
            ps.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return true;
    }

    public Boolean createTask(Task task) throws SQLException {
        try {
            cnx = mydb.getConnection();
            PreparedStatement stm = cnx.prepareStatement("INSERT INTO Tasks (task_name, task_description, task_start_date, task_end_date, task_status, proj_id) VALUES (?, ?, ?, ?, ?, ?)");
            stm.setString(1, task.getTaskName());
            stm.setString(2, task.getTaskDescription());
            stm.setString(3, task.getTaskStartDate());
            stm.setString(4, task.getTaskEndDate());
            stm.setString(5, "open");
            stm.setInt(6, task.getProject().getId());
            System.out.println(" minuib "+ task.getTaskName() + " lll ");
            stm.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return true;
    }
    public Boolean updateTask(Task task) throws SQLException {
        try {
            cnx = mydb.getConnection();
            PreparedStatement stm = cnx.prepareStatement("UPDATE Tasks SET task_name=?, task_description=?, task_start_date=?, task_end_date=?, task_status=?, proj_id=? WHERE task_id=?");
            stm.setString(1, task.getTaskName());
            stm.setString(2, task.getTaskDescription());
            stm.setString(3, task.getTaskStartDate());
            stm.setString(4, task.getTaskEndDate());
            stm.setString(5, task.getTaskStatus());
            stm.setInt(6, task.getProject().getId());
            stm.setInt(7, task.getTaskId());
            stm.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return true;
    }

    public Boolean editTask(int id){  
        try {
            cnx=mydb.getConnection();
            stm=cnx.createStatement();
            rs=stm.executeQuery("SELECT * FROM Tasks t JOIN Projects p ON t.proj_id = p.proj_id WHERE task_id='"+id+"'");
            if(rs.next()){
                System.out.println("hnaya id= "+id);
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskDescription(rs.getString("task_description"));
                task.setTaskStartDate(rs.getString("task_start_date"));
                task.setTaskEndDate(rs.getString("task_end_date"));
                task.setTaskStatus(rs.getString("task_status"));
                Project project = new Project();
                project.setId(rs.getInt("p.proj_id"));
                project.setName(rs.getString("p.proj_name"));
                project.setDescription(rs.getString("p.proj_description"));
                project.setStartDate(rs.getString("p.proj_start_date"));
                project.setEndDate(rs.getString("p.proj_end_date"));
                project.setStatus(rs.getString("p.proj_status"));
                task.setProject(project);
                // sessionMap.put("editTask", task); 
                System.out.println(" jaja "+task.getProject().getName());
                return true;
            }else{
                System.out.println(" makhdamach ");
            }
        }catch(Exception e){  
            System.out.println(e);  
        }         
        return false;  
    }
    public Boolean updateTaskStatus(int id, String newStatus) {
        PreparedStatement stm = null;
        try {
            Task task = new Task();
            cnx = mydb.getConnection();
            stm = cnx.prepareStatement("UPDATE Tasks SET task_status = ? WHERE task_id = ?");
            stm.setString(1, newStatus);
            stm.setInt(2, id);
            stm.executeUpdate();
            task.setTaskStatus(newStatus);
        } catch (SQLException ex) {
            System.out.println("Error executing SQL statement: " + ex.getMessage());
        }
        return true;
    }
    
    
    
    
    

    

    
    
}