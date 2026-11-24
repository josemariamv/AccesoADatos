package HibernateClassicmodels;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;

public class Main {
	private static final Logger LOG_HIBERNATE = Logger.getLogger("org.hibernate");

	public static void main(String[] args) {
		LOG_HIBERNATE.setLevel(Level.SEVERE);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("clasePU");
		EntityManager em = emf.createEntityManager();

		try {
			// búsqueda única por clave primaria simple
			encuentraLineadeProducto(em, "Motorcycles");
			
			// búsqueda única por clave primaria compuesta
			//encuentraOrderDetail(em,10425,"S32_2509");
			
			// búsquedas múltiples por clave no primaria
			//listaProductosdeVendedor(em, "Autoart Studio Design");
			
			// búsquedas únicas por clave o claves no primarias
			// listaEmpleadosOficina(em, "France","75017");
			//listaEmpleadosOficina2(em, "France","75017");
			//listaEmpleadosOficina3(em, "France", "75017");

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		em.close();
		emf.close();
	}
	
	public static void encuentraLineadeProducto(EntityManager em, String linea) throws Exception {
		Productlines elemento = em.find(Productlines.class, linea);
		if (elemento == null)
		    System.out.println("No existe esa Línea de Productos");
		else {
			System.out.println("Descripción de la línea de productos: " + linea);
			System.out.println(elemento.getTextDescription());
		}
	}
	
	public static void encuentraOrderDetail(EntityManager em, int orden, String producto) throws Exception{
		OrderdetailsId id = new OrderdetailsId(orden, producto);
		Orderdetails elemento = em.find(Orderdetails.class, id);
		if (elemento == null)
		    System.out.println("No existe ese elemento");
		else {
			System.out.println("Detalle correspondiente al producto " + producto + " de la orden " + orden);
			System.out.println("Producto: " + elemento.getProducts().getProductName());
		    System.out.println("Cantidad: " + elemento.getQuantityOrdered());
		    System.out.println("Precio unitario: " + elemento.getPriceEach());
		}
	}
	
	public static void listaProductosdeVendedor(EntityManager em, String vendedor) throws Exception {
		String jpql = "FROM Products WHERE productVendor = :vendedor";
		List<Products> listaProductos = em.createQuery(jpql, Products.class)
				.setParameter("vendedor", vendedor)
				.getResultList();
		
		if (listaProductos.isEmpty())
			System.out.println("No existe ningún producto de ese vendedor");
		else
			System.out.println("Productos vendidos por: " + vendedor);
			for (Products producto: listaProductos)
				System.out.println(producto.getProductName());
				
	}

	public static void listaEmpleadosOficina(EntityManager em, String pais, String codPostal) throws Exception {
		String jpql = "FROM Offices WHERE country = :pais AND postalCode = :codpostal";
		List<Offices> listaOficinas = em.createQuery(jpql, Offices.class).setParameter("pais", pais)
				.setParameter("codpostal", codPostal).getResultList();
		if (listaOficinas.isEmpty())
			System.out.println("No existe ninguna oficina con esos datos");
		else
			// Puede que haya mas de una oficina en el mismo codigo postal y el mismo país
			for (int i = 0; i < listaOficinas.size(); i++) {
				Offices oficina = (Offices) listaOficinas.get(i);
				System.out.println(oficina.getCity());
				Set<Employees> empleados = oficina.getEmployeeses();
				if (empleados.isEmpty())
					System.out.println("   - En esta oficina no hay empleados");
				else {
					System.out.println("   Empleados:");
					for (Employees emp : empleados)
						System.out.println("   - " + emp.getFirstName() + " " + emp.getLastName());
				}
			}
	}

	public static void listaEmpleadosOficina2(EntityManager em, String pais, String codPostal) throws Exception {
		String jpql = "FROM Offices WHERE country = :pais AND postalCode = :codpostal";
		List<Offices> listaOficinas = em.createQuery(jpql, Offices.class).setParameter("pais", pais)
				.setParameter("codpostal", codPostal).getResultList();
		// Una forma mas elegante que la original si no trabajamos con la versión 3.2
		// Es usar el operador ternario:
		Offices oficina = listaOficinas.isEmpty() ? null : listaOficinas.get(0);
		if (oficina == null)
			System.out.println("No existe ninguna oficina con esos datos");
		else {
			System.out.println(oficina.getCity());
			Set<Employees> empleados = oficina.getEmployeeses();
			if (empleados.isEmpty())
				System.out.println("   - En esta oficina no hay empleados");
			else {
				System.out.println("   Empleados:");
				for (Employees emp : empleados)
					System.out.println("   - " + emp.getFirstName() + " " + emp.getLastName());
			}
		}
	}

	public static void listaEmpleadosOficina3(EntityManager em, String pais, String codPostal) throws Exception {
		String jpql = "FROM Offices WHERE country = :pais AND postalCode = :codpostal";
		// Cuando se que mi query me devuelve un solo valor o ninguno pero no es clave
		// primaria
		// (y no puedo usar find) puedo usar el método getSingleResult()
		Offices oficina = em.createQuery(jpql, Offices.class).setParameter("pais", pais)
				.setParameter("codpostal", codPostal).getSingleResult();
		// Si no existe ningún resultado provoca una excepción NoResultException
		// a partir de la versión 3.2 / hibernate 7 existe otro método aún mejor que
		// se llama getResultListOrNull() que no provoca una excepción cuando no hay
		// resultados sino que devuelve un null.
		System.out.println(oficina.getCity());
		Set<Employees> empleados = oficina.getEmployeeses();
		if (empleados.isEmpty())
			System.out.println("   - En esta oficina no hay empleados");
		else {
			System.out.println("   Empleados:");
			for (Employees emp : empleados)
				System.out.println("   - " + emp.getFirstName() + " " + emp.getLastName());
		}
	}
		

	}