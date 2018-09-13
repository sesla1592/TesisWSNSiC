package modelo.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "NODO")
public class Nodo {

	@Id
	@Column(name = "nod_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	private String nombre;
	
	private String ip;
	
	private String mac;
	
	private double lat;
	
	private double lon;

	@Column(name = "nod_nombreColeccion")
	private String nombreColeccion;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "nod_pen_fk", referencedColumnName = "nod_id")
	private List<PersonaNodo> personanodos;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "nod_sen_fk", referencedColumnName = "nod_id")
	private List<Sensor> sensores;

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getNombreColeccion() {
		return nombreColeccion;
	}

	public void setNombreColeccion(String nombreColeccion) {
		this.nombreColeccion = nombreColeccion;
	}

	public List<PersonaNodo> getPersonanodos() {
		return personanodos;
	}

	public void setPersonanodos(List<PersonaNodo> personanodos) {
		this.personanodos = personanodos;
	}

	public List<Sensor> getSensores() {
		return sensores;
	}

	public void setSensores(List<Sensor> sensores) {
		this.sensores = sensores;
	}

	@Override
	public String toString() {
		return "Nodo [id=" + id + ", nombreColeccion=" + nombreColeccion + ", personanodos=" + personanodos
				+ ", sensores=" + sensores + "]";
	}

}
