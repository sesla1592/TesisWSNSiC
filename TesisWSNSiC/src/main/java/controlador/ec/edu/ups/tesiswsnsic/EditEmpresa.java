package controlador.ec.edu.ups.tesiswsnsic;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;

@ManagedBean
public class EditEmpresa {

	@Inject
	private EmpresaDAO empresaDAO;
	
	private Empresa empresa;
	
	@PostConstruct
	public void init() {
		empresa = (Empresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("empresaSelected");
		System.out.println("empresa s"+empresa.toString());
	}
	
	public void actualizar(){
		empresaDAO.updateEmpresa(empresa);
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/listaEmpresas.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
}
