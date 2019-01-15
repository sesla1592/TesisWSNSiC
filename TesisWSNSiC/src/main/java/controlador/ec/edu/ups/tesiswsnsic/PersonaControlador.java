package controlador.ec.edu.ups.tesiswsnsic;

import javax.faces.bean.ManagedBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
//	@Inject
//	private Logger log;

	@Inject
	private PersonaDAO pdao;
	
	@Inject
	private RolDAO rolDAO;
	
	@Inject
	private EmpresaDAO edao;
	
	public static int miEmpresa;
	
	private static String roldev = "developer";
	private static String rolbad = "businessadmin";

	private String Loginexiste;
	private Persona personas;
	private String coincidencia;
	
	private Validacion v;

	private String user;
	@NotBlank(message = "Ingrese las contrasenias")
	private String password;

	public static Persona miUsuario;

	@PostConstruct
	public void init() {
		personas = new Persona();
		v = new  Validacion();
		
	}
	
	/**
	 * IniciarSesion inicilizar una Sesion HTTP y establecimiento de parametros en
	 * session, FacesContext acceso tanto al contexto de JSF como HTTP.
	 */
	public static int idUsuario;
//
//	public void iniciarSesion() {
//		System.out.println("INGRESANDO A INICIO SESION");
//		if(pdao.login(personas.getCorreo(), personas.getPassword()).size() != 0) {
//			HttpSession session = SessionUtils.getSession();
//			session.setAttribute("username",
//					pdao.login(personas.getCorreo(), personas.getPassword()).get(0).getCorreo());
//			session.setAttribute("estado",
//					pdao.login(personas.getCorreo(), personas.getPassword()).get(0).getEstado());
//			this.Loginexiste = " ";
//			FacesContext contex = FacesContext.getCurrentInstance();
//			System.out.println("ANTES DAO PERS");
//			/**Obtengo el id de persona con una variable estatica*/
//			List<Persona> pers = pdao.login(personas.getCorreo(), personas.getPassword());
//			idUsuario = pers.get(0).getId();
//			System.out.println("PERS ROL:   "+pers.get(0).getRol()+"   compara:   "+roldev+" "+rolbad);
//			if(pers.get(0).getRol().toUpperCase().equals(roldev.toUpperCase())) {
//				System.out.println("CONTEXTO US");
//				try {
//					contex.getExternalContext().redirect("/mainUS.xhtml?faces-redirect=true");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}else if(pers.get(0).getRol().toUpperCase().equals(rolbad.toUpperCase())){
//				System.out.println("DEVUELVE BA");
//				if(!edao.listEmpresa().isEmpty()) {
//					boolean bandera = false;
//					for(Empresa empre : edao.listEmpresa()) {
//						if(empre.getPersonas().get(0).getId()==idUsuario) {
//							bandera = true;
//							miEmpresa = empre.getId();
//						}
//					}
//					System.out.println("Bandera:  "+bandera);
//					if(bandera == true) {
//						try {
//							//YA ESTA ASOCIADO A UNA EMPRESA
//							System.out.println("REDIRECCIONANDO... BA");
//
//							contex.getExternalContext().redirect("/mainBA.xhtml?faces-redirect=true");
//
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}else if(bandera==false){
//						//crea empresa
//						System.out.println("A REGISTRAR BUSINESS");
//						try {
//							cargarDatosUsuario();
//
//							contex.getExternalContext().redirect("/registerBusiness.xhtml?faces-redirect=true");
//
//							contex.getExternalContext().redirect("../registerBusiness.xhtml?faces-redirect=true");
//
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}else {
//					//crea empresa
//					System.out.println("A REGISTRAR BUSINESS");
//					try {
//						cargarDatosUsuario();
//						contex.getExternalContext().redirect("../registerBusiness.xhtml?faces-redirect=true");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			init();			
//	}
//}
//	
	
	public void login() {
		System.out.println("user "+user +" password "+password);
		miUsuario = pdao.loginPersona(user,password);
		if (miUsuario!=null) {
			System.out.println("login exitoso");
			FacesContext contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().getSessionMap().put("userSelected", miUsuario);
			if(miUsuario.getRolPerson().getId()==1) {//es admin
				System.out.println("es admin");
				try{
					contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/dashboard.xhtml");
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			if(miUsuario.getRolPerson().getId()==2) {//es usuario
				System.out.println("es user");
				if(miUsuario.getEmpresa()!=null) {
					System.out.println("ya tiene empresa");
					try{
						contex.getExternalContext().redirect("/TesisWSNSiC/faces/user/dashboard.xhtml");
					}catch (Exception e) {
						// TODO: handle exception
					}
				}else {
					System.out.println("no tiene empresa");
					try{
						contex.getExternalContext().redirect("/TesisWSNSiC/faces/user/crearEmpresa.xhtml");
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			
		}else {
			System.out.println("login fallido");
		}
	}
	
	public void irLogin() {
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/login/index.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
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
	
	public String crearPersona() {
		try {
			if(coincidirPassword() == true ) {
				System.out.println("CORREO: " + personas.getCorreo());
				System.out.println("Nombre: " + personas.getNombre());
				if(v.validarCorreo(personas.getCorreo()) == true) {
					personas.setEstado("A");
					Rol rol = rolDAO.rolById(2);
					System.out.println("ver--> "+rol.toString());
					personas.setRolPerson(rol);
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
		return "index?faces-redirect=true";
	}

	public void leerUsuario() {

	}

	public void eliminarUsuario() {

	}

	public void listarUsuarios() {

	}

	/**
	 * Coincidir password. Comparacion de los 2 campos referentes a la
	 * password, devolucion(true/false)
	 * 
	 * @return true, if successful
	 */

	public boolean coincidirPassword() {
		try {
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
	

	public String cerrarSesion() {
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		return "index?faces-redirect=true";
	}
	
	public String backRe() {
		
		System.out.println("Si regresa aa....");
		
		return "Registrar?faces-redirect=true";

	}

	public Persona getPersonas() {
		return personas;
	}

	public void setPersonas(Persona personas) {
		this.personas = personas;
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	
}
