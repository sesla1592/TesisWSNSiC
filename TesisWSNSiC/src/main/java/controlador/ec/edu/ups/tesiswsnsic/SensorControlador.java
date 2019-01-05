package controlador.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.SensorDAO;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;

@ViewScoped
@ManagedBean
public class SensorControlador {
	
	@Inject
	private SensorDAO senDAO;
	
	Nodo nodo;
	private Sensor sensor;
	private Sensor sensorSelected;	
	private List<Sensor> ltsSensor;
	
	
	@PostConstruct
	public void init() {
		nodo = (Nodo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("nodoSelected");
		System.out.println("nodo s"+nodo.toString());
		sensor = new Sensor();
		ltsSensor=senDAO.getLtsSensorByNodo(nodo.getId());
		
	}

	public Nodo getNodo() {
		return nodo;
	}


	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}


	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public List<Sensor> getLtsSensor() {
		return ltsSensor;
	}

	public void setLtsSensor(List<Sensor> ltsSensor) {
		this.ltsSensor = ltsSensor;
	}
	
	public Sensor getSensorSelected() {
		return sensorSelected;
	}

	public void setSensorSelected(Sensor sensorSelected) {
		this.sensorSelected = sensorSelected;
	}

	public void irCrearSensor(){
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/nodo/crearSensor.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void guardarSensor(){
		try{
			System.out.println(sensor.toString());
			sensor.setNodo(nodo);
			senDAO.insertarSensor(sensor);
			sensor = new Sensor();
			ltsSensor = senDAO.getLtsSensorByNodo(nodo.getId());
			FacesContext contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/nodo/detalleSensor.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void editarSensor(Sensor sensor){
		setSensorSelected(sensor);
		System.out.println(sensorSelected.toString());
	}
	
	public void guardarEditar(){
		senDAO.updateSensor(sensorSelected);
	}
}
