package primerTrimestre;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AgendaXMLConDup {

	public static void main(String[] args) throws Exception{
		String fichero = "agenda2.xml";

		Document doc = leerXML(fichero);
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		System.out.println("Total de contactos: " + listaContactos.getLength());
		for(int i=0; i<listaContactos.getLength(); i++) {
			Element contacto = (Element)listaContactos.item(i);
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			System.out.println(nombre);
			//String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			NodeList telefonos = contacto.getElementsByTagName("telefono");
			System.out.println("Teléfonos: ");
			for (int j = 0; j < telefonos.getLength(); j++)
				System.out.println(" - " + telefonos.item(j).getTextContent());
			System.out.println("");
		}
	}
	
	public static Document leerXML(String fichero) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(fichero);
	}
}
