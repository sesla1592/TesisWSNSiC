package controlador.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import dao.ec.edu.ups.tesiswsnsic.TipoEmpresaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.TipoEmpresa;

@ManagedBean
public class EmpresaControlador {
	
	@Inject
	private EmpresaDAO edao;
	
	@Inject
	private TipoEmpresaDAO tedao;
	
	private Empresa empresa;
	
	private List<TipoEmpresa> tipoempresas;
	
	private String selecttemp;

	@PostConstruct
	public void init() {
		empresa = new Empresa();
		tipoempresas = new ArrayList<TipoEmpresa>();
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

	public List<TipoEmpresa> getTipoempresas() {
		return tipoempresas;
	}

	public void setTipoempresas(List<TipoEmpresa> tipoempresas) {
		this.tipoempresas = tipoempresas;
	}

	public String getSelecttemp() {
		return selecttemp;
	}

	public void setSelecttemp(String selecttemp) {
		this.selecttemp = selecttemp;
	}

	public void actualizaEmpresaAPersona() {
		Persona p = PersonaControlador.miUsuario;
		System.out.println("ID: "+p.getId() +"  Nombre:"+p.getNombre());
		edao.updateEmpresaPersona(empresa, p.getId());
	}
	
	public static TipoEmpresa tem = new TipoEmpresa();
	public void tipoSeleccionada(){
		System.out.println("SELECCCIONOOOOOOOOOOOOOOOOOOOOOO");
		for(TipoEmpresa tempre : tipoempresas){
			try {
				if(tempre.getDescripcion().equals(selecttemp)) {
					empresa.setTipoempresa(tempre);
					EmpresaControlador.tem = tempre;
				}
			}catch (Exception e) {
				System.out.println("No existe tipos de empresa:" +e.getMessage());
			}
		}
	}
	
	public List<SelectItem> devuelveLista(){
		List<SelectItem> ltipoempresas = new ArrayList<SelectItem>();
		tipoempresas = tedao.listTipoEmpresa();
		for(int i=0; i < tipoempresas.size(); i++){
			ltipoempresas.add(new SelectItem(tipoempresas.get(i).getDescripcion()));
		}
		return ltipoempresas;
	}
	
}
