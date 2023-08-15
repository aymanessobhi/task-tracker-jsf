package estm.dsic.jee.services;
import java.io.IOException;
import java.util.List;

import estm.dsic.jee.controllers.Project;
import estm.dsic.jee.controllers.Task;
import estm.dsic.jee.dal.ProjectDao;
import jakarta.enterprise.inject.Model;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
@Model
public class ProjectService implements IProject {
    
    @Inject FacesContext facesContext; 
    @Inject
    @ManagedProperty(value = "#{project}")
    private Project project;
    @Inject
    private ProjectDao projectDao;
    List<Project> projects;


    public List<Project> allProjects(){
        projects = projectDao.getAllProjects();
        System.out.println("Number of projects retrieved: " + projects.size());
        return projects;
    }


    @Override
    public String getAllProjects() {
        if(allProjects()!= null){
            return "manager/listProjects";
        } else {
            return "appManager";
        }
    }

    public Project getFirstProject() {
        List<Project> projects = allProjects();
        if (projects != null && !projects.isEmpty()) {
            return projects.get(0);
        } else {
            return null;
        }
    }

    public String edit(int id){  
        return projectDao.editProject(id);  
    }
    
    public String update(Project p){  
        if(projectDao.updateProject(p)){
            FacesMessage message = new FacesMessage("Project Updated successfully");
            FacesContext.getCurrentInstance().addMessage("form_project:messages", message);
        }else{
            FacesMessage message=new FacesMessage("project not updated");
            facesContext.addMessage("form_project:messages", message);
        }
        return "/appManager";
    }
    
    public  String delete(int id){  
        if(projectDao.deleteProject(id)){
            FacesMessage message = new FacesMessage("Project deleted successfully");
            FacesContext.getCurrentInstance().addMessage("form_project:messages", message);
        }
        else{
            FacesMessage message=new FacesMessage("project not deleted");
            facesContext.addMessage("form_project:id_project", message);
        }
        return "appManager";
    }

    public String create(Project p){  
        if(projectDao.createProject(p)){
            FacesMessage message=new FacesMessage("project create with success");
            facesContext.addMessage("form_project:message", message);
            
        } 
        else{
            FacesMessage message=new FacesMessage("project not add");
            facesContext.addMessage("form_project:message", message);
            
        }
        return "appManager";
    }

    public String viewTasks(int id){          
        return projectDao.viewTasks(id);  
    } 

    public List<Task> getTasks(int id){          
        return projectDao.getTasks(id);  
    }
  
    
}
