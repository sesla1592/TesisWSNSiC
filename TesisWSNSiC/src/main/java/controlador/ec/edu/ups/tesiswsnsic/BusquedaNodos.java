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

	public void generaArchivoNodos() {
		FileWriter flwriter = null;
		System.out.println("Nodo a buscar: " + nodobuscado.getIpv4());
		try {
			
			flwriter = new FileWriter("C:\\Users\\rommel.inga\\git\\TesisWSNSiC\\TesisWSNSiC\\src\\main\\java\\controlador\\ec\\edu\\ups\\tesiswsnsic\\poolclientes\\"+PersonaControlador.miEmpresa+".txt");
//			flwriter = new FileWriter("\\WIN-1MUBU3QIN9E\\poolclientes\\"+PersonaControlador.miEmpresa+".txt");
			BufferedWriter bfwriter = new BufferedWriter(flwriter);
			bfwriter.write(nodobuscado.getIpv4());
			bfwriter.close();
			System.out.println("Generacion IPS Satisfactoria...!");
			//EJECUTA LINEA PARA PYTHON CON ARCHIVO, ALMOMENTO COLOCAR EN LA MISMA CARPETA DE ARCHIVOS
			//CONSIGUIENTE PYTHON VA SER EJECUTADO DESDE UN RASPBERRY Y PARA EJECUTARLO DEBE SER
			//ALGO ASI Runtime.getRuntime().exec("/usr/local/bin/python example.py") SOLO QUE COLOCANDO LA  IP
			
			try{
				//EXECUTE MEDIANTE WEB SERVICE CLIENTE, SEGUN LA DIRECCION IP DEL NODO CONTROLADOR, W7 EL ARCHIVO DEBE HABER
				//UN WS O "BROKER"
				System.out.println("EXECUTING SCRIPT PYTHON . . . .");
				PythonInterpreter python = new PythonInterpreter();
				python.execfile(pathpython+nspython);
			}catch(Exception e) {
				e.printStackTrace();
			}
			//ELIMINA EL ARCHIVO LUEGO QUE PYTHON RESPONDA Y CONOZCA EL USUARIO REFERENTE AL ESTADO DE SUS NODOS
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (flwriter != null) {
				try {
					flwriter.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

}
