package primerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Para incluir la librería DbUtils nos la descargamos de aquí:
// https://commons.apache.org/dbutils/download_dbutils.cgi
// Descomprimimos el archivo commons-dbutils-1.8.1.jar
// lo añadimos a nuestro build path (properties->java build path->libraries->classpath->add external jars

import org.apache.commons.dbutils.DbUtils;

public class Databases4 {

	public static void main(String[] args) {
		String jdbcURL = "jdbc:mysql://localhost:3306/dam2";
		String usuario = "profe";
		String password = "abc123";
		
		// tenemos que crear los recursos antes de entrar en el bloque try 
		ResultSet res=null;
		Statement sql=null;
		Connection conn=null;

		try {
			conn = DriverManager.getConnection(jdbcURL, usuario, password);
			System.out.println("Conexión realizada correctamente");

			sql = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = "SELECT * FROM alumnos";

			res = sql.executeQuery(query);
			res.first();

			res.updateString("nombre", "Pepe");
			res.updateRow();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// esto cerrará y liberará los recursos correctamente
			DbUtils.closeQuietly(res);
			DbUtils.closeQuietly(sql);
			DbUtils.closeQuietly(conn);
		}

	}

}
