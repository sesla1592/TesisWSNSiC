package controlador.ec.edu.ups.tesiswsnsic;

import java.io.File;
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

import dao.ec.edu.ups.tesiswsnsic.EmpresaDAO;
import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import dao.ec.edu.ups.tesiswsnsic.SensorDAO;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.MedValFec;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;
import utilidades.ec.edu.ups.tesiswsnsic.DBConnection;

@ManagedBean
@ViewScoped
public class DashboardAdminresp {
	
	private String elibre;
	private String eutilizado;
	private String etotal;
	
	Persona user;
	FacesContext contex;
	
	int 	nodoActivo=0;
	int nodoInactivo=0;
	int sensorActivo=0;
	int sensorInactivo=0;
	int empresaActiva=0;
	int empresaInactiva=0;
	
	List<Nodo> ltsNodo;
	List<Sensor> ltsSensor;
	List<Empresa> ltsEmpresa;
	Marker marker;
	Nodo nodoSelected;
	public MapModel simpleModel;
	
	@Inject
	NodoDAO nodoDAO;
	@Inject
	SensorDAO sensorDAO;
	@Inject
	EmpresaDAO empresaDAO;
	
	List<String> ltsSensores = new ArrayList<>();
	private MongoClient mongoClient;
	public Double datoTemp=0.0;
	public Double datoHum=0.0;
	public Double datoRui=0.0;
	public Double datoLum=0.0;
	String sensorSeleccionado;
	protected List<MedValFec> ltsSTemp;
	protected List<MedValFec> ltsSHum;
	protected List<MedValFec> ltsSLum;
	protected List<MedValFec> ltsSRui;
	public String fechaInicio="2018/01/11 00:00:00";
	public String fechaFin;
	public String fec;
	public String medici;
	public double val;
	
