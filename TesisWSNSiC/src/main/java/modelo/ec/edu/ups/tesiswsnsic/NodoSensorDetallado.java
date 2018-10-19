package modelo.ec.edu.ups.tesiswsnsic;

public class NodoSensorDetallado {

	private String nombreNodo;
	private String nombreSensor;
	private String medicion;
	private String fecha;
	private float valor;

	public String getNombreNodo() {
		return nombreNodo;
	}

	public void setNombreNodo(String nombreNodo) {
		this.nombreNodo = nombreNodo;
	}

	public String getNombreSensor() {
		return nombreSensor;
	}

	public void setNombreSensor(String nombreSensor) {
		this.nombreSensor = nombreSensor;
	}

	public String getMedicion() {
		return medicion;
	}

	public void setMedicion(String medicion) {
		this.medicion = medicion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

}
