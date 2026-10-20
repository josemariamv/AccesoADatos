package primerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Databases3 {
	 public static void main(String[] args) {      
	        String database = "jdbc:mysql://localhost:3306/sakila";        
			String usr = "josemaria";
			String pswd = "abc123";
			// Ejemplo de como ver el resultado de un query en una tabla con swing
	       verJTable(database,usr,pswd);
	    }
	       
	     public static void verJTable(String database, String user, String password){
	        String query01 = "SELECT actor_id, first_name, last_name FROM actor;";

        	// la parte sql es igual que si fuese por consola
	        try (Connection conexion = DriverManager.getConnection(database,user,password);) {

	            Statement consulta = conexion.createStatement();
	            ResultSet ejecucion = consulta.executeQuery(query01);
	            
	           //Creamos una ventana
	            JFrame ventana = new JFrame("Contenido de la tabla actor");
	            
	            // Creamos una tabla con JTable
	            DefaultTableModel modelo = new DefaultTableModel();
	            JTable tabla = new JTable(modelo);
	            
	            // Le metemos tres columnas a nuestra tabla, las que queremos mostrar
	            modelo.addColumn("id");
	            modelo.addColumn("nombre");
	            modelo.addColumn("apellido");
	            
	            // Metemos la tabla en la ventana y le añadimos una barra de scroll
	            JScrollPane scroll = new JScrollPane(tabla);
	            ventana.getContentPane().add(scroll);
	                        
	            // Metemos el resultado del select en la tabla
	            // recorremos el ResultSet como hacemos normalmente
	            while(ejecucion.next()){
	            	// creamos una fila
	                Object [] fila = new Object[3];
	                // llenamos esa fila con datos
	                fila[0] = ejecucion.getInt("actor_id");
	                fila[1] = ejecucion.getString("first_name");
	                fila[2] = ejecucion.getString("last_name");
	                // añadimos la fila a la tabla
	                modelo.addRow(fila); 
	            }
	            
	            // Últimos retoques de configuración de la ventana y, por último, la hacemos visible
	            ventana.pack();
	            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            ventana.setVisible(true);
	       } catch (SQLException ex) {
	        	// Podemos obtener información detallada acerca de las posibles excepciones
	            System.out.println("Ha ocurrido una excepción");
	            System.out.println("Código: " + ex.getErrorCode());
	            System.out.println("Mensaje: " + ex.getMessage());
	            System.out.println("Estado: " + ex.getSQLState());
	        }
	    }
}
