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

	@Column(name = "sen_estado")
	private boolean estado;
	
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

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", estado=" + estado
				+ ", nodo=" + nodo + "]";
	}



}
