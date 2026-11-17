package HibernateNaciones;

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
			// Sin usar JPQL
			//listadoNombresPaises(em);
			//verIdiomas(em, "Spain");
			paisesDeUnContinente(em, "Europe");

			// Usando JPQL
			paisesDeUnContinente2(em, "Europe");
			//idiomasOficiales(em, "Canada");
			//estadisticasDeUnPais(em, "Spain", 2018);
			//paisesConMasPib(em, 2018);
			//paisesPorRegion(em);
			//paisesQueEmpiezanPor(em, "Sp");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		em.close();
		emf.close();
	}
	
	public static void listadoNombresPaises(EntityManager em){
		String jpql = "SELECT pais.name FROM Countries pais";
		List<String> listaNombres = em.createQuery(jpql, String.class).getResultList();
		System.out.println("--- Nombres de todos los paises ---");
		for (String nombre : listaNombres) {
			System.out.println(nombre);
			}
		System.out.println("Total de paises: " + listaNombres.size());
	}
	
	public static void verIdiomas(EntityManager em, String pais) {
		// El nombre del pais no es clave primaria. Debería de ser único, pero no está definido así en la base de datos... 
		String jpql = "FROM Countries WHERE name = :pais";
		List<Countries> listaPaises = em.createQuery(jpql, Countries.class)
				.setParameter("pais", pais)
				.getResultList();
		/* Si la lista está vacía es que ese país no existe */
		if (listaPaises.isEmpty())
			System.out.println("No existe ese país");
		else if (listaPaises.size()!= 1)
			System.out.println("Hay mas de un país con el mismo nombre... Y eso no debería de ser posible!");
		else {
			/* En caso contrario el país existe y es único, así que cargamos el primer elemento de la lista */
			Countries miPais = (Countries)listaPaises.get(0);
			System.out.println("El código de " + pais + " es " + miPais.getCountryId());
			// cargamos la lista de idiomas de ese pais
			Set<CountryLanguages> listaIdiomas = miPais.getCountryLanguageses();
			if(listaIdiomas.size()!=0) {
				// Y mostramos los nombres de los idiomas
				System.out.println("--- Idiomas de " + pais + " ---");
				for(CountryLanguages fila: listaIdiomas)
					System.out.println(fila.getLanguages().getLanguage());
			}
			else
				System.out.println("No hay idiomas para " + pais);
		}
	}
	
	private static void paisesDeUnContinente(EntityManager em, String continente) {
		String jpql = "FROM Continents 	WHERE name = :continente";
		List<Continents> c = em.createQuery(jpql, Continents.class)
				.setParameter("continente", continente)
				.getResultList();	
		if (c.isEmpty())
			System.out.println("No existe ese continente");
		else if (c.size()!= 1)
			System.out.println("Hay mas de un continente con el mismo nombre... Y eso no debería de ser posible!");
		else {
			System.out.println("--- Paises de " + continente + " ---");
			Set<Regions> regiones = c.get(0).getRegionses();
			for (Regions r: regiones) {
				Set<Countries> paises = r.getCountrieses();
				for(Countries p: paises) {
					System.out.println(p.getName());
				}
			}
		}
	}
	
	// ejemplos usando JPQL
	private static void paisesDeUnContinente2(EntityManager em, String continente) {
		String jpql = "SELECT c FROM Countries c "
					+ "JOIN c.regions r "
					+ "JOIN r.continents co "
					+ "WHERE co.name = :continente "
					+ "ORDER BY c.name";
			List<Countries> paises = em.createQuery(jpql, Countries.class)
				.setParameter("continente", continente)
				.getResultList();
 
		System.out.println("--- Paises de " + continente + " ---");
		for (Countries pais : paises) {
			System.out.println(pais.getName());
		}
	}
	
	private static void idiomasOficiales(EntityManager em, String nombrePais) {
		String jpql = "SELECT l.language FROM CountryLanguages cl "
					+ "JOIN cl.countries c "
					+ "JOIN cl.languages l "
					+ "WHERE c.name = :pais AND cl.official = true "
					+ "ORDER BY l.language";
		List<String> idiomas = em.createQuery(jpql, String.class)
				.setParameter("pais", nombrePais)
				.getResultList();
 
		System.out.println("--- Idiomas oficiales de " + nombrePais + " ---");
		for (String idioma : idiomas) {
			System.out.println(idioma);
		}
	}
		
		private static void estadisticasDeUnPais(EntityManager em, String nombrePais, int anio) {
			String jpql = "SELECT cs FROM CountryStats cs "
					+ "JOIN cs.countries c "
					+ "WHERE c.name = :pais AND cs.id.year = :anio";
			List<CountryStats> lista = em.createQuery(jpql, CountryStats.class)
					.setParameter("pais", nombrePais)
					.setParameter("anio", anio)
					.getResultList();
	 
			System.out.println("--- Estadisticas de " + nombrePais + " en " + anio + " ---");
			if (lista.isEmpty()) {
				System.out.println("No hay datos");
			} else {
				CountryStats stats = lista.get(0);
				System.out.println("Poblacion: " + stats.getPopulation());
				System.out.println("PIB: " + stats.getGdp());
			}
		}
		
		private static void paisesConMasPib(EntityManager em, int anio) {
			String jpql = "SELECT c.name AS pais, cs.gdp AS pib "
					+ "FROM CountryStats cs "
					+ "JOIN cs.countries c "
					+ "WHERE cs.id.year = :anio AND cs.gdp IS NOT NULL "
					+ "ORDER BY cs.gdp DESC";
			List<Object[]> filas = em.createQuery(jpql, Object[].class)
					.setParameter("anio", anio)
					.setMaxResults(5)
					.getResultList();
	 
			System.out.println("--- Top 5 PIB en " + anio + " ---");
			for (Object[] fila : filas) {
				System.out.println((String)fila[0] + ": " + (Long)fila[1]);
			}
		}
		
		private static void paisesPorRegion(EntityManager em) {
			String jpql = "SELECT r.name AS region, COUNT(c) AS total "
					+ "FROM Countries c "
					+ "JOIN c.regions r "
					+ "GROUP BY r.name "
					+ "ORDER BY COUNT(c) DESC";
			List<Object[]> filas = em.createQuery(jpql, Object[].class).getResultList();
	 
			System.out.println("--- Paises por region ---");
			for (Object[] fila : filas) {
				System.out.println((String)fila[0] + ": " + (Long)fila[1]);
			}
		}
		
		private static void paisesQueEmpiezanPor(EntityManager em, String inicioNombre) {
			String jpql = "SELECT c FROM Countries c "
					+ "JOIN FETCH c.regions r "
					+ "JOIN FETCH r.continents "
					+ "WHERE c.name LIKE :patron "
					+ "ORDER BY c.name";
			List<Countries> paises = em.createQuery(jpql, Countries.class)
					.setParameter("patron", inicioNombre + "%")
					.getResultList();
	 
			System.out.println("--- Paises que empiezan por " + inicioNombre + " ---");
			for (Countries pais : paises) {
				System.out.println(pais.getName() + " - " + pais.getRegions().getName() + " - "
						+ pais.getRegions().getContinents().getName());
			}
		}
}