package screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import Datos.Campos;
import Datos.Objects;
import listeners.Eventos;
import utilidades.Ficheros;

public class PantallaCargaDatos extends JPanel {
	
	private JLabel nassLb, fechaBajaLb, tipoExpedienteLb, tipoComunicadoLb;
	private JTextField nassTf;
	private JDateChooser fechaBajaCal;
	private JComboBox <String> tipoExpedienteCb, tipoComunicadoCb;
	private JButton buscarBt, limpiarBt;
	public static JTable tablaExpedientes;
	private JPanel panelFiltros, panelBotones;
	
	public static JScrollPane panelDatos;
	public static String [][] datosFinalesTabla;
	public static List <String []> datosCargar;
	public static JButton OBTENER_BT;
	
	public PantallaCargaDatos() {
		
		this.setLayout(new BorderLayout());
		this.definirElementos();
		this.cargarDisposicion();
		this.asignarEscuchadores();
		this.updateUI();
	
	}
	
	public void definirElementos() {
		
		nassLb = new JLabel("Nass:");
		nassTf = new JTextField(15);
		
		fechaBajaLb = new JLabel("Fecha de baja:");
		fechaBajaCal = new JDateChooser("dd/MM/yyyy", "##/##/####", '_');
		
		tipoExpedienteLb = new JLabel("Tipo Expediente:");
		tipoComunicadoLb = new JLabel("Tipo Comunicado:");
		tipoExpedienteCb = new JComboBox <String> ();
		tipoComunicadoCb = new JComboBox <String> ();		

		this.almacenarCombos();

		buscarBt = new JButton("Buscar");
		limpiarBt = new JButton("Limpiar");
		PantallaCargaDatos.OBTENER_BT = new JButton("Cargar Datos");
		PantallaCargaDatos.OBTENER_BT.setEnabled(false);
	
	}
	
	public void almacenarCombos() {
		
		//TODO QUITAR LAS REFERENCIAS A POSICIONES FIJAS EN ALGUN MOMENTO
		
		for (int i = 0 ; i < Objects.itemsCombos.get(1).size();i++) {
			tipoExpedienteCb.addItem(Objects.itemsCombos.get(1).get(i)[0]);
		}
		
		//Combo tipos de comunicado
		for (int i = 0 ; i < Objects.itemsCombos.get(2).size();i++) {
			tipoComunicadoCb.addItem(Objects.itemsCombos.get(2).get(i)[0] +"-"+Objects.itemsCombos.get(2).get(i)[1]);
		}
	}
	
	public void cargarDisposicion() {
				
		panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 35, 10));
		panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		panelDatos = new JScrollPane();
		
		panelFiltros.add(nassLb);
		panelFiltros.add(nassTf);
		panelFiltros.add(fechaBajaLb);
		panelFiltros.add(fechaBajaCal);
		panelFiltros.add(tipoExpedienteLb);
		panelFiltros.add(tipoExpedienteCb);
		panelFiltros.add(tipoComunicadoLb);
		panelFiltros.add(tipoComunicadoCb);
				
		panelBotones.add(buscarBt);
		panelBotones.add(PantallaCargaDatos.OBTENER_BT);
		panelBotones.add(limpiarBt);
		
		this.add(panelFiltros, BorderLayout.NORTH);
		this.add(panelDatos,BorderLayout.CENTER);
		this.add(panelBotones,BorderLayout.SOUTH);
		
	}

	private void asignarEscuchadores() {
		nassTf.addKeyListener(new Eventos().consumir(nassTf, Integer.parseInt(Campos.listaCampos.get(3)[2])));
		buscarBt.addActionListener(new PintaLineasGuardadas (this, nassTf, fechaBajaCal, tipoExpedienteCb, tipoComunicadoCb));
		PantallaCargaDatos.OBTENER_BT.addActionListener(new Eventos().cargarValoresExpediente());
		limpiarBt.addActionListener(new Limpieza(this, nassTf, fechaBajaCal, tipoExpedienteCb, tipoComunicadoCb));
	}
	
}

class PintaLineasGuardadas implements ActionListener {
	
	private JTextField nassTf;
	private JDateChooser fechaBajaCal;
	private JComboBox <String> tipoExpedienteCb, tipoComunicadoCb;
	private PantallaCargaDatos pantallaCarga;
	
