package utilidades;

public class Utiles {

	public Utiles() {
		
	}
	
	public boolean contener(String [] array, String opcion) {
		
		boolean encontrado = false;
		for (int i = 0 ; i < array.length ; i++) {
			if (array[i].contentEquals(opcion)) {
				encontrado = true;
			}
		}
		
		return encontrado;
		
	}
	
	public boolean esVacio(String campo, int longitud) {
		return (campo.contentEquals(String.format("%-"+longitud+"s", "")));
	}
	
}
