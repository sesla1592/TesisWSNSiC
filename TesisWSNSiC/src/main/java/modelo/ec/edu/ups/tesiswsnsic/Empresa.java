package modelo.ec.edu.ups.tesiswsnsic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EMPRESA")
public class Empresa {

	@Id
	@Column(name="emp_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name="emp_nombre")
	private String nombre;
	
	@Column(name="emp_direccion")
	private String direccion;
	
	@Column(name="emp_telefono")
	private String telefono;
	
	@Column(name="emp_estado")
	private String estado;
	
	@Column(name="emp_correo")
	private String correo;
	
	@Column(name="emp_ciudad")
	private String ciudad;
	
	@Column(name="emp_pais")
	private String pais;

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

	@Override
	public String toString() {
		return "Empresa [id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono=" + telefono
				+ ", estado=" + estado + ", correo=" + correo + ", ciudad=" + ciudad + ", pais=" + pais + "]";
	}
	
	
}
