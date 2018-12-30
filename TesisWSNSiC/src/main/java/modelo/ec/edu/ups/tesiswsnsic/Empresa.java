package modelo.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "EMPRESA")
public class Empresa {

	@Id
	@Column(name = "emp_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "emp_nombre")
	private String nombre;

	@Column(name = "emp_direccion")
	private String direccion;

	@Column(name = "emp_telefono")
	private String telefono;

	@Column(name = "emp_estado")
	private String estado;

	@Column(name = "emp_correo")
	private String correo;

	@Column(name = "emp_ciudad")
	private String ciudad;

	@Column(name = "emp_pais")
	private String pais;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "emp_tem_id")
	private TipoEmpresa tipoempresa;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.EAGER)
	@JoinColumn(name = "emp_per_fk", referencedColumnName = "emp_id")
	private List<Persona> personas;
	
	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_nod_fk", referencedColumnName = "emp_id")
	private List<Nodo> listaNodos;

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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public TipoEmpresa getTipoempresa() {
		return tipoempresa;
	}

	public void setTipoempresa(TipoEmpresa tipoempresa) {
		this.tipoempresa = tipoempresa;
	}

	public List<Nodo> getListaNodos() {
		return listaNodos;
	}

	public void setListaNodos(List<Nodo> listaNodos) {
		this.listaNodos = listaNodos;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ciudad == null) ? 0 : ciudad.hashCode());
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + id;
		result = prime * result + ((listaNodos == null) ? 0 : listaNodos.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((pais == null) ? 0 : pais.hashCode());
		result = prime * result + ((personas == null) ? 0 : personas.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((tipoempresa == null) ? 0 : tipoempresa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empresa other = (Empresa) obj;
		if (ciudad == null) {
			if (other.ciudad != null)
				return false;
		} else if (!ciudad.equals(other.ciudad))
			return false;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (id != other.id)
			return false;
		if (listaNodos == null) {
			if (other.listaNodos != null)
				return false;
		} else if (!listaNodos.equals(other.listaNodos))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (pais == null) {
			if (other.pais != null)
				return false;
		} else if (!pais.equals(other.pais))
			return false;
		if (personas == null) {
			if (other.personas != null)
				return false;
		} else if (!personas.equals(other.personas))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (tipoempresa == null) {
			if (other.tipoempresa != null)
				return false;
		} else if (!tipoempresa.equals(other.tipoempresa))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Empresa [id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono
				+ ", estado=" + estado + ", correo=" + correo + ", ciudad=" + ciudad + ", pais=" + pais + "]";
	}

	
}
