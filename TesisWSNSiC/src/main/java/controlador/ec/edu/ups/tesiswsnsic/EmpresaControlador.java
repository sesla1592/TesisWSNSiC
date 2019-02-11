package controlador.ec.edu.ups.tesiswsnsic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaDAO;
import dao.ec.edu.ups.tesiswsnsic.TipoEmpresaDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.TipoEmpresa;

@ManagedBean
public class EmpresaControlador {
	
	@Inject
	private EmpresaDAO empresaDAO;
	
	@Inject
	private TipoEmpresaDAO tipoEmpresaDAO;
	
	private Empresa empresa;
	
	private List<TipoEmpresa> tipoempresas;
	private TipoEmpresa tipoEmpSelected;
	private List<Empresa> ltsEmpresa;
	private String selecttemp;
	
	private Persona user;
	
	@Inject
	BlogDAO blogDAO;
	
	@Inject
	PersonaDAO personaDAO;

	@PostConstruct
	public void init() {
		empresa = new Empresa();
		tipoempresas = tipoEmpresaDAO.listTipoEmpresa();
		//System.out.println(tipoempresas.size());
		listarEmpresa();
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("userSelected");
			System.out.println("user "+user.toString());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void leerEmpresa() {

	}

	public void editarEmpresa() {

	}

	public void listarEmpresa() {
		ltsEmpresa = empresaDAO.getAllEmpresas();
		System.out.println("tam lts----> "+ltsEmpresa.size());
//		for (int i = 0; i < ltsEmpresa.size(); i++) {
//			System.out.println("emp "+ltsEmpresa.get(i).toString());
//		}
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

	
	public TipoEmpresa getTipoEmpSelected() {
		return tipoEmpSelected;
	}

	public void setTipoEmpSelected(TipoEmpresa tipoEmpSelected) {
		this.tipoEmpSelected = tipoEmpSelected;
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
		tipoempresas = tipoEmpresaDAO.listTipoEmpresa();
		for(int i=0; i < tipoempresas.size(); i++){
			ltipoempresas.add(new SelectItem(tipoempresas.get(i).getDescripcion()));
		}
		return ltipoempresas;
	}
	///////codigo de paul
	
	public void crearEmpresa(){
		try {
			if(tipoEmpSelected!=null){
				empresa.setTipoempresa(tipoEmpSelected);
			}else{
				tipoEmpSelected = tipoempresas.get(0);
				empresa.setTipoempresa(tipoEmpSelected);
			}
			Date date = new Date();
			DateFormat fechaPub = new SimpleDateFormat("dd/MM/yyyy");
			String fechaPublicacion = fechaPub.format(date);
			
			Blog blog = new Blog();
			blog.setVisitas(0);
			blog.setFechaPub(fechaPublicacion);
			blog = blogDAO.insert(blog);
			empresa.setBlog(blog);
			empresa.setEstado("activo");
			empresa = empresaDAO.insertEmpresa(empresa);
			System.out.println("em ->"+empresa.toString());
			blog.setEmpresa(empresa);
			blogDAO.update(blog);
			user.setEmpresa(empresa);
			personaDAO.updatePersona(user);
			
			FacesContext contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/user/dashboard.xhtml");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TipoEmpresa getLanguageDiff(Integer id) {
		if (id == null){
            throw new IllegalArgumentException("no id provided");
        }
        for (TipoEmpresa languageDiff : tipoempresas){
            if (id.equals(languageDiff.getId())){
                return languageDiff;
            }
        }
        return null;
	}

	public List<Empresa> getLtsEmpresa() {
		return ltsEmpresa;
	}

	public void setLtsEmpresa(List<Empresa> ltsEmpresa) {
		this.ltsEmpresa = ltsEmpresa;
	}
	
	public void editEmpresa(Empresa empresa){
		System.out.println("entro a editar "+empresa.toString());
		FacesContext contex = FacesContext.getCurrentInstance();
		contex.getExternalContext().getSessionMap().put("empresaSelected", empresa);
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/editarEmpresa.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void changeStatusEmpresa(Empresa empresa) {
		if(empresa.getEstado().equals("activo")) {
			empresa.setEstado("inactivo");
		}else {
			empresa.setEstado("activo");
		}
		
		empresaDAO.updateEmpresa(empresa);
	}
	
	public void backAdmin(){
		FacesContext contex = FacesContext.getCurrentInstance();
		contex.getExternalContext().getSessionMap().put("empresaSelected", empresa);
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/dashboard.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
