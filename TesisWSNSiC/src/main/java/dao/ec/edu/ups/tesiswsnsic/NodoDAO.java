package dao.ec.edu.ups.tesiswsnsic;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import modelo.ec.edu.ups.tesiswsnsic.Nodo;

@Stateless
public class NodoDAO {

	@Inject
	private EntityManager em;
	
	
	public void guardarNodo (Nodo n) {
		
		Nodo aux = buscarNodo(n.getId());
		
		try {
			
			if(aux!=null) {
				update(n);
			}else
				
				System.out.println("GRABAR");
				System.out.println("Grabando...!");
			
				insertar(n);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public Nodo buscarNodo (int n) {
	
		Nodo nodo = em.find(Nodo.class, n);
		return nodo;
	}
	
	public void insertar (Nodo nodo) {
		
		em.persist(nodo);
	}
	
	public void update (Nodo nodo) {
		em.merge(nodo);
	
	}
}
