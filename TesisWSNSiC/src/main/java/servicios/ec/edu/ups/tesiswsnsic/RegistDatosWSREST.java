package servicios.ec.edu.ups.tesiswsnsic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	List<String>dataws = new ArrayList<>();

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
	    	MongoClient mongoClient = new MongoClient("35.199.91.181",27017);
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
/*	
	@Path("/prueba")
	@GET
	@Produces("application/json")
	public void prueba() {
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("https://maps.googleapis.com/maps/api/geocode/json?latlng=-2.889550,-79.022854&key=AIzaSyCEjgmfiCvCnSGCwVd-EU0M2ZBYUM08N1k");
//		JsonArray response = target.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
//		System.out.println("res "+response.toString());
		
		/////
		
		try {
			URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=-2.889550,-79.022854&key=AIzaSyCFPa3ras2hBSAdSpYCa7q83OF8fOgCL6g");//your url i.e fetch data from .
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP Error code : "
	                    + conn.getResponseCode());
	        }
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        BufferedReader br = new BufferedReader(in);
	        String salida="";
	        String output="";
	        while ((output=br.readLine()) != null) {
	        	salida=salida+output;
	            
	        }
	     
	        System.out.println(salida);
	        
	        JSONObject js = new JSONObject(salida);
	        
	        String result=js.get("results").toString();
	        //System.out.println(result);
	        JSONArray ltsResult = new JSONArray(result);
//	        for (int i = 0; i < ltsResult.length(); i++) {
//	        		System.out.println(ltsResult.get(i));
//	        		System.out.println("-------");
//			}

	        String direccion = ltsResult.get(0).toString();
	        JSONObject jsonDirec = new JSONObject(direccion);
	        System.out.println(jsonDirec.get("formatted_address"));
	        System.out.println("tam "+ltsResult.length());
	        conn.disconnect();
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
*/	
	
	/*http://localhost:8080/TesisWSNSiC/rs/Registrodatos/RegistrarPOST
	 * */
	@POST
	@Path("RegistrarPOST")
	@Produces("application/json")
	//@Consumes("application/json")
	public Respuesta obtenerDatosNueva(String nod_detalles) {
		Respuesta r = new Respuesta();
		dataws = new ArrayList<>();
		JsonArray gsonArr = new JsonArray();
		try {
	    	MongoClient mongoClient = new MongoClient("35.199.91.181",27017);
	    	System.out.println("Connection Mongo Client");
	    	MongoDatabase database = mongoClient.getDatabase("DBWSNSIN");
	    	System.out.println("Connection to Data Base");
	    	MongoCollection<Document> collection = database.getCollection("Nueva");
	    	System.out.println("Sucess get Collection");
	    	System.out.println(collection.getNamespace());
	    	System.out.println("RECIBIDO  "+nod_detalles.toString());
//	    	JSONParser parser = new JSONParser();
//	    	JsonObject gsonObj = (JsonObject) parser.parse(nod_detalles); 
//	    	System.out.println("GSON OBJECT: "+gsonObj);
//    		conv = gsonObj.toString();
    		doc = Document.parse(nod_detalles.toString());
    		collection.insertOne(doc);
    		System.out.println("Almacenado en el Cloud Publico");
    		r.setMensaje(smssat);
    	
		}catch(JSONException e) {
			r.setCodigo(-90);
			r.setMensaje("Error al almacenar CLOUD SiC");
			 e.printStackTrace();		 
		}		
		return r;		
	}
	
}