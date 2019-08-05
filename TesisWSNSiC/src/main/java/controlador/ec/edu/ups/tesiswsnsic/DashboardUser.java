package controlador.ec.edu.ups.tesiswsnsic;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

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
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaNodoDAO;
import dao.ec.edu.ups.tesiswsnsic.SensorDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Empresa;
import modelo.ec.edu.ups.tesiswsnsic.MedValFec;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Persona;
import modelo.ec.edu.ups.tesiswsnsic.PersonaNodo;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;
import utilidades.ec.edu.ups.tesiswsnsic.DBConnection;
import utilidades.ec.edu.ups.tesiswsnsic.Reporte;
import utilidades.ec.edu.ups.tesiswsnsic.SessionUtils;

@ManagedBean
@ViewScoped
public class DashboardUser {
	
	org.json.simple.JSONObject contenedor = new org.json.simple.JSONObject();
	String contenedor1;

	@Inject
	PersonaNodoDAO personaNodoDAO;
	@Inject
	NodoDAO nodoDAO;
	
	//
	String csvglobal; 

	private Marker marker;
	Persona user;
	public MapModel simpleModel;
	List<Nodo> ltsMyNodos = new ArrayList<>();
	List<String> ltsSensores = new ArrayList<>();
	String sensorSeleccionado = "";
	List<Sensor> ltsSensor;
	boolean graficar = false;
	boolean typeCalendar; // true= calendario ; false = combo

	protected List<MedValFec> ltsDataSensor;

	// private String URL_FOLDER="C:\\Users\\rommel.inga\\tesisfiles\\";
	private String URL_FOLDER = "/Users/paul/dataWSNSIC/";

	private List<Reporte> ltsReporte;

	public String medici;
	public double val;
	public String fec;
	public String medicion = "Temperatura";
	public Nodo nodoSelected;
	private MongoClient mongoClient;
	public Double datoTemp = 0.0;
	public Double datoHum = 0.0;
	public Double datoRui = 0.0;
	public Double datoLum = 0.0;
	public String fechaInicio;
	public String fechaFin;
	public String typeMedicion = "";

	public String tipoFecha;

