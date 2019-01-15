package controlador.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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
	
	@Inject
	NodoDAO nodoDAO;
	@Inject
	SensorDAO sensorDAO;
	@Inject
	EmpresaDAO empresaDAO;
	
	@PostConstruct
	public void init() {
		
		try {
			
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("userSelected");
			System.out.println("user "+user.toString());
			//cantidad de nodos
			ltsNodo = nodoDAO.getAllNodos();
			nodoActivo=ltsNodo.size();
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
}