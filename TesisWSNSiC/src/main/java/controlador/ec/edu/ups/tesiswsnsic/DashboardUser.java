package controlador.ec.edu.ups.tesiswsnsic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.data.mongodb.core.MongoOperations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import dao.ec.edu.ups.tesiswsnsic.PersonaNodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.MedValFec;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;
import utilidades.ec.edu.ups.tesiswsnsic.DBConnection;

@ManagedBean
@ViewScoped
public class DashboardUser implements Serializable {

	@Inject
	PersonaNodoDAO personaNodoDAO;

	private Marker marker;
	Persona user;
	public MapModel simpleModel;
	List<Nodo> ltsMyNodos = new ArrayList<>();
	List<String> ltsSensores = new ArrayList<>();
	String sensorSeleccionado;
	
	protected List<MedValFec> ltsSTemp;
	protected List<MedValFec> ltsSHum;
	private static String djson;
	public String medici;
	public double val;
	public String fec;
	//public String filtroBusqueda = "T";
	String medicion = "Temperatura";
	Nodo nodoSelected;

	private MongoClient mongoClient;

	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			System.out.println("user " + user.getId());
			ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
			System.out.println("lista nodos" + ltsMyNodos.size());
			ltsSTemp = new ArrayList<>();
			ltsSHum = new ArrayList<>();
			simpleModel = new DefaultMapModel();
			// recuperaDatos();
			addMarker();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String getMedici() {
		return medici;
	}

	public void setMedici(String medici) {
		this.medici = medici;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public List<Nodo> getLtsMyNodos() {
		return ltsMyNodos;
	}

	public void setLtsMyNodos(List<Nodo> ltsMyNodos) {
		this.ltsMyNodos = ltsMyNodos;
	}

//	public String getFiltroBusqueda() {
//		return filtroBusqueda;
//	}
//
//	public void setFiltroBusqueda(String filtroBusqueda) {
//		this.filtroBusqueda = filtroBusqueda;
//	}

	public String getMedicion() {
		return medicion;
	}

	public void setMedicion(String medicion) {
		this.medicion = medicion;
	}

	public String getSensorSeleccionado() {
		return sensorSeleccionado;
	}

	public void setSensorSeleccionado(String sensorSeleccionado) {
		this.sensorSeleccionado = sensorSeleccionado;
	}

	public List<String> getLtsSensores() {
		return ltsSensores;
	}

	public void setLtsSensores(List<String> ltsSensores) {
		this.ltsSensores = ltsSensores;
	}

	public List<MedValFec> getLtsSTemp() {
		return ltsSTemp;
	}

	public void setLtsSTemp(List<MedValFec> ltsSTemp) {
		this.ltsSTemp = ltsSTemp;
	}

	public List<MedValFec> getLtsSHum() {
		return ltsSHum;
	}

	public void setLtsSHum(List<MedValFec> ltsSHum) {
		this.ltsSHum = ltsSHum;
	}

	public void grafica() {
		System.out.println("sensor seleccionados: "+sensorSeleccionado);
	}
	public String recuperaDatos() {

		Block<Document> printBlock = new Block<Document>() {
			@Override

			public void apply(final Document document) {
				djson = "[" + document.toJson() + "]";
				// System.out.println(djson);
				JsonParser parser = new JsonParser();
				JsonArray gsonArr = parser.parse(djson).getAsJsonArray();
				for (JsonElement obj : gsonArr) {

					JsonObject gsonObj = obj.getAsJsonObject();
					// ELEMENTOS PRIMITIVOS DE OBJETOS: fecha
					fec = gsonObj.get("fecha").getAsString();
					// ELEMENTOS DENTRO DEL LISTADO DE PRIMITIVO: valor medicion
					// JsonObject gsonObj2= (JsonObject) gsonObj.get("mediciones");

					JsonArray ltsMediciones = (JsonArray) gsonObj.get("ms");
					// System.out.println("tam "+ltsMediciones.size());
					for (int i = 0; i < ltsMediciones.size(); i++) {

						JsonObject gsonObj2 = (JsonObject) ltsMediciones.get(i);
						medici = gsonObj2.get("m").getAsString();// va el nombre del sensor
						if (medici.equals(sensorSeleccionado)) {
							val = gsonObj2.get("v").getAsDouble();
							ltsSTemp.add(new MedValFec(medici, val, fec));

						}
					}
				}
//				for (int i = 0; i < ltsSTemp.size(); i++) {
//					// System.out.println("POINTS "+puntos.get(i).getFecha()+" -
//					// "+puntos.get(i).getMedicion()+" - "+puntos.get(i).getValor());
//				}

			}
		};
		mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
		MongoDatabase database = mongoClient.getDatabase(DBConnection.dbname);
		FindIterable<Document> collection = database.getCollection(DBConnection.dbcollection).find().sort(Sorts.descending("fecha"))
				.limit(10);

		collection.forEach(printBlock);

		
		System.out.println("Connection Succesfull");
		return null;
	}

	public void addMarker() {
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			Marker marker = new Marker(new LatLng(ltsMyNodos.get(i).getLatitud(), ltsMyNodos.get(i).getLongitud()),
					ltsMyNodos.get(i).getIdentificador());
			simpleModel.addOverlay(marker);

		}
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
		nodoSelected = findNodo(marker.getTitle());
		System.out.println("Nodo Selected" + nodoSelected.toString() +" sensores: "+ nodoSelected.getLtssensores().size());
		ltsSensores = new ArrayList<>();
		ltsSensores.add("Todos");
		for (int i = 0; i < nodoSelected.getLtssensores().size(); i++) {
			ltsSensores.add(""+nodoSelected.getLtssensores().get(i).getNombre());
			//nodoSelected.getLtssensores().size()
		}
		
		for (int i = 0; i < ltsSensores.size(); i++) {
			System.out.println("-> "+ltsSensores.get(i));
			//nodoSelected.getLtssensores().size()
		}
	}