	private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLDITALIC);
	private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
	private static final Font headerTable = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
	
	private LineChartModel lineModel2 = new LineChartModel();
	
	///nuevo
	List<Nodo> ltsAllNodos= new ArrayList<>();

	@Inject
	SensorDAO sensorDAO;
	
	@PostConstruct
	public void init() {
		try {
			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			//System.out.println("user " + user.getId());
			ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
			ltsAllNodos = nodoDAO.getAllNodos();
			for (int i = 0; i < ltsAllNodos.size(); i++) {
				for (int j = 0; j < ltsMyNodos.size(); j++) {
					if(ltsAllNodos.get(i).getId()==ltsMyNodos.get(j).getId()) {
						ltsAllNodos.remove(i);
					}
				}
			}
			////
			System.out.println("lista nodos" + ltsMyNodos.size());
			simpleModel = new DefaultMapModel();

			ltsDataSensor = new ArrayList<>();
			// recuperaDatos();
			addMarker();
			// fecha actual
			Date date = new Date();
			// Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaFin = dateFormat.format(date) + " 23:59:59";
			System.out.println("Fecha: fin-->" + fechaFin);
			// lineModel1 = new LineChartModel();
			// createLineModels();
			ltsSensor = sensorDAO.getAllSensor();
			
			//user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			empresa =  user.getEmpresa();
			System.out.println("empresa usuario" + user.getEmpresa().toString());
			nuevoblog = new Blog();
			ltsMyBlogs = blogDao.blogByEmpresa(user.getEmpresa().getId());
			System.out.println("blogs " + ltsMyBlogs.size());
			
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

	// public String getFiltroBusqueda() {
	// return filtroBusqueda;
	// }
	//
	// public void setFiltroBusqueda(String filtroBusqueda) {
	// this.filtroBusqueda = filtroBusqueda;
	// }

	public String getMedicion() {
		return medicion;
	}

	public void setMedicion(String medicion) {
		this.medicion = medicion;
	}

	public String getSensorSeleccionado() {
		return sensorSeleccionado;
	}

	public String getTypeMedicion() {
		return typeMedicion;
	}

	public void setTypeMedicion(String typeMedicion) {
		this.typeMedicion = typeMedicion;
	}

	public void setSensorSeleccionado(String sensorSeleccionado) {
		// sensorSeleccionado = sensorSeleccionado.substring(0, 1);
		try {
			this.sensorSeleccionado = sensorSeleccionado;
			if (this.sensorSeleccionado.equals("Temperatura")) {
				typeMedicion = " C";
			}
			if (this.sensorSeleccionado.equals("Humedad")) {
				typeMedicion = " %";
			}
			if (this.sensorSeleccionado.equals("Ruido")) {
				typeMedicion = " dB";
			}
			if (this.sensorSeleccionado.equals("Luminosidad")) {
				typeMedicion = " Lux";
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un nodo");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		// System.out.println("--> " + this.sensorSeleccionado);
	}

	public List<String> getLtsSensores() {
		return ltsSensores;
	}

	public void setLtsSensores(List<String> ltsSensores) {
		this.ltsSensores = ltsSensores;
	}

	// public List<MedValFec> getLtsSTemp() {
	// return ltsSTemp;
	// }
	//
	// public void setLtsSTemp(List<MedValFec> ltsSTemp) {
	// this.ltsSTemp = ltsSTemp;
	// }
	//
	// public List<MedValFec> getLtsSHum() {
	// return ltsSHum;
	// }
	//
	// public void setLtsSHum(List<MedValFec> ltsSHum) {
	// this.ltsSHum = ltsSHum;
	// }

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

	// public List<MedValFec> getLtsSLum() {
	// return ltsSLum;
	// }
	//
	// public void setLtsSLum(List<MedValFec> ltsSLum) {
	// this.ltsSLum = ltsSLum;
	// }
	//
	// public List<MedValFec> getLtsSRui() {
	// return ltsSRui;
	// }
	//
	// public void setLtsSRui(List<MedValFec> ltsSRui) {
	// this.ltsSRui = ltsSRui;
	// }
	
	public Double getDatoRui() {
		return datoRui;
	}

	public String getCsvglobal() {
		return csvglobal;
	}

	public void setCsvglobal(String csvglobal) {
		this.csvglobal = csvglobal;
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

	public List<MedValFec> getLtsDataSensor() {
		return ltsDataSensor;
	}

	public void setLtsDataSensor(List<MedValFec> ltsDataSensor) {
		this.ltsDataSensor = ltsDataSensor;
	}

	public boolean isTypeCalendar() {
		return typeCalendar;
	}

	public void setTypeCalendar(boolean typeCalendar) {
		this.typeCalendar = typeCalendar;
	}

	
	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}
	
	public List<Nodo> getLtsAllNodos() {
		return ltsAllNodos;
	}

	public void setLtsAllNodos(List<Nodo> ltsAllNodos) {
		this.ltsAllNodos = ltsAllNodos;
	}

	public void addMarker() {
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			String descripcion = "Sensores\n";
			for (int j = 0; j < ltsMyNodos.get(i).getLtssensores().size(); j++) {
				descripcion = descripcion + ltsMyNodos.get(i).getLtssensores().get(j).getNombre() + "\n";
			}
			Marker marker = new Marker(new LatLng(ltsMyNodos.get(i).getLatitud(), ltsMyNodos.get(i).getLongitud()),
					ltsMyNodos.get(i).getNombre(), descripcion);
			marker.setShadow(ltsMyNodos.get(i).getIdentificador());
			simpleModel.addOverlay(marker);

		}
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
		nodoSelected = findNodo(marker.getShadow());
		System.out.println(
				"Nodo Selected" + nodoSelected.toString() + " sensores: " + nodoSelected.getLtssensores().size());
		ltsSensores = new ArrayList<>();

		for (int i = 0; i < nodoSelected.getLtssensores().size(); i++) {
			ltsSensores.add("" + nodoSelected.getLtssensores().get(i).getNombreCompleto());
			// nodoSelected.getLtssensores().size()
		}

		// for (int i = 0; i < ltsSensores.size(); i++) {
		// System.out.println("-> " + ltsSensores.get(i));
		// // nodoSelected.getLtssensores().size()
		// }
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

	public void grafica() {
		if (nodoSelected != null) {
			System.out.println("boolean typo calendario " + typeCalendar);
			if (typeCalendar == false) {
				System.out.println("fecha por combo :" + tipoFecha);
				// cambio las fechas

				if (tipoFecha.equals("Diario")) {
					Date date = new Date();
					// Caso 2: obtener la fecha y salida por pantalla con formato:
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					fechaInicio = dateFormat.format(date) + " 00:00:00";
				}
				if (tipoFecha.equals("Semanal")) {
					Calendar calendar = Calendar.getInstance(); // obtiene la fecha de hoy
					calendar.add(Calendar.DATE, -7); // el -3 indica que se le restaran 3 dias
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					fechaInicio = dateFormat.format(calendar.getTime()) + " 00:00:00";
					System.out.println("fecha semanal " + fechaInicio);
				}
				if (tipoFecha.equals("Mensual")) {
					Calendar calendar = Calendar.getInstance(); // obtiene la fecha de hoy
					calendar.add(Calendar.DATE, -30); // el -3 indica que se le restaran 3 dias
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					fechaInicio = dateFormat.format(calendar.getTime()) + " 00:00:00";
					System.out.println("fecha mensual " + fechaInicio);
				}

			}
			System.out.println("fecha por calendario " + fechaInicio.substring(0, fechaInicio.length() - 9) + " - "
					+ fechaFin.substring(0, fechaFin.length() - 9));
			// String fec_Inicio =
			// df.format(fechaInicio.substring(0,fechaInicio.length()-9));
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate fecha_Ini = LocalDate.parse(fechaInicio.substring(0, fechaInicio.length() - 9), fmt);

			// String fec_fin = df.format(fechaFin.substring(0,fechaInicio.length()-9));
			LocalDate fecha_fin = LocalDate.parse(fechaFin.substring(0, fechaFin.length() - 9), fmt);

			Period periodo = Period.between(fecha_Ini, fecha_fin);
			System.out.println("total de dias " + periodo.getDays());
			
			if(periodo.getDays()>0) {
				graficar=true;
			}else {
				graficar=false;
			}
			//ltsSData = new ArrayList<>();
		
		System.out.println("hay q graficar "+graficar);
		if(!graficar) {
			//muestra un mensaje de fechas invalidas
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Graficas","Fechas invalidas.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
			ltsDataSensor = new ArrayList<>();

			System.out.println("sensor seleccionado: " + sensorSeleccionado);
			System.out.println("fache inicio : " + fechaInicio);
			System.out.println("fache fin : " + fechaFin);
			MongoClient mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));

			BasicDBObject query = new BasicDBObject();
			query.put("n", nodoSelected.getIdentificador());
			query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
			// FindIterable<Document> busquedaNodo = collection.find(query);
			// busquedaNodo.forEach(printBlock);
			DB db = mongoClient.getDB(DBConnection.dbname);
			DBCollection coll = db.getCollection(DBConnection.dbcollection);

			DBCursor d1 = coll.find(query);

			fec = "";
			while (d1.hasNext()) {

				DBObject obj = d1.next();

				JSONArray ltsMediciones = new JSONArray(obj.get("ms").toString());
				fec = obj.get("fecha").toString();
				//fec = fec.substring(0, 10);
				for (int i = 0; i < ltsMediciones.length(); i++) {
					JSONObject gsonObj2 = ltsMediciones.getJSONObject(i);
					medici = gsonObj2.get("m").toString();// va el nombre del sensor
					String sensor = sensorSeleccionado.substring(0, 1);
					if (medici.equals(sensor)) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						// String sensor =sensorSeleccionado.substring(0, 1);
						if (medici.equals(sensor)) {
							val = Double.parseDouble(gsonObj2.get("v").toString());
							MedValFec medicionValue = new MedValFec(medici, val, fec);
							ltsDataSensor.add(medicionValue);

						}
					}
				}
			}

			System.out.println("Connection Succesfull");
			graficaModel();
		}
	}
	
	public void establecerangofechas() {
		if (nodoSelected != null) {
			System.out.println("boolean typo calendario " + typeCalendar);
			if (typeCalendar == false) {
				System.out.println("fecha por combo :" + tipoFecha);
				// cambio las fechas

				if (tipoFecha.equals("Diario")) {
					Date date = new Date();
					// Caso 2: obtener la fecha y salida por pantalla con formato:
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					fechaInicio = dateFormat.format(date) + " 00:00:00";
				}
				if (tipoFecha.equals("Semanal")) {
					Calendar calendar = Calendar.getInstance(); // obtiene la fecha de hoy
					calendar.add(Calendar.DATE, -7); // el -3 indica que se le restaran 3 dias
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					fechaInicio = dateFormat.format(calendar.getTime()) + " 00:00:00";
					System.out.println("fecha semanal " + fechaInicio);
				}
				if (tipoFecha.equals("Mensual")) {
					Calendar calendar = Calendar.getInstance(); // obtiene la fecha de hoy
					calendar.add(Calendar.DATE, -30); // el -3 indica que se le restaran 3 dias
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					fechaInicio = dateFormat.format(calendar.getTime()) + " 00:00:00";
					System.out.println("fecha mensual " + fechaInicio);
				}

			}
			System.out.println("fecha por calendario " + fechaInicio.substring(0, fechaInicio.length() - 9) + " - "
					+ fechaFin.substring(0, fechaFin.length() - 9));
			// String fec_Inicio =
			// df.format(fechaInicio.substring(0,fechaInicio.length()-9));
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDate fecha_Ini = LocalDate.parse(fechaInicio.substring(0, fechaInicio.length() - 9), fmt);

			// String fec_fin = df.format(fechaFin.substring(0,fechaInicio.length()-9));
			LocalDate fecha_fin = LocalDate.parse(fechaFin.substring(0, fechaFin.length() - 9), fmt);

			Period periodo = Period.between(fecha_Ini, fecha_fin);
			System.out.println("total de dias " + periodo.getDays());
			
			if(periodo.getDays()>0) {
				graficar=true;
			}else {
				graficar=false;
			}
			//ltsSData = new ArrayList<>();
		
		System.out.println("hay q graficar "+graficar);
		if(!graficar) {
			//muestra un mensaje de fechas invalidas
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Graficas","Fechas invalidas.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
			ltsDataSensor = new ArrayList<>();

			System.out.println("sensor seleccionado: " + sensorSeleccionado);
			System.out.println("fache inicio : " + fechaInicio);
			System.out.println("fache fin : " + fechaFin);
			MongoClient mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));

			BasicDBObject query = new BasicDBObject();
			query.put("n", nodoSelected.getIdentificador());
			query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
			// FindIterable<Document> busquedaNodo = collection.find(query);
			// busquedaNodo.forEach(printBlock);
			DB db = mongoClient.getDB(DBConnection.dbname);
			DBCollection coll = db.getCollection(DBConnection.dbcollection);

			DBCursor d1 = coll.find(query);

			fec = "";
			while (d1.hasNext()) {

				DBObject obj = d1.next();

				JSONArray ltsMediciones = new JSONArray(obj.get("ms").toString());
				fec = obj.get("fecha").toString();
				//fec = fec.substring(0, 10);
				for (int i = 0; i < ltsMediciones.length(); i++) {
					JSONObject gsonObj2 = ltsMediciones.getJSONObject(i);
					medici = gsonObj2.get("m").toString();// va el nombre del sensor
					String sensor = sensorSeleccionado.substring(0, 1);
					if (medici.equals(sensor)) {
						val = Double.parseDouble(gsonObj2.get("v").toString());
						// String sensor =sensorSeleccionado.substring(0, 1);
						if (medici.equals(sensor)) {
							val = Double.parseDouble(gsonObj2.get("v").toString());
							MedValFec medicionValue = new MedValFec(medici, val, fec);
							ltsDataSensor.add(medicionValue);

						}
					}
				}
			}
		}
	}
	
	public void cargaSensorSelec(){
		Sensor sensor = new Sensor();
		System.out.println("LISTADO CARGADO SIZE: "+ltsSensor.size());
		for (int i = 0; i < ltsSensor.size(); i++) {
			if (ltsSensor.get(i).getNombreCompleto().equals(sensorSeleccionado)) {
				sensor = ltsSensor.get(i);
			}
		}
		int max = 0;
		int min = 50;
		for (int i = 0; i < ltsDataSensor.size(); i++) {
			if (ltsDataSensor.get(i).getValor() > max) {
				max = (int) ltsDataSensor.get(i).getValor();
			}
			if (ltsDataSensor.get(i).getValor() < min) {
				min = (int) ltsDataSensor.get(i).getValor();
			}
		}
		max = max + 5;
		if (min <= 5) {
			min = 0;
		} else {
			min = min - 5;
		}
	/* POSIBLES DATOS  A GRAFICAR */
		System.out.println("...DATOS CARGADOS...");
		System.out.println("OBTENIDOS LTS: "+ltsDataSensor.toString());
	}
	
	/* CARGA EL NODO QUE SELECCIONO */
	
	public void cargarNodoCSV() {
		ltsReporte = new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador(), true);
		}
		formarCSV();
	}

	public void cargarNodoJSON() {
		ltsReporte = new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador(), true);
		}
		System.out.println("PRINT CONSOLE");
		formarJSON();
	}
		
	public String getContenedor1() {
		return contenedor1;
	}

	public void setContenedor1(String contenedor1) {
		this.contenedor1 = contenedor1;
	}

	public org.json.simple.JSONObject getContenedor() {
		return contenedor;
	}

	public void setContenedor(org.json.simple.JSONObject contenedor) {
		this.contenedor = contenedor;
	}

	public void formarJSON() {
		contenedor = new org.json.simple.JSONObject();
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject();

		org.json.simple.JSONArray company = new org.json.simple.JSONArray();
		for (int i = 0; i < ltsReporte.size(); i++) {

			obj.put("codNodo", ltsReporte.get(i).getCodSensor());
			obj.put("fecha", ltsReporte.get(i).getFecha());

			obj.put("latitud", ltsReporte.get(i).getLatitud());
			obj.put("longitud", ltsReporte.get(i).getLongitud());
			obj.put("sensor", ltsReporte.get(i).getSensor());
			obj.put("valor", ltsReporte.get(i).getValor());

			company.add(obj);
			obj = new org.json.simple.JSONObject();
		}
		contenedor.put("data", company);
		contenedor1 = contenedor.toString();
		System.out.println("JSON FORMADO contenedor1: "+contenedor1);
		// try-with-resources statement based on post comment below :)
	}
	
	/* FORMA EL CSV */

	public void formarCSV() {
		String[] ltsCabecera = { "Codigo Sensor", "Fecha", "Latitud", "Longitud", "Sensor", "Valor" };
		try {
			StringWriter sw = new StringWriter();
			CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(ltsCabecera);
			// writer.
			CSVPrinter csvPrinter = new CSVPrinter(sw, csvFormat);
			
			System.out.println("TAMANO CSV: "+ltsReporte.size());
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
			sw.flush();
			csvglobal = sw.toString();
			System.out.println("CSV to String Establecido: "+ csvglobal);
		}catch (Exception e) {
				System.out.println("Error al cargar"+e);
			}
	}


	
	/* PLOTTY */
	 public void Ploty() {
		 establecerangofechas();
		 cargaSensorSelec();
		 cargarNodoJSON();
		 //cargarNodoCSV();
	 }

	public void getDatosTH() {
		datoTemp = 0.0;
		datoHum = 0.0;
		datoRui = 0.0;
		datoLum = 0.0;

		mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
		BasicDBObject query = new BasicDBObject();
		query.put("n", nodoSelected.getIdentificador());

		// busquedaNodo.forEach(printBlock);
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);

		DBObject d1 = coll.findOne(query);
		if (d1 != null) {
			System.out.println("prueba F---> " + d1.toString());
			System.out.println("prueba---> " + d1.get("la"));
			System.out.println("prueba---> " + d1.get("lo"));
			JSONArray ltsSensores = new JSONArray(d1.get("ms").toString());
			for (int i = 0; i < ltsSensores.length(); i++) {
				JSONObject sensorM = ltsSensores.getJSONObject(i);
				if (sensorM.getString("m").equals("T")) {
					datoTemp = Double.parseDouble(sensorM.get("v").toString());
				}
				if (sensorM.getString("m").equals("H")) {
					datoHum = Double.parseDouble(sensorM.get("v").toString());
				}
				if (sensorM.getString("m").equals("L")) {
					datoLum = Double.parseDouble(sensorM.get("v").toString());
				}
				if (sensorM.getString("m").equals("R")) {
					datoRui = Double.parseDouble(sensorM.get("v").toString());
				}

			}
		} else {
			System.out.println("prueba---> no encontro");
		}

		System.out.println("Connection Succesfull");
	}

	public void descargarCSV() {
		ltsReporte = new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador(), true);
		}
		crearCSV();
	}

	public void onDateSelectInicio(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		fechaInicio = format.format(event.getObject()) + " 00:00:00";
		typeCalendar = true;
		System.out.println("fecha seleccionada " + fechaInicio);
		System.out.println("boolean typo calendario " + typeCalendar);
		// facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "Date Selected", format.format(event.getObject())));
	}

	public void onDateSelectFin(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		fechaFin = format.format(event.getObject()) + " 23:59:59";
		System.out.println("fecha fin " + fechaFin);
		typeCalendar = true;
		System.out.println("boolean typo calendario " + typeCalendar);
		// facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "Date Selected", format.format(event.getObject())));
	}

	public void cambioDeFecha(ValueChangeEvent e) {
		typeCalendar = false;
	}

	public void datosNodo(String codSensor, boolean historico) {

		mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
		BasicDBObject query = new BasicDBObject();
		query.put("n", codSensor);
		System.out.println("sensor selected ----> " + codSensor);
		// busquedaNodo.forEach(printBlock);
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);
		if (historico) {
			// System.out.println("fache inicio : "+fechaInicio);
			// System.out.println("fache fin : "+fechaFin);
			// System.out.println("prueba");
			// fechaInicio="2019/01/11 00:00:00";
			// fechaFin="2019/01/11 23:59:59";
			// System.out.println("fache inicio : "+fechaInicio);
			// System.out.println("fache fin : "+fechaFin);
			query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
		}

		DBCursor d1 = coll.find(query);
		double latitud = 0.0;
		double longitud = 0.0;
		String fecha = "";

		while (d1.hasNext()) {

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

				if (sensorM.getString("m").equals("T")) {
					reporte.setSensor("Temperatura");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				if (sensorM.getString("m").equals("H")) {
					reporte.setSensor("Humedad");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				if (sensorM.getString("m").equals("L")) {
					reporte.setSensor("Luminosidad");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				if (sensorM.getString("m").equals("R")) {
					reporte.setSensor("Ruido");
					reporte.setValor(Double.parseDouble(sensorM.get("v").toString()));
				}
				ltsReporte.add(reporte);

			}
		}
		
		System.out.println("tam reporte " + ltsReporte.size());
	}

	public void crearCSV() {
		String[] ltsCabecera = { "Codigo Sensor", "Fecha", "Latitud", "Longitud", "Sensor", "Valor" };
		try {
			StringWriter sw = new StringWriter();
			CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(ltsCabecera);
			// writer.
			CSVPrinter csvPrinter = new CSVPrinter(sw, csvFormat);
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
			
			sw.flush();
			
			csvglobal = sw.toString();
			
			String nameFile = "file.csv";
			// descargar
			FacesContext facesContext = FacesContext.getCurrentInstance();

			// Get HTTP response
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

			// Set response headers
			response.reset(); // Reset the response in the first place
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + nameFile + "\"");

			// Open response output stream
			OutputStream responseOutputStream = response.getOutputStream();

			byte[] backByte = sw.toString().getBytes();
			responseOutputStream.write(backByte);
			// Make sure that everything is out
			responseOutputStream.flush();

			// Close both streams
			// pdfInputStream.close();
			responseOutputStream.close();

			// JSF doc:
			// Signal the JavaServer Faces implementation that the HTTP response for this
			// request has already been generated
			// (such as an HTTP redirect), and that the request processing lifecycle should
			// be terminated
			// as soon as the current phase is completed.
			facesContext.responseComplete();

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void descargarJSON() {
		ltsReporte = new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador(), true);
		}
		// for (int i = 0; i < ltsReporte.size(); i++) {
		// System.out.println(ltsReporte.get(i).toString());
		// }
		crearJSON();
	}

	public void crearJSON() {
		org.json.simple.JSONObject contenedor = new org.json.simple.JSONObject();
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject();

		org.json.simple.JSONArray company = new org.json.simple.JSONArray();
		for (int i = 0; i < ltsReporte.size(); i++) {

			obj.put("codNodo", ltsReporte.get(i).getCodSensor());
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
		try {

			String nameFile = "file.json";
			// descargar
			FacesContext facesContext = FacesContext.getCurrentInstance();

			// Get HTTP response
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

			// Set response headers
			response.reset(); // Reset the response in the first place
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + nameFile + "\"");

			// Open response output stream
			OutputStream responseOutputStream = response.getOutputStream();

			byte[] backByte = contenedor.toString().getBytes();
			responseOutputStream.write(backByte);
			// Make sure that everything is out
			responseOutputStream.flush();

			// Close both streams
			// pdfInputStream.close();
			responseOutputStream.close();

			// JSF doc:
			// Signal the JavaServer Faces implementation that the HTTP response for this
			// request has already been generated
			// (such as an HTTP redirect), and that the request processing lifecycle should
			// be terminated
			// as soon as the current phase is completed.
			facesContext.responseComplete();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		System.out.print(obj);

	}

	public void descargarPDF() {
		ltsReporte = new ArrayList<>();
		for (int i = 0; i < ltsMyNodos.size(); i++) {
			datosNodo(ltsMyNodos.get(i).getIdentificador(), true);
		}
		// for (int i = 0; i < ltsReporte.size(); i++) {
		// System.out.println(ltsReporte.get(i).toString());
		// }
		crearPDF();
	}

	public void crearPDF() {
		try {
			com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 36, 36, 100, 54);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(URL_FOLDER + "prueba.pdf"));
			// HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			// writer.setPageEvent(event);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, stream);

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

			columnHeader = new PdfPCell(new Phrase("Codigo Nodo", headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Fecha", headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Latitud", headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Longitud", headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Sensor", headerTable));
			columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnHeader);
			columnHeader = new PdfPCell(new Phrase("Valor", headerTable));
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

				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getLatitud() + ""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);

				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getLongitud() + ""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);

				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getSensor() + ""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);

				rowPdf = new PdfPCell(new Phrase(ltsReporte.get(row).getValor() + ""));
				rowPdf.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rowPdf);
			}
			// We add the table (Añadimos la tabla)
			chapter.add(table);
			document.add(chapter);
			document.close();

			// descargar
			String nameFile = "file.pdf";
			// descargar
			FacesContext facesContext = FacesContext.getCurrentInstance();

			// Get HTTP response
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

			// Set response headers
			response.reset(); // Reset the response in the first place
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + nameFile + "\"");

			// Open response output stream
			OutputStream responseOutputStream = response.getOutputStream();

			byte[] backByte = stream.toByteArray();
			responseOutputStream.write(backByte);
			// Make sure that everything is out
			responseOutputStream.flush();

			// Close both streams
			// pdfInputStream.close();
			responseOutputStream.close();

			// JSF doc:
			// Signal the JavaServer Faces implementation that the HTTP response for this
			// request has already been generated
			// (such as an HTTP redirect), and that the request processing lifecycle should
			// be terminated
			// as soon as the current phase is completed.
			facesContext.responseComplete();

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	
	public void graficaModel() {
		Sensor sensor = new Sensor();
		for (int i = 0; i < ltsSensor.size(); i++) {
			if (ltsSensor.get(i).getNombreCompleto().equals(sensorSeleccionado)) {
				sensor = ltsSensor.get(i);
			}
		}
		int max = 0;
		int min = 50;
		for (int i = 0; i < ltsDataSensor.size(); i++) {
			if (ltsDataSensor.get(i).getValor() > max) {
				max = (int) ltsDataSensor.get(i).getValor();
			}
			if (ltsDataSensor.get(i).getValor() < min) {
				min = (int) ltsDataSensor.get(i).getValor();
			}
		}
		max = max + 5;
		if (min <= 5) {
			min = 0;
		} else {
			min = min - 5;
		}
		//filtro de 12 datos
		lineModel2 = initCategoryModel();
		lineModel2.setTitle(sensorSeleccionado);
		lineModel2.setAnimate(true);
		lineModel2.setZoom(true);
		lineModel2.setLegendPosition("e");
		lineModel2.setShowPointLabels(true);
		lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Years"));
		//sensorDescripcion = sensor.getDescripcion_web();

		Axis yAxis = lineModel2.getAxis(AxisType.Y);
		yAxis.setLabel(sensor.getMedicion());

		yAxis.setMin(min);
		yAxis.setMax(max);

		DateAxis axis = new DateAxis();
		axis.setTickAngle(-50);
		axis.setTickFormat("%b %#d, %Y %H:%M");

//		System.out.println("fecha dato "+ltsSData.get(0).fecha);
//		System.out.println("fecha dato "+ltsSData.get(ltsSData.size()-1).fecha);
		lineModel2.getAxes().put(AxisType.X, axis);
	}

	private LineChartModel initCategoryModel() {
		LineChartModel model = new LineChartModel();

		ChartSeries series1 = new ChartSeries();
		series1.setLabel(sensorSeleccionado);
		System.out.println("tam lista "+ltsDataSensor.size());
		while(ltsDataSensor.size()>=13) {
			int pos = (int) (Math.random() * ltsDataSensor.size());
			ltsDataSensor.remove(pos);
		}
		System.out.println("tam lista "+ltsDataSensor.size());
		for (int i = 0; i < ltsDataSensor.size(); i++) {
			series1.set(ltsDataSensor.get(i).getFecha(), ltsDataSensor.get(i).getValor());
		}
		// series1.set(1, 2);
		// series1.set(2, 1);
		// series1.set(3, 3);
		// series1.set(4, 6);
		// series1.set(5, 8);

		model.addSeries(series1);

		return model;
	}
	
	public void asociarNodo(Nodo nodo) {
		System.out.println("nodo "+nodo.getNombre());
		PersonaNodo personaNodo = new PersonaNodo();
		personaNodo.setEstado("true");
		personaNodo.setNodo(nodo);
		personaNodo.setPersona(user);
		personaNodoDAO.insertPersonaNodo(personaNodo);
		ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
		ltsAllNodos = nodoDAO.getAllNodos();
		System.out.println("nodos que tengo "+ltsMyNodos.size());
		System.out.println("nodos hay en la bd "+ltsAllNodos.size());
		for (int i = 0; i < ltsAllNodos.size(); i++) {
			for (int j = 0; j < ltsMyNodos.size(); j++) {
				if(ltsAllNodos.get(i).getId()==ltsMyNodos.get(j).getId()) {
					ltsAllNodos.remove(i);
				}
			}
		}
		System.out.println("nodos filtrados "+ltsAllNodos.size());
	}
	
	public void eliminarAsoNodo(Nodo nodo) {
		System.out.println("=------> entro a alieminar");
		PersonaNodo personNodo = personaNodoDAO.getByPersonNodo(user.getId(),nodo.getId());
		if(personNodo==null) {
			//error al buscar nodo
			System.out.println("error al buscar pernona nodo");
		}else{
			personaNodoDAO.remove(personNodo);
			ltsMyNodos = personaNodoDAO.ltsNodosByUser(user.getId());
			ltsAllNodos = nodoDAO.getAllNodos();
			for (int i = 0; i < ltsAllNodos.size(); i++) {
				for (int j = 0; j < ltsMyNodos.size(); j++) {
					if(ltsAllNodos.get(i).getId()==ltsMyNodos.get(j).getId()) {
						ltsAllNodos.remove(i);
					}
				}
			}
			
		}
	}

	
	//Persona user;
	Empresa empresa;
	Blog nuevoblog;
	List<Blog> ltsMyBlogs;
	
	@Inject
	BlogDAO blogDao;
	InputStream fileImag;
	
