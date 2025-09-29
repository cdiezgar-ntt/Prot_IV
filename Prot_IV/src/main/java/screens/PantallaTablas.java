package screens;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import utilidades.Ficheros;

public class PantallaTablas extends JPanel {
    
    private String tabla;
    private List<String[]> contenidoTabla;
	private JButton guardarCambios;
	private JButton addFila;

    public PantallaTablas(String tabla) {
        setLayout(new BorderLayout());

        this.tabla = tabla + ".txt";
        this.contenidoTabla = new Ficheros().leerTabla(this.tabla, ";");

        // Crear el JTable y añadirlo al panel
        if (contenidoTabla != null && !contenidoTabla.isEmpty()) {
            String[] columnNames = contenidoTabla.get(0); // Primer elemento contiene los títulos de las columnas
            String[][] data = contenidoTabla.subList(1, contenidoTabla.size()).toArray(new String[0][]); // Resto de elementos contienen los datos

            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(tableModel);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            table.setRowSorter(sorter);
            

            // Crear el panel de filtrado
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
            JTextField[] filterFields = new JTextField[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) {
                JLabel label = new JLabel(columnNames[i]);
                topPanel.add(label);

                JTextField textField = new JTextField(10);
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

            // Agregar el JTable a un JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
            
    		// Panel inferior para el botón guardar cambios
    		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
    		guardarCambios = new JButton("Guardar Cambios");
    		addFila = new JButton("Añadir a " + tabla);

    		bottomPanel.add(guardarCambios);
    		bottomPanel.add(addFila);
            add(bottomPanel, BorderLayout.SOUTH);

            
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
    
}
