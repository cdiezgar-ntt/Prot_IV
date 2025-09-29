package outLines;

import java.io.File;

import utilidades.Ficheros;

public class LineaSalida {

	public static String lineaSalida = "";
	public static String datosGuardados = "";
	
	public LineaSalida () {
		
	}
	
	public void guardarLinea(File archivo) {
		new Ficheros().escribirFichero(archivo, lineaSalida, true);
		lineaSalida = "";
	}
	
	public void guardarDatos(String nombreFichero) {
		new Ficheros().guardarDatos(nombreFichero, datosGuardados);
		datosGuardados = "";
	}
	
	public void resetLineaSalida() {
		lineaSalida = "";
	}
	
	
	
	
}
