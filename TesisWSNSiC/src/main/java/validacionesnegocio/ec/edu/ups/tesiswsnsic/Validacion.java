package validacionesnegocio.ec.edu.ups.tesiswsnsic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacion {

	/** The Constant PATTERN_EMAIL. */
	/*
	 * Variable para la validacion de la cedula
	 */
	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";  
	
	
	/**
	 * Validar correo. Metodo para la validacion de un correo electronico
	 *
	 * @param correo the correo
	 * @return true, if successful
	 */

	public boolean validarCorreo(String correo) {
		System.out.println("CORREO A VALIDACION:  "+correo);
		String email = correo;
		Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
	}
}
