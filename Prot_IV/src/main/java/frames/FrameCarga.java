package frames;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import listeners.Eventos;
import protIV.Ejecutable;
import screens.PantallaCargaDatos;

public class FrameCarga extends JFrame{
	
	private Dimension dimension;
	
	public FrameCarga() {
	
		dimension = Toolkit.getDefaultToolkit().getScreenSize();

		this.setBounds(0, dimension.height/4, dimension.width, dimension.height/2);
		this.setResizable(false);
		this.setTitle("Buscador de expedientes");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.add(new PantallaCargaDatos());
		this.setVisible(true);
		
		this.addWindowListener(new FrameCargaActivo ());
	
	}
}

class FrameCargaActivo extends WindowAdapter {

	@Override
	public void windowClosing(WindowEvent e) {
		Ejecutable.framePrincipalPIV.setEnabled(true);				
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		Ejecutable.framePrincipalPIV.setEnabled(false);				
	}

}
