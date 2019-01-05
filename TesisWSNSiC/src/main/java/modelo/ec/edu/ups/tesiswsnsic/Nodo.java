package modelo.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "NODO")
public class Nodo {

	@Id
	@Column(name = "nod_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Size(min = 1, max = 45)
	@Column(name = "nombre")
	private String nombre;
	@Size(max = 45)
	@Column(name = "descripcion")
	private String descripcion;

	@Size(min = 1, max = 45)
	@Column(name = "identificador")
	private String identificador;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "latitud")
	private Double latitud;
	
	@Column(name = "longitud")
	private Double longitud;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "nod_pen_fk", referencedColumnName = "nod_id")
	private List<PersonaNodo> personanodos;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.EAGER)
	@JoinColumn(name = "nod_sen_fk", referencedColumnName = "nod_id")
	private List<Sensor> ltssensores = new ArrayList<>();

	public Nodo () {
		
	}
	
	
	public Nodo(int id, String nombre, String identificador) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.identificador = identificador;
	}


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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public List<PersonaNodo> getPersonanodos() {
		return personanodos;
	}

	public void setPersonanodos(List<PersonaNodo> personanodos) {
		this.personanodos = personanodos;
	}


	@Override
	public String toString() {
		return "Nodo [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", identificador="
				+ identificador + ", latitud=" + latitud + ", longitud=" + longitud + "]";
	}

		
}
