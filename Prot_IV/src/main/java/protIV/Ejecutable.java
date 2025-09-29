package protIV;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;


import Datos.AlmacenDatos;
import Datos.Campos;
import Datos.Objects;
import frames.FramePrincipal;

import java.awt.*;

public class Ejecutable {

	public static FramePrincipal framePrincipalPIV;

	public static void main(String[] args) {

		try {
            UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Define la nueva fuente
		Font font = new Font("Arial", Font.PLAIN, 15);
		setUIFont(new javax.swing.plaf.FontUIResource(font));

		SwingUtilities.invokeLater(() -> {
			
			new Campos();
			new Objects();
			new AlmacenDatos();

			framePrincipalPIV = new FramePrincipal();
			
			
		});
	}

	// Método para establecer la fuente globalmente
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, f);
			}
		}
	}
	

}
