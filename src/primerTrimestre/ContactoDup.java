package primerTrimestre;

import java.util.List;

// Ahora telefonos es una lista. Los cambios son mínimos respecto a la anterior clase
// Hemos eliminado todos los métodos que no usamos porque la funcionalidad ahora es mas reducida
// Si quisiéramos un CRUD como en el anterior ejemplo tendríamos que añadirlos de nuevo adaptándonos
// a que ahora usamos una lista para almacenar los teléfonos
public class ContactoDup {
	private String nombre;
	private List<String> telefonos;
	private String dni;

	public ContactoDup(String n, List<String> t, String d) {
		this.nombre = n;
		this.telefonos = t;
		this.dni = d;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String toString() {
		String contacto = "Nombre: " + this.nombre + "\nDNI: " + this.dni + "\nTeléfonos: \n" ;
		for(String tlf: this.telefonos)
			contacto+=" - " + tlf + "\n";
		return contacto;
}
}
