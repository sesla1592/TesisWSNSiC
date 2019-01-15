package servicios.ec.edu.ups.tesiswsnsic;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

@Path("/Registrodatos")
public class RegistDatosWSREST {

	private String conv;
	private static String smssat = "Saved on the cloud SiC";
	private JsonObject gsonObj;
	private Document doc;

	/*
	 * PERMITE PERSISTIR A LA BASE DE DATOS QUE SE ENCUENTRA EN LA NUBE, MONGO DB Y POSTGRESQL
	 *
	 * http://localhost:8080/TesisWSNSiC/rs/Registrodatos/Registrar?nod_detalles=
	 * hollasdcsd
	 */
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
	    	System.out.println("RECIBIDO  "+nod_detalles.toString());
	    	conv = nod_detalles.toString();

	    	if(conv.contains("u'")) {
		    	String converter1 = nod_detalles.toString().replace("u'", "\"");
//		    	System.out.println("CONVERTER1:" +converter1);
		    	String converter2 = String.valueOf(converter1.replace("'", "\""));
//		    	System.out.println("CONV2: "+converter2.toString());
		    	String converter3 = converter2.substring(1, converter2.length()-1);
//		    	System.out.println("FCONV: "+converter3);

		    	try {
		    		doc = Document.parse(converter3);
		    		String djson = "["+doc.toJson()+"]";
		    		JsonParser parser = new JsonParser();
		    		JsonArray gsonArr = parser.parse(djson).getAsJsonArray();
		    		for(JsonElement obj : gsonArr){
		    			gsonObj = obj.getAsJsonObject();
		    			gsonObj.remove("estadocloud");
		    			System.out.println("CONVERTED JSON:  "+gsonObj);
		    			String gsonObj1 =  String.valueOf(gsonObj);
		    			doc = Document.parse(gsonObj1);
		    			System.out.println("CONVERTIDO A DOCUMENTO:  "+doc);
		    	    	collection.insertOne(doc);
		    	    	System.out.println("GRABADO EN LA  NUBE...");
		    			r.setMensaje(smssat);
		    		}
				} catch (Exception e) {
					r.setCodigo(-90);
					r.setMensaje("Error al almacenar CLOUD SiC");
					e.printStackTrace();
				}

	    	}else{
	    		System.out.println("NORMAL:  "+conv);
	    		doc = Document.parse(conv);
	    		System.out.println("MISMO: "+doc);
		    	collection.insertOne(doc);
		    	System.out.println("GRABADO EN LA  NUBE...");
				r.setMensaje(smssat);
	    	}
		}catch(JSONException e) {
			r.setCodigo(-90);
			r.setMensaje("Error al almacenar CLOUD SiC");
			 e.printStackTrace();
		}
		return r;
	}
}
