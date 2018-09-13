package controlador.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import modelo.ec.edu.ups.tesiswsnsic.Nodo;

@ManagedBean
public class NodoControlador {


	private List<Nodo> nodos;
	
	@PostConstruct
	public void init() {
		nodos = new ArrayList<Nodo>();
	}
	
	
	//DEBE MANDAR LACONSULTA AL DAO NOSQL, DE AQUI SE DEBE VERIFICAR LA DETECCION DEL LOS NODOS
	public List<Nodo> buscarNodos(){
		
		return nodos;
	}
}
