package controlador.ec.edu.ups.tesiswsnsic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import dao.ec.edu.ups.tesiswsnsic.SensorDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;

@ManagedBean
@ViewScoped
public class DashboardAdmin {
	
	private String elibre;
	private String eutilizado;
	private String etotal;
	
	Persona user;
	FacesContext contex;
	
	int 	nodoActivo=0;
	int nodoInactivo=0;
	int sensorActivo=0;
	int sensorInactivo=0;
	int empresaActiva=0;
	int empresaInactiva=0;
	
	List<Nodo> ltsNodo;
	List<Sensor> ltsSensor;
	List<Empresa> ltsEmpresa;
	Marker marker;
	Nodo nodoSelected;
	public MapModel simpleModel;
	
	@Inject
	NodoDAO nodoDAO;
	@Inject
	SensorDAO sensorDAO;
	@Inject
	EmpresaDAO empresaDAO;
	
	@PostConstruct
	public void init() {
		calculaEspaciosa();
		try {
			
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("userSelected");
			System.out.println("user "+user.toString());
			//cantidad de nodos
			ltsNodo = nodoDAO.getAllNodos();
			nodoActivo=ltsNodo.size();
			simpleModel = new DefaultMapModel();
			//cantidad de sensores
			ltsSensor = sensorDAO.getAllSensor();
			for (int i = 0; i < ltsSensor.size(); i++) {
				if(ltsSensor.get(i).getEstado() == true) {
					sensorActivo++;
				}else {
					sensorInactivo++;
				}
			}
			//empresas
			
			ltsEmpresa = empresaDAO.getAllEmpresas();
			for (int i = 0; i < ltsEmpresa.size(); i++) {
				if(ltsEmpresa.get(i).getEstado().equals("activo")) {
					empresaActiva++;
				}else {
					empresaInactiva++;
				}
			}
			addMarker();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	public Persona getUser() {
		return user;
	}


	public void setUser(Persona user) {
		this.user = user;
	}


	public int getNodoInactivo() {
		return nodoInactivo;
	}


	public void setNodoInactivo(int nodoInactivo) {
		this.nodoInactivo = nodoInactivo;
	}


	public int getSensorActivo() {
		return sensorActivo;
	}


	public void setSensorActivo(int sensorActivo) {
		this.sensorActivo = sensorActivo;
	}


	public int getSensorInactivo() {
		return sensorInactivo;
	}


	public void setSensorInactivo(int sensorInactivo) {
		this.sensorInactivo = sensorInactivo;
	}


	public int getNodoActivo() {
		return nodoActivo;
	}


	public void setNodoActivo(int nodoActivo) {
		this.nodoActivo = nodoActivo;
	}


	public int getEmpresaActiva() {
		return empresaActiva;
	}


	public void setEmpresaActiva(int empresaActiva) {
		this.empresaActiva = empresaActiva;
	}


	public int getEmpresaInactiva() {
		return empresaInactiva;
	}


	public void setEmpresaInactiva(int empresaInactiva) {
		this.empresaInactiva = empresaInactiva;
	}


	public List<Nodo> getLtsNodo() {
		return ltsNodo;
	}


	public void setLtsNodo(List<Nodo> ltsNodo) {
		this.ltsNodo = ltsNodo;
	}


	public List<Sensor> getLtsSensor() {
		return ltsSensor;
	}


	public void setLtsSensor(List<Sensor> ltsSensor) {
		this.ltsSensor = ltsSensor;
	}


	public List<Empresa> getLtsEmpresa() {
		return ltsEmpresa;
	}


	public void setLtsEmpresa(List<Empresa> ltsEmpresa) {
		this.ltsEmpresa = ltsEmpresa;
	}


	public void crearTipoEmpresa() {
		try{
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/crearTipoEmpresa.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void empresas() {
		try{
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/listaEmpresas.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void nodos() {
		try{
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/nodo/listaNodos.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public String getElibre() {
		return elibre;
	}

	public void setElibre(String elibre) {
		this.elibre = elibre;
	}

	public String getEutilizado() {
		return eutilizado;
	}

	public void setEutilizado(String eutilizado) {
		this.eutilizado = eutilizado;
	}

	public String getEtotal() {
		return etotal;
	}


	public void setEtotal(String etotal) {
		this.etotal = etotal;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}


	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public void addMarker() {
		for (int i = 0; i < ltsNodo.size(); i++) {
			Marker marker = new Marker(new LatLng(ltsNodo.get(i).getLatitud(), ltsNodo.get(i).getLongitud()),
					ltsNodo.get(i).getIdentificador());
			simpleModel.addOverlay(marker);
		}
	}

	public void calculaEspaciosa() {
		File file = new File("C:");
		// Total espacio en disco (Bytes).
        long total = file.getTotalSpace();
        // Espacio libre en disco (Bytes).
        long libre = file.getFreeSpace(); //unallocate
        
        long elib =libre / 1024 / 1024;
        long etot = total / 1024 / 1024;
        long euti = etot - elib;
        
        elibre = String.valueOf(elib)+" MB";
        etotal = String.valueOf(etot)+" MB";
        eutilizado = String.valueOf(euti)+" MB";
	}
	
	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
		nodoSelected = findNodo(marker.getTitle());
		System.out.println("Nodo Selected" + nodoSelected.toString() +" sensores: "+ nodoSelected.getLtssensores().size());
		
	}
	
	public Nodo findNodo(String identificador) {
		Nodo nodo = new Nodo();
		for (int i = 0; i < ltsNodo.size(); i++) {
			if (ltsNodo.get(i).getIdentificador().equals(identificador)) {
				nodo = ltsNodo.get(i);
				break;
			}
		}
		return nodo;
	}
}
