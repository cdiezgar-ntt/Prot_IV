package utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.toedter.calendar.JDateChooser;

import Datos.Campos;
import Datos.Objects;
import outLines.LineaSalida;

public class Ficheros {

	public Ficheros() {
		
	}
	
	public List <String []> leerTabla (String nombreTabla, String delim) {
		
		String rutaTabla = "Tablas/" + nombreTabla;
		
		List <String> lineas = new ArrayList <String> ();
		File file = new File (rutaTabla);
		
		FileReader fr;
		BufferedReader br;
		
		//Se leen todas las lineas de la tabla de datos
		try {
			
			fr = new FileReader (file);
			br = new BufferedReader(fr);
			
			String texto = br.readLine();
			
			while (texto!=null) {
				lineas.add(texto);
				texto= br.readLine();
			}
			
			br.close();
			fr.close();

		} catch (FileNotFoundException fnfe) {

			fnfe.printStackTrace();
			
		} catch (IOException io) {
			
			io.printStackTrace();
		}
		
		//Se almacenan todos los campos
		
		List <String []> datosTabla = new ArrayList <String []> ();
		
		for (int i = 0 ; i < lineas.size(); i++) {
			
			String [] camposPorLinea = lineas.get(i).split(delim,-1);	//El menos 1 es para procesar datos si hay varios delim consecutivos
			datosTabla.add(camposPorLinea);
		
		}
		
		return datosTabla;
		
	}
	
	public boolean escribirFichero(File archivo, String lineaSalida, boolean mantener) {
		
		FileWriter fw;
		BufferedWriter bw;
		
		//Se leen todas las lineas de la tabla de datos
		try {
			
			fw = new FileWriter (archivo,mantener);
			bw = new BufferedWriter(fw);
			
			bw.write(lineaSalida+"\n");
						
			bw.close();
			fw.close();
			
			return true;


		} catch (FileNotFoundException fnfe) {

			fnfe.printStackTrace();
			
			return false;
			
		} catch (IOException io) {
			
			io.printStackTrace();
			
			return false;

			
		}
		
	}
	
	public void guardarDatos(String nombreFichero, String lineaSalida) {
		
		String rutaLinea = "FicherosSalida/"+nombreFichero;
		File file = new File (rutaLinea);
		
		FileWriter fw;
		BufferedWriter bw;
		
		//Se leen todas las lineas de la tabla de datos
		try {
			
			fw = new FileWriter (file,true);
			bw = new BufferedWriter(fw);
			
			bw.write(lineaSalida+"\n");
						
			bw.close();
			fw.close();


		} catch (FileNotFoundException fnfe) {

			fnfe.printStackTrace();
			
		} catch (IOException io) {
			
			io.printStackTrace();
			
		}
		
	}
	
	public ArrayList <String> devolverLineasGuardadas (String fichero){
		
		ArrayList <String> devuelveDatos = new ArrayList <String>();
		String ruta = "FicherosSalida/"+ fichero;
		try {
			FileReader fr = new FileReader (ruta);
			BufferedReader br = new BufferedReader (fr);
			String texto = br.readLine();
			while (texto!=null) {
				devuelveDatos.add(texto);
				texto= br.readLine();
			}
			br.close();
			fr.close();
		}catch (FileNotFoundException e) {
			System.out.println("Error: Fichero de datos no encontrado.");
			System.out.println(e.getMessage());
		}catch (Exception ex) {
			System.out.println("Error de lectura del fichero de datos.");
			System.out.println(ex.getMessage());
		}
		
		return devuelveDatos;
	}
	

	
	

	
	
	
}
