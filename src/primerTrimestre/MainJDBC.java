package primerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

// o import java.sql.*;

public class MainJDBC {
	
	public static void main(String[] args) {
		
	      String jdbcURL = "jdbc:mysql://localhost:3306/dam2";
	      String usuario = "profe";
	      String password = "abc123";
	      
	      /*
	      En ese ejemplo actualizamos uno de los registros devueltos directamente en el ResultSet
	      y lo actualizamos en la base de datos con updateString y updateRow
	      Esto es posible gracias a que el Statement lo hemos creado con el modificador CONCUR_UPDATABLE
	      */
	      
	      try {
	    	 // Conexión a la base de datos. Si no puede conectarse saltará una excepción
	         Connection conn = DriverManager.getConnection(jdbcURL, usuario, password);
	         System.out.println("Conexión realizada correctamente");
	         
	         Statement sql = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	         String query = "SELECT * FROM alumnos";
	          
	         ResultSet res = sql.executeQuery(query);        	         
	         res.first();
		
	         res.updateString("nombre", "Pepe");
	         res.updateRow();
	         
	         conn.close();
	         
	      }
	      catch(SQLException e) {
	         e.printStackTrace();
	      }
	   }
}