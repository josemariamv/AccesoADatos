package primerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Databases2 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/sakila";
		String usr = "josemaria";
		String pswd = "abc123";

		try (Connection conexion = DriverManager.getConnection(url, usr, pswd)) {
			System.out.println("Conexión realizada con exito\n");
			
			//insertarRegistro(conexion);
			//creacion(conexion);
			borrarRegistros(conexion);
			//verInfo(conexion);
			
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	public static void insertarRegistro(Connection conexion) throws SQLException{
		// vamos a hacer un insert en la tabla actor de sakila
		// insert into actor values(NULL,'José María', 'Morales', '2026-04-16 12:00:00');
		PreparedStatement sql = conexion.prepareStatement("INSERT INTO actor VALUES (NULL, ?, ?, ?)");
		sql.setString(1, "José María");
		sql.setString(2, "Morales");
		LocalDateTime fechayHora = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
		String last_update = fechayHora.format(formato);
		sql.setString(3, last_update);
		// cuando la sentencia sql ejecutada no devuelve un resultado (ResultSet) usamos executeUpdate
		// en lugar de executeQuery
		// cuando la sentencia es un insert, delete o update devuelve un entero con el número de filas
		// afectadas por la instrucción
		// cuando la sentencia es un create, alter, drop o similar devuelve siempre 0
		sql.executeUpdate();
	}
	
	public static void creacion(Connection conexion) throws SQLException{
		// vamos ahora a crear, si no existe, una base de datos y una tabla
		 String query1 = "CREATE DATABASE IF NOT EXISTS agenda;";
	     String query2 = "USE agenda";
	     String query3 = "CREATE TABLE IF NOT EXISTS personas(telefono INT(9) PRIMARY KEY, nombre VARCHAR(50));";
	     
	     Statement consulta = conexion.createStatement();
         consulta.executeUpdate(query1);
         consulta.executeUpdate(query2);
         consulta.executeUpdate(query3);
	}
	
	public static void borrarRegistros(Connection conexion) throws SQLException{
	     Statement consulta = conexion.createStatement();
         consulta.executeUpdate("DELETE FROM actor WHERE last_name='Morales'");
	}
	
	public static void verInfo(Connection conexion) throws SQLException{
		// podemos tambien ver las bases de datos, las tablas y cualquier otro listado de información
		String query01 = "SHOW TABLES;";
		Statement consulta = conexion.createStatement();
        ResultSet ejecucion = consulta.executeQuery(query01);
        while(ejecucion.next()){
            System.out.printf("%d - %s%n",ejecucion.getRow(), ejecucion.getString(1));
        }
	}

}
