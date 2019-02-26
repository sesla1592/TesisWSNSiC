package controlador.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;

@ManagedBean
public class PublicBlog {

	@Inject
	BlogDAO blogDAO;
	
	List<Blog> ltsBlogs;
	
	@PostConstruct
	public void init() {
		ltsBlogs = blogDAO.allBlogs();
		for (int i = 0; i < ltsBlogs.size(); i++) {
			ltsBlogs.get(i).setImgBase64(new String(Base64.encodeBase64(ltsBlogs.get(i).getImagen())));
			//imageString= new String(Base64.encodeBase64(blog.getImagen()));
		}
		System.out.println(ltsBlogs.size());
	}

	public List<Blog> getLtsBlogs() {
		return ltsBlogs;
	}

	public void setLtsBlogs(List<Blog> ltsBlogs) {
		this.ltsBlogs = ltsBlogs;
	}
	
	public void irDetalle(Blog blog) {
		FacesContext contex = FacesContext.getCurrentInstance();
		contex.getExternalContext().getSessionMap().put("blogSelected", blog);
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/blog/blogDetalle.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
