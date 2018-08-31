package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Persona;

@Stateless
public class PersonaDAO {

	@Inject
	private EntityManager em;

	public void grabarPersona(Persona p) {
		Persona aux = selectPersona(p.getId());
		try {
			if (aux != null) {
				updatePersona(p);
			} else {
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
		return lpersonas;
	}
}
