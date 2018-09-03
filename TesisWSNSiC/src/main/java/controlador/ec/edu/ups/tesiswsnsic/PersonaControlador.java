package controlador.ec.edu.ups.tesiswsnsic;

import javax.faces.bean.ManagedBean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.hibernate.validator.constraints.NotBlank;

import dao.ec.edu.ups.tesiswsnsic.PersonaDAO;
import dao.ec.edu.ups.tesiswsnsic.RolDAO;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.Rol;
import validacionesnegocio.ec.edu.ups.tesiswsnsic.Validacion;

@ManagedBean
@SessionScoped
public class PersonaControlador {

	@Inject
	private Logger log;

	@Inject
	private PersonaDAO pdao;

	@Inject
	private RolDAO rdao;

	private Persona personas;
	private String coincidencia;
	private List<Rol> roles;
	private String selectrol;
	
	private Validacion v;

	@NotBlank(message = "Ingrese las contrasenias")
	private String password;

	private Persona miUsuario;

	@PostConstruct
	public void init() {
		personas = new Persona();
		v = new  Validacion();
		roles = new ArrayList<Rol>();
		
	}

	public void Login() {

	}

	/**
	 * Crear Persona. Creacion del objeto Persona condicinamiento segun las sentencias de
	 * validacion. Método utiliza ajax através de JSF.
	 * 
	 * @param coincidencia:
	 *            Muestra mensajes de validacion en los campos mencionados para el
	 *            registro de usuarios
	 */
	
	public void crearPersona() {
		try {
			if(coincidirPassword() == true ) {
				System.out.println("CORREO: " + personas.getCorreo());
				System.out.println("Nombre: " + personas.getNombre());
				if(v.validarCorreo(personas.getCorreo()) == true) {
					System.out.println("CONTROLADOR");
					personas.setEstado("A");
					personas.setRol(rol);
					pdao.grabarPersona(personas);
					inicializar();
					this.coincidencia = "Grabado exitoso!";
					personas = new Persona();
				}else {
					this.coincidencia = "El formato del correo es incorrecto";
				}
			}else {
				System.out.println("Contrasenias ingresadas son diferentes");
				this.coincidencia = "No coinciden las  contraseñas";
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error al crear: " + e.getMessage());
			e.printStackTrace();
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
		for (Rol r : roles) {
			try {
				if (r.getDescripcion().equals(selectrol)) {
					personas.setRol(r);
					PersonaControlador.rol = r;
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("No existe la categoria");
			}
		}
	}

	public List<SelectItem> listaRolesCombo() {
		List<SelectItem> lrol = new ArrayList<SelectItem>();
		roles = rdao.listRol();
		for (int i = 0; i < roles.size(); i++) {
			lrol.add(new SelectItem(roles.get(i).getDescripcion()));
		}
		return lrol;
	}

	/**
	 * Coincidir password. Comparacion de los 2 campos referentes a la
	 * password, devolucion(true/false)
	 * 
	 * @return true, if successful
	 */

	public boolean coincidirPassword() {
		try {
			System.out.println("P1: "+personas.getPassword());
			System.out.println("P2: "+password);
			if (personas.getPassword().equals(this.password)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error Password: " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Inicializar. Setea las variable como vacias, ocupado al momento de haber
	 * creado el usuario y dejar los h:inputText del JSF en blanco
	 */

	public void inicializar() {
		personas.setNombre("");
		personas.setApellido("");
		personas.setCorreo("");
		personas.setPassword("");
	}



	public Persona getPersonas() {
		return personas;
	}

	public void setPersonas(Persona personas) {
		this.personas = personas;
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

	public String getCoincidencia() {
		return coincidencia;
	}

	public void setCoincidencia(String coincidencia) {
		this.coincidencia = coincidencia;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
