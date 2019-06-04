package modelo.ec.edu.ups.tesiswsnsic;

import java.util.Arrays;
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
	
	@Column(name = "blo_breve_descripcion",length=100)
	private String breve_descripcion;
	
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

	public String getBreve_descripcion() {
		return breve_descripcion;
	}

	public void setBreve_descripcion(String breve_descripcion) {
		this.breve_descripcion = breve_descripcion;
	}

	public List<Comentario> getLtsComentario() {
		return ltsComentario;
	}

	public void setLtsComentario(List<Comentario> ltsComentario) {
		this.ltsComentario = ltsComentario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((breve_descripcion == null) ? 0 : breve_descripcion.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + (estado ? 1231 : 1237);
		result = prime * result + ((fechaPub == null) ? 0 : fechaPub.hashCode());
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(imagen);
		result = prime * result + ((imgBase64 == null) ? 0 : imgBase64.hashCode());
		result = prime * result + ((ltsComentario == null) ? 0 : ltsComentario.hashCode());
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
		if (breve_descripcion == null) {
			if (other.breve_descripcion != null)
				return false;
		} else if (!breve_descripcion.equals(other.breve_descripcion))
			return false;
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
		if (estado != other.estado)
			return false;
		if (fechaPub == null) {
			if (other.fechaPub != null)
				return false;
		} else if (!fechaPub.equals(other.fechaPub))
			return false;
		if (id != other.id)
			return false;
		if (!Arrays.equals(imagen, other.imagen))
			return false;
		if (imgBase64 == null) {
			if (other.imgBase64 != null)
				return false;
		} else if (!imgBase64.equals(other.imgBase64))
			return false;
		if (ltsComentario == null) {
			if (other.ltsComentario != null)
				return false;
		} else if (!ltsComentario.equals(other.ltsComentario))
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
		return "Blog [id=" + id + ", nombreBlog=" + nombreBlog + ", descripcion=" + descripcion + ", breve_descripcion="
				+ breve_descripcion + ", imagen=" + Arrays.toString(imagen) + ", fechaPub=" + fechaPub + ", visitas="
				+ visitas + ", estado=" + estado + "]";
	}

	
	
	
	
}
