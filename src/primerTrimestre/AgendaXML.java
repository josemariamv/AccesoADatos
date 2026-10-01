package primerTrimestre;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AgendaXML {

	public static void main(String[] args) throws Exception{
		leerAgenda("agenda.xml");
		buscarEnAgenda("Elena", "agenda.xml");
		buscarEnAgenda("Pepe", "agenda.xml");
		eliminarContacto("Elena", "agenda.xml");
		eliminarContacto("Pepe", "agenda.xml");
		nuevoContacto("Leonor", "666554433", "agenda.xml");
		nuevoContacto("Sofía", "666554433", "agenda.xml");
		modificaTelefono("Sofía", "111222333", "agenda.xml");
		modificaTelefono("Andrés", "111222333", "agenda.xml");
	}
	
	public static void leerAgenda(String fichero) throws Exception{
		// Leemos el XML y lo almacenamos en el objeto doc
		Document doc = leerXML(fichero);
		// creamos una lista iterable
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		// recorremos la lista de contactos
		for(int i=0; i<listaContactos.getLength(); i++) {
			//Cojo el elemento i y lo guardo en el objeto contacto
			Node nodo = listaContactos.item(i);
			Element contacto = (Element)nodo;
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			System.out.println(nombre + " - " + telefono);
		}
	}
	
	public static Element buscaContacto(String nombreBuscado, Document doc) {
		Element respuesta = null;
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		for(int i=0; i<listaContactos.getLength() && respuesta == null; i++) {	
			Node nodo = listaContactos.item(i);
			Element contacto = (Element)nodo;
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			if(nombre.equalsIgnoreCase(nombreBuscado))
				respuesta = contacto;
		}
		return respuesta;
	}
	
	public static void buscarEnAgenda(String nombreBuscado, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreBuscado, doc); 
		if(contacto!=null) {
			String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			System.out.println("El telefono de " + nombreBuscado + " es " + telefono);
		}
		else
			System.out.println("El contacto " + nombreBuscado + " no está en tu agenda");
	}
	
	public static void eliminarContacto(String nombreBuscado, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreBuscado, doc);
		if(contacto!=null) {
			Element raiz = doc.getDocumentElement();
			raiz.removeChild(contacto);
			System.out.println("El contacto " + nombreBuscado + " ha sido eliminado");		
			grabarXML(doc, fichero);
		}
		else
			System.out.println("El contacto " + nombreBuscado + " no se puede eliminar porque no existe");
	}
	
	public static void nuevoContacto(String nombreNuevo, String telefonoNuevo, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreNuevo, doc);
		if(contacto !=null)
			System.out.println("Ya existe un contacto llamado " + nombreNuevo);
		else {
			Element nuevoContacto = doc.createElement("contacto");
			Element elementoNombre = doc.createElement("nombre");
			Element elementoTelefono = doc.createElement("telefono");
			elementoNombre.setTextContent(nombreNuevo);
			elementoTelefono.setTextContent(telefonoNuevo);
			nuevoContacto.appendChild(elementoNombre);
			nuevoContacto.appendChild(elementoTelefono);
			Element raiz = doc.getDocumentElement();
			raiz.appendChild(nuevoContacto);
			grabarXML(doc, fichero);
			System.out.println("Creado nuevo contacto: " + nombreNuevo);
		}
	}
	
	public static void modificaTelefono(String nombreBuscado, String nuevoTelefono, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreBuscado, doc);
		if(contacto!=null){
			Element telefono = (Element)contacto.getElementsByTagName("telefono").item(0);
			telefono.setTextContent(nuevoTelefono);
			grabarXML(doc,fichero);
			System.out.println("Telefono modificado en el contacto " + nombreBuscado);
		}
		else
			System.out.println("El contacto " + nombreBuscado + " no existe y no puedo modificarlo");
	}
	
	public static Document leerXML(String fichero) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(fichero);
	}
	
	public static void grabarXML(Document doc, String fichero) throws Exception{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fichero);
		transformer.transform(source, result);
	}
}
