package Datos;

import java.text.SimpleDateFormat;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

public class AlmacenDatos {

	public static String[] camposFichero;
	public static String[] camposGuardados;

	public AlmacenDatos() {

		camposFichero = new String[Objects.objetos.length];
		camposGuardados = new String[Objects.objetos.length];

	}

	public void almacenar() {

		for (int i = 1; i < Campos.sizeCampos; i++) {

			String tipo = Objects.objetos[i].getClass().getName();

			if (tipo.contains("JTextField")) {
				camposFichero[i] = ((JTextField) Objects.objetos[i]).getText();
				camposGuardados[i] = ((JTextField) Objects.objetos[i]).getText().trim();
			}

			if (tipo.contains("JCheckBox")) {

				if (((JCheckBox) Objects.objetos[i]).isSelected()) {
					camposFichero[i] = "S";
					camposGuardados[i] = "true";
				} else {
					camposFichero[i] = "N";
					camposGuardados[i] = "false";
				}

			}

			if (tipo.contains("JComboBox")) {

				if (((JComboBox<String>) Objects.objetos[i]).getSelectedIndex() == 0) {
					camposFichero[i] = "";
					camposGuardados[i] = ((JComboBox<String>) Objects.objetos[i]).getSelectedItem().toString();
				} else {
					for (int j = 0; j < Objects.itemsCombos.get(i).size(); j++) {

						// TODO combo expedientes y comunicados son raros

						if (i == 1) {
							if (((JComboBox<String>) Objects.objetos[1]).getSelectedItem().toString()
									.contentEquals(Objects.itemsCombos.get(1).get(j)[0])) {
								camposFichero[1] = Objects.itemsCombos.get(1).get(j)[0];
								camposGuardados[1] = Objects.itemsCombos.get(1).get(j)[0];
							}
						}

						else if (i == 2) {
							if (((JComboBox<String>) Objects.objetos[2]).getSelectedItem().toString().substring(3, 5)
									.contentEquals(Objects.itemsCombos.get(2).get(j)[1])
									&& ((JComboBox<String>) Objects.objetos[2]).getSelectedItem().toString()
											.substring(0, 2)
											.contentEquals(Objects.itemsCombos.get(2).get(j)[0])) {
								camposFichero[2] = Objects.itemsCombos.get(2).get(j)[1];
								camposGuardados[2] = Objects.itemsCombos.get(2).get(j)[0] + "-"
										+ Objects.itemsCombos.get(2).get(j)[1];
							}
						}

						else if (((JComboBox<String>) Objects.objetos[i]).getSelectedItem().toString()
								.contentEquals(Objects.itemsCombos.get(i).get(j)[1])) {
							camposFichero[i] = Objects.itemsCombos.get(i).get(j)[0];
							camposGuardados[i] = Objects.itemsCombos.get(i).get(j)[1];
						}
					}
				}
			}

			if (tipo.contains("JDateChooser")) {
				try {
					camposFichero[i] = new SimpleDateFormat("yyyyMMdd")
							.format(((JDateChooser) Objects.objetos[i]).getDate());
					camposGuardados[i] = new SimpleDateFormat("dd/MM/yyyy")
							.format(((JDateChooser) Objects.objetos[i]).getDate());
				} catch (NullPointerException np) {
					camposFichero[i] = "00000000";
					camposGuardados[i] = "";
				}
			}

		}

	}

	@SuppressWarnings("unchecked")


	public void filler() {
		camposFichero[AlmacenDatos.camposFichero.length - 1] = "";
		camposGuardados[AlmacenDatos.camposFichero.length - 1] = "";
	}

}
