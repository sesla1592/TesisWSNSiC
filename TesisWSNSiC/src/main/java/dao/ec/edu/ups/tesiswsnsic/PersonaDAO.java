package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.Rol;

@Stateless
public class PersonaDAO {

	@Inject
	private EntityManager em;
	
	@Inject
	private RolDAO rdao;
	
	private Rol r = new Rol();

	public void grabarPersona(Persona p) {
		Persona aux = selectPersona(p.getId());
		System.out.println("GRABARPERSONA DAO: "+p.getId()+" "  + p.getApellido()+" "+ p.getCorreo() +" "+ p.getNombre()+" "+ p.getPassword());
		try {
			if (aux != null) {
				updatePersona(p);
			} else {
			
				System.out.println("GRABAR");
				System.out.println("Grabando...!");
				insertPersona(p);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	public void insertPersona(Persona p) {
		try {
			em.persist(p);
			System.out.println("Almacenado exitoso!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error almacenando: "+ex.getMessage());
		}
	}

	public void updatePersona(Persona p) {
		try {
			em.merge(p);
			System.out.println("Actualizacion exitosa");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error actualizando: "+e.getMessage());
		}
	}

	public Persona selectPersona(int idP) {
		Persona p = em.find(Persona.class, idP);
//		System.out.println(p.getRol());
/*		if(!p.getMovimientos().isEmpty()){
			p.getMovimientos().size();
		}
		if(!p.getPersonanodos().isEmpty()){
			p.getPersonanodos().size();
		}
	*/	
		return p;
	}

	public boolean removePersona(int id) {
		Persona p = selectPersona(id);
		try {
			em.remove(p);
			System.out.println("Usuario eliminado");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al eliminar: "+e.getMessage());
			return false;
		}
	}

	public List<Persona> listPersona() {
		String jpql = "select p from Persona p";
		TypedQuery<Persona> query = em.createQuery(jpql, Persona.class);
		List<Persona> lpersonas = query.getResultList();
		for(Persona p : lpersonas) {
			if(!p.getMovimientos().isEmpty()) {
				p.getMovimientos().size();	
			}
			if(!p.getPersonanodos().isEmpty()) {
				p.getPersonanodos().size();
			}
		}
		return lpersonas;
	}
	
	/**
	 * Login. SQL para encontrar a la personas a través de los parámetros.
	 *
	 * @param user the user
	 * @param pass the pass
	 * @return una list de personas con esos parámetros
	 */
	public List<Persona> login(String user, String pass) {
		String sql = "Select p from Persona p WHERE p.correo = '"+user+"' AND p.password='"+pass+"'";
		TypedQuery<Persona> query = em.createQuery(sql, Persona.class);
		List<Persona> personas = query.getResultList();
		for(Persona pe : personas) {
			/*
			if(!pe.getMovimientos().isEmpty()) {
				pe.getMovimientos().size();
			}
			if(!pe.getPersonanodos().isEmpty()) {
				pe.getPersonanodos().size();
			}
			*/
		}
		return personas;
	}
	
	
	/**
	 * Verifica correo.
	 *
	 * @param user the user
	 * @return the list
	 */
	public List<Persona> verificaCorreo(String user)
	{
		String sql="Select p from Persona p WHERE p.correo = '"+user+"'";
		TypedQuery<Persona> query=em.createQuery(sql,Persona.class);
		List<Persona>personas=query.getResultList();
		for(Persona p : personas) {
			if (!p.getMovimientos().isEmpty()) {
				p.getMovimientos().size();	
			}
			if(!p.getPersonanodos().isEmpty()) {
				p.getPersonanodos().size();
			}
		}
		return personas;
	}

	
	public Rol PersonaRol(int idpers) {
		Persona pe = selectPersona(idpers);
		for(Rol ro : rdao.listRol()){
			
			if(pe.getRol().getId()==ro.getId()){
				System.out.println("PRINT ******"+ ro.getDescripcion());
				r = ro;
			}
			
		}
		return r;
	}

}
