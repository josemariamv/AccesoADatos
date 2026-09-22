package primerTrimestre;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.util.Scanner;

public class AgendaXML {

    private static final String FICHERO = "agenda.xml";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

//        System.out.println("--- Agenda actual ---");
        leerAgenda();

        System.out.println("\n\nAñadimos un nuevo contacto:");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Telefono: ");
        String telefono = sc.nextLine();

        escribirContacto(nombre, telefono);

        System.out.println("\n\nVolvemos a leer la agenda:");
        leerAgenda();

    }

    // Lee y muestra todos los contactos del XML
    public static void leerAgenda() throws Exception {
        File fichero = new File(FICHERO);

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

    // Añade un nuevo contacto al XML existente
    public static void escribirContacto(String nombre, String telefono) throws Exception {
        File fichero = new File(FICHERO);

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
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fichero);
        transformer.transform(source, result);
    }
}