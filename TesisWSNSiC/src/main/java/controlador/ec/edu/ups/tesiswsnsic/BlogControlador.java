package controlador.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import utilidades.ec.edu.ups.tesiswsnsic.SessionUtils;

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
	

	 public void irEditar(Blog blog) {
		 try{
				HttpSession session = SessionUtils.getSession();
				session.setAttribute("blogEdit", blog);
				FacesContext contex = FacesContext.getCurrentInstance();
				contex.getExternalContext().redirect("/TesisWSNSiC/faces/user/editBlog.xhtml");
			}catch (Exception e) {
				// TODO: handle exception
			}
	 }
	 
	 public void eliminarBlog(Blog blog) {
		 try {
			 blogDAO.remove(blog);
			 ltsMyBlogs = blogDAO.blogByEmpresa(user.getEmpresa().getId());
				System.out.println("blogs " + ltsMyBlogs.size());
		 }catch (Exception e) {
			 e.printStackTrace();
			// TODO: handle exception
		}
	 }
}
