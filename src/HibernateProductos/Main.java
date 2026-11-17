package HibernateProductos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
	
	// Atributo estático para deshabilitar el logger de hibernate
    private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
    	// Le decimos a hibernate que solo muestre los errores y no mensajes informativos
    	LOG_HIBERNATE.setLevel(Level.SEVERE);
    	
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("clasePU");
        EntityManager em = emf.createEntityManager();
        
        try {
        	// Consulta JPQL Ojo: se usa el nombre de la CLASE (Producto) y no el de la tabla (productos)
        	String jpql = "SELECT p.nombre, p.precio FROM Producto p";
        	List<Object[]> listaResultados = em.createQuery(jpql, Object[].class).getResultList();
            System.out.println("--- LISTADO DE VARIOS ELEMENTOS DE LOS PRODUCTOS ---");
            for (Object[] fila : listaResultados) {
            	String nombre = (String) fila[0];   // Posición 0: p.nombre
                Double precio = (Double) fila[1];   // Posición 1: p.precio
                
                System.out.println("Producto: " + nombre + " | Precio: " + precio + "€");
            }
            System.out.println("Total de resultados: " + listaResultados.size());
        }catch(Exception e) {
        	System.out.println("Error: " + e.getMessage());
        }    
        em.close();
        emf.close();
    }
}