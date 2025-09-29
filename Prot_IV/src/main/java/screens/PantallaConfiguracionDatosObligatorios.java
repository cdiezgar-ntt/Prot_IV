package screens;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import protIV.Ejecutable;
import utilidades.Ficheros;

public class PantallaConfiguracionDatosObligatorios extends JPanel {

	private JComboBox<String> tipoExpedienteCombo;
	private JComboBox<String> tipoComunicadoCombo;
	private List<String[]> comboExpedientes;
	private List<String[]> comboComunicados;
	private JTextField filtroCampoText;
	private JTextField filtroId;
	private JButton guardarCambios;
	private JPanel camposPanel;
	private List<String[]> listaCampos;

	public PantallaConfiguracionDatosObligatorios() {
		setLayout(new BorderLayout());

		// Lista de campos
		listaCampos = new Ficheros().leerTabla("Campos.txt", ";");

		// Panel inferior para el botón guardar cambios
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
		guardarCambios = new JButton("Guardar Cambios");
		bottomPanel.add(guardarCambios);

		guardarCambios.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				guardarCambios();
			}

		});
		// Panel superior para los combos y el campo de texto
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));

		// Combo para tipo de expediente
		topPanel.add(new JLabel("Tipo de Expediente:"));
		tipoExpedienteCombo = new JComboBox<String>();
		tipoExpedienteCombo.setPreferredSize(new Dimension(150, 20));
		comboExpedientes = new Ficheros().leerTabla("Tipos_expediente.txt", ";");
		for (int i = 0; i < comboExpedientes.size(); i++) {
			tipoExpedienteCombo.addItem(comboExpedientes.get(i)[0]);
		}
		topPanel.add(tipoExpedienteCombo);
		tipoExpedienteCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JComboBox<String> comboBox = (JComboBox<String>) arg0.getSource();

				String selectedItem = (String) comboBox.getSelectedItem();
				
				((JComboBox<String>) tipoComunicadoCombo).removeAllItems();

				if (!selectedItem.contains("-TIPO EXPEDIENTE-")) {

					List<String[]> nuevaLista = comboComunicados.stream().filter(k -> k[0].contentEquals(selectedItem))
							.collect(Collectors.toList());

					for (int i = 0; i < nuevaLista.size(); i++) {
						((JComboBox<String>) tipoComunicadoCombo)
								.addItem(nuevaLista.get(i)[0] + "-" + nuevaLista.get(i)[1]);
					}

				}

			}

		});

		// Combo para tipo de comunicado
		topPanel.add(new JLabel("Tipo de Comunicado:"));
		tipoComunicadoCombo = new JComboBox<String>();
		tipoComunicadoCombo.setPreferredSize(new Dimension(100, 20));
		comboComunicados = new Ficheros().leerTabla("Tipos_comunicado.txt", ";");
		topPanel.add(tipoComunicadoCombo);

		tipoComunicadoCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				JComboBox<String> comboBox = (JComboBox<String>) arg0.getSource();
				String selectedItem = (String) comboBox.getSelectedItem();
				
				if (selectedItem == null) {
					remove(camposPanel.getParent()); // Remove the JScrollPane containing the camposPanel
					camposPanel = null;
					JPanel newCamposPanel = new JPanel();
					JScrollPane scrollPane = new JScrollPane(newCamposPanel);
					camposPanel = newCamposPanel;
					removeAll();
					add(topPanel, BorderLayout.NORTH);
					add(scrollPane, BorderLayout.CENTER);
					add(bottomPanel, BorderLayout.SOUTH);
					revalidate(); // Volver a validar el contenedor
					repaint(); // Repintar el contenedor
				}

				if (selectedItem != null && !selectedItem.contains("TIPO COMUNICADO")) {
					if (camposPanel != null) {
						remove(camposPanel.getParent()); // Remove the JScrollPane containing the camposPanel
						camposPanel = null;
					}

					// Usar SwingWorker para manejar el trabajo en segundo plano
					SwingWorker<JPanel, Void> worker = new SwingWorker<JPanel, Void>() {
						@Override
						protected JPanel doInBackground() throws Exception {
							//JPanel newCamposPanel = new JPanel(new GridBagLayout());
							JPanel newCamposPanel = new JPanel();
							newCamposPanel.setLayout(new BoxLayout(newCamposPanel, BoxLayout.Y_AXIS));
							
							List<String[]> comunicado = comboComunicados.stream()
									.filter(k -> (k[0] + "-" + k[1]).contentEquals(selectedItem))
									.collect(Collectors.toList());

							List<Integer> obligatorios = Arrays.stream(comunicado.get(0)[3].split("-")).map(k -> {
								try {
									return Integer.parseInt(k);
								} catch (NumberFormatException e) {
									return null;
								}
							}).collect(Collectors.toList());

							//GridBagConstraints gbc = new GridBagConstraints();
							//gbc.insets = new Insets(5, 5, 1, 5);
							//gbc.anchor = GridBagConstraints.WEST;

							List<String> campos = listaCampos.stream().map(k -> k[0] + " - " + k[1])
									.collect(Collectors.toList());

							// Determinar el tamaño máximo del label
							int maxWidth = campos.stream().mapToInt(campo -> new JLabel(campo).getPreferredSize().width)
									.max().orElse(0);

							for (int i = 1; i < campos.size(); i++) {
								
								JPanel panel_i = new JPanel(new FlowLayout(FlowLayout.CENTER));
								
								String id = listaCampos.get(i)[0];
								String descripcion = listaCampos.get(i)[1];

								//gbc.gridx = 0;
								//gbc.gridy = i;

								JLabel idLabel = new JLabel(id);
								idLabel.setPreferredSize(new Dimension(20, 20));
								JPanel campoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
								//campoPanel.add(idLabel);
								panel_i.add(idLabel);
								
								//gbc.gridx = 1;
								//gbc.gridy = i;

								JLabel descripcionLabel = new JLabel(descripcion);
								descripcionLabel.setPreferredSize(
										new Dimension(maxWidth, 20));
								//campoPanel.add(descripcionLabel);
								panel_i.add(descripcionLabel);
								
								//gbc.gridx = 2;
								JRadioButton obligatorioBtn = new JRadioButton("Obligatorio");
								//campoPanel.add(obligatorioBtn);
								panel_i.add(obligatorioBtn);

								//gbc.gridx = 3;
								JRadioButton noObligatorioBtn = new JRadioButton("No obligatorio");
								//campoPanel.add(noObligatorioBtn);
								panel_i.add(noObligatorioBtn);

								ButtonGroup group = new ButtonGroup();
								group.add(obligatorioBtn);
								group.add(noObligatorioBtn);

								if (obligatorios.contains(i)) {
									obligatorioBtn.setSelected(true);
								} else {
									noObligatorioBtn.setSelected(true);
								}

								//newCamposPanel.add(campoPanel, gbc);
								newCamposPanel.add(panel_i);
							}
							return newCamposPanel;
						}

						@Override
						protected void done() {
							try {
								JPanel newCamposPanel = get(); // Obtener el panel construido en segundo plano
								JScrollPane scrollPane = new JScrollPane(newCamposPanel);
								camposPanel = newCamposPanel;
								removeAll();
								add(topPanel, BorderLayout.NORTH);
								add(scrollPane, BorderLayout.CENTER);
								add(bottomPanel, BorderLayout.SOUTH);
								revalidate(); // Volver a validar el contenedor
								repaint(); // Repintar el contenedor
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					worker.execute(); // Ejecutar el SwingWorker
				}
			}


		});

		topPanel.add(new JLabel("Id"));
		filtroId = new JTextField(5);
		topPanel.add(filtroId);

		// Campo de texto para filtrar
		topPanel.add(new JLabel("Descripción del campo:"));
		filtroCampoText = new JTextField(50);
		topPanel.add(filtroCampoText);

		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);

		// Acción para el campo de texto de filtro
		filtroCampoText.addActionListener(e -> aplicarFiltroTexto());
		filtroId.addActionListener(e -> aplicarFiltroId());

		filtroCampoText.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				aplicarFiltroTexto();
				
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				aplicarFiltroTexto();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				aplicarFiltroTexto();
			}
		});

		filtroId.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				aplicarFiltroId();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				aplicarFiltroId();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				aplicarFiltroId();
			}
		});

	}

	private void aplicarFiltroTexto() {
		String filtro = filtroCampoText.getText().toLowerCase();
		for (Component comp : camposPanel.getComponents()) {
			if (comp instanceof JPanel) {
				JPanel panel = (JPanel) comp;
				JLabel labelDescripcion = (JLabel) panel.getComponent(1);
				if (labelDescripcion.getText().toLowerCase().contains(filtro)) {
					panel.setVisible(true);
				} else {
					panel.setVisible(false);
				}
			}
		}
		camposPanel.revalidate();
		camposPanel.repaint();
	}

	private void aplicarFiltroId() {
		String filtro = filtroId.getText().toLowerCase();
		for (Component comp : camposPanel.getComponents()) {
			if (comp instanceof JPanel) {
				JPanel panel = (JPanel) comp;
				JLabel labelId = (JLabel) panel.getComponent(0);
				if (labelId.getText().toLowerCase().contentEquals(filtro) || filtro.trim().contentEquals("")) {
					panel.setVisible(true);
				} else {
					panel.setVisible(false);
				}
			}
		}
		camposPanel.revalidate();
		camposPanel.repaint();
	}

	private void guardarCambios() {
		
		if (tipoComunicadoCombo.getItemCount() == 0) {
			JOptionPane.showMessageDialog(this, "El tipo de comunicado es obligatorio", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String comunicado = tipoComunicadoCombo.getSelectedItem().toString();
		String obligatorios = "";


		for (Component comp : camposPanel.getComponents()) {
			if (comp instanceof JPanel) {
				JPanel panel = (JPanel) comp;
				JLabel labelId = (JLabel) panel.getComponent(0); // Asumiendo que el primer componente es el JLabel id
				JRadioButton obligatorioBtn = null;

				// Buscar los JRadioButton dentro del panel
				for (Component subComp : panel.getComponents()) {
					if (subComp instanceof JRadioButton) {
						JRadioButton radioButton = (JRadioButton) subComp;
						if (radioButton.getText().equals("Obligatorio")) {
							obligatorioBtn = radioButton;
						}
					}
				}

				if (obligatorioBtn != null) {
					// Obtener el estado de los JRadioButton
					boolean esObligatorio = obligatorioBtn.isSelected();

					if (esObligatorio) {
						obligatorios += labelId.getText() + "-";
					}
				}
			}
		}

		// Quitamos el ultimo caracter
		obligatorios = (obligatorios.length() > 0) ? obligatorios.substring(0, obligatorios.length() - 1)
				: obligatorios;

		// Se accede a comboComunicados para modificar los campos obligatorios del campo
		// que coincida con el comunicado.
		String[] com = comunicado.split("-");

		String lineas = "";

		for (String[] c : comboComunicados) {
			if (c[0].contentEquals(com[0]) && c[1].contentEquals(com[1])) {
				c[3] = obligatorios;
			}

			lineas += c[0] + ";" + c[1] + ";" + c[2] + ";" + c[3] + "\n";
		}

		lineas = lineas.trim();

		if (lineas.split("\n").length != comboComunicados.size()) {
			JOptionPane.showMessageDialog(Ejecutable.framePrincipalPIV, "Ha ocurrido un error inesperado al modificar la configuracion",
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		// Se vuelve a escribir el fichero
		File file = new File("Tablas/Tipos_comunicado.txt");

		if (new Ficheros().escribirFichero(file, lineas, false)) {
			JOptionPane.showMessageDialog(Ejecutable.framePrincipalPIV,
					"Se han modificado los campos obligatorios del expediente " + comunicado, "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			tipoExpedienteCombo.setSelectedIndex(0);
		}

	}
	
}
