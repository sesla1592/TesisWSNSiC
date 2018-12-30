package dao.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import controlador.ec.edu.ups.tesiswsnsic.EmpresaControlador;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.TipoEmpresa;

@Stateless
public class EmpresaDAO {

	@Inject
	private EntityManager em;

	@Inject
	private PersonaDAO pdao;
	
	public void insertEmpresa(Empresa empresa){
		try {
			em.persist(empresa);
		} catch (Exception e) {
			System.out.println("error al insertar "+this.getClass().getName());
			// TODO: handle exception
		}
	}
	public void updateEmpresa(Empresa empresa){
		try {
			em.merge(empresa);
		} catch (Exception e) {
			System.out.println("error al insertar "+this.getClass().getName());
			// TODO: handle exception
		}
	}
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
	
	public List<Empresa> getAllEmpresas(){
		List<Empresa> empresas = new ArrayList<Empresa>();
		String jpql = "Select e from Empresa e order by e.id";
		TypedQuery<Empresa> query = em.createQuery(jpql, Empresa.class);
		empresas = query.getResultList();
		return empresas;
	}
	
	public Empresa selectEmpresa(int idem) {
		Empresa empres = new  Empresa();
		empres = em.find(Empresa.class, idem);
		//System.out.println("**************QUERY SINGLE *************"+empres.getDireccion());
		return empres;
	}
	
	public void updateEmpresaPersona(Empresa empres, int idp) {
		System.out.println("EMPRESA: "+empres.getNombre());
		
		Persona persona = pdao.selectPersona(idp);
		List<Persona> personas = new ArrayList<Persona>();
		personas.add(persona);
		int idempre = maxId();
		empres.setId(idempre);
		empres.setPersonas(personas);
		empres.setEstado("A");
		empres.setTipoempresa(EmpresaControlador.tem);
		List<Empresa> lemp = new ArrayList<Empresa>();
		lemp.add(empres);
		em.merge(empres);	
		
		/*
		Empresa empobt = selectEmpresa(idempre);
		List<Empresa> lempobt = new ArrayList<Empresa>();
		lempobt.add(empobt);
		EmpresaControlador.tem.setEmpresas(lempobt);
		em.merge(lempobt);
		*/
	}	
	
	public int maxId() {
		String jpql =  "Select em from Empresa em";
		TypedQuery<Empresa> q = em.createQuery(jpql, Empresa.class);
		List<Empresa> el = q.getResultList();
		if(el.size()==0) {
			return 1;
		}else {
			String jpqlm = "Select max(em.id) from Empresa em";
			int id = (int) em.createQuery(jpqlm).getSingleResult();
			int idnew = id+1;
			return idnew;
		}
	}
}
