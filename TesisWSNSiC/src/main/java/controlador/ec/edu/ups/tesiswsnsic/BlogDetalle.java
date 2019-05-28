package controlador.ec.edu.ups.tesiswsnsic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import dao.ec.edu.ups.tesiswsnsic.PersonaNodoDAO;
import modelo.ec.edu.ups.tesiswsnsic.Blog;
import modelo.ec.edu.ups.tesiswsnsic.Comentario;
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

	@Inject
	ComentariosDAO comentarioDAO;

	public MapModel simpleModel;
	public Nodo nodoSelected;

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
	public String tipoFecha;
	public String medici;
	public double val;
	public String fec;
	List<Comentario> ltsComentarios;
	public Comentario comentario;
	String imageString;
	boolean typeCalendar;
	public String typeMedicion = "";

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

	public void grafica() {

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

			} else {
				System.out.println("fecha por calendario");
			}
			System.out.println("sensor seleccionado: " + sensorSeleccionado);
			System.out.println("fecha inicio " + fechaInicio);
			System.out.println("fecha fin " + fechaFin);
			//// fecha fin

			MongoClient mongoClient = new MongoClient(new MongoClientURI(DBConnection.connectionMomgo));

			BasicDBObject query = new BasicDBObject();
			query.put("n", nodoSelected.getIdentificador());
			query.put("fecha", BasicDBObjectBuilder.start("$gte", fechaInicio).add("$lte", fechaFin).get());
			// FindIterable<Document> busquedaNodo = collection.find(query);
			// busquedaNodo.forEach(printBlock);
			DB db = mongoClient.getDB(DBConnection.dbname);
			DBCollection coll = db.getCollection(DBConnection.dbcollection);

			DBCursor d1 = coll.find(query);

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
				}

				// cont++;
			}
			System.out.println("Connection Succesfull");
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
}
