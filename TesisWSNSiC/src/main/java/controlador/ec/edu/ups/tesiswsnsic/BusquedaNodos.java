package controlador.ec.edu.ups.tesiswsnsic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import modelo.ec.edu.ups.tesiswsnsic.NodoBuscado;

@ManagedBean
@ViewScoped
public class BusquedaNodos {
	private NodoBuscado nodobuscado;
	
//	private String pathpython = "C:\\Users\\rommel.inga\\git\\TesisWSNSiC\\TesisWSNSiC\\src\\main\\java\\controlador\\ec\\edu\\ups\\tesiswsnsic\\";
	private String pathpython = "\\WIN-1MUBU3QIN9E\\poolclientes";
	private String nspython = "test.py";		//ARCHIVO QUE DEBE ESTAR EN EL NODO CONTROLADOR
	

	private List<NodoBuscado> nodosbuscados;

	@PostConstruct
	public void init() {
		System.out.println("INICIALIZANDO..");
		nodobuscado = new NodoBuscado();
		nodosbuscados = new ArrayList<NodoBuscado>();
	}

	public NodoBuscado getNodobuscado() {
		return nodobuscado;
	}

	public void setNodobuscado(NodoBuscado nodobuscado) {
		this.nodobuscado = nodobuscado;
	}

	public List<NodoBuscado> getNodosbuscados() {
		return nodosbuscados;
	}

	public void setNodosbuscados(List<NodoBuscado> nodosbuscados) {
		this.nodosbuscados = nodosbuscados;
	}

}
