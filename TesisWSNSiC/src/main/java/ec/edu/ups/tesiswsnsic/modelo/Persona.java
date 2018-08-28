package ec.edu.ups.tesiswsnsic.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