	public Nodo findNodo(String identificador) {
		Nodo nodo = new Nodo();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			if (ltsMyNodos.get(i).getIdentificador().equals(identificador)) {
				nodo = ltsMyNodos.get(i);
				break;
			}
		}
		return nodo;
	}

	public Marker getMarker() {
		return marker;
	}
	
	public void consulta() {
		ltsSHum= new ArrayList<>();
		ltsSTemp = new ArrayList<>();
		System.out.println("sensor seleccionado: "+sensorSeleccionado);
		MongoClient mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
//		MongoDatabase database = mongoClient.getDatabase(DBConnection.dbname);
//		MongoCollection<Document> collection = database.getCollection(DBConnection.dbcollection);
//		
		BasicDBObject query = new BasicDBObject();
		query.put("n", nodoSelected.getIdentificador());
		query.put("fecha", BasicDBObjectBuilder.start("$gte", "2019/01/10 00:00:00").add("$lte", "2019/01/16 23:59:59").get());
		//FindIterable<Document> busquedaNodo = collection.find(query);
		//busquedaNodo.forEach(printBlock); 
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);
		
		DBCursor d1 = coll.find(query);
		//int cont=0;
		//System.out.println("contador "+cont+ " tam "+d1.count() +" - "+d1.length());
		if(sensorSeleccionado.equals("Todos")) {
			System.out.println("selecciono todos");
			while(d1.hasNext()) {
				
				DBObject obj = d1.next();
				//System.out.println(cont+" prueba F---> "+obj.toString());
				//JsonArray ltsMediciones = (JsonArray) obj.get("ms");
				JSONArray ltsMediciones = new JSONArray(obj.get("ms").toString());
				fec = obj.get("fecha").toString();
				for (int i = 0; i < ltsMediciones.length(); i++) {
					JSONObject gsonObj2 = ltsMediciones.getJSONObject(i);
					//JsonObject gsonObj2= (JsonObject) ltsMediciones.get(i);
					medici = gsonObj2.get("m").toString();//va el nombre  del sensor
					if(medici.equals("T")) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						ltsSTemp.add(new MedValFec(medici, val, fec));
						
					}
					if(medici.equals("H")) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						ltsSHum.add(new MedValFec(medici, val, fec));
						
					}
				}
				//cont++;
			}
		}else {
			System.out.println("selecciono uno");
			while(d1.hasNext()) {
				
				DBObject obj = d1.next();
				//System.out.println(cont+" prueba F---> "+obj.toString());
				//JsonArray ltsMediciones = (JsonArray) obj.get("ms");
				JSONArray ltsMediciones = new JSONArray(obj.get("ms").toString());
				fec = obj.get("fecha").toString();
				for (int i = 0; i < ltsMediciones.length(); i++) {
					JSONObject gsonObj2 = ltsMediciones.getJSONObject(i);
					//JsonObject gsonObj2= (JsonObject) ltsMediciones.get(i);
					medici = gsonObj2.get("m").toString();//va el nombre  del sensor
					if(medici.equals(sensorSeleccionado)) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						if(sensorSeleccionado.equals("T")) {
							ltsSTemp.add(new MedValFec(medici, val, fec));
						}else {
							ltsSHum.add(new MedValFec(medici, val, fec));
						}
						
						
					}
				}
				//cont++;
			}
		}
		
//		System.out.println("contador "+cont);
//		for (int i = 0; i < puntos.size(); i++) {
//			System.out.println("punto "+puntos.get(i).toString());
//		}
		System.out.println("Connection Succesfull");
	}
	

}
