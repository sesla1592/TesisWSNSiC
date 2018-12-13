package mongodb.ec.edu.ups.tesiswsnsic;

import java.net.UnknownHostException;
import java.util.Arrays;


import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.model.Filters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.bson.Document;

public class MongoConnectionDB {
 
	private static String djson;
  

	public String recuperaDatos() {
		
	
		String  nodo ="n2";
    	String  sensor ="d1"; 
    	String  medicion ="humedad";
    	 Block<Document> printBlock = new Block<Document>() {
    	        @Override
    	        
    	        public void apply(final Document document) {
    	        	
    	            System.out.println(document.toJson());
    	            djson = document.toJson();
    	            JsonObject jsonObject = new JsonParser().parse(djson).getAsJsonObject();
    	            String medici = jsonObject.getAsJsonObject("mediciones").get("medicion").getAsString();
    	            String valor = jsonObject.getAsJsonObject("mediciones").get("valor").getAsString();
    	            System.out.println(medici);
    	            System.out.println(valor);
    	        }
    	    }; 
    	
    	MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    	MongoDatabase database = mongoClient.getDatabase("wsnbd");
    	MongoCollection<Document> collection = database.getCollection("Nueva");   	
    	collection.aggregate(
    		      Arrays.asList(
    		              Aggregates.match(Filters.eq("nombreNodo", nodo)),
    		              Aggregates.unwind( "$mediciones", new UnwindOptions().preserveNullAndEmptyArrays(true)),
    		              Aggregates.match(Filters.eq("mediciones.nombreSensor", sensor)),
    		              Aggregates.match(Filters.eq("mediciones.medicion", medicion)),
    		              Aggregates.project(
    		                      Projections.fields(
    		                            Projections.excludeId(),
    		                            Projections.include("mediciones.medicion"),
    		                            Projections.include("mediciones.valor"),
    		                            Projections.include("mediciones.fecha")
    		                      )
    		                  )
    		             
    		      )
    		).forEach(printBlock);
		

		System.out.println("Connection Succefull");
		return null;

	}

}