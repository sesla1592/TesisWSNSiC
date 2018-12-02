package controlador.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.SensorDAO;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;

@ManagedBean
public class SensorControlador {
	
	@Inject
	private SensorDAO senDAO;
	
	private Sensor sensor;
	private List<Sensor> milistSensores = null;
	
	
	@PostConstruct
	public void init() {
		
		sensor = new Sensor();
		
		
	}

	public SensorDAO getSenDAO() {
		return senDAO;
	}

	public void setSenDAO(SensorDAO senDAO) {
		this.senDAO = senDAO;
	}


	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public List<Sensor> getMilistSensores() {
		return milistSensores;
	}

	public void setMilistSensores(List<Sensor> milistSensores) {
		this.milistSensores = milistSensores;
	}

	public List<Sensor> getListSensor(){
		if(milistSensores==null) {
			milistSensores=senDAO.listSensor();
		}
		return milistSensores;
	}
	
}
