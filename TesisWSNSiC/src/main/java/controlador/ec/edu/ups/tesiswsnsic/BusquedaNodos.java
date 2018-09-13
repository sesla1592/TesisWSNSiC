package controlador.ec.edu.ups.tesiswsnsic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.ec.edu.ups.tesiswsnsic.NodoBuscado;

@ManagedBean
@ViewScoped
public class BusquedaNodos {
	private NodoBuscado nodobuscado;

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
		System.out.println("PASO - GENERAR");
		System.out.println("Nodo a buscar: " + nodobuscado.getIpv4());
		System.out.println("Generando archivo de texto...");
		try {
			flwriter = new FileWriter("ips.txt");
			BufferedWriter bfwriter = new BufferedWriter(flwriter);
			bfwriter.write(nodobuscado.getIpv4());
			bfwriter.close();
			System.out.println("Generacion Satisfactoria...!");
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
