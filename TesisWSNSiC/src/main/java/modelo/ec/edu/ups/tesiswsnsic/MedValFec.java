package modelo.ec.edu.ups.tesiswsnsic;

/* CLASE QUE PERMITE ALMACENAR EN LA LISTA LOS VALORES PARA SER REFLEJADOS EN LA GRAFICA
 * */
public class MedValFec {
	public String medicion;
	public double valor;
	public String fecha;
	public double latitud;
	public double longitud;
	
	public MedValFec() {
	}

	public MedValFec(String medicion, double valor, String fecha, double latitud, double longitud) {
		super();
		this.medicion = medicion;
		this.valor = valor;
		this.fecha = fecha;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public String getMedicion() {
		return medicion;
	}

	public void setMedicion(String medicion) {
		this.medicion = medicion;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	
}
