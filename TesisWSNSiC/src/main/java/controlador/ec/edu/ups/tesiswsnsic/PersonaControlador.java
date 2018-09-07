package controlador.ec.edu.ups.tesiswsnsic;

import javax.faces.bean.ManagedBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.hibernate.validator.constraints.NotBlank;

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaDAO;
import dao.ec.edu.ups.tesiswsnsic.RolDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.Rol;
import utilidades.ec.edu.ups.tesiswsnsic.SessionUtils;
import validacionesnegocio.ec.edu.ups.tesiswsnsic.Validacion;

@ManagedBean
@SessionScoped
public class PersonaControlador {

	@Inject
	private Logger log;

	@Inject
	private PersonaDAO pdao;
	
	@Inject
	private EmpresaDAO edao;

	@Inject
	private RolDAO rdao;

	private String Loginexiste;
	private Empresa empresa;
	private Persona personas;
	private String coincidencia;
	private List<Rol> roles;
	private String selectrol;
	
	private Validacion v;

	@NotBlank(message = "Ingrese las contrasenias")
	private String password;

	public static Persona miUsuario;

	@PostConstruct
	public void init() {
		personas = new Persona();
		v = new  Validacion();
		roles = new ArrayList<Rol>();
		
	}
	
	/**
	 * IniciarSesion inicilizar una Sesion HTTP y establecimiento de parametros en
	 * session, FacesContext acceso tanto al contexto de JSF como HTTP.
	 */
	public static int idUsuario;

	public void iniciarSesion() {
		if(pdao.login(personas.getCorreo(), personas.getPassword()).size() != 0) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username",
					pdao.login(personas.getCorreo(), personas.getPassword()).get(0).getCorreo());
//			session.setAttribute("perfil",
//					pdao.login(personas.getCorreo(), personas.getPassword()).get(0).getPerfil());
			session.setAttribute("estado",
					pdao.login(personas.getCorreo(), personas.getPassword()).get(0).getEstado());
			this.Loginexiste = " ";
			FacesContext contex = FacesContext.getCurrentInstance();
			
			/**Obtengo el id de persona con una variable estatica*/
			List<Persona> pers = pdao.login(personas.getCorreo(), personas.getPassword());
			idUsuario = pers.get(0).getId();
			
			//BUSCAR EMPRESA A ESTE USUARIO
			if(!edao.listEmpresa().isEmpty()) {
				System.out.println("******************INGRESA IS EMPTY EMPRESA");
				for(Empresa empre : edao.listEmpresa()) {
				System.out.println("ID DE PERSONA EMPRESA: "+empre.getPersonas().get(0).getId());
				System.out.println("idUsuario: "+idUsuario);
					if(!empre.getPersonas().isEmpty()) {
						if(empre.getPersonas().get(0).getId()==idUsuario) {
							//SI SE ENCUENTRA ASOCIADO A UNA EMPRESA DEBE ARROJARLE LA PAGINA DE  BA O LA RESPECTIVA
							//System.out.println("******************EMPRESA: " +empre.getNombre() +"PARA USUARIO ID: "+idUsuario);
							
							if (pers.get(0).getRol().getDescripcion().equals("BA")) {
								System.out.println("CONTEXTO BA");
								try {
									contex.getExternalContext().redirect("mainBA.xhtml");
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else if (pers.get(0).getRol().getDescripcion().equals("US")) {
								System.out.println("CONTEXTO US");
								try {
									contex.getExternalContext().redirect("mainUS.xhtml");
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}else {
							System.out.println("******************NO PERTENECE A NINGUNA");
							//SI NO TIENE ASOCIADO NINGUNA EMPRESA, SE LANZA LA PANTALLA DE CREACION DE LA EMPRESA SIEM
							//PRE Y CUANDO TENGA EL ROL DE BA
			
							//REDIRECCINOAMIENTO A  LA RESPECTIVA PAGINA SEGUN EL ROL
							if (pers.get(0).getRol().getDescripcion().equals("BA")) {
								System.out.println("CONTEXTO BA");
								try {
									//CREACION DE LA EMPRESA CAMBIAR LA NAVEGACION
									cargarDatosUsuario();
									contex.getExternalContext().redirect("registerBusiness.xhtml");
//									contex.getExternalContext().redirect("mainBA.xhtml");
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else if (pers.get(0).getRol().getDescripcion().equals("SA")) {
								System.out.println("CONTEXTO SA");
								try {
									contex.getExternalContext().redirect("mainSA.xhtml");
								} catch (IOException e) {
									e.printStackTrace();
								}
							}	else if (pers.get(0).getRol().getDescripcion().equals("US")) {
								System.out.println("CONTEXTO US");
								try {
									contex.getExternalContext().redirect("mainUS.xhtml");
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
							
							
						}
					}
				}
			}else {
				//Cuando no se crean ninguna empresa
				System.out.println("******************ARROJA LA PANTALLA DE LA CREACION DE UNA NUEVA EMPRESA LA PRIMERA");		
			}
	}
	}
	
	
	public void cargarDatosUsuario() {
		miUsuario = new Persona();
		HttpSession session = SessionUtils.getSession();
		String nus = (String) session.getAttribute("username");
		System.out.println("NUS " + nus);
		try {
			if (pdao.verificaCorreo(nus).size() != 0) {
				List<Persona> lusuario = new ArrayList<Persona>();
				lusuario = pdao.verificaCorreo(nus);
				miUsuario = lusuario.get(0);
				System.out.println("MI USUARIO: " + miUsuario.getCorreo());
			}
		} catch (Exception e) {
		}
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

	public static Persona getMiUsuario() {
		return miUsuario;
	}

	public static void setMiUsuario(Persona miUsuario) {
		PersonaControlador.miUsuario = miUsuario;
	}

}
