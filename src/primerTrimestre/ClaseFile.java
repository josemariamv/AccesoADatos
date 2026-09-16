package primerTrimestre;

import java.io.File; // clase clásica para manejar ficheros
import java.io.FilenameFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.nio.file.Files; // clase avanzada y mas moderna que amplia la funcionalidad de la anterior
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.Date;

public class ClaseFile {

	// File.separator me permite hacer programas multiplataforma
	private static final String DIRECTORIO_CONFIG = "DAM2";
	// File.separator nos ayuda a hacer una aplicación multiplataforma obviando que
	// necesitemos saber
	// el separador de directorios que usa el sistema de archivos donde ejecutamos
	// la app
	private static final String SUBDIRECTORIO = DIRECTORIO_CONFIG + File.separator + "josemaria";
	private static final String ARCHIVO_CONFIG = SUBDIRECTORIO + File.separator + "config.txt";
	private static final String ARCHIVO_CONFIG_RENOMBRADO = SUBDIRECTORIO + File.separator + "config_backup.txt";

	public static void main(String[] args) {

		try {
			String directorioTrabajo = new File(".").getAbsolutePath();
			System.out.println("El directorio actual es: " + directorioTrabajo);
			// File subDir = new File(DIRECTORIO_CONFIG);
			File subDir = new File(SUBDIRECTORIO);

			// mkdir() solo puede crear directorios simples
			// mkdirs() puede crear una estructura compleja con mas de un nivel de una sóla
			// vez
			// if (subDir.mkdir()) {
			if (subDir.mkdirs())
				System.out.println("Directorio creado con éxito");
			else
				System.out.println("Los directorios no pudieron crearse o ya existen");

			if (subDir.exists()) {
				System.out.println("El directorio existe");
				/*
				 * FileWriter escritor = new FileWriter(ARCHIVO_CONFIG, true); if (escritor ==
				 * null) System.out.println("No he podido crear el archivo de configuración");
				 * else {
				 * System.out.println("El archivo de configuración ya existía o ha sido creado"
				 * ); escritor.close(); }
				 */
				// también puedo crearlo así:
				File archivo = new File(ARCHIVO_CONFIG);
				
				if (archivo.createNewFile())
					System.out.println("Archivo de confguración creado");
				else
					System.out.println("El archivo ya existe o no puedo crearlo");
			} else
				System.out.println("El directorio no existe");
			
			// ver el espacio libre en disco
			System.out.println("Espacio libre en esta partición del disco en Gigas: " + new File(".").getFreeSpace() / 1024 / 1024 / 1024 + " GB");
			
			// ver las propiedades de un archivo:
			File archivo = new File("/home/josemaria/Documentos/Equal-Earth-Map-0-ES.jpg");
			System.out.println("Nombre: " + archivo.getName());
			System.out.println("Nombre y ruta: " + archivo.getAbsolutePath());
			System.out.println("¿Es un archivo de datos?: " + archivo.isFile());
			System.out.println("¿Es un directorio?: " + archivo.isDirectory());
			System.out.println("¿Está oculto?: " + archivo.isHidden());
			System.out.println("Tamaño actual (en bytes): " + archivo.length());
			System.out.println("Última modificación: " + new Date(archivo.lastModified()));

		} catch (IOException e) {
			System.err.println("Ocurrió un error grave en el sistema de archivos: " + e.getMessage());
			e.printStackTrace();
		}
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
