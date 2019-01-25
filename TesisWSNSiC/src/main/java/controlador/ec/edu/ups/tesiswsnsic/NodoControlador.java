package controlador.ec.edu.ups.tesiswsnsic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.sound.midi.Soundbank;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lowagie.text.Utilities;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;
import utilidades.ec.edu.ups.tesiswsnsic.DBConnection;

@SessionScoped
@ManagedBean
public class NodoControlador {

	@Inject
	private NodoDAO nodoDAO;
	
	Nodo nodo;
	Nodo nodoSelected;
	boolean insert;
	
	private static String resultadoBusqueda;
	private List<Nodo> ltsNodo;
	String codNodo="";
	
	@PostConstruct
	public void init() {
		ltsNodo = nodoDAO.getAllNodos();
		nodo = new Nodo();
		insert=false;
		//busquedaNodoMongo();
	}
	
	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}

	public List<Nodo> getLtsNodo() {
		return ltsNodo;
	}

	public void setLtsNodo(List<Nodo> ltsNodo) {
		this.ltsNodo = ltsNodo;
	}

	public Nodo getNodoSelected() {
		return nodoSelected;
	}

	public void setNodoSelected(Nodo nodoSelected) {
		this.nodoSelected = nodoSelected;
	}

	public String getCodNodo() {
		return codNodo;
	}

	public void setCodNodo(String codNodo) {
		this.codNodo = codNodo;
	}

	public void crearNodo(){
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/nodo/crearNodo.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void guardarNodo(){
		
		try {
			//extraigo dato de la ubicacion
			URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+nodo.getLatitud()+","+nodo.getLongitud()+"&key=AIzaSyCFPa3ras2hBSAdSpYCa7q83OF8fOgCL6g");//your url i.e fetch data from .
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
	        nodo.setDescripcion(jsonDirec.get("formatted_address").toString());
	        conn.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error al encontrar lugar");
			nodo.setDescripcion("lugar desconocido");
		}
			try{
				
				///
				System.out.println(nodo.toString());
				nodoDAO.insert(nodo);
				nodo = new Nodo();
				nodoSelected = new Nodo();
				codNodo="";
				ltsNodo = nodoDAO.getAllNodos();
				FacesContext contex = FacesContext.getCurrentInstance();
				contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/nodo/listaNodos.xhtml");
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		

	}
	
	public void editarNodo(Nodo nodo){
		setNodoSelected(nodo);
		System.out.println(nodoSelected.toString());
	}
	
	public void guardarEditar(){
		nodoDAO.update(nodoSelected);
	}
	
	public void irDetalles(Nodo nodoSelected){
		System.out.println("--> "+nodoSelected.toString());
		FacesContext contex = FacesContext.getCurrentInstance();
		contex.getExternalContext().getSessionMap().put("nodoSelected", nodoSelected);
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/nodo/detalleSensor.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	String latitud ="";
	String longitud = "";
	
	public void busquedaNodoMongo() {
		System.out.println("entro a la busqueda cod "+codNodo);
		
		MongoClient mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
		MongoDatabase database = mongoClient.getDatabase(DBConnection.dbname);
		MongoCollection<Document> collection = database.getCollection(DBConnection.dbcollection);
		
		BasicDBObject query = new BasicDBObject();
		query.put("n", codNodo);
		FindIterable<Document> busquedaNodo = collection.find(query);
		//busquedaNodo.forEach(printBlock); 
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);
		
		BasicDBObject b1 = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject("n", codNodo).append("la", "").append("lo", "");
		
		DBObject d1 = coll.findOne(query);
		if(d1!=null) {
			System.out.println("prueba F---> "+d1.toString());
			System.out.println("prueba---> "+d1.get("la"));
			System.out.println("prueba---> "+d1.get("lo"));
			boolean nodoInsert = nodoDAO.existsNode(codNodo);
			if(nodoInsert) {
				//existe el nodo
				System.out.println("existe el nodo en la bd");
			}else {
				//
				System.out.println("no existe el nodo en la bd");
				try {
					nodo.setIdentificador(codNodo);
					nodo.setLatitud(Double.parseDouble(d1.get("la").toString()));
					nodo.setLongitud(Double.parseDouble(d1.get("lo").toString()));
					System.out.println("sensores "+ d1.get("ms"));
					JSONArray ltsSensores = new JSONArray(d1.get("ms").toString());
					System.out.println("tam sensores "+ltsSensores.length());
					List<Sensor> ltsSensor = new ArrayList<>();
					for (int i = 0; i < ltsSensores.length(); i++) {
						JSONObject sensorM = ltsSensores.getJSONObject(i);
						Sensor sensor = new Sensor();
						sensor.setNombre(sensorM.getString("m"));
						sensor.setDescripcion(sensorM.getString("s"));
						sensor.setEstado(true);
						ltsSensor.add(sensor);
					}
					nodo.setLtssensores(ltsSensor);
					guardarNodo();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("prueba---> no encontro");
		}
		
		System.out.println("Connection Succesfull");
		
	}
	
	public void back() {
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/nodo/listaNodos.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void backMenu() {
		FacesContext contex = FacesContext.getCurrentInstance();
		try{
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/dashboard.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
