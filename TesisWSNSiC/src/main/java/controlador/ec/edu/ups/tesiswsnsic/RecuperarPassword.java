package controlador.ec.edu.ups.tesiswsnsic;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.PersonaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import utilidades.ec.edu.ups.tesiswsnsic.SendEmail;


@ViewScoped
@ManagedBean
public class RecuperarPassword {
	
	public String email;
	
	@Inject
	PersonaDAO personaDAO;
	
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void sendEmailRecovery() {
		Persona persona = personaDAO.getByEmail(email);
		if(persona==null) {
			//se muestra un mensaje de error
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error", "No existe el usuario"));
		}else {
			String newPassword = generatepassword();
			persona.setPassword(newPassword);
			persona.setCambioPassword(true);
			personaDAO.updatePersona(persona);
			//metodo para enviar el correo
			SendEmail sendEmail = new SendEmail();
			String cuerpo = "Se ha generado la siguiente contraseña para su cuenta: "+newPassword+" \n usela una vez y cambie su contraseña.";
			boolean email = sendEmail.enviarConGMail(persona.getCorreo(), "Recuperación contraseña", cuerpo);
			if(email) {
				//mensaje de q si se envio
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Mensaje", "Mensaje Enviado exitosamente!"));
			}else {
				//mensaje de q no se envio
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Error", "Error al enviar el mensaje, Por favor reintentelo mas tarde."));
			}
		}
	}
	
	public String generatepassword() {
		String NUMEROS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String pswd = "";

		for (int i = 0; i < 10; i++) {
			pswd += (NUMEROS.charAt((int) (Math.random() * NUMEROS.length())));
		}
		return pswd;
	}
}
