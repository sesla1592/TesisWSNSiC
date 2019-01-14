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
			TypedQuery<PersonaNodo> query = em.createQuery("Select n from PersonaNodo n", PersonaNodo.class);
			ltsPersonNodo = query.getResultList();
			List<Nodo> ltsNodo = new ArrayList<>();
			for(PersonaNodo personNodo : ltsPersonNodo) {
				ltsNodo.add(personNodo.getNodo());
			}
			return ltsNodo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}