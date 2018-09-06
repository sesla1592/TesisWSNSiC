package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Rol;

@Stateless
public class RolDAO {

	@Inject
	private EntityManager em;
	
	public List<Rol> listRol(){
		String jpql = "select r from Rol r";
		TypedQuery<Rol> query = em.createQuery(jpql, Rol.class);
		List<Rol> roles = query.getResultList();
		return roles;
	}
}