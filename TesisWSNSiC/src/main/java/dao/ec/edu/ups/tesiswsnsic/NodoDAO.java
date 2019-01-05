package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;

@Stateless
public class NodoDAO {

	@Inject
	private EntityManager em;
	
	public void insertarNodo(Nodo n) {
		try {
			em.persist(n);
			System.out.println("Insercion realizada...");
		} catch (Exception e) {
			System.out.println("Error al insertar Line:16, NodoDAO");
		}
	}
	
	public void updateNodo(Nodo n) {
		try {
			em.merge(n);
			System.out.println("Actualización correcta...");
		} catch (Exception e) {
			System.out.println("Error al actualizar Line:25, NodoDAO");
			
		}
	}
	
	public boolean eliminarNodo(int idN) {
		Nodo n = selectNodo(idN);
		try {
			em.remove(n);
			System.out.println("Eliminando "+ n.getId());
			return true;
		} catch (Exception e) {
			System.out.println("Error al eliminar");
			return false;
		}
	}
	
	public void grabarNodo(Nodo n) {
		Nodo aux = selectNodo(n.getId());
		System.out.println("Grabando...");
		
		try {
			if(aux!=null) {
				System.out.println("Actualizando Nodo con el ID: "+n.getId());
				updateNodo(n);
			}else {
				System.out.println("Insertando Nodo con el ID: "+n.getId());
				insertarNodo(n);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Nodo selectNodo(int idN) {
		Nodo n =em.find(Nodo.class, idN);
		
		System.out.println("Iniciando LAZY.. "+"Line:64, NodoDAO.java" );
		//Iniciando lazy
		if(n.getPersonanodos().isEmpty()) {
			n.getPersonanodos().size();
		}
		return n;
	}
	public List<Nodo> listNodos(){
		String jpql ="Select n from Nodo n";
		TypedQuery<Nodo> query = em.createQuery(jpql, Nodo.class);
		List<Nodo> lnodos = query.getResultList();
		for(Nodo pn: lnodos) {
			if(!pn.getPersonanodos().isEmpty()) {
				pn.getPersonanodos().size();
			}
		}
		return lnodos;
	}
	
	///paul
	
	public void insert(Nodo n) {
		try {
			em.persist(n);
			System.out.println("Insercion realizada...");
		} catch (Exception e) {
			System.out.println("Error al insertar "+this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	public void update(Nodo n) {
		try {
			em.merge(n);
			System.out.println("actualizacion realizada...");
		} catch (Exception e) {
			System.out.println("Error al actualizar "+this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	public void remove(Nodo n) {
		try {
			em.remove(n);
			System.out.println("eliminacion realizada...");
		} catch (Exception e) {
			System.out.println("Error al eliminar "+this.getClass().getName());
			e.printStackTrace();
		}
	}
	public List<Nodo> getAllNodos(){
		String jpql ="Select n from Nodo n";
		TypedQuery<Nodo> query = em.createQuery(jpql, Nodo.class);
		List<Nodo> lnodos = query.getResultList();
		return lnodos;
	}
	
}
