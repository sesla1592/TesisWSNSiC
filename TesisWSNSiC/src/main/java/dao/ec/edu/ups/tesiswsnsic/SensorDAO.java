package dao.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


import modelo.ec.edu.ups.tesiswsnsic.Sensor;

@Stateless
public class SensorDAO {

	@Inject
	private EntityManager em;

	public void insertarSensor(Sensor s) {
		try {
			em.persist(s);
			System.out.println("Insercion realizada...");
		} catch (Exception e) {
			System.out.println("Error al insertar Line:14, SensorDAO");
		}
	}

	public void updateSensor(Sensor s) {
		try {
			em.merge(s);
			System.out.println("Actualización correcta...");
		} catch (Exception e) {
			System.out.println("Error al actualizar Line:25, SensorDAO");

		}
	}

	public boolean eliminarSensor(int idS) {
		Sensor sen = selectSensor(idS);
		try {
			em.remove(sen);
			System.out.println("Eliminando " + sen.getId());
			return true;
		} catch (Exception e) {
			System.out.println("Error al eliminar");
			return false;
		}
	}

	public void grabarSensor(Sensor s) {
		Sensor aux = selectSensor(s.getId());
		System.out.println("Grabando...");

		try {
			if (aux != null) {
				System.out.println("Actualizando Nodo con el ID: " + s.getId());
				updateSensor(s);
			} else {
				System.out.println("Insertando Nodo con el ID: " + s.getId());
				insertarSensor(s);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Sensor selectSensor(int idS) {
		Sensor se = em.find(Sensor.class, idS);

		System.out.println("Iniciando LAZY.. " + "Line:64, SensorDAO.java");
		// Iniciando lazy
		//if (se.getSensorNod().isEmpty()) {
		//	se.getSensorNod().size();
		//}
		return se;
	}
	
	public List<Sensor> listSensor(){
		String jpql ="Select s from Sensor s";
		TypedQuery<Sensor> query = em.createQuery(jpql, Sensor.class);
		List<Sensor> lsensores = query.getResultList();
		for(Sensor sen: lsensores) {
			//if(!sen.getSensorNod().isEmpty()) {
			//	sen.getSensorNod().size();
			//}
		}
		return lsensores;
	}
	
	////paul
	
	public List<Sensor> getAllSensor(){
		TypedQuery<Sensor> query = em.createQuery("Select s from Sensor s", Sensor.class);
		
		try{
			List<Sensor> lsensores = query.getResultList();
			return lsensores;
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error getLtsSensorByNodo - "+ this.getClass().getName());
			e.printStackTrace();
			List<Sensor> lsensores = new ArrayList<>();
			return lsensores;
		}
		
	}
	
	public List<Sensor> getLtsSensorByNodo(int idNodo){
		System.out.println("llego "+idNodo);
		TypedQuery<Sensor> query = em.createQuery("Select s from Sensor s where s.nodo.id = :idNodo", Sensor.class);
		query.setParameter("idNodo", idNodo);
		
		try{
			List<Sensor> lsensores = query.getResultList();
			return lsensores;
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error getLtsSensorByNodo - "+ this.getClass().getName());
			e.printStackTrace();
			List<Sensor> lsensores = new ArrayList<>();
			return lsensores;
		}
		
	}
}