	@PostConstruct
	public void init() {
		calculaEspaciosa();
		try {
			
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("userSelected");
			System.out.println("user "+user.toString());
			ltsSTemp = new ArrayList<>();
			ltsSHum = new ArrayList<>();
			ltsSRui = new ArrayList<>();
			ltsSLum = new ArrayList<>();
			//cantidad de nodos
			ltsNodo = nodoDAO.getAllNodos();
			nodoActivo=ltsNodo.size();
			simpleModel = new DefaultMapModel();
			//cantidad de sensores
			ltsSensor = sensorDAO.getAllSensor();
			for (int i = 0; i < ltsSensor.size(); i++) {
				if(ltsSensor.get(i).getEstado() == true) {
					sensorActivo++;
				}else {
					sensorInactivo++;
				}
			}
			//empresas
			
			ltsEmpresa = empresaDAO.getAllEmpresas();
			for (int i = 0; i < ltsEmpresa.size(); i++) {
				if(ltsEmpresa.get(i).getEstado().equals("activo")) {
					empresaActiva++;
				}else {
					empresaInactiva++;
				}
			}
			Date date = new Date();
			//Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaFin=dateFormat.format(date)+" 23:59:59";
			System.out.println("Fecha: fin-->"+fechaFin);
			
			addMarker();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	public Persona getUser() {
		return user;
	}


	public void setUser(Persona user) {
		this.user = user;
	}


	public int getNodoInactivo() {
		return nodoInactivo;
	}


	public void setNodoInactivo(int nodoInactivo) {
		this.nodoInactivo = nodoInactivo;
	}


	public int getSensorActivo() {
		return sensorActivo;
	}


	public void setSensorActivo(int sensorActivo) {
		this.sensorActivo = sensorActivo;
	}


	public int getSensorInactivo() {
		return sensorInactivo;
	}


	public void setSensorInactivo(int sensorInactivo) {
		this.sensorInactivo = sensorInactivo;
	}


	public int getNodoActivo() {
		return nodoActivo;
	}


	public void setNodoActivo(int nodoActivo) {
		this.nodoActivo = nodoActivo;
	}


	public int getEmpresaActiva() {
		return empresaActiva;
	}


	public void setEmpresaActiva(int empresaActiva) {
		this.empresaActiva = empresaActiva;
	}


	public int getEmpresaInactiva() {
		return empresaInactiva;
	}


	public void setEmpresaInactiva(int empresaInactiva) {
		this.empresaInactiva = empresaInactiva;
	}


	public List<Nodo> getLtsNodo() {
		return ltsNodo;
	}


	public void setLtsNodo(List<Nodo> ltsNodo) {
		this.ltsNodo = ltsNodo;
	}


	public List<Sensor> getLtsSensor() {
		return ltsSensor;
	}


	public void setLtsSensor(List<Sensor> ltsSensor) {
		this.ltsSensor = ltsSensor;
	}


	public List<Empresa> getLtsEmpresa() {
		return ltsEmpresa;
	}


	public void setLtsEmpresa(List<Empresa> ltsEmpresa) {
		this.ltsEmpresa = ltsEmpresa;
	}


	public Marker getMarker() {
		return marker;
	}


	public void setMarker(Marker marker) {
		this.marker = marker;
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
		if(sensorSeleccionado!=null) {
			if(sensorSeleccionado.equals("Todos")) {
				this.sensorSeleccionado = sensorSeleccionado;
			}else {
				sensorSeleccionado=sensorSeleccionado.substring(0, 1);
				this.sensorSeleccionado = sensorSeleccionado;
			}
		}
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


	public void crearTipoEmpresa() {
		try{
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/crearTipoEmpresa.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void empresas() {
		try{
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/listaEmpresas.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void nodos() {
		try{
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/nodo/listaNodos.xhtml");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public String getElibre() {
		return elibre;
	}

	public void setElibre(String elibre) {
		this.elibre = elibre;
	}

	public String getEutilizado() {
		return eutilizado;
	}

	public void setEutilizado(String eutilizado) {
		this.eutilizado = eutilizado;
	}

	public String getEtotal() {
		return etotal;
	}


	public void setEtotal(String etotal) {
		this.etotal = etotal;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}


	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public void addMarker() {
		for (int i = 0; i < ltsNodo.size(); i++) {
			Marker marker = new Marker(new LatLng(ltsNodo.get(i).getLatitud(), ltsNodo.get(i).getLongitud()),
					ltsNodo.get(i).getNombre());
			marker.setShadow(ltsNodo.get(i).getIdentificador());
			simpleModel.addOverlay(marker);
		}
	}

	public void calculaEspaciosa() {
		File file = new File("C:");
		// Total espacio en disco (Bytes).
        long total = file.getTotalSpace();
        // Espacio libre en disco (Bytes).
        long libre = file.getFreeSpace(); //unallocate
        
        long elib =libre / 1024 / 1024;
        long etot = total / 1024 / 1024;
        long euti = etot - elib;
        
        elibre = String.valueOf(elib)+" MB";
        etotal = String.valueOf(etot)+" MB";
        eutilizado = String.valueOf(euti)+" MB";
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
		for (int i = 0; i < ltsNodo.size(); i++) {
			if (ltsNodo.get(i).getIdentificador().equals(identificador)) {
				nodo = ltsNodo.get(i);
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
	

	public void consulta() {
		ltsSHum= new ArrayList<>();
		ltsSTemp = new ArrayList<>();
		ltsSLum = new ArrayList<>();
		ltsSRui = new ArrayList<>();
//		System.out.println("tipo de filtro fecha "+tipoFecha);
//		if(tipoFecha.equals("Diario")) {
//			Date date = new Date();
//			//Caso 2: obtener la fecha y salida por pantalla con formato:
//			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//			String fechaPrueba = dateFormat.format(date)+" 00:00:00";
//		}
//		if(tipoFecha.equals("Semanal")) {
//			Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
//			calendar.add(Calendar.DATE, -7); //el -3 indica que se le restaran 3 dias 
//			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//			String fechaPrueba = dateFormat.format(calendar.getTime())+" 00:00:00";
//			System.out.println("fecha semanal "+fechaPrueba);
//		}
//		if(tipoFecha.equals("Mensual")) {
//			Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
//			calendar.add(Calendar.DATE, -30); //el -3 indica que se le restaran 3 dias 
//			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//			String fechaPrueba = dateFormat.format(calendar.getTime())+" 00:00:00";
//			System.out.println("fecha mensual "+fechaPrueba);
//		}
		
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
		System.out.println("tam tem: "+ltsSTemp.size());
		System.out.println("tam Hum: "+ltsSHum.size());
		System.out.println("tam lum: "+ltsSLum.size());
		System.out.println("tam rui: "+ltsSRui.size());
		System.out.println("Connection Succesfull");
	}
	
}
