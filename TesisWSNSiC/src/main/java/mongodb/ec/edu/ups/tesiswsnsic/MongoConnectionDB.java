package mongodb.ec.edu.ups.tesiswsnsic;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoConnectionDB {

	public static void main(String[] args) {


		try {
			
			MongoClient mongoClient = new MongoClient("localhost",27017);
			DB db=mongoClient.getDB("wsnbd");
			DBCollection collection=db.getCollection("Sensor");
			
			DBCursor cursor = collection.find();
			
				while (cursor.hasNext()) {
					int i=1;
					System.out.println(cursor.next());
					i++;
				}	
			
			System.out.println("Connection Succefull");
		} catch (UnknownHostException e) {
		
			e.printStackTrace();
		}

	}

}
