package modelo.ec.edu.ups.tesiswsnsic;

import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

//import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ROL")
public class Rol {

	@Id
	@Column(name = "rol_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "rol_descripcion")
	private String descripcion;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="per_rol_id", referencedColumnName="rol_id")
	private List<Persona> personas = new ArrayList<Persona>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	@Override
	public String toString() {
		return "Rol [id=" + id + ", descripcion=" + descripcion + ", personas=" + personas + "]";
	}

}