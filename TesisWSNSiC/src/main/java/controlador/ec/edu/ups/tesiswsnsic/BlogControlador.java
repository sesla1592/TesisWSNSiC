package controlador.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;

@ManagedBean
public class BlogControlador {
	
	@Inject
	BlogDAO blogDAO;
	
	Persona user;
	Empresa empresa;
	
	List<Blog> ltsMyBlogs;
	
	@PostConstruct
	public void init() {
		user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
		empresa =  user.getEmpresa();
		ltsMyBlogs = blogDAO.blogByEmpresa(empresa.getId());
	}

	public List<Blog> getLtsMyBlogs() {
		return ltsMyBlogs;
	}

	public void setLtsMyBlogs(List<Blog> ltsMyBlogs) {
		this.ltsMyBlogs = ltsMyBlogs;
	}
	
	
}
