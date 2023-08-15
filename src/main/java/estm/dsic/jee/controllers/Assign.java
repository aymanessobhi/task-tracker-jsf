package estm.dsic.jee.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class Assign {
    private int id;
    private Task task = new Task();
    private Employee employee = new Employee();

    private int selectedProjectId;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee2) {
        this.employee = employee2;
    }
    public int getSelectedProjectId() {
        return selectedProjectId;
    }
    public void setSelectedProjectId(int selectedProjectId) {
        this.selectedProjectId = selectedProjectId;
    }
    
}
