package ejercicios;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Coordenadas {
	public static void main(String[] args) {
		String ruta = "coordenadas.dat";
		final int TAMANYO_REGISTRO = 20;

		System.out.println("SATELITES Y COORDENADAS: ");

		try (DataInputStream fichero = new DataInputStream(new FileInputStream(ruta))) {
			// El número de registros es el tamaño del fichero dividido entre el tamaño del mismo
			File archivo = new File(ruta);
			int numRegistros = (int) archivo.length() / TAMANYO_REGISTRO;

			for (int i = 0; i < numRegistros; i++) {
				int id = fichero.readInt();
				float latitud = fichero.readFloat();
				float longitud = fichero.readFloat();
				
				String estado = "";
				for (int j = 0; j < 4; j++)
					estado += fichero.readChar();
				// Mostrar resultados
				System.out.printf("Satélite ID: %d | Posición: (%.4f, %.4f) | Estado: %s%n", id, latitud, longitud, estado);
			}
		} catch (Exception e) {
			System.out.println("Error al leer el archivo: " + e.getMessage());
		}
	}
}
