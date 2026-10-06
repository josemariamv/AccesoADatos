package primerTrimestre;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class AgendaJSON {
	public static void main(String[] args) {
		String rutaArchivo = "agenda.json";
		leerAgenda(rutaArchivo);
		Contacto contacto = new Contacto("Maripili", "999888777", "11223344G");
		crearContacto(rutaArchivo, contacto);
		borrarContacto(rutaArchivo, "Maripili");
		modificarTelefono(rutaArchivo, "Leonor", "888777666");
	}

	public static List<Contacto> cargarAgenda(String rutaArchivo) {
		List<Contacto> contactos = null;
		try (Reader lector = new FileReader(rutaArchivo)) {
			Gson gson = new Gson();
			// Gson convierte el JSON directamente en un objeto AgendaDup
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			// Y de esta forma obtenemos una lista de contactos
			contactos = agenda.getContactos();
		} catch (Exception e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
		}
		if(contactos == null)
			System.out.println("Error al leer los contactos de la agenda");
		return contactos;
	}

	private static void guardarAgenda(String rutaArchivo, List<Contacto> contactos) {
		Agenda agenda = new Agenda();
		agenda.setContactos(contactos);
		//Gson gson = new Gson();
		// Mejor así para que el JSON se guarde bien indentado y legible
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer escritor = new FileWriter(rutaArchivo)) {
			// graba físicamente el json en el archivo físico
			gson.toJson(agenda, escritor);
		} catch (Exception e) {
			System.err.println("Error al guardar el archivo: " + e.getMessage());
		}
	}
	
	public static Contacto buscarContacto(List<Contacto> contactos, String nombreBuscado) {
        Contacto contacto = null;
        for (Contacto c : contactos)
            if (c.getNombre().equalsIgnoreCase(nombreBuscado))
                contacto = c;
        // si el contacto no existe va a devolver un null
        return contacto;
    }

	public static void leerAgenda(String rutaArchivo) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		if (contactos != null) {
			System.out.println("AGENDA");
			// el método size me da el número de contactos de la lista
			System.out.println("Total de contactos: " + contactos.size());
			for (Contacto c : contactos)
				System.out.println(c);
		} 
	}

	public static void crearContacto(String rutaArchivo, Contacto contacto) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		if (contactos != null)
			if(buscarContacto(contactos,contacto.getNombre()) == null){
				contactos.add(contacto);
				guardarAgenda(rutaArchivo, contactos);
				System.out.println("ContactoDup creado");
			}
			else
				System.out.println("Ya existe un contacto con ese nombre");
	}
	
	public static void modificarTelefono(String rutaArchivo, String nombreBuscado, String nuevoTelefono) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		Contacto encontrado = buscarContacto(contactos,nombreBuscado);
        if (encontrado!=null){
        	encontrado.setTelefono(nuevoTelefono);
            guardarAgenda(rutaArchivo, contactos);
            System.out.println("Teléfono actualizado");
        }
        else
        	System.out.println("No existe ese contacto");
    }
	
	public static void borrarContacto(String rutaArchivo, String nombreBuscado) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		if (contactos != null) {
			Contacto encontrado = buscarContacto(contactos,nombreBuscado);
			if(encontrado != null){
				contactos.remove(encontrado);
				guardarAgenda(rutaArchivo, contactos);
				System.out.println("ContactoDup eliminado");
			}
			else
				System.out.println("No existe ese contacto");
		}
	}
}
