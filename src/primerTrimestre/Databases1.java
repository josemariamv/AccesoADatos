package primerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 PARA TRABAJAR CON BASES DE DATOS MYSQL/MARIADB DESDE ECLIPSE:
 - Descargar el conector J (antes JDBC) de la página oficial de Oracle
    https://dev.mysql.com/downloads/connector/j/
    Elegir la opción de "plataforma independiente"
    Descargar el archivo con cualquiera de los dos formatos propuestos y descomprimirlo en local
    NOTA: aunque parezca que hay que crearse una cuenta, en la parte inferior de la página aparece una opción para descargar el archivo sin necesidad de hacerlo

- En el proyecto de Eclipse donde vamos a trabajar con el
    - Pulsar con el botón derecho sobre el nombre del proyecto
    - Elegir Build Path
    - Elegir Add External Archives
    - Navegar hasta donde hemos descomprimido el conector J y elegir el archivo .jar que estaba dentro
    IMPORTANTE: si borras el archivo del disco duro dejará de funcionar. 
    
    NOTA: si no nos aparece la opción de "Add External Archives" en Build Path elegir la perspectiva de Java y a continuación "Resetear" Perspectiva".
 */

public class Databases1 {

	public static void main(String[] args) {
		// En la cadena de conexión indicamos la ubicación del gestor de bases de datos
		// String url = "jdbc:mysql://localhost:3306/";
		// Y, opcionalmente, la base de datos con la que queremos trabajar.
		// Sería equivalente a ejecutar el comando USE después de hacer la conexión
		String url = "jdbc:mysql://localhost:3306/sakila";
		String usr = "josemaria";
		String pswd = "abc123";

		// El objeto de la clase Connection es obligatorio para conectar a la base de
		// datos
		// podemos usar la estrategia de try-with-resource como en los ficheros
		// la conexión se cierra automáticamente al cerrarse el bloque try
		// Si no lo hacemos así tenemos que cerrar manualmente
		try (Connection conexion = DriverManager.getConnection(url, usr, pswd)) {
			// Si se ejecuta esta línea es que la conexión ha funcionado
			// Si no, saltaría al catch
			System.out.println("Conexión realizada con exito\n");
			
			//primerEjemplo(conexion);
			segundoEjemplo(conexion);
			// tercerEjemplo(conexion);
			//cuartoEjemplo(conexion);
			
			// Si no usamos el try-with-resources al final de la ejecución hay que cerrar el
			// objeto de conexión
			// conexion.close();

		} catch (SQLException e) {
			// SQLException nos da mas información que Exception sobre los posibles errores
			System.err.println("Error: " + e.getMessage());
			// printStackTrace() nos da mucha mas información pero también es mas "alarmante" para el usuario
			e.printStackTrace();
		}
	}

	public static void primerEjemplo(Connection conexion) throws SQLException{
		// Statement es la clase que nos permite lanzar queryes
		Statement sql = conexion.createStatement();
		// Y ResultSet la que nos permite recoger los resultados
		// podemos hacer cualquier query con JOIN, WHERE, ORDER, etc!
		ResultSet resultado = sql.executeQuery("SELECT * FROM actor");
		// el método .next() devuelve false cuando el query ya no tenga mas resultados
		// mientras tanto, avanza a la siguiente línea de los resultados del query
		// cada vez que lo ejecutamos. Igual que readLine en un fichero!
		while (resultado.next()) {
			// Podemos recoger los resultados indicando el número de la columna (empezando por 1)
			// System.out.printf("ID: %d - %s, %s\n", resultado.getInt(1),
			// resultado.getString(3), resultado.getString(2));
			// O el nombre que devuelve la base de datos
			// Si lo hacemos con el número de la columna recuerda que es según el orden con que
			// aparecen en el query y no en la base de datos
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
			// o con la posición que ocupa en el query, empezando por 0
		}
		// Otros get: getLong(), getDouble(), getFloat(), getBoolean(), getDate() y getTimeStamp()
	}
	
