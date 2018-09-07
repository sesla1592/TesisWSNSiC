package dao.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Empresa;

@Stateless
public class EmpresaDAO {

	@Inject
	private EntityManager em;

	public List<Empresa> listEmpresa() {
		List<Empresa> empresas = new ArrayList<Empresa>();
		String jpql = "Select e from Empresa e";
		TypedQuery<Empresa> query = em.createQuery(jpql, Empresa.class);
		empresas = query.getResultList();
		for(Empresa e : empresas) {
			if(!e.getPersonas().isEmpty()) {
				e.getPersonas().size();
			}
		}
		return empresas;
	}
}
