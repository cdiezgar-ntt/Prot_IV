package frames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import Datos.AlmacenDatos;
import Datos.Campos;
import Datos.Objects;
import screens.DatosExpediente;
import screens.PantallaCargaDatos;
import screens.PantallaConfiguracionDatosObligatorios;
import screens.PantallaTablas;

public class FramePrincipal extends JFrame {

	private JPanel menuPanel;
	private JPanel tablasPanel;
	private JPanel contentPanel;
	private JPanel panelBotones, panelBotones2;
	
	public FramePrincipal () {
		
		this.setTitle("PROTOCOLO IV");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setResizable(true);
		
		this.setLayout(new BorderLayout());
		
		menuPanel = new JPanel(new BorderLayout());
		tablasPanel = new JPanel();
		tablasPanel.setLayout(new BorderLayout());
    	panelBotones = new JPanel();
    	panelBotones.setLayout(new BoxLayout(panelBotones,BoxLayout.Y_AXIS));
    	panelBotones2 = new JPanel();
    	panelBotones2.setLayout(new BoxLayout(panelBotones2,BoxLayout.Y_AXIS));
    	
		JButton nuevoExpedienteButton = new JButton("Nuevo Expediente");
		JButton configuracionButton = new JButton("Obligatorios");
		
		nuevoExpedienteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		configuracionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		
        // Establece un tamaño fijo para los botones
        Dimension buttonSize = new Dimension(180, 30);
        nuevoExpedienteButton.setPreferredSize(buttonSize);
        nuevoExpedienteButton.setMaximumSize(buttonSize);
        nuevoExpedienteButton.setMinimumSize(buttonSize);
        configuracionButton.setPreferredSize(buttonSize);
        configuracionButton.setMaximumSize(buttonSize);
        configuracionButton.setMinimumSize(buttonSize);
		
        panelBotones2.add(Box.createRigidArea(new Dimension(0, 10)));

        // Añade el primer botón
        panelBotones2.add(nuevoExpedienteButton);

        // Añade espacio entre los botones
        panelBotones2.add(Box.createRigidArea(new Dimension(0, 10)));

        // Añade el segundo botón
        panelBotones2.add(configuracionButton);
        
        //Creamos un botón para configuraciones del resto de tablas
        
        File directorio = new File("Tablas");
        if (!directorio.exists() || !directorio.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Directorio no encontrado: " + "Tablas");
            return;
        }
        
        // Filtrar los ficheros que terminan en ".txt" y eliminar la extensión
        File[] ficheros = directorio.listFiles((dir, name) -> name.endsWith(".txt"));
        if (ficheros == null || ficheros.length == 0) {
            return;
        }
        
        //Creacion de los botones
        for (File fichero : ficheros) {
        	
            panelBotones.add(Box.createRigidArea(new Dimension(0, 10)));
        	
            String nombreFichero = fichero.getName();
            String nombreSinExtension = nombreFichero.substring(0, nombreFichero.length() - 4);

            // Crear un botón para cada fichero
            JButton boton = new JButton(nombreSinExtension);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            boton.setPreferredSize(buttonSize);
            boton.setMaximumSize(buttonSize);
            boton.setMinimumSize(buttonSize);
            
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	
    				showPanel(nombreSinExtension);
                }
            });

            // Añadir el botón al frame
            panelBotones.add(boton);
            

        }
        
        
        menuPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)); // Línea vertical sólida a la derecha
        tablasPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK)); // Línea vertical sólida a la izquierda

		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		
		nuevoExpedienteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPanel("Expediente");
			}
			
		});
		
		configuracionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPanel("Obligatorios");
			}
			
		});
		
        // Crear paneles de margen para los botones
        JPanel margenIzquierdo = new JPanel();
        JPanel margenDerecho = new JPanel();
        JPanel margenIzquierdo2 = new JPanel();
        JPanel margenDerecho2 = new JPanel();
        
        // Establecer el tamaño de los márgenes
        int margenTamano = 20;
        margenIzquierdo.setPreferredSize(new Dimension(margenTamano, 0));
        margenDerecho.setPreferredSize(new Dimension(margenTamano, 0));
        
        menuPanel.add(margenIzquierdo2, BorderLayout.WEST);
        menuPanel.add(panelBotones2, BorderLayout.CENTER);
        menuPanel.add(margenDerecho2, BorderLayout.EAST);

        tablasPanel.add(margenIzquierdo, BorderLayout.WEST);
        tablasPanel.add(panelBotones, BorderLayout.CENTER);
        tablasPanel.add(margenDerecho, BorderLayout.EAST);

		add(menuPanel, BorderLayout.WEST);
		add(contentPanel, BorderLayout.CENTER);
		add(tablasPanel, BorderLayout.EAST);
		
		this.setVisible(true);
	}
	
	private void showPanel(String nombrePanel) {
		
		contentPanel.setVisible(false);
		contentPanel.removeAll();
		
		if (nombrePanel.contentEquals("Obligatorios")) {
			JPanel pantallaConfiguracion = new PantallaConfiguracionDatosObligatorios();
			contentPanel.add(pantallaConfiguracion, BorderLayout.CENTER);
		}
		
		else if (nombrePanel.contentEquals("Expediente")) {
			new Campos();
			new Objects();
			new AlmacenDatos();
			JPanel pantallaExpediente = new DatosExpediente();
			contentPanel.add(pantallaExpediente);
		}
		
		else {
			JPanel pantallaTabla = new PantallaTablas(nombrePanel);
			contentPanel.add(pantallaTabla);
		}
		
		contentPanel.setVisible(true);

	}


}

