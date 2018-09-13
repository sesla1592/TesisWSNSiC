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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

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
