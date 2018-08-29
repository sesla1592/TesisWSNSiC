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
@Table(name = "TIPOPAGO")
public class TipoPago {

	@Id
	@Column(name = "tpa_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "tpa_descripcion")
	private String descripcion;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "tpa_mov_fk", referencedColumnName = "tpa_id")
	private List<TipoPago> tipopagos;

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

	public List<TipoPago> getTipopagos() {
		return tipopagos;
	}

	public void setTipopagos(List<TipoPago> tipopagos) {
		this.tipopagos = tipopagos;
	}

	@Override
	public String toString() {
		return "TipoPago [id=" + id + ", descripcion=" + descripcion + ", tipopagos=" + tipopagos + "]";
	}
}
