package primerTrimestre;
/*
 * Necesitamos una segunda clase que represente los hijos del JSON Los
 * nombres de los elementos del JSON deben de coincidir con los atributos de la
 * clase de esta forma Gson los asigna de forma automática
 */
public class Contacto {
	private String nombre;
	private String telefono;
	private String dni;

	public Contacto(String n, String t, String d) {
		this.nombre = n;
		this.telefono = t;
		this.dni = d;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String toString() {
		return "Nombre: " + this.nombre + "\nDNI: " + this.dni + "\nTeléfono: " + this.telefono + "\n"; 
	}
}
