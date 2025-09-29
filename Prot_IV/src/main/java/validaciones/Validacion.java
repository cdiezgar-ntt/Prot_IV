package validaciones;

import Datos.AlmacenDatos;
import Datos.Campos;
import Datos.Objects;
import utilidades.Utiles;

public class Validacion {

	private final String dniChars = "TRWAGMYFPDXBNJZSQVHLCKE";

	public static String validaciones = "";
	private String[] valoresValidos;
	private String valoresValidos_str = "";
	public int numeroErrores = 0;

	public Validacion() {
		validarTipoExpedienteVacio();
		validarTipoComunicado();
		validarNass();
		validarIpfVacio();
		validarNombreVacio();
		validarApellido1Vacio();
		validarApellido2Vacio();
	}

	private void validarTipoExpedienteVacio() {
		if (new Utiles().esVacio(AlmacenDatos.camposFichero[1], 2)) {
			numeroErrores++;
			validaciones = validaciones + "El tipo de expediente es obligatorio\n";
		}
	}

	private void validarTipoComunicado() {

		if (new Utiles().esVacio(AlmacenDatos.camposFichero[2], 2)) {
			numeroErrores++;
			validaciones = validaciones + "El tipo de comunicado es obligatorio\n";
		}
	}

	private void validarNass() {
		if (new Utiles().esVacio(AlmacenDatos.camposFichero[3], 12)) {
			numeroErrores++;
			validaciones = validaciones + "El numero de afiliacion de la seguridad social es obligatorio\n";
		} else {
			validarNassCompleto();
		}
	}

	private void validarNassCompleto() {
		if (AlmacenDatos.camposFichero[3].contains(" ")) {
			numeroErrores++;
			validaciones = validaciones + "El numero de afiliacion de la seguridad social debe contener 12 digitos\n";
		} else {
			validarNassNumerico();
		}
	}

	private void validarNassNumerico() {
		try {
			Long.parseLong(AlmacenDatos.camposFichero[3]);
			validarPrimerosDigitosNass();
		} catch (NumberFormatException nfe) {
			numeroErrores++;
			validaciones = validaciones + "El numero de afiliacion de la seguridad social debe ser numerico\n "
					+ AlmacenDatos.camposFichero[3];
		}
	}

	private void validarPrimerosDigitosNass() {

		// TODO QUITAR LAS REFERENCIAS A LA PROVINCIA 11

		boolean nassCorrecto = false;
		for (int i = 0; i < Objects.itemsCombos.get(11).size(); i++) {
			if (AlmacenDatos.camposFichero[3].substring(0, 2).contentEquals(Objects.itemsCombos.get(11).get(i)[0])) {
				nassCorrecto = true;
			}
		}

		if (!nassCorrecto) {
			numeroErrores++;
			validaciones = validaciones
					+ "Los 2 primeros digitos del Nass deben corresponderse con un codigo de CA valido\n";
		}
	}

	private void validarIpfVacio() {
		if (new Utiles().esVacio(AlmacenDatos.camposFichero[6], 10)) {
			numeroErrores++;
			validaciones = validaciones + "El numero de identificacion personal es obligatorio\n";
		} else {
			if (AlmacenDatos.camposFichero[5].contentEquals("1"))
				validarLetraDni();
		}
	}

	private void validarLetraDni() {

		try {
			char ltrDNI = AlmacenDatos.camposFichero[6].charAt(9);
			int valNumDni = Integer.parseInt(AlmacenDatos.camposFichero[6].substring(1, 9)) % 23;
			if (dniChars.charAt(valNumDni) != ltrDNI) {
				numeroErrores++;
				validaciones = validaciones + "El DNI informado no es valido\n";
			}
		} catch (NumberFormatException nfe) {
			numeroErrores++;
			validaciones = validaciones + "Los primeros digitos del DNI deben ser numericos\n";
		}
	}

	private void validarNombreVacio() {
		if (new Utiles().esVacio(AlmacenDatos.camposFichero[7], 33)) {
			numeroErrores++;
			validaciones = validaciones + "El nombre del asegurado es obligatorio\n";
		}
	}

	private void validarApellido1Vacio() {
		if (new Utiles().esVacio(AlmacenDatos.camposFichero[8], 33)) {
			numeroErrores++;
			validaciones = validaciones + "El primer apellido del asegurado es obligatorio\n";
		}
	}

	private void validarApellido2Vacio() {
		if (new Utiles().esVacio(AlmacenDatos.camposFichero[9], 33)) {
			numeroErrores++;
			validaciones = validaciones + "El segundo apellido del asegurado es obligatorio\n";
		}
	}

}
