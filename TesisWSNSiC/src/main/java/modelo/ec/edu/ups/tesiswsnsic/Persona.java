package ec.edu.ups.tesiswsnsic.modelo;

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
@Table(name="PERSONA")
public class Persona {

	@Id
	@Column(name="per_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name="per_nombre")
	private String nombre;
	
	@Column(name="per_apellido")
	private String apellido;
	
	@Column(name="per_estado")
	private String estado;
	
	@Column(name="per_correo")
	private String correo;
	
	@Column(name="per_password")
	private String password;
	
	@OneToMany(cascade=(javax.persistence.CascadeType.ALL),fetch=FetchType.LAZY)
	@JoinColumn(name="per_mov_fk", referencedColumnName="per_id")
	private List<Movimiento> movimientos;
	
	@OneToMany(cascade=(javax.persistence.CascadeType.ALL),fetch=FetchType.LAZY)
	@JoinColumn(name="per_nod_fk", referencedColumnName="per_id")
	private List<PersonaNodo> personanodos;

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

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public List<PersonaNodo> getPersonanodos() {
		return personanodos;
	}

	public void setPersonanodos(List<PersonaNodo> personanodos) {
		this.personanodos = personanodos;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", estado=" + estado
				+ ", correo=" + correo + ", password=" + password + ", movimientos=" + movimientos + ", personanodos="
				+ personanodos + "]";
	}	
	
}
