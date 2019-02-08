package controlador.ec.edu.ups.tesiswsnsic;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;

@ManagedBean
public class BlogControlador {

	Persona user;
	Empresa empresa;
	Blog blog;
	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			empresa =  user.getEmpresa();
			System.out.println("empresa " + user.getEmpresa().toString());
			blog = empresa.getBlog();
			System.out.println("blog " + blog.toString());
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error la extraer usuario");
		}
	}
	
	public Blog getBlog() {
		return blog;
	}
	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
	
}
