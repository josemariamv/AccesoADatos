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
		Contacto contacto = new Contacto("Maripili", "999888777");
		crearContacto(rutaArchivo, contacto);
		borrarContacto(rutaArchivo, "Maripili");
		modificarTelefono(rutaArchivo, "Leonor", "888777666");
	}

	public static List<Contacto> cargarAgenda(String rutaArchivo) {
		// El año pasado trabajamos mas con ArrayList que es mas completa que List
		// Gson trabaja con List, pero si nos sentimos mas cómodos podemos seguir usando
		// a nuestra amiga:
		// ArrayList<Contacto> contactos = (ArrayList)agenda.contactos;
		// Para lo que vamos a hacer no vamos a encontrar diferencia
		List<Contacto> contactos = null;
		try (Reader lector = new FileReader(rutaArchivo)) {
			Gson gson = new Gson();
			// Gson convierte el JSON directamente en un objeto Agenda
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			// Y de esta forma creamos una lista de contactos
			contactos = agenda.contactos;
		} catch (Exception e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
		}
		if(contactos == null)
			System.out.println("Error al leer los contactos de la agenda");
		return contactos;
	}

	private static void guardarAgenda(String rutaArchivo, List<Contacto> contactos) {
		Agenda agenda = new Agenda();
		agenda.contactos = contactos;
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
            if (c.nombre.equalsIgnoreCase(nombreBuscado))
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
				System.out.println(c.nombre + ": " + c.telefono);
		} 
	}

	public static void crearContacto(String rutaArchivo, Contacto contacto) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		if (contactos != null)
			if(buscarContacto(contactos,contacto.nombre) == null){
				contactos.add(contacto);
				guardarAgenda(rutaArchivo, contactos);
				System.out.println("Contacto creado");
			}
			else
				System.out.println("Ya existe un contacto con ese nombre");
	}
	
	public static void modificarTelefono(String rutaArchivo, String nombreBuscado, String nuevoTelefono) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		Contacto encontrado = buscarContacto(contactos,nombreBuscado);
        if (encontrado!=null){
        	encontrado.telefono = nuevoTelefono;
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
				System.out.println("Contacto eliminado");
			}
			else
				System.out.println("No existe ese contacto");
		}
	}

	/*
	 * Necesitamos una clase que represente el raiz de nuestro JSON Con la anotación
	 * SerializedName le decimos el elemento raíz que tiene que buscar en nuestro
	 * JSON
	 */
	private static class Agenda {
		@SerializedName("agenda")
		List<Contacto> contactos;
	}

	/*
	 * Y luego necesitamos una segunda clase que represente los hijos del JSON Los
	 * nombres de los elementos del JSON deben de coincidir con los atributos de la
	 * clase de esta forma Gson los asigna de forma automática
	 */
	private static class Contacto {
		private String nombre;
		private String telefono;

		public Contacto(String n, String t) {
			this.nombre = n;
			this.telefono = t;
		}
	}
}
