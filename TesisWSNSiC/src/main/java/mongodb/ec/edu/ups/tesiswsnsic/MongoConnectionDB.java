package mongodb.ec.edu.ups.tesiswsnsic;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoConnectionDB {

	public static void main(String[] args) {
 

		try {
			/**Conexion a mongodb */			
			MongoClient mongoClient = new MongoClient("localhost",27017);
			/**BD a elegir */	
			DB db=mongoClient.getDB("wsnbd");
			/**Lista de colecciones de la BD */	
			Set<String> collection=db.getCollectionNames(); 
		
			/**Recorrer las colecciones y mostrar */	
			for (String s : collection) {
				System.out.println(s);
				}
			/**Ver documentos de una coleccion */	
			DBCollection coll = db.getCollection("Persona");
			
				
			DBObject myDoc = coll.findOne();
			System.out.println(myDoc);
			
			DBCursor cursor = coll.find();
			
			try {
			   while(cursor.hasNext()) {
			       System.out.println(cursor.next());
			   }
			} finally {
			   cursor.close();
			}
			
			System.out.println("Connection Succefull");
		} catch (UnknownHostException e) {
		
			e.printStackTrace();
		}

	}

}
