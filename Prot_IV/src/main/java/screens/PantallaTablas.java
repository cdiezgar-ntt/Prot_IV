package screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import utilidades.Ficheros;

public class PantallaTablas extends JPanel {
    
    private String tabla;
    private List<String[]> contenidoTabla;
	private JButton guardarCambios;
	private JButton addFila;
	private JButton removeFila;
	private JButton addRandomPatient;
	private DefaultTableModel tableModel;
	private JTable table;

    public PantallaTablas(String tabla) {
    	
    	JLabel titulo = new JLabel(tabla);
		titulo.setFont(new Font("Arial", Font.BOLD, 20));
    	
        setLayout(new BorderLayout());

        this.tabla = tabla + ".txt";
        this.contenidoTabla = new Ficheros().leerTabla(this.tabla, ";");

        // Crear el JTable y añadirlo al panel
        if (contenidoTabla != null && !contenidoTabla.isEmpty()) {
            String[] columnNames = contenidoTabla.get(0); // Primer elemento contiene los títulos de las columnas
            String[][] data = contenidoTabla.subList(1, contenidoTabla.size()).toArray(new String[0][]); // Resto de elementos contienen los datos

            tableModel = new DefaultTableModel(data, columnNames);
            table = new JTable(tableModel);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            
            // Establecer un comparador para cada columna que contiene solo datos numéricos
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                boolean isNumericColumn = true;
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    Object value = tableModel.getValueAt(row, col);
                    if (!(value instanceof Number)) {
                        try {
                            // Intentar convertir a número
                            Integer.parseInt(value.toString());
                        } catch (NumberFormatException e) {
                            isNumericColumn = false;
                            break;
                        }
                    }
                }

                if (isNumericColumn) {
                    sorter.setComparator(col, new Comparator<Object>() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            Integer int1 = Integer.valueOf(o1.toString());
                            Integer int2 = Integer.valueOf(o2.toString());
                            return int1.compareTo(int2);
                        }
                    });
                }
            }

            table.setRowSorter(sorter);            

            // Crear el panel de filtrado
            
            JPanel topPanel = new JPanel(new GridLayout(0, 10, 5, 10));
                                        
            JTextField[] filterFields = new JTextField[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) {
               
            	JLabel label = new JLabel(columnNames[i]);
                topPanel.add(label);

                JTextField textField = new JTextField(8);
                filterFields[i] = textField;
                topPanel.add(textField);

                // Añadir un DocumentListener para actualizar el filtro
                textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                    @Override
                    public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        applyFilter(sorter, filterFields);
                    }

                    @Override
                    public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        applyFilter(sorter, filterFields);
                    }

                    @Override
                    public void changedUpdate(javax.swing.event.DocumentEvent e) {
                        applyFilter(sorter, filterFields);
                    }
                });
            }

            add(topPanel, BorderLayout.NORTH);
            topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen de 5 píxeles alrededor del título

            // Agregar el JTable a un JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);

	         // Habilitar el scroll horizontal
	         scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	         // Evitar que las columnas se contraigan y mantener su tamaño preferido
	         
		      // Establecer el mismo ancho fijo para todas las columnas
	
		         if (table.getColumnCount() >= 10) {

			         int fixedColumnWidth = 200; // Ancho fijo en píxeles para todas las columnas

		        	 table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			         for (int i = 0; i < table.getColumnCount(); i++) {
			             TableColumn column = table.getColumnModel().getColumn(i);
			             column.setPreferredWidth(fixedColumnWidth); // Asignar ancho fijo
			         }
		         }

	         // Añadir el scrollPane al layout
	         add(scrollPane, BorderLayout.CENTER);
            
    		// Panel inferior para el botón guardar cambios
    		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
    		guardarCambios = new JButton("Guardar Cambios");
    		addFila = new JButton("Nuevo");
    		removeFila = new JButton("Eliminar");
    		
    		bottomPanel.add(guardarCambios);
    		bottomPanel.add(addFila);
    		bottomPanel.add(removeFila);
    		
  
    		
            add(bottomPanel, BorderLayout.SOUTH);
            
            addFila.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					int columns = tableModel.getColumnCount();
					
					Object [] filaVacia = new Object[columns];
					
				    for (int i = 0; i < columns; i++) {
				        filaVacia[i] = "{" + tableModel.getColumnName(i) + "}";
				    }
					
					tableModel.addRow(filaVacia);

				}
            	
            });
            
            removeFila.addActionListener(new ActionListener() {

 				@Override
 				public void actionPerformed(ActionEvent arg0) {
 	                int selectedRow = table.getSelectedRow();
 	                if (selectedRow != -1) {
 	                    tableModel.removeRow(selectedRow);
 	                } else {
 	                    JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna fila.", "Error", JOptionPane.ERROR_MESSAGE);
 	                }

 				}
             	
             });
            
            guardarCambios.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    // Crear la lista de listas para almacenar los datos
                    List<List<String>> dataList = new ArrayList<>();

                    // Crear una lista para los nombres de las columnas
                    List<String> columnNames = new ArrayList<>();
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        columnNames.add(tableModel.getColumnName(col));
                    }

                    // Añadir la lista de nombres de columnas como la primera fila en dataList
                    dataList.add(columnNames);

                    // Aquí recorremos todas las filas del modelo, no las visibles por el filtro
                    for (int modelRow = 0; modelRow < tableModel.getRowCount(); modelRow++) {
                        List<String> rowData = new ArrayList<>();
                        for (int col = 0; col < tableModel.getColumnCount(); col++) {
                            rowData.add(tableModel.getValueAt(modelRow, col).toString());
                        }
                        dataList.add(rowData);
                    }

                    // Aquí puedes realizar cualquier operación con dataList, como guardarlo
                    if (new Ficheros().guardarTablaConfiguracion(tabla, dataList)) {
                        JOptionPane.showMessageDialog(null, "Datos guardados correctamente", "Guardar " + tabla, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Se ha producido un error al guardar los datos", "Guardar " + tabla, JOptionPane.ERROR_MESSAGE);
                    }
                }
            });



            
        } else {
            // Mostrar un mensaje si no hay datos
            add(new JLabel("No se encontraron datos en la tabla."), BorderLayout.CENTER);
        }
    }

    private void applyFilter(TableRowSorter<DefaultTableModel> sorter, JTextField[] filterFields) {
        List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
        for (int i = 0; i < filterFields.length; i++) {
            String text = filterFields[i].getText();
            if (text.trim().length() > 0) {
                filters.add(RowFilter.regexFilter("(?i)" + text, i));
            }
        }
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }
    
 // Métodos auxiliares
    private String getConsonantes(String str, int numConsonantes) {
        String consonantes = str.replaceAll("[AEIOUaeiou\\s]", "").toUpperCase();
        return consonantes.length() >= numConsonantes ? consonantes.substring(0, numConsonantes) : consonantes;
    }

    private String generarDniValido() {
        int numero = (int) (Math.random() * 100000000);
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        char letra = letras.charAt(numero % 23);
        return String.format("%08d%c", numero, letra);
    }
    
    private String calcularDigitosControlNass(String numero) {
        // Paso 2: Realizamos la operación Mod 97
        long resto = Long.parseLong(numero) % 97;

        // Paso 3 y 4: Si el resto es menor que 10, agregamos un cero delante
        return (resto < 10) ? "0" + resto : String.valueOf(resto);
    }
    



    private String generarNumeroAleatorio(int length) {
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < length; i++) {
            numero.append((int) (Math.random() * 10));
        }
        return numero.toString();
    }
    
}
