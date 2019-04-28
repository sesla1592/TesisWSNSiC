package modelo.ec.edu.ups.tesiswsnsic;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "BLOG")
public class Blog {
	
	@Id
	@Column(name = "blo_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "blo_nombreBlog")
	private String nombreBlog;
	

	@Column(name = "blo_descripcion",length=8192)
	private String descripcion;
	
	@Column(name = "blo_img64")
	private byte[] imagen;
	
	@Column(name = "blo_fecha_pub")
	private String fechaPub;
	
	@Column(name = "blo_visitas")
	private int visitas;
	
	@Column(name = "blo_estado")
	private boolean estado;
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id")
	Empresa empresa;

	@OneToMany(cascade = (javax.persistence.CascadeType.ALL), fetch = FetchType.LAZY,mappedBy="blog")
	private List<Comentario> ltsComentario;
	
	@Transient
	String imgBase64;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombreBlog() {
		return nombreBlog;
	}

	public void setNombreBlog(String nombreBlog) {
		this.nombreBlog = nombreBlog;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	public int getVisitas() {
		return visitas;
	}

	public void setVisitas(int visitas) {
		this.visitas = visitas;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getFechaPub() {
		return fechaPub;
	}

	public void setFechaPub(String fechaPub) {
		this.fechaPub = fechaPub;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getImgBase64() {
		return imgBase64;
	}

	public void setImgBase64(String imgBase64) {
		this.imgBase64 = imgBase64;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + id;
		result = prime * result + ((imagen == null) ? 0 : imagen.hashCode());
		result = prime * result + ((nombreBlog == null) ? 0 : nombreBlog.hashCode());
		result = prime * result + visitas;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Blog other = (Blog) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (empresa == null) {
			if (other.empresa != null)
				return false;
		} else if (!empresa.equals(other.empresa))
			return false;
		if (id != other.id)
			return false;
		if (imagen == null) {
			if (other.imagen != null)
				return false;
		} else if (!imagen.equals(other.imagen))
			return false;
		if (nombreBlog == null) {
			if (other.nombreBlog != null)
				return false;
		} else if (!nombreBlog.equals(other.nombreBlog))
			return false;
		if (visitas != other.visitas)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", nombreBlog=" + nombreBlog + ", descripcion=" + descripcion + ", imagen=" + imagen
				+ ", visitas=" + visitas + "]";
	}
	
	
}
