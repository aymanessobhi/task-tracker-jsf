package estm.dsic.jee.services;

import java.util.Map;

import estm.dsic.jee.controllers.Employee;
import estm.dsic.jee.controllers.Login;
import estm.dsic.jee.dal.UserDao;
import jakarta.enterprise.inject.Model;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Model
public class UserServices {
    @Inject FacesContext facesContext; 
    @Inject
    @ManagedProperty(value = "#{login}")
    private Login login;
    @Inject
    private UserDao userDao;
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();  ;
    private EmployeeService employeeService;
    
    public String check(){
        if (userDao.auth(login.getEmail(),login.getPasswd(),login.getUserType())){
            if(login.getUserType().equals("employee")){
               System.out.println(login.getEmail() +" "+login.getFirstName());
                return "app";
            }   
            else if(login.getUserType().equals("project manager")){
                return "appManager";
            }
            else 
                return "";
        }
        else {
            FacesMessage message=new FacesMessage("login , type or password incorrect");
            facesContext.addMessage("form_login:id_login", message);
            return "login";
        }
    }
}
