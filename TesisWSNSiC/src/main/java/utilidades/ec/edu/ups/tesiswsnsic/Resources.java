package utilidades.ec.edu.ups.tesiswsnsic;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// TODO: Auto-generated Javadoc
/**
 * The Class Resources.
 */
public class Resources {

	   /** The em. */
	   @Produces
	   @PersistenceContext
	   private EntityManager em;
	   
	   /**
	    * Produce log.
	    *
	    * @param injectionPoint the injection point
	    * @return the logger
	    */
	   @Produces
	   public Logger produceLog(InjectionPoint injectionPoint) {
	      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	   }
	   
	   /**
	    * Produce faces context.
	    *
	    * @return the faces context
	    */
	   @Produces
	   @RequestScoped
	   public FacesContext produceFacesContext() {
	      return FacesContext.getCurrentInstance();
	   }
	   

}
