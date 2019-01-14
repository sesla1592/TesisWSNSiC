package controlador.ec.edu.ups.tesiswsnsic;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import dao.ec.edu.ups.tesiswsnsic.TipoEmpresaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.TipoEmpresa;

@ManagedBean
public class EditTipoEmpresa {

	@Inject
	private TipoEmpresaDAO tipoEmoresaDAO;
	
	public TipoEmpresa tipoEmpresaSelected;
	
	@PostConstruct
	public void init() {
		tipoEmpresaSelected = (TipoEmpresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("empresaTipoSelected");
		System.out.println("typoempresa s"+tipoEmpresaSelected.toString());
	}
	
	public void actualizar(){
		tipoEmoresaDAO.update(tipoEmpresaSelected);
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/crearTipoEmpresa.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public TipoEmpresa getTipoEmpresaSelected() {
		return tipoEmpresaSelected;
	}

	public void setTipoEmpresaSelected(TipoEmpresa tipoEmpresaSelected) {
		this.tipoEmpresaSelected = tipoEmpresaSelected;
	}

	public void back() {
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/crearTipoEmpresa.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
