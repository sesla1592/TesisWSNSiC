package servicios.ec.edu.ups.tesiswsnsic;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.bson.Document;

import org.json.JSONException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("/Registrodatos")
public class RegistDatosWSREST {
	private static String smssat = "Saved on the cloud SiC";
	/*
	 * PERMITE PERSISTIR A LA BASE DE DATOS QUE SE ENCUENTRA EN LA NUBE, MONGO DB
	 * 
	 * http://localhost:8080/TesisWSNSiC/rs/Registrodatos/Registrar?nod_detalles=hollasdcsd
	 * */  
	@Path("/Registrar")
	@GET 
	@Produces("application/json")
	@Consumes("application/json")
	public Respuesta obtenerDatos(@QueryParam("nod_detalles")String nod_detalles ) {
		Respuesta r = new Respuesta();
		try {
	    	MongoClient mongoClient = new MongoClient("localhost",27017);
	    	System.out.println("Connection Mongo Client");
	    	MongoDatabase database = mongoClient.getDatabase("DBWSNSIN");
	    	System.out.println("Connection to Data Base");
	    	MongoCollection<Document> collection = database.getCollection("Nueva");
	    	System.out.println("Sucess get Collection");
	    	System.out.println(collection.getNamespace());
	    	Document doc = Document.parse(nod_detalles.toString());
	    	collection.insertOne(doc);		
			r.setMensaje(smssat);
		}catch(JSONException e) {
			r.setCodigo(-90);
			r.setMensaje("Error al almacenar CLOUD SiC");
			 e.printStackTrace();
		}
		return r;
	}
}
