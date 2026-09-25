package primerTrimestre;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class AgendaJSON {
    public static void main(String[] args) {
        String rutaArchivo = "agenda.json";
        leerAgenda(rutaArchivo);
    }
    
    public static void leerAgenda(String rutaArchivo) {

    	try (Reader lector = new FileReader(rutaArchivo)) {

    		Gson gson = new Gson();
            // Gson convierte el JSON directamente en un objeto Agenda
            Agenda agenda = gson.fromJson(lector, Agenda.class);
            // Y de esta forma creamos una lista de contactos
            List<Contacto> contactos = agenda.contactos;

            // El año pasado trabajamos mas con ArrayList que es mas completa que List
            // Gson trabaja con List, pero si nos sentimos mas cómodos podemos seguir usando a nuestra amiga:
            // ArrayList<Contacto> contactos = (ArrayList)agenda.contactos;
            // Para lo que vamos a hacer no vamos a encontrar diferencia

            System.out.println("AGENDA");
            System.out.println("Total de contactos: " + contactos.size());

            for (Contacto c : contactos)
                System.out.println(c.nombre + ": " + c.telefono);

        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    /*
     * Necesitamos una clase que represente el raiz de nuestro JSON
     * Con la anotación SerializedName le decimos el elemento raíz que tiene que buscar en nuestro JSON 
     */
    private static class Agenda {
        @SerializedName("agenda")
        List<Contacto> contactos;
    }

    /*
     * Y luego necesitamos una segunda clase que represente los hijos del JSON
     * Los nombres de los elementos del JSON deben de coincidir con los atributos de la clase
     * de esta forma Gson los asigna de forma automática
     */
    private static class Contacto {
        String nombre;
        String telefono;
    }
}
