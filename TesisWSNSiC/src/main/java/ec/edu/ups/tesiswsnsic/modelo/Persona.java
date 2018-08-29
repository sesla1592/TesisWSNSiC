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

	
	
	
	
	
	
}
