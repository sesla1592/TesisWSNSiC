package controlador.ec.edu.ups.tesiswsnsic;

import javax.faces.bean.ManagedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.PersonaDAO;
import dao.ec.edu.ups.tesiswsnsic.RolDAO;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.Rol;

@ManagedBean
@SessionScoped
public class PersonaControlador {

	@Inject
	private Logger log;
	
	@Inject
	private PersonaDAO pdao;
	
	@Inject
	private RolDAO rdao;
	
	private List<Rol> roles;
	private String selectrol;
	
	private Persona persona;
	
	private Persona miUsuario;
	
	
	@PostConstruct
	public void init() {
		persona = new Persona();
		roles = new ArrayList<Rol>();
	}

	public void Login(){
		
	}

	public void crearPersona() {
		try {
			persona.setEstado("A");
			persona.setRol(rol);
			pdao.grabarPersona(persona);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al crear: "+e.getMessage());
		}	
	}

	public void leerUsuario() {

	}

	public void eliminarUsuario() {

	}

	public void listarUsuarios() {

	}
	
	public static Rol rol = new Rol();
	public void rolSeleccionado() {
		for(Rol r : roles) {
			try {
				if(r.getDescripcion().equals(selectrol)) {
					persona.setRol(r);
					PersonaControlador.rol = r;
				}
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("No existe la categoria");
			}
		}
	}
	
	public List<SelectItem> listaRolesCombo(){
		List<SelectItem> lrol = new ArrayList<SelectItem>();
		roles = rdao.listRol();
		for(int i = 0; i < roles.size(); i++) {
			lrol.add(new SelectItem(roles.get(i).getDescripcion()));
		}
		return lrol;
	}
	
	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public String getSelectrol() {
		return selectrol;
	}

	public void setSelectrol(String selectrol) {
		this.selectrol = selectrol;
	}
	

}
