package primerTrimestre;

import java.io.File; // clase clásica para manejar ficheros
import java.io.FilenameFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.file.Files;	// clase avanzada y mas moderna que amplia la funcionalidad de la anterior
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.Date;

public class ClaseFile {

	// File.separator me permite hacer programas multiplataforma
	private static final String DIRECTORIO_CONFIG = "DAM2";
	// File.separator nos ayuda a hacer una aplicación multiplataforma obviando que necesitemos saber
	// el separador de directorios que usa el sistema de archivos donde ejecutamos la app
	private static final String SUBDIRECTORIO = DIRECTORIO_CONFIG + File.separator + "josemaria";
	private static final String ARCHIVO_CONFIG = SUBDIRECTORIO + File.separator + "config.txt";
	private static final String ARCHIVO_CONFIG_RENOMBRADO = SUBDIRECTORIO + File.separator + "config_backup.txt";

	public static void main(String[] args) {

		try {
			String directorioTrabajo = new File(".").getAbsolutePath();
	        System.out.println("El directorio actual es: " + directorioTrabajo);
	        //File subDir = new File(DIRECTORIO_CONFIG);
	        File subDir = new File(SUBDIRECTORIO);
	       
	        // mkdir() solo puede crear directorios simples
	        // mkdirs() puede crear una estructura compleja con mas de un nivel de una sóla vez
	        //if (subDir.mkdir()) {
			if (subDir.mkdirs())
				System.out.println("Directorio creado con éxito");
			else
				System.out.println("Los directorios no pudieron crearse o ya existen");
			
			 if(subDir.exists()) {
		        	System.out.println("El directorio existe");
		        	FileWriter escritor = new FileWriter(ARCHIVO_CONFIG , true);
		        	if(escritor==null)
		        		System.out.println("No he podido crear el archivo de configuración");
		        	else {
		        		System.out.println("El archivo de configuración ya existía o ha sido creado");
		        		escritor.close();
		        	}
			 }
		        else
		        	System.out.println("El directorio no existe");
			
			System.out.println("Espacio libre en esta partición del disco: " + new File(".").getFreeSpace() /1024 /1024 /1024 + " GB");

		} catch (Exception e) {
			System.err.println("Ocurrió un error grave en el sistema de archivos: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static File gestionarMetadatosArchivo() throws IOException {
		System.out.println("--- 2. Creación y Metadatos del Archivo ---");
		File archivo = new File(ARCHIVO_TEXTO);

		// createNewFile() crea el archivo físico solo si no existe previamente
		if (archivo.createNewFile()) {
			System.out.println("[OK] Archivo físico creado en el disco: " + ARCHIVO_TEXTO);
		} else {
			System.out.println("[INFO] El archivo ya existe. Trabajaremos sobre el actual.");
		}

		// Análisis completo de propiedades (Inspección técnica)
		System.out.println("Nombre del archivo: " + archivo.getName());
		System.out.println("Ruta absoluta: " + archivo.getAbsolutePath());
		System.out.println("¿Es un archivo de datos?: " + archivo.isFile());
		System.out.println("¿Es un directorio/carpeta?: " + archivo.isDirectory());
		System.out.println("¿Está oculto en el sistema?: " + archivo.isHidden());
		System.out.println("Tamaño actual en bytes: " + archivo.length());
		System.out.println("Última modificación: " + new Date(archivo.lastModified()));
		System.out.println("Espacio libre en esta partición de disco: " + (archivo.getFreeSpace() / 1024 / 1024 / 1024) + " GB");
		System.out.println();

		return archivo;
	}

	/**
	 * Escritura de texto utilizando un BufferedWriter acoplado a un FileWriter.
	 * Incluye el parámetro 'append' para no borrar lo que ya existía.
	 */
	private static void escribirContenido(File archivo) throws IOException {
		System.out.println("--- 3. Escritura Eficiente (Buffer) ---");

		// El segundo parámetro 'true' activa el modo APPEND (añadir al final sin
		// sobrescribir)
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true))) {
			escritor.write("Línea 1: Bienvenidos a la clase avanzada de archivos.\n");
			escritor.write("Línea 2: Java gestiona streams eficientemente.\n");
			escritor.newLine(); // Método nativo para saltar de línea según el Sistema Operativo
			escritor.write("Línea 3: Registro añadido el: " + new Date() + "\n");

			System.out.println("[OK] Datos escritos correctamente dentro del archivo.");
		} // El try-with-resources cierra automáticamente el buffer y libera el archivo
			// del sistema
		System.out.println();
	}

	/**
	 * Lectura de archivos a alta velocidad línea por línea, evitando saturar la
	 * memoria RAM.
	 */
	private static void leyendoContenido(File archivo) throws IOException {
		System.out.println("--- 4. Lectura Eficiente (Línea por Línea) ---");

		try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
			String linea;
			int contadorLineas = 1;

			System.out.println(">> Contenido del archivo impreso en consola:");
			while ((linea = lector.readLine()) != null) {
				System.out.println("   [Fila " + contadorLineas + "]: " + linea);
				contadorLineas++;
			}
		}
		System.out.println();
	}

	/**
	 * Restricción y apertura de permisos de seguridad directamente sobre el
	 * archivo.
	 */
	private static void modificarPermisos(File archivo) {
		System.out.println("--- 5. Modificación de Permisos de Seguridad ---");

		System.out.println("¿Se puede leer actualmente?: " + archivo.canRead());
		System.out.println("¿Se puede escribir actualmente?: " + archivo.canWrite());
		System.out.println("¿Es ejecutable?: " + archivo.canExecute());

		// Cambiar permisos: Quitamos permisos de escritura temporalmente
		boolean resultadoPermiso = archivo.setWritable(false);
		System.out.println("Intentando quitar permiso de escritura... ¿Logrado?: " + resultadoPermiso);
		System.out.println("Comprobación - ¿Se puede escribir ahora?: " + archivo.canWrite());

		// Devolvemos el permiso para que el programa pueda seguir operando sin fallos
		// en el futuro
		archivo.setWritable(true);
	}

	/**
	 * Escanea una carpeta y filtra el contenido en tiempo real usando un criterio
	 * de búsqueda.
	 */
	private static void listarYFiltrarArchivos() {
		System.out.println("\n--- 6. Listado y Filtros Avanzados (FilenameFilter) ---");
		File carpeta = new File(SUB_CARPETA);

		// Creamos un filtro para buscar únicamente archivos que terminen en ".txt"
		FilenameFilter filtroTxt = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String nombre) {
				return nombre.toLowerCase().endsWith(".txt");
			}
		};

		String[] listaFiltrada = carpeta.list(filtroTxt);

		if (listaFiltrada != null && listaFiltrada.length > 0) {
			System.out.println("Archivos '.txt' encontrados en " + SUB_CARPETA + ":");
			for (String nombreArchivo : listaFiltrada) {
				System.out.println(" -> " + nombreArchivo);
			}
		} else {
			System.out.println("No se encontraron archivos con esa extensión.");
		}
		System.out.println();
	}

	/**
	 * Transición a Java NIO (Tecnología Moderna). Permite renombrar/mover archivos
	 * y extraer metadatos del núcleo del SO de manera óptima.
	 */
	private static void operacionesModernasNIO() throws IOException {
		System.out.println("--- 7. Operaciones de Vanguardia con Java NIO.2 ---");

		Path origen = Paths.get(ARCHIVO_TEXTO);
		Path destino = Paths.get(ARCHIVO_RENOMBRADO);

		if (Files.exists(origen)) {
			// Renombrar o mover el archivo de forma atómica en el disco duro
			Files.move(origen, destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			System.out.println("[OK] Archivo renombrado de forma segura a: " + destino.getFileName());
		}

		// Lectura de atributos de bajo nivel (Windows/Linux/Mac) con una sola petición
		BasicFileAttributes atributos = Files.readAttributes(destino, BasicFileAttributes.class);
		System.out.println("Fecha de creación real: " + atributos.creationTime());
		System.out.println("¿Es un enlace simbólico/acceso directo?: " + atributos.isSymbolicLink());
		System.out.println();
	}

	/**
	 * Eliminar archivos y carpetas requiere un orden estricto. Java no borrará una
	 * carpeta si esta contiene archivos adentro.
	 */
	private static void limpiarTodo() {
		System.out.println("--- 8. Borrado Seguro y Recursivo ---");
		File archivoAElminar = new File(ARCHIVO_RENOMBRADO);
		File subCarpeta = new File(SUB_CARPETA);
		File carpetaRaiz = new File(CARPETA_RAIZ);

		// 1º Borrar el archivo interno
		if (archivoAElminar.delete())
			System.out.println("Archivo eliminado.");
		// 2º Borrar la subcarpeta ahora que está vacía
	}
}
