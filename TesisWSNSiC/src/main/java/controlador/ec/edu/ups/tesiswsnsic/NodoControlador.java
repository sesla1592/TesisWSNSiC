package controlador.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.NodoSensor;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;

@ManagedBean
public class NodoControlador {

	@Inject
	private NodoDAO ndao;
	
	
	private List<Nodo> milist=null;
	private Nodo nodo;
	
	@PostConstruct
	public void init() {
		milist = new ArrayList<Nodo>();
		nodo = new Nodo();
		
		
	}
	protected void initializeEmbeddableKey() {
    }

	
	public Nodo prepareCreate() {
		nodo = new Nodo();
		 initializeEmbeddableKey();
		 return nodo;
	}
	
	
	public NodoDAO getNdao() {
		return ndao;
	}

	public void setNdao(NodoDAO ndao) {
		this.ndao = ndao;
	}

	public List<Nodo> getMilist() {
		return milist;
	}

	public void setMilist(List<Nodo> milist) {
		this.milist = milist;
	}



	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}


	public static Nodo nod = new Nodo();
	public static Sensor sor = new Sensor();
	
	public List<Nodo> getItems(){
		
		if(milist==null) {
			milist=ndao.listNodos();
		}
		return milist;
	}
	
	public String guardarNodo() {
		
		if(nodo!=null) {
			try {
				ndao.grabarNodo(nodo);
				System.out.println("Insertado exitosamente...");
				
			} catch (Exception e) {
				System.out.println("Nodo se ha insertado exitosamente...");
				
			}
		}
		
	return null;
	}
		
	public void addNodoSensor() {
		System.out.println("entry");
		NodoSensor ns = new NodoSensor();
		ns.setNodoID(nodo);
		nodo.getNodosensores().add(ns);
	}
	
	
}
