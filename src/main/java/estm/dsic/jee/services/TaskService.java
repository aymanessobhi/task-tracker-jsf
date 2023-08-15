package estm.dsic.jee.services;

import java.sql.SQLException;
import java.util.List;

import estm.dsic.jee.controllers.Task;
import estm.dsic.jee.dal.TaskDao;
import jakarta.enterprise.inject.Model;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
@Model
public class TaskService {

    @Inject FacesContext facesContext; 
    @Inject
    @ManagedProperty(value = "#{task}")
    private Task task;
    @Inject
    private TaskDao taskDao;
    List<Task> tasks;

    public List<Task> allTasks() throws SQLException{
        tasks = taskDao.getAllTasks();
        System.out.println("Number of Tasks retrieved: " + tasks.size());
        return tasks;
    }
    public String getAllTasks() throws SQLException {
        if(allTasks()!= null){
            return "manager/listTasks";
        } else {
            return "appManager";
        }
    }

    public String createTask(Task task) throws SQLException {
        if(taskDao.createTask(task)){
            FacesMessage message=new FacesMessage("Task create with success");
            facesContext.addMessage(null, message);
        }else{
            FacesMessage message=new FacesMessage("Task not created");
            facesContext.addMessage(null, message);
            
        }
        return "manager/listManager";
    }
    public String updateTask(Task task) throws SQLException {
        if(taskDao.updateTask(task)){
            FacesMessage message=new FacesMessage("Task updated with success");
            facesContext.addMessage("form_task:message", message);
        }else{
            FacesMessage message=new FacesMessage("Task not updated");
            facesContext.addMessage("form_task:message", message);
        }
        return "manager/listManager";
    }
    public String deleteTaskById(int taskId) throws SQLException {
        if(taskDao.deleteTaskById(taskId)){
            FacesMessage message=new FacesMessage("Task deleted with success");
            facesContext.addMessage("form_task:message", message);
        }else{
            FacesMessage message=new FacesMessage("Task not deleted");
            facesContext.addMessage("form_task:message", message);
        }
        return "manager/listManager";
    }
    public String editTask(int id){ 
        if(taskDao.editTask(id)) {
            System.out.println(" han kayn "+taskDao.editTask(id));
            return "task/editTask"; 
        }
        else 
            return "appManager";
    }
    
    public String updateTaskStatus(int id ,String newStatus) {
            if(taskDao.updateTaskStatus(id, newStatus)){
                FacesMessage message=new FacesMessage("Task deleted with success");
                facesContext.addMessage("form_task:message", message);
            }else{
                FacesMessage message=new FacesMessage("Task not deleted");
                facesContext.addMessage("form_task:message", message);
            }
            return "app";
            
    }
    
}
