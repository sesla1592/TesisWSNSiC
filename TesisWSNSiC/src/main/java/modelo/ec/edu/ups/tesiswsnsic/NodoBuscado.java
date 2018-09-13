package modelo.ec.edu.ups.tesiswsnsic;

public class NodoBuscado {
	
	private String ipv4;

	public NodoBuscado() {		
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	@Override
	public String toString() {
		return "NodoBuscado [ipv4=" + ipv4 + "]";
	}	

}
