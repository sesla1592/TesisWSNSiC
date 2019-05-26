package controlador.ec.edu.ups.tesiswsnsic;

import java.io.File;
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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.HorizontalBarChartModel;
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
public class DashboardAdmin {

	private String elibre;
	private String eutilizado;
	private String etotal;

	Persona user;
	FacesContext contex;

	int nodoActivo = 0;
	int nodoInactivo = 0;
	int sensorActivo = 0;
	int sensorInactivo = 0;
	int empresaActiva = 0;
	int empresaInactiva = 0;

	List<Nodo> ltsNodo;
	List<Sensor> ltsSensor;
	List<Empresa> ltsEmpresa;
	Marker marker;
	Nodo nodoSelected;
	private String sensorDescripcion;
	public MapModel simpleModel;
	public String tipoFecha;
	public String typeMedicion = "";
	
	private LineChartModel lineModel2 = new LineChartModel();
	
	@Inject
	NodoDAO nodoDAO;
	@Inject
	SensorDAO sensorDAO;
	@Inject
	EmpresaDAO empresaDAO;

	List<String> ltsSensores = new ArrayList<>();
	private MongoClient mongoClient;
	public Double datoTemp = 0.0;
	public Double datoHum = 0.0;
	public Double datoRui = 0.0;
	public Double datoLum = 0.0;
	String sensorSeleccionado;
	protected List<MedValFec> ltsSData;
	public String fechaInicio;
	public String fechaFin;
	public String fec;
	public String medici;
	public double val;
	boolean typeCalendar;

	@PostConstruct
	public void init() {
		calculaEspaciosa();
		try {

			user = (Persona) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSelected");
			System.out.println("user " + user.toString());
			// cantidad de nodos
			ltsNodo = nodoDAO.getAllNodos();
			nodoActivo = ltsNodo.size();
			simpleModel = new DefaultMapModel();
			// cantidad de sensores
			ltsSensor = sensorDAO.getAllSensor();
			for (int i = 0; i < ltsSensor.size(); i++) {
				if (ltsSensor.get(i).getEstado() == true) {
					sensorActivo++;
				} else {
					sensorInactivo++;
				}
			}
			// empresas
			System.out.println("acabo sensor ");
			ltsEmpresa = empresaDAO.getAllEmpresas();
			for (int i = 0; i < ltsEmpresa.size(); i++) {
				if (ltsEmpresa.get(i).getEstado().equals("activo")) {
					empresaActiva++;
				} else {
					empresaInactiva++;
				}
			}
			System.out.println("acabo empresa ");
			Date date = new Date();
			// Caso 2: obtener la fecha y salida por pantalla con formato:
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			fechaFin = dateFormat.format(date) + " 23:59:59";
			System.out.println("Fecha: fin-->" + fechaFin);
			System.out.println("acabo fecha ");
			addMarker();
			ltsSData = new ArrayList<>();
		} catch (Exception e) {
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
		}
		
	}

