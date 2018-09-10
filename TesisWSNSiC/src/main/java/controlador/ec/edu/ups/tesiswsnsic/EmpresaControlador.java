package controlador.ec.edu.ups.tesiswsnsic;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;

@ManagedBean
public class EmpresaControlador {
	
	@Inject
	private EmpresaDAO edao;
	
	private Empresa empresa;

	@PostConstruct
	public void init() {
		empresa = new Empresa();
	}

	public void crearEmpresa() {

	}

	public void leerEmpresa() {

	}

	public void editarEmpresa() {

	}

	public void listarEmpresa() {

	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	

	public void actualizaEmpresaAPersona() {
		Persona p = PersonaControlador.miUsuario;
		System.out.println("ID: "+p.getId() +"  Nombre:"+p.getNombre());
		edao.updateEmpresaPersona(empresa, p.getId());
	}
	
}
