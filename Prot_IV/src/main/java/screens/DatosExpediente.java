package screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import com.toedter.calendar.JDateChooser;

import Datos.AlmacenDatos;
import Datos.Campos;
import Datos.Objects;
import listeners.Eventos;
import utilidades.Ficheros;

public class DatosExpediente extends JPanel {

	public static JButton BOTON_GENERAR;
	public static JButton BOTON_LIMPIAR;
	public static JButton BOTON_VALIDAR;
	public static JButton BOTON_MODIFICAR;
	public static JButton BOTON_CARGARDATOS;

	public static JPanel[] panelesIndividualesLabels;
	public static JPanel[] panelesIndividualesObjects;

	public DatosExpediente() {

		panelesIndividualesLabels = new JPanel[Campos.sizeCampos];
		panelesIndividualesObjects = new JPanel[Campos.sizeCampos];

		JPanel panelGrid = new JPanel(new GridLayout(Campos.sizeCampos + 2, 2));
		JScrollPane scroll = new JScrollPane(panelGrid);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel panelBotoneraInferior = new JPanel();
		JPanel panelBotoneraSuperior = new JPanel();

		BOTON_GENERAR = new JButton("Generar");
		BOTON_LIMPIAR = new JButton("Limpiar");
		BOTON_VALIDAR = new JButton("Validar");
		BOTON_MODIFICAR = new JButton("Modificar");
		BOTON_CARGARDATOS = new JButton("CargarDatos");

		this.setLayout(new BorderLayout());

		for (int i = 1; i < Campos.sizeCampos - 1; i++) {
			panelesIndividualesLabels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panelesIndividualesObjects[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panelesIndividualesLabels[i].add(Campos.labelsCampos[i]);
			panelesIndividualesLabels[i].setToolTipText(Campos.labelsCampos[i].getText());
			panelesIndividualesObjects[i].add((Component) Objects.objetos[i]);
			panelGrid.add(panelesIndividualesLabels[i]);
			panelGrid.add(panelesIndividualesObjects[i]);
			Border bordeInferior = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK); // Puedes personalizar el
			panelesIndividualesLabels[i].setBorder(bordeInferior);
			panelesIndividualesObjects[i].setBorder(bordeInferior);
		}

		this.consumirTextFields();
		this.recargarTooltips();
		this.configurarAnchosDateChoosers();

		BOTON_GENERAR.setEnabled(false);

		// Cada vez que cambia tipo de expediente se cambian tipos de comunicado
		((JComboBox<String>) Objects.objetos[1]).addActionListener(new Eventos().recargaItemsComboComunicados());

		// Cada vez que cambia el tipo de comunicado se cambia la apariencia de los
		// campos obligatorios
		((JComboBox<String>) Objects.objetos[2]).addActionListener(new Eventos().cambiaAparienciaCamposObligatorios());

		BOTON_GENERAR.addActionListener(new Eventos().generarLinea());
		BOTON_LIMPIAR.addActionListener(new Eventos().limpiar());
		BOTON_LIMPIAR.addActionListener(new Eventos().habilitarCampos());
		BOTON_VALIDAR.addActionListener(new Eventos().validarCampos());
		BOTON_VALIDAR.addActionListener(new Eventos().deshabilitarCampos());
		BOTON_MODIFICAR.addActionListener(new Eventos().habilitarCampos());
		BOTON_CARGARDATOS.addActionListener(new Eventos().cargarDatos());

		panelBotoneraSuperior.add(BOTON_CARGARDATOS);

		panelBotoneraInferior.add(BOTON_VALIDAR);
		panelBotoneraInferior.add(BOTON_MODIFICAR);
		panelBotoneraInferior.add(BOTON_LIMPIAR);
		panelBotoneraInferior.add(BOTON_GENERAR);

		this.add(panelBotoneraSuperior, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(panelBotoneraInferior, BorderLayout.SOUTH);

	}

	public void consumirTextFields() {

		for (int i = 1; i < Campos.sizeCampos; i++) {
			if (Objects.objetos[i].getClass().getName().contains("JTextField")) {
				((JTextField) Objects.objetos[i]).addKeyListener(new Eventos().consumir((JTextField) Objects.objetos[i],
						Integer.parseInt(Campos.listaCampos.get(i)[2])));
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void configurarAnchosDateChoosers() {

		for (int i = 1; i < Campos.sizeCampos; i++) {
			if (Objects.objetos[i].getClass().getName().contains("JDateChooser")) {
				((JDateChooser) Objects.objetos[i]).setPreferredSize(new Dimension(150, 35)); // Ajusta estos valores
																								// según sea necesario
			}
			if (Objects.objetos[i].getClass().getName().contains("JComboBox")) {
				((JComboBox<String>) Objects.objetos[i]).setPreferredSize(new Dimension(400, 35)); // Ajusta estos
																									// valores según sea																				// necesario
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void recargarTooltips() {
		((JComboBox<String>) Objects.objetos[1]).addItemListener(
				new Eventos().recargarTooltips(((JComboBox<String>) Objects.objetos[1]), "TIPO EXPEDIENTE"));
		((JComboBox<String>) Objects.objetos[2]).addItemListener(
				new Eventos().recargarTooltips(((JComboBox<String>) Objects.objetos[2]), "TIPO COMUNICADO"));
	}

}
