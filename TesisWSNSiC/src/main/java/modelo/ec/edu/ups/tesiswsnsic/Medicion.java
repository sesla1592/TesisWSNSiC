package modelo.ec.edu.ups.tesiswsnsic;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "MEDICION")
public class Medicion {

	@Id
	@Column(name = "med_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int med_id;

	@Basic(optional = false)
	@Size(min = 1, max = 45)
	@Column(name = "med_nombre")
	private String med_nombre;

	@Basic(optional = false)
	@Size(min = 1, max = 45)
	@Column(name = "med_identificador")
	private String med_identificador;

	@Basic(optional = false)
	@Size(min = 1, max = 45)
	@Column(name = "med_escala")
	private String med_escala;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sen_med_fk")
	private Sensor sensorid;

	public int getMed_id() {
		return med_id;
	}

	public void setMed_id(int med_id) {
		this.med_id = med_id;
	}

	public String getMed_nombre() {
		return med_nombre;
	}

	public void setMed_nombre(String med_nombre) {
		this.med_nombre = med_nombre;
	}

	public String getMed_identificador() {
		return med_identificador;
	}

	public void setMed_identificador(String med_identificador) {
		this.med_identificador = med_identificador;
	}

	public String getMed_escala() {
		return med_escala;
	}

	public void setMed_escala(String med_escala) {
		this.med_escala = med_escala;
	}

	public Sensor getSensorid() {
		return sensorid;
	}

	public void setSensorid(Sensor sensorid) {
		this.sensorid = sensorid;
	}

	
		
}