	public PintaLineasGuardadas (PantallaCargaDatos pantallaCarga, JTextField nassTf, JDateChooser fechaBajaCal, JComboBox <String> tipoExpedienteCb, JComboBox <String> tipoComunicadoCb) {
		this.nassTf = nassTf;
		this.fechaBajaCal = fechaBajaCal;
		this.tipoExpedienteCb = tipoExpedienteCb;
		this.tipoComunicadoCb = tipoComunicadoCb;
		this.pantallaCarga = pantallaCarga;
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		
		PantallaCargaDatos.OBTENER_BT.setEnabled(true);
		
		PantallaCargaDatos.panelDatos.setVisible(false);
		
		List <String> lineasDatos = new Ficheros().devolverLineasGuardadas("DatosGuardados.txt");
		
		PantallaCargaDatos.datosCargar = new ArrayList <String []> ();
		
		lineasDatos.forEach(k -> {
			PantallaCargaDatos.datosCargar.add(k.split(";", -1));
		});
				
		if (nassTf.getText().length() > 0) {
			PantallaCargaDatos.datosCargar.removeIf(k -> !k[2].contentEquals(nassTf.getText()));
		}
		
		if (fechaBajaCal.getDate() != null) {
			PantallaCargaDatos.datosCargar.removeIf(k -> !k[3].contentEquals(new SimpleDateFormat("dd/MM/yyyy").format(fechaBajaCal.getDate())));
		}
		
		if (tipoExpedienteCb.getSelectedIndex() != 0) {
			PantallaCargaDatos.datosCargar.removeIf(k -> !k[0].contentEquals(tipoExpedienteCb.getSelectedItem().toString()));
		}
		
		if (tipoComunicadoCb.getSelectedIndex() != 0) {
			PantallaCargaDatos.datosCargar.removeIf(k -> !k[1].contentEquals(tipoComunicadoCb.getSelectedItem().toString()));
		}
		
		PantallaCargaDatos.datosFinalesTabla = new String [PantallaCargaDatos.datosCargar.size()][7];

		for (int i = 0 ; i < PantallaCargaDatos.datosFinalesTabla.length ; i++) {
			PantallaCargaDatos.datosFinalesTabla[i][0] = String.valueOf(i);
			PantallaCargaDatos.datosFinalesTabla[i][1] = PantallaCargaDatos.datosCargar.get(i)[0];
			PantallaCargaDatos.datosFinalesTabla[i][2] = PantallaCargaDatos.datosCargar.get(i)[1];
			PantallaCargaDatos.datosFinalesTabla[i][3] = PantallaCargaDatos.datosCargar.get(i)[2];
			PantallaCargaDatos.datosFinalesTabla[i][4] = PantallaCargaDatos.datosCargar.get(i)[5];
			PantallaCargaDatos.datosFinalesTabla[i][5] = PantallaCargaDatos.datosCargar.get(i)[7].trim() 
					+ " " +PantallaCargaDatos.datosCargar.get(i)[8].trim() 
					+ ", " + PantallaCargaDatos.datosCargar.get(i)[6].trim();
			PantallaCargaDatos.datosFinalesTabla[i][6] = PantallaCargaDatos.datosCargar.get(i)[3];
		}
		
		PantallaCargaDatos.tablaExpedientes = this.crearTabla(PantallaCargaDatos.datosFinalesTabla);
		PantallaCargaDatos.panelDatos = new JScrollPane(PantallaCargaDatos.tablaExpedientes);
		
		pantallaCarga.add(PantallaCargaDatos.panelDatos,BorderLayout.CENTER);
		PantallaCargaDatos.panelDatos.setVisible(true);
		pantallaCarga.updateUI();
		
		if (PantallaCargaDatos.datosFinalesTabla.length == 0) {
			JOptionPane.showMessageDialog(Eventos.frameCargaDatos, "No se ha encontrado ningun resultado", "Buscador Expedientes", JOptionPane.INFORMATION_MESSAGE);
		}
		
		}
	
	
	private JTable crearTabla(String[][] datosTabla) {
	    String[] rotulos = new String[]{"ID", "TIPO EXPEDIENTE", "TIPO COMUNICADO", "NASS", "IPF", "NOMBRE Y APELLIDOS", "FECHA DE BAJA"};

	    JTable tabla = new JTable();
	    DefaultTableModel model = new DefaultTableModel() {
	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            if (columnIndex == 0 || columnIndex == 3) { // Columna "ID" y "NASS"
	                return Integer.class; // Tratar como números enteros
	            } else {
	                return String.class; // Por defecto, tratar como texto
	            }
	        }
	    };

	    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
	    model.setDataVector(datosTabla, rotulos);
	    tabla.setModel(model);
	    tabla.setRowSorter(sorter);
	    tabla.setCellSelectionEnabled(false);
	    
	    // Para ordenar la columna id correctamente
	    sorter.setComparator(0, new Comparator<String>() {
	        @Override
	        public int compare(String id1, String id2) {
	            try {
	            	int id1_int = Integer.parseInt(id1);
	            	int id2_int = Integer.parseInt(id2);
	                return Integer.compare(id1_int, id2_int);
	            } catch (NumberFormatException  e) {
	                return 0; // Manejar errores de parseo
	            }
	        }
	    });

	    // Para ordenar la columna de fecha correctamente
	    sorter.setComparator(6, new Comparator<String>() {
	        @Override
	        public int compare(String fecha1, String fecha2) {
	            try {
	                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	                Date date1 = dateFormat.parse(fecha1);
	                Date date2 = dateFormat.parse(fecha2);
	                return date1.compareTo(date2);
	            } catch (ParseException e) {
	                return 0; // Manejar errores de parseo
	            }
	        }
	    });

	    return tabla;
	}

	
}

class Limpieza implements ActionListener {

	private JTextField nassTf;
	private JDateChooser fechaBajaCal;
	private JComboBox <String> tipoExpedienteCb, tipoComunicadoCb;
	private PantallaCargaDatos pantallaCarga;
	
	public Limpieza (PantallaCargaDatos pantallaCarga, JTextField nassTf, JDateChooser fechaBajaCal, JComboBox <String> tipoExpedienteCb, JComboBox <String> tipoComunicadoCb) {
		this.nassTf = nassTf;
		this.fechaBajaCal = fechaBajaCal;
		this.tipoExpedienteCb = tipoExpedienteCb;
		this.tipoComunicadoCb = tipoComunicadoCb;
		this.pantallaCarga = pantallaCarga;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		nassTf.setText("");
		fechaBajaCal.setDate(null);
		tipoExpedienteCb.setSelectedIndex(0);
		tipoComunicadoCb.setSelectedIndex(0);
		
		PantallaCargaDatos.OBTENER_BT.setEnabled(false);
		PantallaCargaDatos.tablaExpedientes.setVisible(false);
		pantallaCarga.updateUI();
		
	}
	
	
}




