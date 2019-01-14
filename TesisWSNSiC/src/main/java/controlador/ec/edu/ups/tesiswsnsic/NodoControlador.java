package controlador.ec.edu.ups.tesiswsnsic;

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
import utilidades.ec.edu.ups.tesiswsnsic.DBConnection;

@SessionScoped
@ManagedBean
public class NodoControlador {

	@Inject
	private NodoDAO nodoDAO;
	
	Nodo nodo;
	Nodo nodoSelected;
	
	private static String resultadoBusqueda;
	private List<Nodo> ltsNodo;
	String codNodo="";
	
	@PostConstruct
	public void init() {
		ltsNodo = nodoDAO.getAllNodos();
		nodo = new Nodo();
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
		try{
			System.out.println("code "+codNodo);
			System.out.println(nodo.toString());
			nodoDAO.insert(nodo);
			nodo = new Nodo();
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
		Block<Document> printBlock = new Block<Document>() {
			@Override

			public void apply(final Document document) {
				resultadoBusqueda = "[" +document.toJson()+ "]";
				JsonParser parser = new JsonParser();
				JsonArray gsonArr = parser.parse(resultadoBusqueda).getAsJsonArray();
//				for(JsonElement obj : gsonArr) {	
//					
//					JsonObject gsonObj = obj.getAsJsonObject();
//					//ELEMENTOS PRIMITIVOS DE OBJETOS: fecha
//					latitud = gsonObj.get("la").getAsString();
//					longitud = gsonObj.get("lo").getAsString();
//				}
				JsonObject gsonObj = gsonArr.get(0).getAsJsonObject();
				//ELEMENTOS PRIMITIVOS DE OBJETOS: fecha
				latitud = gsonObj.get("la").getAsString();
				longitud = gsonObj.get("lo").getAsString();
				
				System.out.println("latitud "+latitud+" Longitud "+longitud);
			
			}
		};
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
			System.out.println("prueba---> "+d1.get("n"));
			System.out.println("prueba---> "+d1.get("la"));
			System.out.println("prueba---> "+d1.get("lo"));
			try {
				nodo.setIdentificador(codNodo);
				nodo.setLatitud(Double.parseDouble(d1.get("la").toString()));
				nodo.setLongitud(Double.parseDouble(d1.get("lo").toString()));
			}catch (Exception e) {
				// TODO: handle exception
			}
			guardarNodo();
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
