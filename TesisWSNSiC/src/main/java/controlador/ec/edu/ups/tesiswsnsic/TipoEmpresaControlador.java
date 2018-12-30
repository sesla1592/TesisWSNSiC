package controlador.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.TipoEmpresaDAO;
import modelo.ec.edu.ups.tesiswsnsic.TipoEmpresa;
@SessionScoped
@ManagedBean
public class TipoEmpresaControlador {

	@Inject
	private TipoEmpresaDAO tipoEmpDAO;
	
	public TipoEmpresa tipoEmpresa;
	
	public List<TipoEmpresa> ltsTipoEmpresa;
	
	@PostConstruct
	public void init() {
		tipoEmpresa = new TipoEmpresa();
		ltsTipoEmpresa = tipoEmpDAO.listTipoEmpresa();
	}
	
	public void crearTipoEmpresa(){
		try{
			System.out.println(tipoEmpresa.toString());
			tipoEmpDAO.insert(tipoEmpresa);
			ltsTipoEmpresa = tipoEmpDAO.listTipoEmpresa();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editEmpresa(TipoEmpresa tipoEmpresa){
		try{
			FacesContext contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().getSessionMap().put("empresaTipoSelected", tipoEmpresa);
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/editarTipoEmpresa.xhtml");
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public TipoEmpresa getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(TipoEmpresa tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public List<TipoEmpresa> getLtsTipoEmpresa() {
		return ltsTipoEmpresa;
	}

	public void setLtsTipoEmpresa(List<TipoEmpresa> ltsTipoEmpresa) {
		this.ltsTipoEmpresa = ltsTipoEmpresa;
	}
	
	
}
