package dao.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.ec.edu.ups.tesiswsnsic.Comentario;

@Stateless
public class ComentariosDAO {

	@Inject
	private EntityManager em;
	
	public boolean insert(Comentario comentario){
		try {
			em.persist(comentario);
			return true;
		} catch (Exception e) {
			System.out.println("error al insertar "+this.getClass().getName());
			// TODO: handle exception
			return false;
		}
	}
	
	public void update(Comentario comentario) {
		try {
			em.merge(comentario);
		}catch (Exception e) {
			System.out.println("error al actualizar "+this.getClass().getName());
			// TODO: handle exception
		}
	}
	
	public void delete(Comentario comentario) {
		try {
			em.remove(comentario);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error al eliminar "+this.getClass().getName());
		}
	}
	
	public List<Comentario> allComByBlog(int idBlog){
		try {
			TypedQuery<Comentario> query = em.createQuery(
					"Select c from Comentario c "
					+ "where c.blog.id = :idBlog order by c.id DESC", Comentario.class);
			query.setParameter("idBlog", idBlog);
			
			List<Comentario> ltsComentarios = query.getResultList();
			return ltsComentarios;
		} catch (Exception e) {
			e.printStackTrace();
			//e.printStackTrace();
			return null;
		}
	}
}
