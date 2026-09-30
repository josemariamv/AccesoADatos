package primerTrimestre;

import java.util.List;
import com.google.gson.annotations.SerializedName;

/*
 * Necesitamos una clase que represente el raiz de nuestro JSON Con la anotación
 * SerializedName le decimos el elemento raíz que tiene que buscar en nuestro
 * JSON
 */
public class Agenda {
	@SerializedName("agenda")
	// El año pasado trabajamos mas con ArrayList que es mas completa que List
	// Gson trabaja con List, pero si nos sentimos mas cómodos podemos seguir usando
	// a nuestra amiga:
	
	// ArrayList<Contacto> contactos = (ArrayList)agenda.contactos;
	
	// Pero, como ves en la línea de arriba, nos obliga a usar casts
	// Y pra lo que vamos a hacer no vamos a encontrar diferencia
	private List<Contacto> contactos;
	
	public List<Contacto> getContactos() {
		return contactos;
	}
	
	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
}
