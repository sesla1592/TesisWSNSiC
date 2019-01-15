package modelo.ec.edu.ups.tesiswsnsic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PERSONANODO")
public class PersonaNodo {

	@Id
	@Column(name = "pen_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "pen_estado")
	private String estado;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="nod_pen_fk")
	private Nodo nodo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="per_pen_fk")
	private Persona persona;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Nodo getNodo() {
		return nodo;
	}

	public void setNodo(Nodo nodo) {
		this.nodo = nodo;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Override
	public String toString() {
		return "PersonaNodo [id=" + id + ", estado=" + estado + "]";
	}

}
