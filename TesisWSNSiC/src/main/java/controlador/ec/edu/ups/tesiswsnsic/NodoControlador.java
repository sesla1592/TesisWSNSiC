package controlador.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.sound.midi.Soundbank;

import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;

@SessionScoped
@ManagedBean
public class NodoControlador {

	@Inject
	private NodoDAO nodoDAO;
	Nodo nodo;
	Nodo nodoSelected;
	
	private List<Nodo> ltsNodo;
	
	@PostConstruct
	public void init() {
		ltsNodo = nodoDAO.getAllNodos();
		nodo = new Nodo();	
	}
	
	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}

	public List<Nodo> getLtsNodo() {
		return ltsNodo;
	}

	public void setLtsNodo(List<Nodo> ltsNodo) {
		this.ltsNodo = ltsNodo;
	}

	public Nodo getNodoSelected() {
		return nodoSelected;
	}

	public void setNodoSelected(Nodo nodoSelected) {
		this.nodoSelected = nodoSelected;
	}

	public void crearNodo(){
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/nodo/crearNodo.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void guardarNodo(){
		try{
			System.out.println(nodo.toString());
			nodoDAO.insert(nodo);
			nodo = new Nodo();
			ltsNodo = nodoDAO.getAllNodos();
			FacesContext contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/nodo/listaNodos.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void editarNodo(Nodo nodo){
		setNodoSelected(nodo);
		System.out.println(nodoSelected.toString());
	}
	
	public void guardarEditar(){
		nodoDAO.update(nodoSelected);
	}
	
	public void irDetalles(Nodo nodoSelected){
		System.out.println("--> "+nodoSelected.toString());
		FacesContext contex = FacesContext.getCurrentInstance();
		contex.getExternalContext().getSessionMap().put("nodoSelected", nodoSelected);
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/nodo/detalleSensor.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
