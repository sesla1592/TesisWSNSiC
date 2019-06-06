package mongodb.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UnwindOptions;

import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaNodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.MedValFec;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Persona;

import com.mongodb.client.model.Filters;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.data.domain.Sort;

@ManagedBean
@RequestScoped
public class MongoConnectionDB {

	@Inject
	PersonaNodoDAO personaNodoDAO;
	
	Persona user;
	public MapModel simpleModel;
	
	public String typemedic = "line";
	protected List<MedValFec> puntos;
	
	public String medici;
	public double val;
	public String fec;
	public String filtroBusqueda="T";

	private static String djson;

	/* TEMPORALMENTE ESTA COLOCADO VALORES, ESTO DEBE PASARSE COMO PARAMETRO
	 * */
	String nodo ="n2";
	String sensor ="d1";
	String medicion ="Temperatura";
	
	List<Nodo> ltsMyNodos=new ArrayList<>();
	
	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("userSelected");
			System.out.println("user "+user.getId());
			ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
			System.out.println("lista nodos"+ltsMyNodos.size());
			puntos = new ArrayList<>();
			simpleModel = new DefaultMapModel();
			recuperaDatos();
			addMarker();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
	
	public String getTypemedic() {
		return typemedic;
	}

	public void setTypemedic(String typemedic) {
		this.typemedic = typemedic;
	}
	
	public String getMedici() {
		return medici;
	}

	public void setMedici(String medici) {
		this.medici = medici;
	}

	public String getFec() {
		return fec;
	}

	public void setFec(String fec) {
		this.fec = fec;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	public List<MedValFec> getPuntos() {
		return puntos;
	}

	public void setPuntos(List<MedValFec> puntos) {
		this.puntos = puntos;
	}

	public String getNodo() {
		return nodo;
	}

	public void setNodo(String nodo) {
		this.nodo = nodo;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getMedicion() {
		return medicion;
	}

	public void setMedicion(String medicion) {
		this.medicion = medicion;
	}

	public String getFiltroBusqueda() {
		return filtroBusqueda;
	}

	public void setFiltroBusqueda(String filtroBusqueda) {
		this.filtroBusqueda = filtroBusqueda;
	}

	public void LineChartBean() {
		recuperaDatos();
	}
	
	
	public MapModel getSimpleModel() {
		//addMarker();
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public void mapa(double lat, double longi,String nombre) {
		System.out.println("datos q llegan "+lat+","+longi+","+nombre);
        
        //Shared coordinates
        LatLng coord = new LatLng(lat,longi);
        
          
        //Basic marker
        simpleModel.addOverlay(new Marker(coord, nombre));
        System.out.println("agrego");
	}
	
	public String recuperaDatos() {

		Block<Document> printBlock = new Block<Document>() {
			@Override

			public void apply(final Document document) {
				djson = "[" +document.toJson()+ "]";
//				System.out.println(djson);
				JsonParser parser = new JsonParser();
				JsonArray gsonArr = parser.parse(djson).getAsJsonArray();
				for(JsonElement obj : gsonArr) {	
					
					JsonObject gsonObj = obj.getAsJsonObject();
					//ELEMENTOS PRIMITIVOS DE OBJETOS: fecha
					fec = gsonObj.get("fecha").getAsString();
					//ELEMENTOS DENTRO DEL LISTADO DE PRIMITIVO: valor medicion
					//JsonObject gsonObj2= (JsonObject) gsonObj.get("mediciones");
					
					JsonArray ltsMediciones = (JsonArray) gsonObj.get("ms");
					//System.out.println("tam "+ltsMediciones.size());
					for (int i = 0; i < ltsMediciones.size(); i++) {
						
						JsonObject gsonObj2= (JsonObject) ltsMediciones.get(i);
						medici = gsonObj2.get("m").getAsString();//va el nombre  del sensor
						if(medici.equals(filtroBusqueda)) {
							val = gsonObj2.get("v").getAsDouble();
							puntos.add(new MedValFec(medici, val, fec));
							
						}
					}
				}
				for(int i=0;i<puntos.size();i++) {
					System.out.println("POINTS   "+puntos.get(i).getFecha()+" - "+puntos.get(i).getMedicion()+" - "+puntos.get(i).getValor());
				}
				
			}
		};
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://35.199.91.181:27017"));
		MongoDatabase database = mongoClient.getDatabase("DBWSNSIN");
		FindIterable<Document> collection = database.getCollection("Nueva").find().sort(Sorts.descending("fecha")).limit(10);
		 
		collection.forEach(printBlock); 
		
//		collection
//				.aggregate(Arrays.asList(Aggregates.match(Filters.eq("nombreNodo", nodo)),
//						Aggregates.unwind("$mediciones", new UnwindOptions().preserveNullAndEmptyArrays(true)),
//						Aggregates.match(Filters.eq("mediciones.nombreSensor", sensor)),
//						Aggregates.match(Filters.eq("mediciones.medicion", medicion)),
//						
//						Aggregates.project(Projections.fields(Projections.excludeId(),
//								Projections.include("mediciones.medicion"), Projections.include("mediciones.valor"),
//								Projections.include("fecha")))
//				)).forEach(printBlock);
		System.out.println("Connection Succesfull");
		return null;
	}

	public void addMarker() {
//        Marker marker = new Marker(new LatLng(-2.904770, -79.001693), "nodo");
//        simpleModel.addOverlay(marker);
        for (int i = 0; i < ltsMyNodos.size(); i++) {
        	 	Marker marker = new Marker(new LatLng(ltsMyNodos.get(i).getLatitud(), ltsMyNodos.get(i).getLongitud()), ltsMyNodos.get(i).getNombre());
             simpleModel.addOverlay(marker);
			
		}
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:, Lng:"));
    }
}