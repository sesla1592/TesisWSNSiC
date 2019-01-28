package controlador.ec.edu.ups.tesiswsnsic;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import utilidades.ec.edu.ups.tesiswsnsic.DBConnection;
import utilidades.ec.edu.ups.tesiswsnsic.Reporte;



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
	protected List<MedValFec> ltsSLum;
	protected List<MedValFec> ltsSRui;
	
	private String URL_FOLDER="/Users/paul/dataWSNSIC/";
	
	private List<Reporte> ltsReporte;
	
	private static String djson;
	public String medici;
	public double val;
	public String fec;
	public String medicion = "Temperatura";
	public Nodo nodoSelected;
	private MongoClient mongoClient;
	public Double datoTemp=0.0;
	public Double datoHum=0.0;
	public Double datoRui=0.0;
	public Double datoLum=0.0;
	public String fechaInicio="2018/01/11 00:00:00";
	public String fechaFin;
	
	public String tipoFecha;
	
	
	private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLDITALIC);
	private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
	private static final Font headerTable = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			System.out.println("user " + user.getId());
			ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
			System.out.println("lista nodos" + ltsMyNodos.size());
			ltsSTemp = new ArrayList<>();
			ltsSHum = new ArrayList<>();
			ltsSRui = new ArrayList<>();
			ltsSLum = new ArrayList<>();
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
		if(sensorSeleccionado.equals("Todos")) {
			this.sensorSeleccionado = sensorSeleccionado;
		}else {
			sensorSeleccionado=sensorSeleccionado.substring(0, 1);
			this.sensorSeleccionado = sensorSeleccionado;
		}
		
		System.out.println("--> "+this.sensorSeleccionado);
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

	public Persona getUser() {
		return user;
	}

	public void setUser(Persona user) {
		this.user = user;
	}

	public List<MedValFec> getLtsSLum() {
		return ltsSLum;
	}

	public void setLtsSLum(List<MedValFec> ltsSLum) {
		this.ltsSLum = ltsSLum;
	}

	public List<MedValFec> getLtsSRui() {
		return ltsSRui;
	}

	public void setLtsSRui(List<MedValFec> ltsSRui) {
		this.ltsSRui = ltsSRui;
	}

	public Double getDatoRui() {
		return datoRui;
	}

	public void setDatoRui(Double datoRui) {
		this.datoRui = datoRui;
	}

	public Double getDatoLum() {
		return datoLum;
	}

	public void setDatoLum(Double datoLum) {
		this.datoLum = datoLum;
	}

	public String getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(String tipoFecha) {
		this.tipoFecha = tipoFecha;
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
					ltsMyNodos.get(i).getNombre(),descripcion);
			marker.setShadow(ltsMyNodos.get(i).getIdentificador());
			simpleModel.addOverlay(marker);

		}
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
		nodoSelected = findNodo(marker.getShadow());
		System.out.println("Nodo Selected" + nodoSelected.toString() +" sensores: "+ nodoSelected.getLtssensores().size());
		ltsSensores = new ArrayList<>();
		ltsSensores.add("Todos");
		for (int i = 0; i < nodoSelected.getLtssensores().size(); i++) {
			ltsSensores.add(""+nodoSelected.getLtssensores().get(i).getNombreCompleto());
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
		ltsSLum = new ArrayList<>();
		ltsSRui = new ArrayList<>();
		System.out.println("tipo de filtro fecha "+tipoFecha);
		if(tipoFecha.equals("Diario")) {
			Date date = new Date();
			//Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String fechaPrueba = dateFormat.format(date)+" 00:00:00";
		}
		if(tipoFecha.equals("Semanal")) {
			Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
			calendar.add(Calendar.DATE, -7); //el -3 indica que se le restaran 3 dias 
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String fechaPrueba = dateFormat.format(calendar.getTime())+" 00:00:00";
			System.out.println("fecha semanal "+fechaPrueba);
		}
		if(tipoFecha.equals("Mensual")) {
			Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
			calendar.add(Calendar.DATE, -30); //el -3 indica que se le restaran 3 dias 
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String fechaPrueba = dateFormat.format(calendar.getTime())+" 00:00:00";
			System.out.println("fecha mensual "+fechaPrueba);
		}
		
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
					if(medici.equals("L")) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						ltsSLum.add(new MedValFec(medici, val, fec));
						
					}
					if(medici.equals("R")) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						ltsSRui.add(new MedValFec(medici, val, fec));
						
					}
				}
				//cont++;
			}
		}else {
			System.out.println("selecciono uno");
			while(d1.hasNext()) {
				
				DBObject obj = d1.next();
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
						}
						if(sensorSeleccionado.equals("H")) {
							ltsSHum.add(new MedValFec(medici, val, fec));
						}
						if(sensorSeleccionado.equals("L")) {
							ltsSLum.add(new MedValFec(medici, val, fec));
						}
						if(sensorSeleccionado.equals("R")) {
							ltsSRui.add(new MedValFec(medici, val, fec));
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
		datoTemp=0.0;
		datoHum=0.0;
		datoRui=0.0;
		datoLum=0.0;
		
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
				if(sensorM.getString("m").equals("L")) {
					datoLum= Double.parseDouble(sensorM.get("v").toString());
				}
				if(sensorM.getString("m").equals("R")) {
					datoRui= Double.parseDouble(sensorM.get("v").toString());
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
	
	public void descargarCSV() {
		ltsReporte= new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador());
		}
//		for (int i = 0; i < ltsReporte.size(); i++) {
//			System.out.println(ltsReporte.get(i).toString());
//		}
		crearCSV();
	}
	
	public void datosNodo(String codSensor) {

		mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
		BasicDBObject query = new BasicDBObject();
		query.put("n", codSensor);
		
		//busquedaNodo.forEach(printBlock); 
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);
		
		
		DBCursor d1 = coll.find(query);
		double latitud=0.0;
		double longitud=0.0;
		String fecha="";
		
		while(d1.hasNext()) {
			
			DBObject obj = d1.next();
			JSONArray ltsSensores = new JSONArray(obj.get("ms").toString());
			fecha = obj.get("fecha").toString();
			latitud = Double.parseDouble(obj.get("la").toString());
			longitud = Double.parseDouble(obj.get("lo").toString());
			
			for (int i = 0; i < ltsSensores.length(); i++) {
				JSONObject sensorM = ltsSensores.getJSONObject(i);
				Reporte reporte = new Reporte();
				reporte.setCodSensor(codSensor);
				reporte.setFecha(fecha);
				reporte.setLatitud(latitud);
				reporte.setLongitud(longitud);
				
				if(sensorM.getString("m").equals("T")) {
					reporte.setSensor("Temperatura");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				if(sensorM.getString("m").equals("H")) {
					reporte.setSensor("Humedad");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				if(sensorM.getString("m").equals("L")) {
					reporte.setSensor("Luminosidad");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				if(sensorM.getString("m").equals("R")) {
					reporte.setSensor("Ruido");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				ltsReporte.add(reporte);
				
			}
		}
	}
	
	public void crearCSV() {
		String[] ltsCabecera = { "Codigo Sensor", "Fecha", "Latitud", "Longitud", "Sensor",
				"Valor" };
		try {
			Writer writer = Files.newBufferedWriter(
					Paths.get(URL_FOLDER + "data_1.csv"));
			CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(ltsCabecera);
			CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);
			for (int i = 0; i < ltsReporte.size(); i++) {
				List<Object> colum = new ArrayList<>();
				colum.add(ltsReporte.get(i).getCodSensor());
				colum.add(ltsReporte.get(i).getFecha());
				colum.add(ltsReporte.get(i).getLatitud());
				colum.add(ltsReporte.get(i).getLongitud());
				colum.add(ltsReporte.get(i).getSensor());
				colum.add(ltsReporte.get(i).getValor());
				csvPrinter.printRecord(colum);
			}
			csvPrinter.flush();
//			for (int i = 0; i < ltsCabecera.length; i++) {
//				
//			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void descargarJSON() {
		ltsReporte= new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador());
		}
//		for (int i = 0; i < ltsReporte.size(); i++) {
//			System.out.println(ltsReporte.get(i).toString());
//		}
		crearJSON();
	}
	
	public void crearJSON() {
		org.json.simple.JSONObject contenedor = new org.json.simple.JSONObject();
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
		
		org.json.simple.JSONArray company = new org.json.simple.JSONArray();
		for (int i = 0; i < ltsReporte.size(); i++) {
			
			obj.put("codSensor", ltsReporte.get(i).getCodSensor());
			obj.put("fecha", ltsReporte.get(i).getFecha());
			
			obj.put("latitud", ltsReporte.get(i).getLatitud());
			obj.put("longitud", ltsReporte.get(i).getLongitud());
			obj.put("sensor", ltsReporte.get(i).getSensor());
			obj.put("valor", ltsReporte.get(i).getValor());
			
			company.add(obj);
			obj = new org.json.simple.JSONObject();
		}
		contenedor.put("data", company);
		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter(URL_FOLDER+"/file1.json")) {
			file.write(contenedor.toJSONString());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

        System.out.print(obj);
		
	}
	
	public void descargarPDF() {
		ltsReporte= new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador());
		}
//		for (int i = 0; i < ltsReporte.size(); i++) {
//			System.out.println(ltsReporte.get(i).toString());
//		}
		crearPDF();
	}
	public void crearPDF() {
		try {
			com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 36, 36, 100, 54);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(URL_FOLDER+"/prueba.pdf"));
			//HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			//writer.setPageEvent(event);
			document.open();
			// We add metadata to PDF
			// Añadimos los metadatos del PDF
			document.addTitle("Reporte Nodos");
			document.addAuthor("WSNSIC");
			Chunk chunk = new Chunk();

			// Let's create de first Chapter (Creemos el primer capítulo)
			Chapter chapter = new Chapter(new Paragraph(chunk), 1);
			chapter.setNumberDepth(0);
						
			chapter.add(new Paragraph(" ", paragraphFont));
			Paragraph title = new Paragraph("Reporte Nodos ", chapterFont);
			title.setAlignment(Element.ALIGN_CENTER);
			chapter.add(title);
			chapter.add(new Paragraph(" ", paragraphFont));
			PdfPTable table = new PdfPTable(6);
			// Now we fill the PDF table
			// Ahora llenamos la tabla del PDF
			PdfPCell columnHeader;
			// Fill table rows (rellenamos las filas de la tabla).
			
			columnHeader = new PdfPCell(new Phrase("Codigo Sensor",headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Fecha",headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Latitud",headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Longitud",headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Sensor",headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Valor",headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			
			table.setHeaderRows(1);
			// Fill table rows (rellenamos las filas de la tabla).
			PdfPCell rowPdf;
			for (int row = 0; row < ltsReporte.size(); row++) {
				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getCodSensor()));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);
				
				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getFecha()));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);
				
				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getLatitud()+""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);
				
				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getLongitud()+""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);

				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getSensor()+""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);
				
				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getValor()+""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);
			}
			// We add the table (Añadimos la tabla)
			chapter.add(table);
			document.add(chapter);
			document.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}