//	@PostConstruct
//	public void init() {
//		try {
//			
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("error la extraer usuario");
//			e.printStackTrace();
//		}
//	}
	
	
//	public void actualizar() {
//		blogDao.update(blog);
//	}
	
	public Blog getNuevoblog() {
		return nuevoblog;
	}


	public void setNuevoblog(Blog nuevoblog) {
		this.nuevoblog = nuevoblog;
	}


	public List<Blog> getLtsMyBlogs() {
		return ltsMyBlogs;
	}


	public void setLtsMyBlogs(List<Blog> ltsMyBlogs) {
		this.ltsMyBlogs = ltsMyBlogs;
	}


	public void actualizar(Blog blog) {
		try {
//			byte [] bytes;
//			bytes = IOUtils.toByteArray(fileImag);
//	        // Store image to DB
//	        blog.setImagen(bytes);
	        blogDao.update(blog);
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Succesful", "Informacion Actualizada.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        
		}catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Error al guardar su informacion.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void crearBlog() {
		try {
//			byte [] bytes;
//			bytes = IOUtils.toByteArray(fileImag);
//	        // Store image to DB
//	        blog.setImagen(bytes);
			nuevoblog.setEmpresa(empresa);
			byte [] bytes;
			bytes = IOUtils.toByteArray(fileImag);
	        // Store image to DB
	        nuevoblog.setImagen(bytes);
	        Date date = new Date();
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        //System.out.println("Fecha: "+dateFormat.format(date));
	        nuevoblog.setFechaPub(dateFormat.format(date));
	        blogDao.insert(nuevoblog);
	        System.out.println(nuevoblog.toString());
	        nuevoblog = new Blog();
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Succesful", "Blog Creado.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        
	        
		}catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Error al crearblog.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	 public void handleFileUpload(FileUploadEvent event) {
	        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        System.out.println("file "+event.getFile().getFileName());
	        //fileImag = event.getFile();
	        try {
	        	fileImag = event.getFile().getInputstream();
	        	byte [] bytes;
				bytes = IOUtils.toByteArray(event.getFile().getInputstream());
		        // Store image to DB
		        nuevoblog.setImagen(bytes);
		        System.out.println(nuevoblog.getImagen().length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error al pasar imagen a input");
				e.printStackTrace();
				
			}
	    }
	 public void irEditar(Blog blog) {
		 try{
				HttpSession session = SessionUtils.getSession();
				session.setAttribute("blogEdit", blog);
				FacesContext contex = FacesContext.getCurrentInstance();
				contex.getExternalContext().redirect("/TesisWSNSiC/faces/user/editBlog.xhtml");
			}catch (Exception e) {
				// TODO: handle exception
			}
	 }
	 
	 public void eliminarBlog(Blog blog) {
		 try {
			 blogDao.remove(blog);
			 ltsMyBlogs = blogDao.blogByEmpresa(user.getEmpresa().getId());
				System.out.println("blogs " + ltsMyBlogs.size());
		 }catch (Exception e) {
			 e.printStackTrace();
			// TODO: handle exception
		}
	 }
	 
}
