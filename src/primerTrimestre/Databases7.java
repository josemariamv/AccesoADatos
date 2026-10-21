package primerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Databases7 {

	public static void main(String[] args) {
		// Ahora creamos nuestra conexión a través de la clase PoolConexiones que hemos creado
		// Como ves, no necesitamos crear ningún objeto de este tipo para llamar al metod getConnection()
		// y podemos usar try-with-resources
		try(Connection con = PoolConexiones.getConnection()){
			System.out.println("Conexión realizada correctamente");
			
			// Usaremos prepareStatement para mejorar la eficiencia
			String query = "SELECT * FROM alumnos";
			PreparedStatement sql = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			// El resto es igual a lo que hemos visto hasta ahora
			ResultSet res = sql.executeQuery(query);
			res.first();

			res.updateString("nombre", "Aurora");
			res.updateRow();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
