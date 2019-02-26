package controlador.ec.edu.ups.tesiswsnsic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.PersonaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Persona;


@ViewScoped
@ManagedBean
public class ChangePassword {
	String newPassword1;
	String newPassword2;
	
	@Inject
	PersonaDAO personaDAO;
	
	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}
	
	public void changePassword() {
		if(newPassword1.equals(newPassword2)) {
			//se hace el cambio
			System.out.println("son iguales");
			Persona persona = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("userSelected");
			persona.setPassword(newPassword1);
			persona.setCambioPassword(false);
			personaDAO.updatePersona(persona);
			
			try {    
				FacesContext contex = FacesContext.getCurrentInstance();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Cambio", "Cambio Exitoso"));
				contex.getExternalContext().redirect("/TesisWSNSiC/faces/login/Login.xhtml");
		    } catch (IOException ex) {
		        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		        
		    }
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error", "Las contraseñas no coinciden. Intentelo de nuevo"));
		}
	}
}
