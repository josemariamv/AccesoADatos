package primerTrimestre;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.util.Scanner;

public class AgendaXML {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        leerAgenda("agenda.xml");
        buscarEnAgenda("Pepe", "agenda.xml");
        buscarEnAgenda("Elena", "agenda.xml");
/*        
        System.out.println("\n\nAñadimos un nuevo contacto:");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Telefono: ");
        String telefono = sc.nextLine();
        escribirContacto(nombre, telefono, "agenda.xml");
*/
        eliminarContacto("agenda.xml", "Pepe");
        modificarTelefono("agenda.xml", "Elena", "954212270");
        
        System.out.println("\n\nVolvemos a leer la agenda:");
        leerAgenda("agenda.xml");

    }
    
    // Lee y muestra todos los contactos del XML
    public static void leerAgenda(String nombreFichero) throws Exception {
        File fichero = new File(nombreFichero);

        // cargamos en el objeto doc todo el xml 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fichero);

        // corrige posibles errores o inconsistencias en xml complejos. Aquí podríamos prescindir de ello
        //doc.getDocumentElement().normalize();

        // obtenemos la lista de contactos del xml
        NodeList listaContactos = doc.getElementsByTagName("contacto");

        // iteramos a través de ella
        for (int i = 0; i < listaContactos.getLength(); i++) {
            Node nodo = listaContactos.item(i);

            Element contacto = (Element) nodo;
            String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
            String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();

            System.out.println(nombre + " - " + telefono);
        }
    }

    // Busca si existe un contacto en el XML
    public static void buscarEnAgenda(String nombreBuscado, String nombreFichero) throws Exception {
        File fichero = new File(nombreFichero);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fichero);

        NodeList listaContactos = doc.getElementsByTagName("contacto");
        boolean encontrado = false;
        for (int i = 0; i < listaContactos.getLength() && encontrado == false; i++) {
            Element contacto = (Element) listaContactos.item(i);
            String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
            if(nombre.equalsIgnoreCase(nombreBuscado)) {
            	encontrado = true;
            	String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
            	System.out.println("El teléfono de " + nombre + " es " + telefono);
            }
        }
        if(encontrado == false)	
        	System.out.println("El contacto " + nombreBuscado + " no está en la agenda");
    }
    
    // Añade un nuevo contacto al XML existente
    public static void escribirContacto(String nombre, String telefono, String nombreFichero) throws Exception {
        File fichero = new File(nombreFichero);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fichero);

        // Cogemos el elemento raíz del XML
        Element raiz = doc.getDocumentElement();

        // Creamos un nuevo <contacto>
        Element nuevoContacto = doc.createElement("contacto");

        // creamos los elementos nombre y telefono
        Element nombreElem = doc.createElement("nombre");
        nombreElem.setTextContent(nombre);

        Element telefonoElem = doc.createElement("telefono");
        telefonoElem.setTextContent(telefono);

        // Añadimos los hijos (nombre y telefono) al contacto
        nuevoContacto.appendChild(nombreElem);
        nuevoContacto.appendChild(telefonoElem);

        // añadimos el nuevo contacto al raiz del xml
        raiz.appendChild(nuevoContacto);

        // Guardamos los  cambios en el fichero
        // Como lo tendremos que usar igual otras veces lo guardamos en un método
        guardarXML(doc, fichero);
    }  
        
    public static void guardarXML(Document doc, File fichero) throws Exception{
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fichero);
        transformer.transform(source, result);
    }
    
    public static void eliminarContacto(String nombreFichero, String nombreBuscado) throws Exception {
        File fichero = new File(nombreFichero);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fichero);

        Element raiz = doc.getDocumentElement();
        NodeList listaContactos = doc.getElementsByTagName("contacto");

        boolean encontrado = false;
        for (int i = 0; i < listaContactos.getLength() && encontrado == false; i++) {
            Element contacto = (Element) listaContactos.item(i);
            String nombreActual = contacto.getElementsByTagName("nombre").item(0).getTextContent();
            if (nombreActual.equalsIgnoreCase(nombreBuscado)) {
            	encontrado = true;
            	// eliminamos el contacto encontrado
                raiz.removeChild(contacto);
                guardarXML(doc, fichero);
                System.out.println("El contacto " + nombreBuscado + " ha sido eliminado");
            }
        }
        if(encontrado == false)
        	System.out.println("No he podido eliminar el contacto " + nombreBuscado + " porque no existe en la agenda");
    }

    // Modifica el telefono de un contacto buscando por nombre
    public static void modificarTelefono(String nombreFichero, String nombreBuscado, String nuevoTelefono) throws Exception {
        File fichero = new File(nombreFichero);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fichero);

        NodeList listaContactos = doc.getElementsByTagName("contacto");

        boolean encontrado = false;
        for (int i = 0; i < listaContactos.getLength() && encontrado == false; i++) {
            Element contacto = (Element) listaContactos.item(i);
            String nombreActual = contacto.getElementsByTagName("nombre").item(0).getTextContent();

            if (nombreActual.equalsIgnoreCase(nombreBuscado)) {
            	encontrado = true;
                Element telefono = (Element) contacto.getElementsByTagName("telefono").item(0);
                telefono.setTextContent(nuevoTelefono);
                guardarXML(doc, fichero);
                System.out.println("El telefono del contacto " + nombreBuscado + " ha sido modificado");
            }
        }
        if(encontrado == false)
        	System.out.println("No he podido modificar el teléfono del contacto " + nombreBuscado + " porque no existe en la agenda");
    }

}