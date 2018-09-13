package controlador.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;



import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import mongodb.ec.edu.ups.tesiswsnsic.MongoConnectionDB;

@ManagedBean
@SessionScoped
public class NodoControlador {

	
	@Inject
	private NodoDAO nod;
	
	private Nodo nodos;
	
	private MongoConnectionDB conexion;
	
	@PostConstruct
	public void init() {
		
		nodos = new Nodo();
	}
	
	public void insertarNodo() {
		nod.guardarNodo(nodos);
	}
	
	public  void obtenerColeccion() {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("correo").is("@gmail.com"));
	
	}
	
	
	
	
}
