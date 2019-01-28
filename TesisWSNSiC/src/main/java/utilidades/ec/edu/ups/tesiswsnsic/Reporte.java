package utilidades.ec.edu.ups.tesiswsnsic;

public class Reporte {
	
	String codSensor;
	String fecha;
	double latitud;
	double longitud;
	String sensor;
	double valor;
	
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
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public String getCodSensor() {
		return codSensor;
	}
	public void setCodSensor(String codSensor) {
		this.codSensor = codSensor;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	@Override
	public String toString() {
		return "Reporte [codSensor=" + codSensor + ", fecha=" + fecha + ", latitud=" + latitud + ", longitud="
				+ longitud + ", sensor=" + sensor + ", valor=" + valor + "]";
	}
	
}
