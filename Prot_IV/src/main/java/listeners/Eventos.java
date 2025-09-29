package listeners;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.Printable;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import Datos.AlmacenDatos;
import Datos.Campos;
import Datos.Objects;
import frames.FrameCarga;
import outLines.LineaSalida;
import protIV.Ejecutable;
import screens.DatosExpediente;
import screens.PantallaCargaDatos;
import utilidades.Ficheros;
import validaciones.Validacion;


public class Eventos {
	
	public static FrameCarga frameCargaDatos;

	public KeyAdapter consumir(JTextField textFieldConsumir, int sizeMaximo) {
		return new ConsumirTextField(textFieldConsumir,sizeMaximo);
	}

	public ActionListener generarLinea () {
		return new Generacion();
	}
	
	public ActionListener limpiar () {
		return new Limpieza();
	}
	
	public ActionListener validarCampos () {
		return new Validaciones();
	}
	
	public ActionListener habilitarCampos () {
		return new HabilitarCampos();
	}
	
	public ActionListener deshabilitarCampos() {
		return new DeshabilitarCampos();
	}
	
	public ActionListener cargarDatos() {
		return new CargaDatos();
	}
	
	public ItemListener recargarTooltips(JComboBox <String> combo, String nombreCombo) {
		return new ToolTipsCombos(combo, nombreCombo);
	}
	
	public ActionListener cargarValoresExpediente() {
		return new CargaValoresExpediente();
	}
	
	public ActionListener recargaItemsComboComunicados() {
		return new ActualizaComboComunicados();
	}
	
	public ActionListener cambiaAparienciaCamposObligatorios() {
		return new ActualizarAparienciaCamposObligatorios();
	}
}

class ConsumirTextField extends KeyAdapter {

	private JTextField textFieldConsumir;
	private int sizeMaximo;
	
	public ConsumirTextField(JTextField textFieldConsumir, int sizeMaximo) {
		this.textFieldConsumir = textFieldConsumir;
		this.sizeMaximo = sizeMaximo;
	}
	
	public void keyTyped(KeyEvent e) {
		if (textFieldConsumir.getText().length() == sizeMaximo) e.consume();
	}


}

class Generacion implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		
	    JFileChooser guardar = new JFileChooser();
	    guardar.setCurrentDirectory(new File("FicherosSalida"));
	    guardar.showSaveDialog(null);
	    guardar.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

	    File archivo = guardar.getSelectedFile();
		
		new LineaSalida().guardarLinea(archivo);
		new LineaSalida().guardarDatos("DatosGuardados.txt");
		
		JOptionPane.showMessageDialog(Ejecutable.framePrincipalPIV, "El fichero se ha generado correctamente", "Generar Expediente", JOptionPane.INFORMATION_MESSAGE);
		
		DatosExpediente.BOTON_GENERAR.setEnabled(false);
	}
	
}

class Limpieza implements ActionListener {

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		
		for (int i = 1; i < Objects.objetos.length; i++) {
			
			AlmacenDatos.camposFichero[i] = "";
			
			if (Objects.objetos[i].getClass().getName().contains("JTextField")) {
				((JTextField) Objects.objetos[i]).setText("");
			}
			if (Objects.objetos[i].getClass().getName().contains("JComboBox")) {
				((JComboBox <String>) Objects.objetos[i]).setSelectedIndex(0);
			}
			if (Objects.objetos[i].getClass().getName().contains("JDateChooser")) {
				((JDateChooser) Objects.objetos[i]).setDate(null);
			}
			if (Objects.objetos[i].getClass().getName().contains("JCheckBox")) {
				((JCheckBox) Objects.objetos[i]).setSelected(false);;
			}
			
		}
		
		LineaSalida.lineaSalida = "";
		DatosExpediente.BOTON_GENERAR.setEnabled(false);
		
	}
	
}

class DeshabilitarCampos implements ActionListener {
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		
		for (int i = 1; i < Objects.objetos.length; i++) {
			
			AlmacenDatos.camposFichero[i] = "";
			
			if (Objects.objetos[i].getClass().getName().contains("JTextField")) {
				((JTextField) Objects.objetos[i]).setEnabled(false);
			}
			if (Objects.objetos[i].getClass().getName().contains("JComboBox")) {
				((JComboBox <String>) Objects.objetos[i]).setEnabled(false);
			}
			if (Objects.objetos[i].getClass().getName().contains("JDateChooser")) {
				((JDateChooser) Objects.objetos[i]).setEnabled(false);
			}
			if (Objects.objetos[i].getClass().getName().contains("JCheckBox")) {
				((JCheckBox) Objects.objetos[i]).setEnabled(false);
			}
			
		}
	}
		
}
	
	
	class HabilitarCampos implements ActionListener {
		
		@SuppressWarnings("unchecked")
		
		
		public void actionPerformed(ActionEvent e) {
			
			DatosExpediente.BOTON_GENERAR.setEnabled(false);

			for (int i = 1; i < Objects.objetos.length; i++) {
				
				AlmacenDatos.camposFichero[i] = "";
				
				if (Objects.objetos[i].getClass().getName().contains("JTextField")) {
					((JTextField) Objects.objetos[i]).setEnabled(true);
				}
				if (Objects.objetos[i].getClass().getName().contains("JComboBox")) {
					((JComboBox <String>) Objects.objetos[i]).setEnabled(true);
				}
				if (Objects.objetos[i].getClass().getName().contains("JDateChooser")) {
					((JDateChooser) Objects.objetos[i]).setEnabled(true);
				}
				if (Objects.objetos[i].getClass().getName().contains("JCheckBox")) {
					((JCheckBox) Objects.objetos[i]).setEnabled(true);
				}
				
			}
			
			LineaSalida.lineaSalida = "";
			LineaSalida.datosGuardados = "";
	}
}

