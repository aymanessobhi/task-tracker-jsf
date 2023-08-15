package estm.dsic.jee.controllers;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class Logout implements Serializable{
    
    @Inject ExternalContext ec;
    
    public String logout(){
        ec.invalidateSession();
        return "login";
    }
    
}
