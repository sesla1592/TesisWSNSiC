package modelo.ec.edu.ups.tesiswsnsic;

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
@Table(name = "NODOSENSOR")
public class NodoSensor {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int nodosen_id;
	
	 
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="nod_sen_fk")
	private Nodo nodoID;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sen_nods_fk")
	private Sensor sensorID;

	public int getNodosen_id() {
		return nodosen_id;
	}

	public void setNodosen_id(int nodosen_id) {
		this.nodosen_id = nodosen_id;
	}

	public Nodo getNodoID() {
		return nodoID;
	}

	public void setNodoID(Nodo nodoID) {
		this.nodoID = nodoID;
	}

	public Sensor getSensorID() {
		return sensorID;
	}

	public void setSensorID(Sensor sensorID) {
		this.sensorID = sensorID;
	}

	@Override
	public String toString() {
		return "NodoSensor [nodosen_id=" + nodosen_id + ", nodoID=" + nodoID + ", sensorID=" + sensorID + "]";
	}
	
}
