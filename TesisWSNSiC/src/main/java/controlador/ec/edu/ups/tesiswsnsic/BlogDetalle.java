package controlador.ec.edu.ups.tesiswsnsic;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaNodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.MedValFec;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import utilidades.ec.edu.ups.tesiswsnsic.DBConnection;

@ManagedBean
@ViewScoped
public class BlogDetalle {

	Blog blog;
	List<Nodo> ltsNodos = new ArrayList<>();
	
	@Inject
	PersonaNodoDAO personaNodoDAO;
	
	@Inject
	BlogDAO blogDAO;
	
	public MapModel simpleModel;
	public Nodo nodoSelected;
	
	protected List<MedValFec> ltsSData;
	public String fechaInicio;
	public String fechaFin;
	private Marker marker;
	private MongoClient mongoClient;
	public Double datoTemp=0.0;
	public Double datoHum=0.0;
	public Double datoRui=0.0;
	public Double datoLum=0.0;
	List<String> ltsSensores = new ArrayList<>();
	String sensorSeleccionado="";
	public String tipoFecha;
	public String medici;
	public double val;
	public String fec;
	
	@PostConstruct
	public void init() {
		
		try {
			blog  = (Blog) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("blogSelected");
			System.out.println("blog " + blog.toString());
			ltsNodos = personaNodoDAO.ltsNodosByUser(blog.getEmpresa().getPersonas().get(0).getId());
			System.out.println("tam nodos em "+ltsNodos.size());
			ltsSData = new ArrayList<>();
			simpleModel = new DefaultMapModel();
			// recuperaDatos();
			addMarker();
			//fecha actual
			Date date = new Date();
			//Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaFin=dateFormat.format(date)+" 23:59:59";
			System.out.println("Fecha: fin-->"+fechaFin);
			blog.setVisitas(blog.getVisitas()+1);
			blogDAO.update(blog);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("error la extraer blog");
		}
	
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	
	public List<Nodo> getLtsNodos() {
		return ltsNodos;
	}

	public void setLtsNodos(List<Nodo> ltsNodos) {
		this.ltsNodos = ltsNodos;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public Nodo getNodoSelected() {
		return nodoSelected;
	}

	public void setNodoSelected(Nodo nodoSelected) {
		this.nodoSelected = nodoSelected;
	}

	public List<MedValFec> getLtsSData() {
		return ltsSData;
	}

	public void setLtsSData(List<MedValFec> ltsSData) {
		this.ltsSData = ltsSData;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
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

	public List<String> getLtsSensores() {
		return ltsSensores;
	}

	public void setLtsSensores(List<String> ltsSensores) {
		this.ltsSensores = ltsSensores;
	}

	public String getSensorSeleccionado() {
		return sensorSeleccionado;
	}

	public void setSensorSeleccionado(String sensorSeleccionado) {
		this.sensorSeleccionado = sensorSeleccionado;
	}
	
	public String getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(String tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	public void addMarker() {
		for (int i = 0; i < ltsNodos.size(); i++) {
			String descripcion="Sensores\n";
			for (int j = 0; j < ltsNodos.get(i).getLtssensores().size(); j++) {
				descripcion=descripcion+ltsNodos.get(i).getLtssensores().get(j).getNombre()+"\n";
			}
			Marker marker = new Marker(new LatLng(ltsNodos.get(i).getLatitud(), ltsNodos.get(i).getLongitud()),
					ltsNodos.get(i).getNombre(),descripcion);
			marker.setShadow(ltsNodos.get(i).getIdentificador());
			simpleModel.addOverlay(marker);

		}
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
		nodoSelected = findNodo(marker.getShadow());
		System.out.println("Nodo Selected" + nodoSelected.toString() +" sensores: "+ nodoSelected.getLtssensores().size());
		ltsSensores = new ArrayList<>();
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
		for (int i = 0; i < ltsNodos.size(); i++) {
			if (ltsNodos.get(i).getIdentificador().equals(identificador)) {
				nodo = ltsNodos.get(i);
				break;
			}
		}
		return nodo;
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
			
		}else {
			System.out.println("prueba---> no encontro");
		}
		
		System.out.println("Connection Succesfull");
	}
	
	public void consulta() {
		ltsSData= new ArrayList<>();
		System.out.println("tipo de filtro fecha "+tipoFecha);
		if(tipoFecha.equals("Diario")) {
			Date date = new Date();
			//Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaInicio = dateFormat.format(date)+" 00:00:00";
		}
		if(tipoFecha.equals("Semanal")) {
			Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
			calendar.add(Calendar.DATE, -7); //el -3 indica que se le restaran 3 dias 
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaInicio = dateFormat.format(calendar.getTime())+" 00:00:00";
			System.out.println("fecha semanal "+fechaInicio);
		}
		if(tipoFecha.equals("Mensual")) {
			Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
			calendar.add(Calendar.DATE, -30); //el -3 indica que se le restaran 3 dias 
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaInicio = dateFormat.format(calendar.getTime())+" 00:00:00";
			System.out.println("fecha mensual "+fechaInicio);
		}
		
		System.out.println("sensor seleccionado: "+sensorSeleccionado);
		System.out.println("fache inicio : "+fechaInicio);
		System.out.println("fache fin : "+fechaFin);
		MongoClient mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));

		BasicDBObject query = new BasicDBObject();
		query.put("n", nodoSelected.getIdentificador());
		query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
		//FindIterable<Document> busquedaNodo = collection.find(query);
		//busquedaNodo.forEach(printBlock); 
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);
		
		DBCursor d1 = coll.find(query);

			while(d1.hasNext()) {
				
				DBObject obj = d1.next();
				JSONArray ltsMediciones = new JSONArray(obj.get("ms").toString());
				fec = obj.get("fecha").toString();
				for (int i = 0; i < ltsMediciones.length(); i++) {
					JSONObject gsonObj2 = ltsMediciones.getJSONObject(i);
					medici = gsonObj2.get("m").toString();// va el nombre del sensor
					String sensor = sensorSeleccionado.substring(0, 1);
					if (medici.equals(sensor)) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						// String sensor =sensorSeleccionado.substring(0, 1);
						if (medici.equals(sensor)) {
							val = Double.parseDouble(gsonObj2.get("v").toString());
							ltsSData.add(new MedValFec(medici, val, fec));

						}
					}
				}
				
				//cont++;
			}
		System.out.println("Connection Succesfull");
	}
}