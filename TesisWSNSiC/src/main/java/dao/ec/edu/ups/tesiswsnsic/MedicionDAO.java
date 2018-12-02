package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.ec.edu.ups.tesiswsnsic.Medicion;

@Stateless
public class MedicionDAO {

	@Inject
	private EntityManager em;
	
	public void insertarMedicion(Medicion me) {
		try {
			em.persist(me);
			System.out.println("Insercion realizada...");
		} catch (Exception e) {
			System.out.println("Error al insertar Line:16, NodoDAO");
		}
	}
	
	public void updateMedicion(Medicion me) {
		try {
			em.merge(me);
			System.out.println("Actualización correcta...");
		} catch (Exception e) {
			System.out.println("Error al actualizar Line:25, NodoDAO");
			
		}
	}
	
	public boolean eliminarMedicion(int idM) {
		Medicion md = selectMedicion(idM);
		try {
			em.remove(md);
			System.out.println("Eliminando "+ md.getMed_id());
			return true;
		} catch (Exception e) {
			System.out.println("Error al eliminar");
			return false;
		}
	}
	
	public void grabarMedicion(Medicion me) {
		Medicion aux = selectMedicion(me.getMed_id());
		System.out.println("Grabando...");
		
		try {
			if(aux!=null) {
				System.out.println("Actualizando Nodo con el ID: "+me.getMed_id());
				updateMedicion(me);
			}else {
				System.out.println("Insertando Nodo con el ID: "+me.getMed_id());
				insertarMedicion(me);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Medicion selectMedicion(int idM) {
		Medicion med =em.find(Medicion.class, idM);
		return med;
	}
	
	public List<Medicion> listMediciones(){
		String jpql ="Select m from Medicion m";
		TypedQuery<Medicion> query = em.createQuery(jpql, Medicion.class);
		List<Medicion> lmediciones = query.getResultList();
		
		return lmediciones;
	}
}
