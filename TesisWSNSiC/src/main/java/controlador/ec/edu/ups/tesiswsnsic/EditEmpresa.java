package controlador.ec.edu.ups.tesiswsnsic;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;

@ManagedBean
public class EditEmpresa {

	@Inject
	private EmpresaDAO empresaDAO;
	
	@Inject
	private PersonaDAO personaDAO;
	
	private Empresa empresa;
	Persona user;
	
	private String newPassword;
	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			empresa =  user.getEmpresa();
			System.out.println("user " + user.getEmpresa().toString());
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error la extraer usuario");
		}
//		empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
//				.get("empresaSelected");
//		System.out.println("empresa s"+empresa.toString());
	}
	
	public void actualizar(){
		empresaDAO.updateEmpresa(empresa);
		//mensaje de cambio realizado
	}
	
	public void actualizarPersona() {
		if(newPassword!=null) {
			if(newPassword.equals(user.getPassword())){
				System.out.println("contrasenas iguales");
				personaDAO.updatePersona(user);
			}else {
				System.out.println("contrasenas diferentes");
				//mensaje de no guardado
			}
		}else {
			personaDAO.updatePersona(user);
		}
		
		
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Persona getUser() {
		return user;
	}

	public void setUser(Persona user) {
		this.user = user;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
	
}
