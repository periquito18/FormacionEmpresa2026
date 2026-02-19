/*
 * Clase JPAUtil
 * Clase utilitaria que gestiona el EntityManagerFactory de forma centralizada.
 * El EMF se crea UNA SOLA VEZ para toda la aplicación y se reutiliza.
 * Los EntityManager sí se crean y cierran en cada operación.
 */
package modelo.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author SGame
 */
public class JPAUtil {

    // Nombre de la unidad de persistencia definida en persistence.xml
    private static final String PERSISTENCE_UNIT = "FormacionEmpresa2026PU";

    /**
     * El EMF es estático: existe uno solo para toda la aplicación.
     * Se crea en el momento en que la clase se carga en memoria (lazy init).
     * Abre el pool de conexiones y lee la configuración de persistence.xml.
     */
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

    /**
     * Devuelve un EntityManager nuevo.
     * Cada operación con la BD debe crear el suyo, usarlo y cerrarlo.
     * El EM es barato de crear porque reutiliza el pool de conexiones del EMF.
     *
     * @return Un EntityManager listo para usar
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Cierra el EntityManagerFactory.
     * Solo debe llamarse cuando la aplicación se apaga (en el listener de cierre).
     * Cerrar el EMF libera todas las conexiones del pool.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}