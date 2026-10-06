package ejercicios;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;

import primerTrimestre.Contacto;

public class AgendaJSONConDup {
	
	public static void main(String[] args) {
		String rutaArchivo = "agenda2.json";
		List<ContactoDup> contactos = null;
		try (Reader lector = new FileReader(rutaArchivo)) {
			Gson gson = new Gson();
			AgendaDup agenda = gson.fromJson(lector, AgendaDup.class);
			contactos = agenda.getContactos();
		} catch (Exception e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
		}
		if(contactos == null)
			System.out.println("Error al leer los contactos de la agenda");
		else {
			System.out.println("AGENDA");
			System.out.println("Total de contactos: " + contactos.size());
			for (ContactoDup c : contactos)
				System.out.println(c);
		} 
	}
}
