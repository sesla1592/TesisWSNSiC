package controlador.ec.edu.ups.tesiswsnsic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import org.apache.commons.codec.binary.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import dao.ec.edu.ups.tesiswsnsic.BlogDAO;
import dao.ec.edu.ups.tesiswsnsic.ComentariosDAO;
import dao.ec.edu.ups.tesiswsnsic.NodoDAO;
import dao.ec.edu.ups.tesiswsnsic.PersonaNodoDAO;
import dao.ec.edu.ups.tesiswsnsic.SensorDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Comentario;
import modelo.ec.edu.ups.tesiswsnsic.MedValFec;
import modelo.ec.edu.ups.tesiswsnsic.Nodo;
import modelo.ec.edu.ups.tesiswsnsic.Sensor;
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

	@Inject
	ComentariosDAO comentarioDAO;
	
	@Inject
	SensorDAO sensorDAO;
	
	@Inject
	NodoDAO nododao;
	
	Sensor sensor = new Sensor();

	public MapModel simpleModel;
	public Nodo nodoSelected;
	
	String sensorDescripcion = " ";

	protected List<MedValFec> ltsSData;
	public String fechaInicio;
	public String fechaFin;
	private Marker marker;
	private MongoClient mongoClient;
	public Double datoTemp = 0.0;
	public Double datoHum = 0.0;
	public Double datoRui = 0.0;
	public Double datoLum = 0.0;
	List<String> ltsSensores = new ArrayList<>();
	String sensorSeleccionado = "";
	public static String nuevalinea = System.getProperty("line.separator");
	public String tipoFecha;
	public String medici;
	public double val;
	public String fec;
	List<Comentario> ltsComentarios;
	public Comentario comentario;
	String imageString;
	boolean typeCalendar;
	public String typeMedicion = "";
	boolean graficar=false;
	List<Sensor> ltsSensor;
	private LineChartModel lineModel2 = new LineChartModel();
	
	//VARIABLES PARA LAS GRAFICAS
	org.json.simple.JSONObject contenedor = new org.json.simple.JSONObject();
	String contenedor1;
	org.json.simple.JSONObject contenedordinamico = new org.json.simple.JSONObject();
	JSONArray ltsMediciones;
	org.json.simple.JSONObject storageJSON = new org.json.simple.JSONObject();
	JSONObject gsonObj2;
	JSONObject gsonObj2minimo;
	//propiedades iniciales para la grafica
	String tipograficaSelect = "lineal";
	String typeSelect = "scatter";
	String modeSelect = "lines";
	String fillSelect = "none";
	//Variables maximo y minimo
	double max = 0;
	double min = 0;
	List<Double> listvalores;
	boolean maxminestablecidos = false;
	org.json.simple.JSONObject contenedormaximos = new org.json.simple.JSONObject();
	String contenedor1maximos;
	org.json.simple.JSONObject contenedorminimos = new org.json.simple.JSONObject();
	String contenedor1minimos;
	
	protected List<MedValFec> ltsDataSensor;

	@PostConstruct
	public void init() {

		try {
			FacesContext context = FacesContext.getCurrentInstance ();
			Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
	        int idBlog = Integer.parseInt(paramMap.get ("idBlog"));
	        System.out.println("------------> "+idBlog);
	        
			//// comentar
			
			blog = blogDAO.findById(idBlog);
			System.out.println("blog " + blog.toString());
			// comentarios
			ltsComentarios = comentarioDAO.allComByBlog(blog.getId());
			System.out.println("comentarios " + ltsComentarios.size());
			//
			ltsNodos = personaNodoDAO.ltsNodosByUser(blog.getEmpresa().getPersonas().get(0).getId());
			System.out.println("tam nodos em " + ltsNodos.size());
			ltsSensor = sensorDAO.getAllSensor();
			ltsSData = new ArrayList<>();
			simpleModel = new DefaultMapModel();
			// recuperaDatos();
			addMarker();
			// fecha actual
			Date date = new Date();
			// Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaFin = dateFormat.format(date) + " 23:59:59";
			System.out.println("Fecha: fin-->" + fechaFin);
			blog.setVisitas(blog.getVisitas() + 1);
			blogDAO.update(blog);
			comentario = new Comentario();
			// imagen
			// String fileName ="/Users/paul/pruebaaaaa.png";
			// FileOutputStream fos = new FileOutputStream(new File(fileName));
			// fos.write(blog.getImagen());

			imageString = new String(Base64.encodeBase64(blog.getImagen()));
			// fos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("error la extraer blog");
		}

	}

	public String getSensorDescripcion() {
		return sensorDescripcion;
	}

	public void setSensorDescripcion(String sensorDescripcion) {
		this.sensorDescripcion = sensorDescripcion;
	}

	private StreamedContent dbImage;

	public StreamedContent getDbImage() {
		return dbImage;
	}

	public void setDbImage(StreamedContent dbImage) {
		this.dbImage = dbImage;
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
		}catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un nodo");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// TODO: handle exception
		}
	}

	public String getTipograficaSelect() {
		System.out.println();
		return tipograficaSelect;
	}

	public String getModeSelect() {
		return modeSelect;
	}
	
	public String getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(String tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	public List<Comentario> getLtsComentarios() {
		return ltsComentarios;
	}

	public void setLtsComentarios(List<Comentario> ltsComentarios) {
		this.ltsComentarios = ltsComentarios;
	}

	public Comentario getComentario() {
		return comentario;
	}

	public void setComentario(Comentario comentario) {
		this.comentario = comentario;
	}

	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public String getTypeMedicion() {
		return typeMedicion;
	}

	public void setTypeMedicion(String typeMedicion) {
		this.typeMedicion = typeMedicion;
	}

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}

	public void addMarker() {
		for (int i = 0; i < ltsNodos.size(); i++) {
			String descripcion = "Sensores\n";
			for (int j = 0; j < ltsNodos.get(i).getLtssensores().size(); j++) {
				descripcion = descripcion + ltsNodos.get(i).getLtssensores().get(j).getNombre() + "\n";
			}
			Marker marker = new Marker(new LatLng(ltsNodos.get(i).getLatitud(), ltsNodos.get(i).getLongitud()),
					ltsNodos.get(i).getNombre(), descripcion);
			marker.setShadow(ltsNodos.get(i).getIdentificador());
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

		for (int i = 0; i < ltsSensores.size(); i++) {
			System.out.println("-> " + ltsSensores.get(i));
			// nodoSelected.getLtssensores().size()
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
		datoTemp = 0.0;
		datoHum = 0.0;
		datoRui = 0.0;
		datoLum = 0.0;

		Sensor sensorobj = new Sensor();
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
				sensorobj = ltsSensor.get(i);
				sensorDescripcion += sensorobj.getDescripcion_web()+nuevalinea;
				sensorDescripcion.replace("null", "");
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
	
	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	//public String tipograficaSelect = "scatter";
	//public String mode = "lines";
	//public String fill = "none";
	public void setTipograficaSelect(String tipograficaSelect) {
		try {
			System.out.println("GRAFICA SELECCIONADA:  "+tipograficaSelect);
			if(this.tipograficaSelect.equals("lineal")) {
				System.out.println("LINEAL");
				typeSelect = "scatter";
				modeSelect = "lines";
				fillSelect = "none";
			}
			if(this.tipograficaSelect.equals("linealdispersion")) {
				System.out.println("LINEAL");
				typeSelect = "scatter";
				modeSelect = "lines+markers";
				fillSelect = "none";
			}
			if(this.tipograficaSelect.equals("barra")) {
				System.out.println("BARRA");
				typeSelect = "bar";
				modeSelect = "markers";
				fillSelect = "none";	
			}
			if(this.tipograficaSelect.equals("dispersion")) {
				System.out.println("DISPERSION");
				typeSelect = "scatter";
				modeSelect = "markers";
				fillSelect = "none";	
			}
			if(this.tipograficaSelect.equals("area")) {
				System.out.println("AREA");
				typeSelect = "scatter";
				modeSelect = "lines";
				fillSelect = "tonexty";
			}
			if(this.tipograficaSelect.equals("histograma")) {
				System.out.println("HISTOGRAMA");
				typeSelect = "histogram";
				modeSelect = "lines";
				fillSelect = "none";
			}
			if(this.tipograficaSelect.equals("histograma2dcontorno")) {
				System.out.println("HISTOGRAMAA-2D");
				typeSelect = "histogram2dcontour";
				modeSelect = "lines";
				fillSelect = "none";
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		this.tipograficaSelect = tipograficaSelect;
	}

	public void grafica() {
		System.out.println("--------INGRESA A PLOTLY GRAFICA--------");
		contenedor = new org.json.simple.JSONObject();
		org.json.simple.JSONArray company = new org.json.simple.JSONArray();
		org.json.simple.JSONObject obj1 = new org.json.simple.JSONObject();
		
		org.json.simple.JSONObject obj1minimo = new org.json.simple.JSONObject();
		org.json.simple.JSONArray companyminimo = new org.json.simple.JSONArray();
		
		org.json.simple.JSONObject obj1maximo = new org.json.simple.JSONObject();
		org.json.simple.JSONArray companymaximo = new org.json.simple.JSONArray();
		if (nodoSelected != null) {
			//FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Seleccione un nodo");
			//FacesContext.getCurrentInstance().addMessage(null, msg);
		//} else {
			ltsSData = new ArrayList<>();
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
			ltsSData = new ArrayList<>();
		
		System.out.println("hay q graficar "+graficar);
		if(!graficar) {
			//muestra un mensaje de fechas invalidas
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Graficas","Fechas invalidas.");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		ltsDataSensor = new ArrayList<>();
		
			System.out.println("sensor seleccionado: " + sensorSeleccionado);
			System.out.println("fecha inicio " + fechaInicio);
			System.out.println("fecha fin " + fechaFin);
			//// fecha fin

			System.out.println("sensor seleccionado: " + sensorSeleccionado);
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
			String nsensor = "";
			String tipesensor = "";
			String lat = "";
			String lon = "";
			String direccion = "";
			String unidadmedida = "";
			listvalores = new ArrayList<Double>();
			while (d1.hasNext()) {

				DBObject obj = d1.next();
				JSONArray ltsMediciones = new JSONArray(obj.get("ms").toString());
				fec = obj.get("fecha").toString().replace("/", "-");
				nsensor = obj.get("n").toString();
				direccion = nododao.selectDireccion(nsensor);
				lat = obj.get("la").toString();
				lon = obj.get("lo").toString();
				for (int i = 0; i < ltsMediciones.length(); i++) {
					gsonObj2 = ltsMediciones.getJSONObject(i);
					medici = gsonObj2.get("m").toString();// va el nombre del sensor
					tipesensor = retornaTipoSensor(medici);
					unidadmedida = retornaUnidadMed(medici);
					System.out.println("MEDICI(n)   :"+medici);
					String sensor = sensorSeleccionado.substring(0, 1);
					System.out.println("SENSOR(n)   :"+sensor);
					
					if (medici.equals(sensor)) {
						val = Double.parseDouble(gsonObj2.get("v").toString());

						if (medici.equals(sensor)) {
							System.out.println("ALMACENAR VALOR D: "+Double.parseDouble(gsonObj2.get("v").toString()));
							listvalores.add(Double.parseDouble(gsonObj2.get("v").toString()));
							System.out.println("PASA LIST VALORES");
							val = Double.parseDouble(gsonObj2.get("v").toString());
							System.out.println("PASA VAL");
							MedValFec medicionValue = new MedValFec(medici, val, fec);
							System.out.println("PASA MedValFec medicionValue");
							ltsDataSensor.add(medicionValue);
							System.out.println("PASA ltsDataSensor");
							if(maxminestablecidos==true) {
								//CARGAR LOS VALORES DE MAXIMOS Y MINIMOS
								if(val==max) {
								}else if (val==min) {
								}	
							}
							
							obj1.put("codNodo", nsensor.toUpperCase());
							obj1.put("fecha", fec);
							
							obj1.put("latitud", lat);
							obj1.put("longitud", lon);
							obj1.put("tsensor", tipesensor);
							obj1.put("valor", val);
							obj1.put("direccion", direccion);
							obj1.put("unidadmedida", unidadmedida);
							company.add(obj1);
							obj1 = new org.json.simple.JSONObject();
					}
				}

			}			
				contenedormaximos.put("data", companymaximo);
				contenedor1maximos = contenedormaximos.toString();
				System.out.println("contenedor1maximos :"+contenedor1maximos);
				
				contenedorminimos.put("data", companyminimo);
				contenedor1minimos = contenedorminimos.toString();
				System.out.println("contenedor1minimos :"+contenedor1minimos);
				
				contenedor.put("data", company);
				contenedor1 = contenedor.toString();
				System.out.println("LTSDATASENSORES (Str):  "+ltsDataSensor.toString());
		}
			estableceMaxMin(listvalores);
		}
	}

	public void estableceMaxMin(List<Double> listv) {
		min=max=0;
		for(int i = 0 ; i < listv.size(); i++) {
			if(max < listv.get(i)) {
				max = listv.get(i);
			}
		}
		min = max;
		for(int i = 0 ; i < listv.size(); i++) {
			if(min > listv.get(i)) {
				min = listv.get(i);
			}
		}
		System.out.println("MAX: "+max+",    MIN:"+min);
		maxminestablecidos = true;
		putMaxMin();
	}
	
	public void putMaxMin() {
		org.json.simple.JSONObject obj1minimo = new org.json.simple.JSONObject();
		org.json.simple.JSONArray companyminimo = new org.json.simple.JSONArray();
		
		org.json.simple.JSONObject obj1maximo = new org.json.simple.JSONObject();
		org.json.simple.JSONArray companymaximo = new org.json.simple.JSONArray();
		
		System.err.println("PUTTING MAX MIN");
		MongoClient mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));

		BasicDBObject query = new BasicDBObject();
		query.put("n", nodoSelected.getIdentificador());
		query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
		// FindIterable<Document> busquedaNodo = collection.find(query);
		// busquedaNodo.forEach(printBlock);
		DB db = mongoClient.getDB(DBConnection.dbname);
		DBCollection coll = db.getCollection(DBConnection.dbcollection);

		DBCursor d1 = coll.find(query);
		System.out.println("FECHA INICIO FIN: "+fechaInicio+" "+fechaFin+"     QUERY:"+query.get("n")+" "+query.get("fecha"));
		
		fec = "";
		String nsensor = "";
		String tipesensor = "";
		String lat = "";
		String lon = "";
		String direccion = "";
		String unidadmedida = "";
		listvalores = new ArrayList<Double>();

		while (d1.hasNext()) {
			DBObject obj = d1.next();

			JSONArray ltsMediciones = new JSONArray(obj.get("ms").toString());
			
			fec = obj.get("fecha").toString().replace("/", "-");
			nsensor = obj.get("n").toString();
			direccion = nododao.selectDireccion(nsensor);
			lat = obj.get("la").toString();
			lon = obj.get("lo").toString();
//			System.out.println("DIRECCION:  "+direccion);
//			System.out.println("lat:  "+lat);
//			System.out.println("lon:  "+lon);
			for (int i = 0; i < ltsMediciones.length(); i++) {
//				System.out.println("LTS(n)  :"+ltsMediciones.getJSONObject(i));
				gsonObj2 = ltsMediciones.getJSONObject(i);
				medici = gsonObj2.get("m").toString();// va el nombre del sensor, referente al tipo de sensor
				tipesensor = retornaTipoSensor(medici);
				unidadmedida = retornaUnidadMed(medici);
//				System.out.println("MEDICI(n)   :"+medici);
				String sensor = sensorSeleccionado.substring(0, 1);
//				System.out.println("SENSOR(n)   :"+sensor);
				if (medici.equals(sensor)) {
					val = Double.parseDouble(gsonObj2.get("v").toString());
					// String sensor =sensorSeleccionado.substring(0, 1);
					if (medici.equals(sensor)) {
						listvalores.add(Double.parseDouble(gsonObj2.get("v").toString()));
						val = Double.parseDouble(gsonObj2.get("v").toString());
						MedValFec medicionValue = new MedValFec(medici, val, fec);
						ltsDataSensor.add(medicionValue);
						System.out.println("ESTO VAL: "+val+",   ESTO MIN: "+min);
						if(val==max) {
							System.out.println("MAXIMO ES: "+fec);
							
							obj1maximo.put("codNodo", nsensor.toUpperCase());
							obj1maximo.put("fecha", fec);
							
							obj1maximo.put("latitud", lat);
							obj1maximo.put("longitud", lon);
							obj1maximo.put("tsensor", tipesensor);
							obj1maximo.put("valor", val);
							obj1maximo.put("direccion", direccion);
							obj1maximo.put("unidadmedida", unidadmedida);
							companymaximo.add(obj1maximo);
							obj1maximo = new org.json.simple.JSONObject();							
						}else if (val==min) {
							System.out.println("MINIMO ES: "+fec);
							
							obj1minimo.put("codNodo", nsensor.toUpperCase());
							obj1minimo.put("fecha", fec);
							
							obj1minimo.put("latitud", lat);
							obj1minimo.put("longitud", lon);
							obj1minimo.put("tsensor", tipesensor);
							obj1minimo.put("valor", val);
							obj1minimo.put("direccion", direccion);
							obj1minimo.put("unidadmedida", unidadmedida);
							companyminimo.add(obj1minimo);
							obj1minimo = new org.json.simple.JSONObject();
															
						}	
					}
				}
			}	
		}
		if(tipograficaSelect.equals("histograma2dcontorno") || tipograficaSelect.equals("histograma")) {
			contenedormaximos.put("data", companymaximo);
			contenedor1maximos = "";
			System.out.println("contenedor1maximos :"+contenedor1maximos);
			
			contenedorminimos.put("data", companyminimo);
			contenedor1minimos = "";
			System.out.println("contenedor1minimos :"+contenedor1minimos);
		}else {
		contenedormaximos.put("data", companymaximo);
		contenedor1maximos = contenedormaximos.toString();
		System.out.println("contenedor1maximos :"+contenedor1maximos);
		
		contenedorminimos.put("data", companyminimo);
		contenedor1minimos = contenedorminimos.toString();
		System.out.println("contenedor1minimos :"+contenedor1minimos);
		}
	}
	
	public String retornaUnidadMed(String umed) {
		if(umed.equals("L")) {
			return "lux";
		}else if(umed.equals("H")) {
			return "%";
		}else if(umed.equals("R")) {
			return "dbs";
		}else if(umed.equals("T")) {
			return "ºC";	
		}
		return null;		
	}
	
	public String retornaTipoSensor(String inic) {
		if(inic.equals("L")) {
			return "Luminosidad";
		}else if(inic.equals("H")) {
			return "Humedad";
		}else if(inic.equals("R")) {
			return "Ruido";
		}else if(inic.equals("T")) {
			return "Temperatura";	
		}
		return null;
	}
	
	/* PLOTTY */
	 public void Ploty() {
		 grafica();
	 }

	 
	public String getContenedor1() {
		return contenedor1;
	}

	public void setContenedor1(String contenedor1) {
		this.contenedor1 = contenedor1;
	}

	public String getTypeSelect() {
		return typeSelect;
	}

	public void setTypeSelect(String typeSelect) {
		this.typeSelect = typeSelect;
	}
	
	public String getFillSelect() {
		return fillSelect;
	}

	public void setFillSelect(String fillSelect) {
		this.fillSelect = fillSelect;
	}

	public String getContenedor1maximos() {
		return contenedor1maximos;
	}

	public void setContenedor1maximos(String contenedor1maximos) {
		this.contenedor1maximos = contenedor1maximos;
	}

	public String getContenedor1minimos() {
		return contenedor1minimos;
	}

	public void setContenedor1minimos(String contenedor1minimos) {
		this.contenedor1minimos = contenedor1minimos;
	}
	
	public void onDateSelectInicio(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		fechaInicio = format.format(event.getObject()) + " 00:00:00";
		System.out.println("fecha seleccionada " + fechaInicio);
		typeCalendar = true;
		// facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "Date Selected", format.format(event.getObject())));
	}

	public void onDateSelectFin(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		fechaFin = format.format(event.getObject()) + " 23:59:59";
		System.out.println("fecha fin " + fechaFin);
		typeCalendar = true;
		// facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
		// "Date Selected", format.format(event.getObject())));
	}

	public void cambioDeFecha(ValueChangeEvent e) {
		typeCalendar = false;
	}

	public void guardarComentario() {
		System.out.println("cometario " + comentario.toString());
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		comentario.setFecha(dateFormat.format(date));
		comentario.setBlog(blog);
		comentarioDAO.insert(comentario);
		comentario = new Comentario();
		ltsComentarios = comentarioDAO.allComByBlog(blog.getId());

	}
	
	public void grafica_datos() {
		//filtro de 12 datos
		
		
		sensor = new Sensor();
		for (int i = 0; i < ltsSensor.size(); i++) {
			if (ltsSensor.get(i).getNombreCompleto().equals(sensorSeleccionado)) {
				sensor = ltsSensor.get(i);
			}
		}
		int max = 0;
		int min = 50;
		for (int i = 0; i < ltsSData.size(); i++) {
			if (ltsSData.get(i).getValor() > max) {
				max = (int) ltsSData.get(i).getValor();
			}
			if (ltsSData.get(i).getValor() < min) {
				min = (int) ltsSData.get(i).getValor();
			}
		}
		max = max + 5;
		if (min <= 5) {
			min = 0;
		} else {
			min = min - 5;
		}
		
		sensorDescripcion = sensor.getDescripcion_web();
		System.out.println("SENSOR DESCRIPCION: " +sensorDescripcion);
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
		System.out.println("tam lista "+ltsSData.size());
		while(ltsSData.size()>=13) {
			int pos = (int) (Math.random() * ltsSData.size());
			ltsSData.remove(pos);
		}
		System.out.println("tam lista "+ltsSData.size());
		for (int i = 0; i < ltsSData.size(); i++) {
			series1.set(ltsSData.get(i).fecha, ltsSData.get(i).getValor());
		}
		// series1.set(1, 2);
		// series1.set(2, 1);
		// series1.set(3, 3);
		// series1.set(4, 6);
		// series1.set(5, 8);

		model.addSeries(series1);

		return model;
	}
}
