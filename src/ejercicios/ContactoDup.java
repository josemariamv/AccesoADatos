package ejercicios;

import java.util.List;

/*
 * Necesitamos una segunda clase que represente los hijos del JSON Los
 * nombres de los elementos del JSON deben de coincidir con los atributos de la
 * clase de esta forma Gson los asigna de forma automática
 */
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
	
/*	public String toString() {
		return "Nombre: " + this.nombre + "\nDNI: " + this.dni + "\nTeléfonos: " + this.telefonos + "\n"; 
	}*/
	
	public String toString() {
		String contacto = "Nombre: " + this.nombre + "\nDNI: " + this.dni + "\nTeléfonos: \n" ;
		for(String tlf: this.telefonos)
			contacto+=" - " + tlf + "\n";
		return contacto;
}
}
