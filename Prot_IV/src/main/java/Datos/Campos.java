package Datos;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JLabel;

import utilidades.Ficheros;

public class Campos {

	public static List <String []> listaCampos;
	public static JLabel [] labelsCampos;
	public static int sizeCampos;
	
	public Campos () {
		
		listaCampos = new Ficheros().leerTabla("Campos.txt", ";");
		
		labelsCampos = new JLabel [listaCampos.size()];
		
		sizeCampos = labelsCampos.length;
		
		for (int i = 0 ; i < sizeCampos; i++) {
			labelsCampos[i] = new JLabel (listaCampos.get(i)[0] +".   "+ listaCampos.get(i)[1]);
			labelsCampos[i].setPreferredSize(new Dimension(600,20));
		}
	}
	
}
