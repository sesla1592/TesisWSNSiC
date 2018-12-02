package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import modelo.ec.edu.ups.tesiswsnsic.NodoSensor;

@Stateless
public class NodoSensorDAO {

	@Inject
	private EntityManager em;
	
	public void insertarNodoSen(NodoSensor ns) {
		try {
			em.persist(ns);
			System.out.println("Insercion realizada...");
		} catch (Exception e) {
			System.out.println("Error al insertar Line:16, NodoDAO");
		}
	}
	
	public void updateNodoSen(NodoSensor ns) {
		try {
			em.merge(ns);
			System.out.println("Actualización correcta...");
		} catch (Exception e) {
			System.out.println("Error al actualizar Line:25, NodoDAO");
			
		}
	}
	
	public boolean eliminarNodoSen(int idNS) {
		NodoSensor ns= selectNodoSen(idNS);
		try {
			em.remove(ns);
			System.out.println("Eliminando "+ ns.getNodosen_id());
			return true;
		} catch (Exception e) {
			System.out.println("Error al eliminar");
			return false;
		}
	}
	
	public void grabarNodoSen(NodoSensor ns) {
		NodoSensor aux = selectNodoSen(ns.getNodosen_id());
		System.out.println("Grabando...");
		
		try {
			if(aux!=null) {
				System.out.println("Actualizando Nodo con el ID: "+ns.getNodosen_id());
				updateNodoSen(ns);
			}else {
				System.out.println("Insertando Nodo con el ID: "+ns.getNodosen_id());
				insertarNodoSen(ns);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public NodoSensor selectNodoSen(int idNS) {
		NodoSensor ns =em.find(NodoSensor.class, idNS);
		
		return ns;
	}
	
	public List<NodoSensor> listNodosSen(){
		String jpql ="Select ns from NodoSensor ns";
		TypedQuery<NodoSensor> query = em.createQuery(jpql, NodoSensor.class);
		List<NodoSensor> lnodosSen = query.getResultList();
		
		return lnodosSen;
	}
}
