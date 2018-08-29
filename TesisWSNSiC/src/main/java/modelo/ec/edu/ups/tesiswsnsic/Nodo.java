package modelo.ec.edu.ups.tesiswsnsic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NODO")
public class Nodo {

	@Id
	@Column(name="nod_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name="nod_nombreColeccion")
	private String nombreColeccion;
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNombreColeccion() {
		return nombreColeccion;
	}



	public void setNombreColeccion(String nombreColeccion) {
		this.nombreColeccion = nombreColeccion;
	}



	@Override
	public String toString() {
		return "Nodo [id=" + id + ", nombreColeccion=" + nombreColeccion + "]";
	}
	
	
}
