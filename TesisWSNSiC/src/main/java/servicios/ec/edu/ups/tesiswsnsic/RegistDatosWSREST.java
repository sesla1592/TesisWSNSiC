package servicios.ec.edu.ups.tesiswsnsic;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;

import mongodb.ec.edu.ups.tesiswsnsic.MongoConnectionDB;


@Path("/Registrodatos")
public class RegistDatosWSREST {


	/*
	 * http://localhost:8080/TesisWSNSiC/rs/Registrodatos/Registrar?nod_detalles=hollasdcsd
	 * */
	
	@Path("/Registrar")
	@GET 
	@Produces("application/json")
	@Consumes("application/json")
	public Respuesta obtenerDatos(@QueryParam("nod_detalles")String nod_detalles ) {

		MongoConnectionDB mongo = new MongoConnectionDB();
		
		System.out.println("EXECUTING WS JAVAEE...");
		Respuesta r = new Respuesta();
		try {
			//String comillas="\"";
			//String backslash ="\\";	
			 //nod_detalles= "\""+"\""+nod_detalles+"\""+"\"";   
			String nod_deta= ""+nod_detalles;
			//String nod_deta="\"{\"gutter_url\":\"\", \"result\":[{\"album_sort\":\"Wall, The\",\"release_year\":1979,\"afs\":\"Y\"}],\"sort_order\":\"popularity\"}\"";
							  //String "{"gutter_url":"", "result":[{"album_sort":"Wall, The","release_year":1979,"afs":"Y"}],"sort_order":"popularity"}"
							  // JSON 
							  // "{\"nomNodo\":\"sector2\",\"meds\":[{\"noSen\":\"T1\",\"med\":\"tmp\",\"valor\":20.00},{\"noSen\":\"H1\",\"med\":\"hmd\",\"valor\":35.00}]}"
								
			System.out.println("nodo_detalles ======" +nod_deta);
			
			
			
			int i = nod_deta.indexOf("{");
			nod_deta = nod_deta.substring(i);
			JSONObject jsonFile = new JSONObject(nod_deta);
			System.out.println("Input JSON data: "+ jsonFile);
			
			
			//mongo.main(
					//MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
					//MongoDatabase database = mongoClient.getDatabase("wsnbd");
					
					
					//);
		
			r.setMensaje("Envio correcto!");
		}catch(JSONException e) {
			r.setCodigo(-90);
			r.setMensaje("Error en envio");
			 e.printStackTrace();
		}
		return r;
	}

	
	
}
