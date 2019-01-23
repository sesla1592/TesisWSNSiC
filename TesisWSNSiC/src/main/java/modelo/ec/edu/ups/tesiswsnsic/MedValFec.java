package modelo.ec.edu.ups.tesiswsnsic;

/* CLASE QUE PERMITE ALMACENAR EN LA LISTA LOS VALORES PARA SER REFLEJADOS EN LA GRAFICA
 * */
public class MedValFec {
	@Override
	public String toString() {
		return "MedValFec [medicion=" + medicion + ", valor=" + valor + ", fecha=" + fecha + "]";
	}

	public String medicion;
	public double valor;
	public String fecha;
	
	public MedValFec() {
	}

	public MedValFec(String medicion, double valor, String fecha) {
		super();
		this.medicion = medicion;
		this.valor = valor;
		this.fecha = fecha;
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

	
}
