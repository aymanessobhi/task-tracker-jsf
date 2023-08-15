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

import estm.dsic.jee.controllers.Project;
import estm.dsic.jee.controllers.Task;
import jakarta.annotation.Resource;
import jakarta.faces.context.FacesContext;

public class ProjectDao {
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap(); 
    private Map<String,Object> sessionMap1 = FacesContext.getCurrentInstance().getExternalContext().getSessionMap() ;
    @Resource(name = "jdbc/mydb")
    private DataSource mydb;
    private Connection cnx;
    private Statement stm;
    private ResultSet rs;
    
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        try {
            cnx=mydb.getConnection();
            stm=cnx.createStatement();
            rs=stm.executeQuery("SELECT * FROM Projects");
            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("proj_id"));
                project.setName(rs.getString("proj_name"));
                project.setDescription(rs.getString("proj_description"));
                project.setStartDate(rs.getString("proj_start_date"));
                project.setEndDate(rs.getString("proj_end_date"));
                project.setStatus(rs.getString("proj_status"));
                projects.add(project);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return projects;
    }

    public Boolean updateProject(Project project) {
        PreparedStatement stm = null;
        try {
            cnx = mydb.getConnection();
            stm = cnx.prepareStatement("UPDATE Projects SET proj_name=?, proj_description=?, proj_start_date=?, proj_end_date=?, proj_status=? WHERE proj_id=?");
            stm.setString(1, project.getName());
            stm.setString(2, project.getDescription());
            stm.setString(3,  project.getStartDate());
            stm.setString(4,  project.getEndDate());
            stm.setString(5, project.getStatus());
            stm.setInt(6, project.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(stm!=null) 
            return true;
        return false;    
    }

    public String viewTasks(int id){
        List<Task> tasks = new ArrayList<>();  
        List<Task> unassignedTasks = new ArrayList<>();
        try {
            System.out.println("hnaya id= "+id);
            cnx=mydb.getConnection();
            stm=cnx.createStatement();
            rs=stm.executeQuery("SELECT * FROM Tasks WHERE proj_id='"+id+"'");
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskDescription(rs.getString("task_description"));
                task.setTaskStartDate(rs.getString("task_start_date"));
                task.setTaskEndDate(rs.getString("task_end_date"));
                task.setTaskStatus(rs.getString("task_status"));
                Project project = new Project();
                project.setId(id);
                task.setProject(project);
                System.out.println(project.getId()+" hhh");
                tasks.add(task);
                sessionMap.put("tasks", tasks);
            }
            Statement stm1 = cnx.createStatement();;
            rs = stm1.executeQuery("SELECT * FROM Tasks t LEFT JOIN Assignments a ON t.task_id = a.task_id WHERE a.task_id IS NULL  AND t.proj_id = " +id);
                while (rs.next()) { 
                    Task task = new Task();
                    task.setTaskId(rs.getInt("task_id"));
                    task.setTaskName(rs.getString("task_name"));
                    task.setTaskDescription(rs.getString("task_description"));
                    task.setTaskStartDate(rs.getString("task_start_date"));
                    task.setTaskEndDate(rs.getString("task_end_date"));
                    task.setTaskStatus(rs.getString("task_status"));
                    unassignedTasks.add(task);
                    sessionMap.put("unassignedTasks", unassignedTasks);
            }

        }catch(Exception e){  
            System.out.println(e);  
        }         
        return "project/TasksByProject";  
    } 

    public List<Task> getTasks(int id){
        List<Task> tasks = new ArrayList<>();  
        try {
            System.out.println("hnaya id= "+id);
            cnx=mydb.getConnection();
            stm=cnx.createStatement();
            rs=stm.executeQuery("SELECT * FROM Tasks WHERE proj_id='"+id+"'");
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setTaskDescription(rs.getString("task_description"));
                task.setTaskStartDate(rs.getString("task_start_date"));
                task.setTaskEndDate(rs.getString("task_end_date"));
                task.setTaskStatus(rs.getString("task_status"));
                Project project = new Project();
                project.setId(id);
                task.setProject(project);
                tasks.add(task);
            }
        }catch(Exception e){  
            System.out.println(e);  
        }         
        return tasks;  
    }
    

    public String editProject(int id){  
        try {
            System.out.println("hnaya id= "+id);
            cnx=mydb.getConnection();
            stm=cnx.createStatement();
            rs=stm.executeQuery("SELECT * FROM Projects WHERE proj_id='"+id+"'");
            if(rs.next()){
                Project project = new Project();
                project.setId(rs.getInt("proj_id"));
                project.setName(rs.getString("proj_name"));
                project.setDescription(rs.getString("proj_description"));
                project.setStartDate(rs.getString("proj_start_date"));
                project.setEndDate(rs.getString("proj_end_date"));
                project.setStatus(rs.getString("proj_status"));
                System.out.println(project.getName());
                sessionMap.put("editProject", project); 
            }else{
                System.out.println(" makhdamach ");
            }
        }catch(Exception e){  
        System.out.println(e);  
        }         
        return "project/editProject";  
        }  
    public Boolean createProject(Project project) {
        PreparedStatement stm = null;
        int result = 0;
        try {
            cnx = mydb.getConnection();
            stm = cnx.prepareStatement("INSERT INTO Projects (proj_name, proj_description, proj_start_date, proj_end_date, proj_status) VALUES (?, ?, ?, ?, ?)");
            stm.setString(1, project.getName());
            stm.setString(2, project.getDescription());
            stm.setString(3, project.getStartDate());
            stm.setString(4, project.getEndDate());
            stm.setString(5, project.getStatus());
            result = stm.executeUpdate();  
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(result !=0)  
            return true;
        return false;  
    }

    public boolean deleteProject(int projectId) {
        PreparedStatement stm = null;
        try {
            cnx = mydb.getConnection();
            stm = cnx.prepareStatement("DELETE FROM Projects WHERE proj_id = ?");
            stm.setInt(1, projectId);
            stm.executeUpdate();
            System.out.println(stm.executeUpdate() + " courage ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; 
    }  
}