class Validaciones implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		
		AlmacenDatos datosAlmacenados = new AlmacenDatos();
		
		datosAlmacenados.almacenar();
		datosAlmacenados.filler();
		
		for (int i = 1; i < AlmacenDatos.camposFichero.length; i++) {
			int espaciosRellenar = Integer.parseInt(Campos.listaCampos.get(i)[2]);
			AlmacenDatos.camposFichero[i] = String.format("%-"+espaciosRellenar+"s", AlmacenDatos.camposFichero[i]);
			LineaSalida.lineaSalida = LineaSalida.lineaSalida + AlmacenDatos.camposFichero[i];
			LineaSalida.datosGuardados = LineaSalida.datosGuardados + AlmacenDatos.camposGuardados[i];
			if (i < AlmacenDatos.camposFichero.length - 1) LineaSalida.datosGuardados = LineaSalida.datosGuardados + ";";
		}
		
		if (new Validacion().numeroErrores > 0) {
			
			JOptionPane.showMessageDialog(Ejecutable.framePrincipalPIV, Validacion.validaciones,
					"ERROR DE VALIDACION",JOptionPane.ERROR_MESSAGE);
			DatosExpediente.BOTON_GENERAR.setEnabled(false);
		} else {
			JOptionPane.showMessageDialog(Ejecutable.framePrincipalPIV,"Todos los campos son correctos.",
					"VALIDACION",JOptionPane.INFORMATION_MESSAGE);
			DatosExpediente.BOTON_GENERAR.setEnabled(true);
			
		}
		
		Validacion.validaciones = "";
	}
	
}

class CargaDatos implements ActionListener {
		
	public CargaDatos () {
	}

	public void actionPerformed(ActionEvent e) {
		
		Eventos.frameCargaDatos = new FrameCarga();

	}
}

class ToolTipsCombos implements ItemListener {

	private JComboBox <String> combo;
	private String nombreCombo;

	public ToolTipsCombos (JComboBox <String> combo, String nombreCombo) {
		this.combo = combo;
		this.nombreCombo = nombreCombo;
	}
	
	public void itemStateChanged(ItemEvent e) {
		
		List <String []> opcionesCombo;
		int posicion = 0;
		
		switch (nombreCombo) {
		
		case("TIPO EXPEDIENTE"):
			opcionesCombo = Objects.itemsCombos.get(1);
			posicion = 1;
			for (int i = 0; i < combo.getItemCount(); i++) {
				if (combo.getSelectedItem().toString().contentEquals(opcionesCombo.get(i)[0]))
						combo.setToolTipText(opcionesCombo.get(i)[posicion]);
			}
			break;
			
		case ("TIPO COMUNICADO"):
			opcionesCombo = Objects.itemsCombos.get(2);
			posicion = 2;
			for (int i = 0; i < combo.getItemCount(); i++) {
				if (combo.getSelectedItem().toString().contentEquals(opcionesCombo.get(i)[0]+"-"+opcionesCombo.get(i)[1]))
						combo.setToolTipText(opcionesCombo.get(i)[posicion]);
			}
			break;
		}
		
	}

}

class CargaValoresExpediente implements ActionListener {

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String elementosNoExistenEnCombos = "";

