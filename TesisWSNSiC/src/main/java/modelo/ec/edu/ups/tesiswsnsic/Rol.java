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
@Table(name="ROL")
public class Rol {
	
	@Id
	@Column(name="rol_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name="rol_descripcion")
	private String descripcion;
	
//	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="eve_cat_id", referencedColumnName="cat_id")
	private List<Persona> personas;


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

	@Override
	public String toString() {
		return "Rol [id=" + id + ", descripcion=" + descripcion + "]";
	}
	
}
