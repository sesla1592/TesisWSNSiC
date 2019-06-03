package controlador.ec.edu.ups.tesiswsnsic;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import utilidades.ec.edu.ups.tesiswsnsic.SessionUtils;

@ManagedBean
public class BlogControlador {

	Persona user;
	Empresa empresa;
	Blog nuevoblog;
	List<Blog> ltsMyBlogs;
	
	@Inject
	BlogDAO blogDao;
	InputStream fileImag;
	
	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			empresa =  user.getEmpresa();
			System.out.println("empresa usuario" + user.getEmpresa().toString());
			nuevoblog = new Blog();
			ltsMyBlogs = blogDao.blogByEmpresa(user.getEmpresa().getId());
			System.out.println("blogs " + ltsMyBlogs.size());
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error la extraer usuario");
			e.printStackTrace();
		}
	}
	
	
//	public void actualizar() {
//		blogDao.update(blog);
//	}
	
	public Blog getNuevoblog() {
		return nuevoblog;
	}


	public void setNuevoblog(Blog nuevoblog) {
		this.nuevoblog = nuevoblog;
	}


	public List<Blog> getLtsMyBlogs() {
		return ltsMyBlogs;
	}


	public void setLtsMyBlogs(List<Blog> ltsMyBlogs) {
		this.ltsMyBlogs = ltsMyBlogs;
	}


	public void actualizar(Blog blog) {
		try {
//			byte [] bytes;
//			bytes = IOUtils.toByteArray(fileImag);
//	        // Store image to DB
//	        blog.setImagen(bytes);
	        blogDao.update(blog);
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Succesful", "Informacion Actualizada.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        
		}catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Error al guardar su informacion.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void crearBlog() {
		try {
//			byte [] bytes;
//			bytes = IOUtils.toByteArray(fileImag);
//	        // Store image to DB
//	        blog.setImagen(bytes);
			nuevoblog.setEmpresa(empresa);
	        blogDao.insert(nuevoblog);
	        nuevoblog = new Blog();
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Succesful", "Blog Creado.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        
	        
		}catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Error al crearblog.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	 public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        System.out.println("file "+event.getFile().getFileName());
	        //fileImag = event.getFile();
	        try {
	        	fileImag = event.getFile().getInputstream();
	        	byte [] bytes;
				bytes = IOUtils.toByteArray(event.getFile().getInputstream());
		        // Store image to DB
		        nuevoblog.setImagen(bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error al pasar imagen a input");
				e.printStackTrace();
				
			}
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
			 blogDao.remove(blog);
			 ltsMyBlogs = blogDao.blogByEmpresa(user.getEmpresa().getId());
				System.out.println("blogs " + ltsMyBlogs.size());
		 }catch (Exception e) {
			 e.printStackTrace();
			// TODO: handle exception
		}
	 }
}
