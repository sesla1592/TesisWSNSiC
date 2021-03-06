package modelo.ec.edu.ups.tesiswsnsic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	@Column(name = "sen_descripcion")
	private String descripcion;
	
	@Column(name = "sen_descripcion_web",length=8192)
	private String descripcion_web;
	
	@Column(name = "sen_medicion")
	private String medicion;

	@Column(name = "sen_estado")
	private boolean estado;
	
	@Column(name = "sen_nombre_completo")
	private String nombreCompleto;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="nod_sen_fk")
	private Nodo nodo;
	

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

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}
	

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	
	public String getDescripcion_web() {
		return descripcion_web;
	}

	public void setDescripcion_web(String descripcion_web) {
		this.descripcion_web = descripcion_web;
	}

	public String getMedicion() {
		return medicion;
	}

	public void setMedicion(String medicion) {
		this.medicion = medicion;
	}

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", estado=" + estado
				+ ", nodo=" + nodo + "]";
	}



}
