/*
 * Clase de configuración de Jersey.
 * @ApplicationPath define la ruta base de todos los endpoints REST.
 * Todos los endpoints estarán bajo /api/...
 */
package rest;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author SGame
 */
@ApplicationPath("/api")
public class RestConfig extends ResourceConfig {
    
    public RestConfig() {
        // Indicamos el paquete donde Jersey buscará las clases REST (@Path)
        packages("rest");
    }
}