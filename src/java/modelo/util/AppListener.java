/*
 * Clase AppListener
 * Listener del ciclo de vida de la aplicación web.
 * Tomcat lo ejecuta automáticamente al arrancar y al apagar la aplicación
 * gracias a la etiqueta @WebListener.
 * Su responsabilidad es cerrar el EntityManagerFactory al apagar,
 * liberando todas las conexiones de la base de datos correctamente.
 */
package modelo.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 *
 * @author SGame
 */
@WebListener
public class AppListener implements ServletContextListener{
    
    /**
     * Se ejecuta cuando Tomcat arranca la aplicación.
     * Podríamos inicializar recursos aquí, pero JPAUtil ya lo hace solo.
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Aplicación FormacionEmpresa2026 iniciada.");
    }

    /**
     * Se ejecuta cuando Tomcat apaga la aplicación.
     * Cerramos el EMF para liberar el pool de conexiones correctamente.
     * Sin esto, las conexiones quedarían abiertas en MariaDB.
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
        System.out.println("Aplicación FormacionEmpresa2026 apagada. Conexiones cerradas.");
    }
}
