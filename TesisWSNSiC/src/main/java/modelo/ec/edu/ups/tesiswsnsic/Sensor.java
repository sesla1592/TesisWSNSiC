package modelo.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SENSOR")
public class Sensor {

	@Id
	@Column(name = "sen_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "sen_nombre")
	private String nombre;

	@Column(name = "sen_estado")
	private String estado;

	@Column(name = "sen_latitud")
	private double latitud;

	@Column(name = "sen_longitud")
	private double longitud;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="nod_sen_fk")
	private Nodo nodo;
	
	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "sen_med_fk", referencedColumnName = "sen_id")
	private List<Medicion> mediciones;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public List<Medicion> getMediciones() {
		return mediciones;
	}

	public void setMediciones(List<Medicion> mediciones) {
		this.mediciones = mediciones;
	}

	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", nombre=" + nombre + ", estado=" + estado + ", latitud=" + latitud + ", longitud="
				+ longitud + "]";
	}

}
