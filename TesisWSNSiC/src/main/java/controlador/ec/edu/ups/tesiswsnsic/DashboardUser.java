package controlador.ec.edu.ups.tesiswsnsic;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.mongodb.client.MongoCollection;
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
	public String medicion = "Temperatura";
	public Nodo nodoSelected;
	public Double datoTemp;
	public Double datoHum;
	public String fechaInicio="2019/01/10 00:00:00";
	public String fechaFin;
	
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
			//fecha actual
			Date date = new Date();
			//Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaFin=dateFormat.format(date)+" 23:59:59";
			System.out.println("Fecha: fin-->"+fechaFin);
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
	
	public Nodo getNodoSelected() {
		return nodoSelected;
	}

	public void setNodoSelected(Nodo nodoSelected) {
		this.nodoSelected = nodoSelected;
	}

	public Double getDatoTemp() {
		return datoTemp;
	}

	public void setDatoTemp(Double datoTemp) {
		this.datoTemp = datoTemp;
	}

	public Double getDatoHum() {
		return datoHum;
	}

	public void setDatoHum(Double datoHum) {
		this.datoHum = datoHum;
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
			String descripcion="Sensores\n";
			for (int j = 0; j < ltsMyNodos.get(i).getLtssensores().size(); j++) {
				descripcion=descripcion+ltsMyNodos.get(i).getLtssensores().get(j).getNombre()+"\n";
			}
			Marker marker = new Marker(new LatLng(ltsMyNodos.get(i).getLatitud(), ltsMyNodos.get(i).getLongitud()),
					ltsMyNodos.get(i).getIdentificador(),descripcion);
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
		getDatosTH();
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

		BasicDBObject query = new BasicDBObject();
		query.put("n", nodoSelected.getIdentificador());
		query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
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
	
	public void getDatosTH() {
		mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
		BasicDBObject query = new BasicDBObject();
		query.put("n", nodoSelected.getIdentificador());
		
		//busquedaNodo.forEach(printBlock); 
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);
		
		
		DBObject d1 = coll.findOne(query);
		if(d1!=null) {
			System.out.println("prueba F---> "+d1.toString());
			System.out.println("prueba---> "+d1.get("la"));
			System.out.println("prueba---> "+d1.get("lo"));
			JSONArray ltsSensores = new JSONArray(d1.get("ms").toString());
			for (int i = 0; i < ltsSensores.length(); i++) {
				JSONObject sensorM = ltsSensores.getJSONObject(i);
				if(sensorM.getString("m").equals("T")) {
					datoTemp= Double.parseDouble(sensorM.get("v").toString());
				}
				if(sensorM.getString("m").equals("H")) {
					datoHum= Double.parseDouble(sensorM.get("v").toString());
				}
				
			}
			
			//boolean nodoInsert = true;//nodoDAO.existsNode(codNodo);
//			if(nodoInsert) {
//				//existe el nodo
//				System.out.println("existe el nodo en la bd");
//			}else {
//				//
//				System.out.println("no existe el nodo en la bd");
//				try {
//					nodo.setIdentificador(codNodo);
//					nodo.setLatitud(Double.parseDouble(d1.get("la").toString()));
//					nodo.setLongitud(Double.parseDouble(d1.get("lo").toString()));
//					System.out.println("sensores "+ d1.get("ms"));
//					JSONArray ltsSensores = new JSONArray(d1.get("ms").toString());
//					System.out.println("tam sensores "+ltsSensores.length());
//					List<Sensor> ltsSensor = new ArrayList<>();
//					for (int i = 0; i < ltsSensores.length(); i++) {
//						JSONObject sensorM = ltsSensores.getJSONObject(i);
//						Sensor sensor = new Sensor();
//						sensor.setNombre(sensorM.getString("m"));
//						sensor.setDescripcion(sensorM.getString("s"));
//						sensor.setEstado(true);
//						ltsSensor.add(sensor);
//					}
//					nodo.setLtssensores(ltsSensor);
//					guardarNodo();
//				}catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//			}
		}else {
			System.out.println("prueba---> no encontro");
		}
		
		System.out.println("Connection Succesfull");
	}
}
