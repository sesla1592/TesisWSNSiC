package controlador.ec.edu.ups.tesiswsnsic;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import modelo.ec.edu.ups.tesiswsnsic.Persona;

@ManagedBean
@SessionScoped
public class PersonaControlador {

	private Persona p;
	
	@PostConstruct
	public void init() {
		p = new Persona();
	}
	
	public void Login(){
		
	}

	public void crearUsuario() {

	}

	public void leerUsuario() {

	}

	public void eliminarUsuario() {

	}

	public void listarUsuarios() {

	}

}
