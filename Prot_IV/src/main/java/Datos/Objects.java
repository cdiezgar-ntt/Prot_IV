package Datos;

import java.awt.Component;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import listeners.Eventos;
import utilidades.Ficheros;

public class Objects {

	//Almacen de los objetos donde se introducen o cogen los datos. Cada indice se corresponde con el indice del label
	
	public static Object [] objetos;
	
	public static HashMap<Integer, List <String[]>> itemsCombos;

	public Objects () {
		
		this.definirObjetos();
		this.cargarCombos();
		
	}
	
	public void definirObjetos() {
		
		List <Object> objetosCargados = new ArrayList <Object>();
		
		itemsCombos = new HashMap <Integer, List <String[]>> ();

		for(String[] campo: Campos.listaCampos) {
			switch (campo[4].toLowerCase().trim()) {
			case("combo"):
				objetosCargados.add(new JComboBox <String> ());
				itemsCombos.put(Integer.parseInt(campo[0]), new Ficheros().leerTabla(campo[5], ";"));
			break;
			case("texto"):
				objetosCargados.add(new JTextField(20));
			break;
			case("fecha"):
				objetosCargados.add(new JDateChooser("dd/MM/yyyy", "##/##/####", '_'));
			break;
			case("check"):
				objetosCargados.add(new JCheckBox());
			break;
			case("filler"):
				objetosCargados.add(new Object());
			break;
			default:
				objetosCargados.add(new Object());
			break;
			}		
		}
		
		objetos = objetosCargados.toArray();

	}
	
	@SuppressWarnings("unchecked")
//	public void cargarCombos() {
//		
//		//TODO Los combos expediente y comunicado son especiales
//		
//		// Añadir elementos a los JComboBox
//		itemsCombos.forEach((k, v) -> {
//		    for (String[] items : v) {
//		    	
//		    	if (k == 1) {
//			        ((JComboBox<String>)objetos[k]).addItem(items[0]);
//		    	}
//		    	else if (k == 2) {
//			        ((JComboBox<String>)objetos[k]).addItem(items[0] + "-" + items[1]);
//		    	}
//		    	else {
//			        ((JComboBox<String>)objetos[k]).addItem(items[1]);
//		    	}
//		    }
//		});
//
//	}
	public void cargarCombos() {
	    
	    //TODO Los combos expediente y comunicado son especiales
	    
	    // Añadir elementos a los JComboBox
	    itemsCombos.forEach((k, v) -> {
	        List<String> elementosOrdenados = new ArrayList<>();
	        
	        // Extraer los elementos del combo
	        for (String[] items : v) {
	            if (k == 1) {
	                elementosOrdenados.add(items[0]);
	            } else if (k == 2) {
	                elementosOrdenados.add(items[0] + "-" + items[1]);
	            } else {
	                elementosOrdenados.add(items[1]);
	            }
	        }
	        
	        // Crear un Collator para el locale deseado (por ejemplo, español)
	        Collator collator = Collator.getInstance(new Locale("es", "ES"));
	        
	        collator.setStrength(Collator.PRIMARY);
	        
	        // Ordenar los elementos usando el Collator
	        Collections.sort(elementosOrdenados, collator);	        
	        // Añadir los elementos ordenados al JComboBox
	        JComboBox<String> comboBox = (JComboBox<String>) objetos[k];
	        
	        //Si empieza por guion lo ponemos el primero
	        String itemBase = "";
	        int index = 0;
	        
	        for (String item : elementosOrdenados) {
	        	if (item.trim().substring(0,1).contentEquals("-")) {
	        		itemBase = item;
	        		index = elementosOrdenados.indexOf(itemBase);
	        		break;
	        	}
	        }
	        
	        elementosOrdenados.remove(index);
	        comboBox.addItem(itemBase);
	        
	        for (String item : elementosOrdenados) {
	            comboBox.addItem(item);
	        }
	        
	    });
	}

		

}