		if (PantallaCargaDatos.tablaExpedientes.getSelectedRow() == -1) {
			
			JOptionPane.showMessageDialog(Eventos.frameCargaDatos, "No ha seleccionado ningun resultado", "Carga datos expediente", JOptionPane.INFORMATION_MESSAGE);
			
		} else {
			
			int lineaElegida = 0;
			
			for (int i = 0 ; i < PantallaCargaDatos.datosCargar.size() ; i++) {

				if (PantallaCargaDatos.tablaExpedientes.getValueAt(
						PantallaCargaDatos.tablaExpedientes.getSelectedRow(), 0).toString().contentEquals(
								i+"")) {
					
					lineaElegida = i;
				}
				
			}
			
			
			for (int i = 1; i < Objects.objetos.length; i++) {
				
				
				try {
				
				AlmacenDatos.camposFichero[i] = "";
				
				if (Objects.objetos[i].getClass().getName().contains("JTextField")) {
					((JTextField) Objects.objetos[i]).setText(PantallaCargaDatos.datosCargar.get(lineaElegida)[i-1]);
				}
				if (Objects.objetos[i].getClass().getName().contains("JComboBox")) {
					
					if (!itemExistsInComboBox(((JComboBox <String>) Objects.objetos[i]),PantallaCargaDatos.datosCargar.get(lineaElegida)[i-1])){
						elementosNoExistenEnCombos += "El elemento "+PantallaCargaDatos.datosCargar.get(lineaElegida)[i-1]+" ya no existe en el combo " +((JComboBox <String>) Objects.objetos[i]).getItemAt(0) + "\n";
					}
					
					((JComboBox <String>) Objects.objetos[i]).setSelectedItem(PantallaCargaDatos.datosCargar.get(lineaElegida)[i-1]);
				}
				if (Objects.objetos[i].getClass().getName().contains("JDateChooser")) {
					try {
						if (!PantallaCargaDatos.datosCargar.get(lineaElegida)[i-1].contentEquals("")) {
							((JDateChooser) Objects.objetos[i]).setDate (new SimpleDateFormat("dd/MM/yyyy").parse(PantallaCargaDatos.datosCargar.get(lineaElegida)[i-1]));
						}
					} catch (ParseException e1) {
					}
				}
				if (Objects.objetos[i].getClass().getName().contains("JCheckBox")) {					
					((JCheckBox) Objects.objetos[i]).setSelected(Boolean.parseBoolean(PantallaCargaDatos.datosCargar.get(lineaElegida)[i-1]));
				}
				

				
				} catch (ArrayIndexOutOfBoundsException e2) {
					JOptionPane.showMessageDialog(Ejecutable.framePrincipalPIV, "Este dato no ha sido creado con la última versión del protocolo, por lo que los datos podrían no ser correctos");
					break;
				}

			}
		

					
		}
		
		if (!elementosNoExistenEnCombos.contentEquals("")) {
			JOptionPane.showMessageDialog(Ejecutable.framePrincipalPIV, elementosNoExistenEnCombos, "Alerta" , JOptionPane.WARNING_MESSAGE);
		}

		Eventos.frameCargaDatos.dispose();
		Ejecutable.framePrincipalPIV.setEnabled(true);				
		Ejecutable.framePrincipalPIV.toFront();		

	}
	
	private boolean itemExistsInComboBox(JComboBox<String> comboBox, String item) {
	    for (int i = 0; i < comboBox.getItemCount(); i++) {
	        if (comboBox.getItemAt(i).equals(item)) {
	            return true;
	        }
	    }
	    return false;
	}
	
}

class ActualizaComboComunicados implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        String selectedItem = (String) comboBox.getSelectedItem();
        
		List<String[]> nuevaLista = Objects.itemsCombos.get(2).stream().filter(k -> k[0].contentEquals(selectedItem)).collect(Collectors.toList());
		nuevaLista.add(0, new String [] {"-TIPO EXPEDIENTE","-TIPO COMUNICADO-"});
		
		//El elemento 2 es el comunicado
		((JComboBox <String>) Objects.objetos[2]).removeAllItems();

		for (int i = 0 ; i < nuevaLista.size();i++) {
			((JComboBox <String>) Objects.objetos[2]).addItem(nuevaLista.get(i)[0] +"-"+nuevaLista.get(i)[1]);
		}
		
    }
}

class ActualizarAparienciaCamposObligatorios implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	Ejecutable.framePrincipalPIV.getContentPane().setVisible(false);
    	
    	for (int i = 1; i < Campos.listaCampos.size() - 1; i++) {
			DatosExpediente.panelesIndividualesLabels[i].setBackground(new Color(0, 0, 0, 0));
			DatosExpediente.panelesIndividualesObjects[i].setBackground(new Color(0, 0, 0, 0));
			Campos.labelsCampos[i].setForeground(new Color(0, 0, 0, 255));
    	}
    	
    	try {
    		
    	
    	JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        
        String selectedItem = (String) comboBox.getSelectedItem();
                
        if (selectedItem != null && !selectedItem.contains("TIPO COMUNICADO")) {
            
        	List <String[]> comunicado = Objects.itemsCombos.get(2).stream().filter(k -> (k[0]+"-"+k[1]).contentEquals(selectedItem)).collect(Collectors.toList());
            
        	List <Integer> obligatorios = (List<Integer>) Arrays.asList(comunicado.get(0)[3].split("-"))
	            .stream()
	            .map(k -> Integer.parseInt(k)).collect(Collectors.toList()); // Transforma los elementos a enteros
        	
        	for (int i = 1; i < Campos.listaCampos.size() - 1; i++) {
 
        		if (obligatorios.contains(i)) {
        			DatosExpediente.panelesIndividualesLabels[i].setBackground(new Color(0, 0, 250, 80));
        			DatosExpediente.panelesIndividualesObjects[i].setBackground(new Color(0, 0, 250, 80));
        			Campos.labelsCampos[i].setForeground(new Color(255, 255, 255, 255));
        		}
        		
        	}
            
            
        }
        
    	}catch (Exception e2) {
    		System.out.print(e2);
    	}
    	
    	Ejecutable.framePrincipalPIV.getContentPane().setVisible(true);
        
    }
}




