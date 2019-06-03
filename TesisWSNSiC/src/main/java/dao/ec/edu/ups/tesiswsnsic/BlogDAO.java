package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Blog;

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
			em.remove(em.contains(blog) ? blog : em.merge(blog));
		} catch (Exception e) {
			System.out.println("error al eliminar "+this.getClass().getName());
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public Blog findById(int idblog){
		try {
			return em.find(Blog.class, idblog);
		} catch (Exception e) {
			System.out.println("error al eliminar "+this.getClass().getName());
			// TODO: handle exception
			return null;
		}
	}
	
	public List<Blog> allBlogs(){
		try {
			TypedQuery<Blog> query = em.createQuery(
					"Select b from Blog b where b.estado = true order by b.visitas DESC", Blog.class);
			//query.setParameter("codeNodo", codeNodo);
			query.setMaxResults(10);
			List<Blog> ltsBlogs = query.getResultList();
			return ltsBlogs;
		} catch (Exception e) {
			e.printStackTrace();
			//e.printStackTrace();
			return null;
		}
	}
	public List<Blog> bestBlogs(){
		try {
			TypedQuery<Blog> query = em.createQuery(
					"Select b from Blog b where b.estado = true order by b.visitas DESC", Blog.class);
			//query.setParameter("codeNodo", codeNodo);
			query.setMaxResults(3);
			List<Blog> ltsBlogs = query.getResultList();
			return ltsBlogs;
		} catch (Exception e) {
			e.printStackTrace();
			//e.printStackTrace();
			return null;
		}
	}
	
	public List<Blog> blogByEmpresa(int idEmpresa){
		try {
			TypedQuery<Blog> query = em.createQuery(
					"Select b from Blog b where b.estado = true and empresa.id = :idEmpresa", Blog.class);
			query.setParameter("idEmpresa", idEmpresa);
			
			List<Blog> ltsBlogs = query.getResultList();
			return ltsBlogs;
		} catch (Exception e) {
			e.printStackTrace();
			//e.printStackTrace();
			return null;
		}
	}
}
