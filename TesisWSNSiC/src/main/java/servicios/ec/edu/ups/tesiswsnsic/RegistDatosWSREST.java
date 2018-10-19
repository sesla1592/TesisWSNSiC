package servicios.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//import modelo.ec.edu.ups.tesiswsnsic.NodoSensorDetallado;

@Path("/Registrodatos")
public class RegistDatosWSREST {

	 	
//	private NodoSensorDetallado nsd;
	//AQUI SE VA ALMACENAR LSO VALORES Y DEBO COLOCAR UN BUCLE PARA OBTENER TODOS LOS STRINGS

	/*
	 * http://localhost:8080/TesisWSNSiC/rs/Registrodatos/Registrar?nod_detalles=hollasdcsd
	 * */
	@Path("/Registrar")
	@GET 
	@Produces("application/json")
	@Consumes("application/json")
	public Respuesta obtenerDatos(@QueryParam("nod_detalles")String nod_detalles) {
		System.out.println("EXECUTING WS JAVAEE...");
		Respuesta r = new Respuesta();
		try {
				
			    String value = "\"+nod_detalles+\"";
				System.out.println("nodo_detalles ======" +nod_detalles);
				
			
			
			
			
			r.setMensaje("Envio correcto!");
		}catch(JSONException e) {
			r.setCodigo(-90);
			r.setMensaje("Error en envio");
			 e.printStackTrace();
		}
		return r;
	}

}
