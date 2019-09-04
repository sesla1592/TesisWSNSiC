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

import org.hibernate.annotations.ColumnTransformer;

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

	@Column(name = "per_password", columnDefinition = "bytea")
	@ColumnTransformer( 
			read = "pgp_sym_decrypt (" + 
					"per_password, " +
					"    current_setting('session_replication_role')" + 
					")", 
			write = "pgp_sym_encrypt ( " + 
					"?, " + 
	                "    current_setting('session_replication_role')" +
					")"
					)
	private String password;
	
	@Column(name = "per_cambio_password")
	private boolean cambioPassword;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY)
	@JoinColumn(name = "per_pen_fk", referencedColumnName = "per_id")
	private List<PersonaNodo> personanodos = new ArrayList<PersonaNodo>();
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_per_fk")
	private Empresa empresa;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="per_rol_id")
	private Rol rolPerson;
	
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

	
	public Rol getRolPerson() {
		return rolPerson;
	}


	public void setRolPerson(Rol rolPerson) {
		this.rolPerson = rolPerson;
	}


	public Empresa getEmpresa() {
		return empresa;
	}


	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}


	public boolean isCambioPassword() {
		return cambioPassword;
	}


	public void setCambioPassword(boolean cambioPassword) {
		this.cambioPassword = cambioPassword;
	}


	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", estado=" + estado
				+ ", correo=" + correo + "]";
	}
}
