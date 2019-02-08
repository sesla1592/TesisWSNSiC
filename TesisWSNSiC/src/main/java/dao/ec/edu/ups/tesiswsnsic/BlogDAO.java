package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;

@Stateless
public class BlogDAO {

	@Inject
	private EntityManager em;
	
	public Blog insert(Blog blog){
		try {
			em.persist(blog);
			em.flush();
			return blog;
		} catch (Exception e) {
			System.out.println("error al insertar "+this.getClass().getName());
			// TODO: handle exception
			return null;
		}
	}
	
	public void update(Blog blog){
		try {
			em.merge(blog);
		} catch (Exception e) {
			System.out.println("error al actualizar "+this.getClass().getName());
			// TODO: handle exception
		}
	}
	
	public void remove(Blog blog){
		try {
			em.remove(blog);
		} catch (Exception e) {
			System.out.println("error al eliminar "+this.getClass().getName());
			// TODO: handle exception
		}
	}
	
	public List<Blog> allBlogs(){
		try {
			TypedQuery<Blog> query = em.createQuery(
					"Select b from Blog b where b.estado = true", Blog.class);
			//query.setParameter("codeNodo", codeNodo);
			List<Blog> ltsBlogs = query.getResultList();
			return ltsBlogs;
		} catch (Exception e) {
			e.printStackTrace();
			//e.printStackTrace();
			return null;
		}
	}
}
