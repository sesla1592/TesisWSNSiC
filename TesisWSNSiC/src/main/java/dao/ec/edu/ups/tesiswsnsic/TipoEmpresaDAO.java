package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.TipoEmpresa;

@Stateless
public class TipoEmpresaDAO {

	@Inject
	private EntityManager em;
	
	
	public List<TipoEmpresa> listTipoEmpresa(){
		String jpql = "Select temp from TipoEmpresa temp";
		TypedQuery<TipoEmpresa> query = em.createQuery(jpql, TipoEmpresa.class);
		List<TipoEmpresa> listado =query.getResultList();
		return listado;
	}
	
	/*
	public void updateTEEmpresaUsuario(){
		TipoEmpresa te = new TipoEmpresa();
		te.setEmpresas(empresas);
	}
	*/
}
