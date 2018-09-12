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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TIPOEMPRESA")
public class TipoEmpresa {

	@Id
	@Column(name = "tem_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "tem_nombre")
	private String descripcion;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_tem_id", referencedColumnName = "tem_id")
	private List<Empresa> empresas;

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

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	@Override
	public String toString() {
		return "TipoEmpresa [id=" + id + ", descripcion=" + descripcion + ", empresas=" + empresas + "]";
	}

}
