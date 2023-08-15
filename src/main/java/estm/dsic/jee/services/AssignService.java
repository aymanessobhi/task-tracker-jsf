package estm.dsic.jee.services;

import java.sql.SQLException;
import java.util.List;

import estm.dsic.jee.controllers.Assign;
import estm.dsic.jee.dal.AssignDao;
import jakarta.enterprise.inject.Model;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Model
public class AssignService {
    @Inject FacesContext facesContext; 
    @Inject
    @ManagedProperty(value = "#{assign}")
    private Assign Assign;
    @Inject
    private AssignDao assignDao;
    List<Assign> assigns;

    public List<Assign> allAssignments() throws SQLException{
        assigns = assignDao.getAllAssignments();
        System.out.println("Number of Assigns retrieved: " + assigns.size());
        return assigns;
    }

    public String getAllAssigns() throws SQLException {
        if(allAssignments()!= null){
            return "manager/listAssigns";
        } else {
            return "appManager";
        }
    }
    
    public String createAssigns(Assign a) throws SQLException {
        if(assignDao.save(a)){
            FacesMessage message=new FacesMessage("Assignment create with success");
            facesContext.addMessage(null, message);
        }
        else{
            FacesMessage message=new FacesMessage("Assignment create with success");
            facesContext.addMessage(null, message);
        } 
        return "manager/listAssigns";
    }

    private List<Assign> assignments;

    public List<Assign> getAssignByEmail(String email) {

        assignments = assignDao.getAssignByEmail(email);
        for (Assign assign1 : assignments) {
            System.out.println(assign1.getTask().getTaskStatus() + " end ");
        }
        return assignDao.getAssignByEmail(email);
    }

    public String delete(int assignId) {

        if(assignDao.delete(assignId)){
            FacesMessage message=new FacesMessage("Assignment delete with success");
            facesContext.addMessage("form_project:message", message);
        }
        else{
            FacesMessage message=new FacesMessage("Assignment not deleted");
            facesContext.addMessage("form_assign:message", message);
        }
        return "manager/listAssigns";
    }
    
}
