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

	
}
