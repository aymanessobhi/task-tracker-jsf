package estm.dsic.jee.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class Employee {
    private int id;
    private Login user = new Login();
    private String department;

    public Login getUser() {
        return user;
    }
    public void setUser(Login user) {
        this.user = user;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
