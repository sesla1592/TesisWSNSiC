package controlador.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
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
	List<Blog> ltsBlogsDestacados = new ArrayList<Blog>();
	
	@PostConstruct
	public void init() {
		ltsBlogs = blogDAO.allBlogs();
		for (int i = 0; i < ltsBlogs.size(); i++) {
			try {
				ltsBlogs.get(i).setImgBase64(new String(Base64.encodeBase64(ltsBlogs.get(i).getImagen())));
			}catch (Exception e) {
				ltsBlogs.get(i).setImgBase64("");
				// TODO: handle exception
			}
			//imageString= new String(Base64.encodeBase64(blog.getImagen()));
		}
		System.out.println(ltsBlogs.size());
		
		ltsBlogsDestacados = blogDAO.bestBlogs();
		for (int i = 0; i < ltsBlogsDestacados.size(); i++) {
			ltsBlogsDestacados.get(i).setImgBase64(new String(Base64.encodeBase64(ltsBlogsDestacados.get(i).getImagen())));
			//ltsBlogsDestacados= new String(Base64.encodeBase64(blog.getImagen()));
		}
		System.out.println(ltsBlogsDestacados.size());
	}

	public List<Blog> getLtsBlogs() {
		return ltsBlogs;
	}

	public void setLtsBlogs(List<Blog> ltsBlogs) {
		this.ltsBlogs = ltsBlogs;
	}
	
	public List<Blog> getLtsBlogsDestacados() {
		return ltsBlogsDestacados;
	}

	public void setLtsBlogsDestacados(List<Blog> ltsBlogsDestacados) {
		this.ltsBlogsDestacados = ltsBlogsDestacados;
	}

	public void irDetalle(Blog blog) {
		FacesContext contex = FacesContext.getCurrentInstance();
		contex.getExternalContext().getSessionMap().put("blogSelected", blog);
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/blog/blogDetalle.xhtml?idBlog="+blog.getId());
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
