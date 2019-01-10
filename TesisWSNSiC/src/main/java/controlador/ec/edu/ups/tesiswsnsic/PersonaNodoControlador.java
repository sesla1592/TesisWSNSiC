package controlador.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaNodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.PersonaNodo;

@ManagedBean
@RequestScoped
public class PersonaNodoControlador {

	Persona user;
	List<Nodo> ltsMyNodos=new ArrayList<>();
	List<Nodo> ltsAllNodos= new ArrayList<>();
	
	@Inject
	PersonaNodoDAO personaNodoDAO;
	
	@Inject
	NodoDAO nodoDAO;
	
	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("userSelected");
			System.out.println("user "+user.getId());
			ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
			ltsAllNodos = nodoDAO.getAllNodos();
			for (int i = 0; i < ltsAllNodos.size(); i++) {
				for (int j = 0; j < ltsMyNodos.size(); j++) {
					if(ltsAllNodos.get(i).getId()==ltsMyNodos.get(j).getId()) {
						ltsAllNodos.remove(i);
					}
				}
			}
			System.out.println("lista nodos"+ltsMyNodos.size());
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List<Nodo> getLtsMyNodos() {
		return ltsMyNodos;
	}

	public void setLtsMyNodos(List<Nodo> ltsMyNodos) {
		this.ltsMyNodos = ltsMyNodos;
	}
	
	public List<Nodo> getLtsAllNodos() {
		return ltsAllNodos;
	}

	public void setLtsAllNodos(List<Nodo> ltsAllNodos) {
		this.ltsAllNodos = ltsAllNodos;
	}

	public void asociarNodo(Nodo nodo) {
		System.out.println("nodo "+nodo.getNombre());
		PersonaNodo personaNodo = new PersonaNodo();
		personaNodo.setEstado("true");
		personaNodo.setNodo(nodo);
		personaNodo.setPersona(user);
		personaNodoDAO.insertPersonaNodo(personaNodo);
		ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
	}
	
	public void eliminarAsoNodo() {
		
	}
}
