package dao.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.PersonaNodo;

@Stateless
public class PersonaNodoDAO {

	@Inject
	private EntityManager em;
	
	public void insertPersonaNodo(PersonaNodo personaNodo){
		try {
			em.persist(personaNodo);
			
		} catch (Exception e) {
			System.out.println("error al insertar "+this.getClass().getName());
			// TODO: handle exception
			
		}
	}
	
	public void remove(PersonaNodo personaNodo){
		try {
			//em.remove(personaNodo);
			PersonaNodo managed = findbyId(personaNodo.getId());
			em.remove(managed);
		} catch (Exception e) {
			System.out.println("error al eliminar "+this.getClass().getName());
			e.printStackTrace();
			// TODO: handle exception
			
		}
	}
	
	public PersonaNodo findbyId(int id) {
		return em.find(PersonaNodo.class, id);
	}
	
	public void updatePersonaNodo(PersonaNodo personaNodo){
		try {
			em.merge(personaNodo);
			
		} catch (Exception e) {
			System.out.println("error al insertar "+this.getClass().getName());
			// TODO: handle exception
			
		}
	}
	
	public List<Nodo> ltsNodosByUser(int userId) {
		try {
			List<PersonaNodo> ltsPersonNodo = new ArrayList<PersonaNodo>();

//			TypedQuery<PersonaNodo> query = em.createQuery("Select n from PersonaNodo n", PersonaNodo.class);

			TypedQuery<PersonaNodo> query = em.createQuery("Select n from PersonaNodo n "
					+ "where n.persona.id = :userId", PersonaNodo.class);
			query.setParameter("userId", userId);

			ltsPersonNodo = query.getResultList();
			List<Nodo> ltsNodo = new ArrayList<>();
			for (int i = 0; i < ltsPersonNodo.size(); i++) {
				ltsPersonNodo.get(i).getNodo().getLtssensores().size();
				ltsNodo.add(ltsPersonNodo.get(i).getNodo());
			}
			return ltsNodo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public PersonaNodo getByPersonNodo(int userId, int nodoId) {

		TypedQuery<PersonaNodo> query = em.createQuery("Select n from PersonaNodo n "
				+ "where n.persona.id = :userId and n.nodo.id = :nodoId", PersonaNodo.class);
		query.setParameter("userId", userId);
		query.setParameter("nodoId", nodoId);
		try {
			PersonaNodo personaNodo = query.getSingleResult();
			return personaNodo;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
}