	public static void segundoEjemplo(Connection conexion) throws SQLException{
		// Por defecto en los ResultSet sólo se puede avanzar hacía delante y no se pueden modificar los resultados
        // Pero se puede cambiar con los siguientes modificadores:
        // TYPE_FORWARD_ONLY: Sólo podemos ir hacía delante en el ResultSet
        // TYPE_SCROLL_INSENSITIVE: Podemos avanzar y retroceder y posicionarnos en el ResulSet
        // TYPE_SCROLL_SENSITIVE: Idem al anterior pero además las modificaciones hechas en la bb.dd. se reflejan en el ResultSet. 
        //		No implementado en MySQL/MariaDB
        // CONCUR_READ_ONLY: El ResultSet no puede modificarse
        // CONCUR_UPDATABLE: Podemos modificar el ResultSet y se actualiza el resultado en la base de datos
		// Las opciones por defecto si no ponemos nada son TYPE_FORWARD_ONLY y CONCUR_READ_ONLY
		
		Statement sql = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultado = sql.executeQuery("SELECT * FROM actor");
		// Nos posicionamos en la última linea
		resultado.last();
		// getRow() nos dice el número de línea del total del resultado. Como es la última, sería como un size() en un ArrayList
		System.out.println("El query ha devuelto " + resultado.getRow() + " líneas");
		// nos posicionamos después de la última
		resultado.afterLast();
		// y ahora vamos retrocediendo con previous() en lugar de avanzando con next()
		// el bucle terminará ahora cuando nos situemos antes de la primera
		// podemos ir a esa posición con el método beforerFirst()
		while (resultado.previous()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
		// Otros métodos que nos permiten posicionarnos en el ResultSet:
		// absolute(n) Salta a la fila n, contando desde el principio (la 1 es la primera)
		// usando números negativos cuenta desde el final (la -1 es la última)
		// relative(n) salta n filas hacía adelante desde la posición actual. Con números negativos salta hacía atras.
	}
	
	public static void tercerEjemplo(Connection conexion) throws SQLException{
		// vamos a modificar una línea
		Statement sql = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet resultado = sql.executeQuery("SELECT * FROM actor");
		// nos posicionamos en la última línea
		// resultado.last()
		// o en la tercera empezando por detrás
		resultado.absolute(-3);
		// o cinco por detrás de la anterior
		resultado.relative(-5);
		// modificamos dos campos
		resultado.updateString("first_name", "Inés");
		resultado.updateString("last_name", "Perado");
		// si no ejecutamos updateRow() los cambios se pierden
		resultado.updateRow();
		// nos colocamos antes de la primera línea
		resultado.beforeFirst();
		// y listamos
		while (resultado.next()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
	}
	
	public static void cuartoEjemplo(Connection conexion) throws SQLException{
		// PreparedStatement me permite parametrizar una query
		// Colocamos las incógnitas con ?
		PreparedStatement sql = conexion.prepareStatement("SELECT * FROM actor WHERE first_name = ?");
		// Y aplicamos valores así
		sql.setString(1, "MARY");
		ResultSet resultado = sql.executeQuery();
		while (resultado.next()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
		
		// Podemos poner tantas como queramos
		sql = conexion.prepareStatement("SELECT * FROM actor WHERE first_name = ? AND last_name = ?");
		sql.setString(1, "MARY");
		sql.setString(2, "KEITEL");
		resultado = sql.executeQuery();
		while (resultado.next()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
			}
		
		// Si queremos modificar el comportamiento del PreparedStatement ponemos los modificadores después del query
		sql = conexion.prepareStatement("SELECT * FROM actor WHERE first_name = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		sql.setString(1, "MARY");
		resultado = sql.executeQuery();
		resultado.afterLast();
		while (resultado.previous()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
		
	}
}