	public String getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(String tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	public String getSensorDescripcion() {
		return sensorDescripcion;
	}

	public void setSensorDescripcion(String sensorDescripcion) {
		this.sensorDescripcion = sensorDescripcion;
	}

	public void crearTipoEmpresa() {
		try {
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/crearTipoEmpresa.xhtml");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String getTypeMedicion() {
		return typeMedicion;
	}

	public void setTypeMedicion(String typeMedicion) {
		this.typeMedicion = typeMedicion;
	}

	public void empresas() {
		try {
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/empresa/listaEmpresas.xhtml");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void nodos() {
		try {
			contex = FacesContext.getCurrentInstance();
			contex.getExternalContext().redirect("/TesisWSNSiC/faces/admin/nodo/listaNodos.xhtml");
		} catch (Exception e) {
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

	public List<MedValFec> getLtsSData() {
		return ltsSData;
	}

	public void setLtsSData(List<MedValFec> ltsSData) {
		this.ltsSData = ltsSData;
	}

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}

	public void addMarker() {
		for (int i = 0; i < ltsNodo.size(); i++) {
			Marker marker = new Marker(new LatLng(ltsNodo.get(i).getLatitud(), ltsNodo.get(i).getLongitud()),
					ltsNodo.get(i).getNombre());
			marker.setShadow(ltsNodo.get(i).getIdentificador());
			marker.setIcon("/TesisWSNSiC/faces/admin/loading.gif");
			simpleModel.addOverlay(marker);
		}
	}

	public void calculaEspaciosa() {
		File file = new File("C:");
		// Total espacio en disco (Bytes).
		long total = file.getTotalSpace();
		// Espacio libre en disco (Bytes).
		long libre = file.getFreeSpace(); // unallocate

		long elib = libre / 1024 / 1024;
		long etot = total / 1024 / 1024;
		long euti = etot - elib;

		elibre = String.valueOf(elib) + " MB";
		etotal = String.valueOf(etot) + " MB";
		eutilizado = String.valueOf(euti) + " MB";
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
		for (int i = 0; i < ltsNodo.size(); i++) {
			if (ltsNodo.get(i).getIdentificador().equals(identificador)) {
				nodo = ltsNodo.get(i);
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

		mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));
		BasicDBObject query = new BasicDBObject();
		query.put("n", nodoSelected.getIdentificador());

		// busquedaNodo.forEach(printBlock);
		@SuppressWarnings("deprecation")
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

	public void graficaDatos() {
		
		if (nodoSelected != null) {
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

			} else {
				System.out.println("fecha por calendario");
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String fec_Inicio = df.format(fechaInicio);
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate fecha_Ini = LocalDate.parse(fec_Inicio, fmt);

				String fec_fin = df.format(fechaInicio);
				LocalDate fecha_fin = LocalDate.parse(fec_fin, fmt);

				Period periodo = Period.between(fecha_Ini, fecha_fin);
				
				System.out.println("total de dias "+periodo.getDays());
				
			}

			System.out.println("sensor seleccionado: " + sensorSeleccionado);
			System.out.println("fache inicio : " + fechaInicio);
			System.out.println("fache fin : " + fechaFin);

			System.out.println("sensor seleccionado: " + sensorSeleccionado);
			mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));

			BasicDBObject query = new BasicDBObject();
			query.put("n", nodoSelected.getIdentificador());
			query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
			// FindIterable<Document> busquedaNodo = collection.find(query);
			// busquedaNodo.forEach(printBlock);
			DB db = mongoClient.getDB(DBConnection.dbname);
			DBCollection coll = db.getCollection(DBConnection.dbcollection);

			DBCursor d1 = coll.find(query);
			// int cont=0;
			// System.out.println("contador "+cont+ " tam "+d1.count() +" - "+d1.length());

			System.out.println("selecciono uno");
			while (d1.hasNext()) {

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

					// cont++;
				}

			}
			System.out.println("Connection Succesfull");
			prueba();
		}

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

	
	public void prueba() {
		Sensor sensor= new Sensor();
		for (int i = 0; i < ltsSensor.size(); i++) {
			if(ltsSensor.get(i).getNombreCompleto().equals(sensorSeleccionado)) {
				sensor = ltsSensor.get(i);
			}
		}
		int max =0;
		int min =50;
		for (int i = 0; i < ltsSData.size(); i++) {
			if(ltsSData.get(i).getValor()>max) {
				max = (int) ltsSData.get(i).getValor();
			}
			if(ltsSData.get(i).getValor()<min) {
				min = (int) ltsSData.get(i).getValor();
			}
		}
		max = max +5;
		if(min<=5) {
			min=0;
		}else {
			min = min -5;
		}
		lineModel2 = initCategoryModel();
        lineModel2.setTitle(sensorSeleccionado);
        lineModel2.setAnimate(true);
        lineModel2.setZoom(true);
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        sensorDescripcion=sensor.getDescripcion_web();
        
        Axis yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel(sensor.getMedicion());
        
        yAxis.setMin(min);
        yAxis.setMax(max);
        
        DateAxis axis = new DateAxis();
        axis.setTickAngle(-50);
        axis.setTickFormat("%b %#d, %Y");
 
        lineModel2.getAxes().put(AxisType.X, axis);
	}
	
	private LineChartModel initCategoryModel() {
		LineChartModel model = new LineChartModel();
 
        ChartSeries series1 = new ChartSeries();
        series1.setLabel(sensorSeleccionado);
        for (int i = 0; i < ltsSData.size(); i++) {
        		series1.set( ltsSData.get(i).fecha, ltsSData.get(i).getValor());
		}
//        series1.set(1, 2);
//        series1.set(2, 1);
//        series1.set(3, 3);
//        series1.set(4, 6);
//        series1.set(5, 8);
 
        model.addSeries(series1);
 
        return model;
    }
}
