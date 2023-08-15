package estm.dsic.jee.services;

import java.sql.SQLException;
import java.util.List;

import estm.dsic.jee.controllers.Employee;
import estm.dsic.jee.controllers.Login;
import estm.dsic.jee.dal.EmployeeDao;
import estm.dsic.jee.dal.UserDao;
import jakarta.enterprise.inject.Model;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.context.FacesContext;
import jakarta.faces.flow.ReturnNode;
import jakarta.inject.Inject;

@Model
public class EmployeeService {
    @Inject FacesContext facesContext; 
    @Inject
    @ManagedProperty(value = "#{employee}")
    private Employee employee;
    @Inject
    private EmployeeDao employeeDao;
    private UserDao userDao;
    List<Employee> employees;

    public List<Employee> allEmployees() throws SQLException{
        employees = employeeDao.getAllEmployees();
        System.out.println("Number of Employees retrieved: " + employees.size());
        return employees;
    }

    public String getAllEmployees() throws SQLException {
        if(allEmployees()!= null){
            return "manager/listEmployees";
        } else {
            return "appManager";
        }
    }

    public String create(Employee e) throws SQLException{  
        return employeeDao.createEmployee(e);  
    }
    
    public Employee getEmployeeByEmail(String email) {
        Login user = userDao.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        return employeeDao.getEmployeeByUser(user);
    }
    
    public List<Employee> getEmployeesWithoutAssignment() throws SQLException {
        employees = employeeDao.getEmployeesWithoutAssignment();
        for (Employee employee : employees) {
            System.out.println(" ayman "+employee.getUser().getLastName());
        }
        return employees;
    }

    public String delete(int id) throws SQLException {
        System.out.println(" raha khdama !!!!??????");
        return employeeDao.delete(id);
    }

    public String editEmployee(int id) {  
        System.out.println(" rani fservices");
        if(employeeDao.editEmployee(id)){
            System.out.println(" raha khdama !!!!??????"+employeeDao.editEmployee(id));
            return "employee/editEmployee";
        }
        return null;
    }

    public String update(Employee employee) throws SQLException {
        if(employeeDao.updateEmployee(employee)){
            return "manager/listEmployees";
        }
        return null;

    }

}
