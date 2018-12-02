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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PERSONA")
public class Persona {

	@Id
	@Column(name = "per_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "per_nombre")
	private String nombre;

	@Column(name = "per_apellido")
	private String apellido;

	@Column(name = "per_estado")
	private String estado;

	@Column(name = "per_correo")
	private String correo;

	@Column(name = "per_password")
	private String password;
	
	@Column(name = "per_rol")
	private String rol;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "per_pen_fk", referencedColumnName = "per_id")
	private List<PersonaNodo> personanodos = new ArrayList<PersonaNodo>();

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


	public String getApellido() {
		return apellido;
	}


	public void setApellido(String apellido) {
		this.apellido = apellido;
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


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public List<PersonaNodo> getPersonanodos() {
		return personanodos;
	}


	public void setPersonanodos(List<PersonaNodo> personanodos) {
		this.personanodos = personanodos;
	}


	public String getRol() {
		return rol;
	}


	public void setRol(String rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", estado=" + estado
				+ ", correo=" + correo + ", password=" + password + ", rol=" + rol + ", personanodos=" + personanodos
				+ "]";
	}
}
