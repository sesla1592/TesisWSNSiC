package mongodb.ec.edu.ups.tesiswsnsic;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.model.Filters;


import org.bson.Document;

public class MongoConnectionDB {

	public static void main(String[] args) throws UnknownHostException {
 

//		/**Conexion a mongodb */			
//		//MongoClient mongoClient = new MongoClient("localhost",27017);
//		/**BD a elegir */	
//		DB db=mongoClient.getDB("wsnbd"); 
//		/**Lista de colecciones de la BD */	
//		Set<String> collection=db.getCollectionNames(); 
//
//		/**Recorrer las colecciones y mostrar */	
//		for (String s : collection) {
//			System.out.println(s);
//			}
//		/**Ver documentos de una coleccion */	
//		DBCollection coll = db.getCollection("Nueva");
//		
//		
//		
		
		String  nodo ="n1";
    	String  sensor ="d1";
    	String  medicion ="temperatura";
    
//    	
//    	MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
//    	DB database = mongoClient.getDB("wsnbd");
//    	DBCollection collection = database.getCollection("Nueva");
//    	
//    	DBObject match = new BasicDBObject("$match", new BasicDBObject("nombreNodo", nodo)); 
//    	DBObject unwind = new BasicDBObject("$unwind", "$mediciones");
//    	DBObject match1 = new BasicDBObject("$match", new BasicDBObject("mediciones.nombreSensor", sensor).append("mediciones.medicion", medicion)); 
//    	DBObject project = new BasicDBObject("$project", new BasicDBObject("_id", 0)
//    			 .append("nombre","$mediciones.nombreSensor")
//    			 .append("medicion","$mediciones.medicion")
//    			 .append("fecha","$mediciones.fecha")
//    			 .append("valor","$mediciones.valor")
//    			 
//    			 );
//
//       
//    	
//        
//        AggregationOutput output = collection.aggregate(match, unwind, match1, project);
//
//        Iterable<DBObject> result = output.results();
//        Iterator<DBObject> iterator = result.iterator();
//		
	
    	
    	 Block<Document> printBlock = new Block<Document>() {
    	        @Override
    	        public void apply(final Document document) {
    	            System.out.println(document.toJson());
    	        }
    	    };
    	
    	MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    	MongoDatabase database = mongoClient.getDatabase("wsnbd");
    	MongoCollection<Document> collection = database.getCollection("Nueva");

//    	
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

	}

}
