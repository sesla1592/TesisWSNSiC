package security.ec.edu.ups.tesiswsnsic;

public interface Cryptographical {
	String encrypt(String plaintext);
	String decrypt(String ciphertext);
}
