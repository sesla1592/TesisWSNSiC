package dao.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;

@Stateless
public class EmpresaDAO {

	@Inject
	private EntityManager em;

	@Inject
	private PersonaDAO pdao;
	
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
	
	
	public void updateEmpresaPersona(Empresa empres, int idp) {
System.out.println("EMPRESA: "+empres.getNombre());
		//Empresa emp = new Empresa();
		//emp.equals(empresa);
		Persona persona = pdao.selectPersona(idp);
		List<Persona> personas = new ArrayList<Persona>();
		personas.add(persona);
		empres.setPersonas(personas);
		empres.setEstado("A");
		em.merge(empres);		
	}	
}
