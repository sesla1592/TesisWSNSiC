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
@Table(name = "TIPOSENSOR")
public class TipoSensor {

	@Id
	@Column(name = "tse_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "tse_descripcion")
	private String descripcion;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "tse_sen_fk", referencedColumnName = "tse_id")
	private List<TipoSensor> tiposensores;

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

	public List<TipoSensor> getTiposensores() {
		return tiposensores;
	}

	public void setTiposensores(List<TipoSensor> tiposensores) {
		this.tiposensores = tiposensores;
	}

	@Override
	public String toString() {
		return "TipoSensor [id=" + id + ", descripcion=" + descripcion + ", tiposensores=" + tiposensores + "]";
	}

}
