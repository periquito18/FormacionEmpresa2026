/*
 * Clase FiltroSesion
 * Filtro de sesión que protege todas las páginas que requieren autenticación
 * Si no hay sesión activa, redirige al login automáticamente
 * Las rutas protegidas se definen en urlPatterns de la anotación @WebFilter
 */
package filtros;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author SGame
 */
@WebFilter(filterName = "FiltroSesion",
    urlPatterns = {"/Inicio", "/directiva/*", "/comun/*", "/CerrarSesion"},
    dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD, 
        DispatcherType.ERROR, DispatcherType.INCLUDE})
public class FiltroSesion implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // getSession(false): devuelve null si no existe sesión, sin crear una nueva
        HttpSession session = httpRequest.getSession(false);

        // Comprobamos si hay sesión activa con profesor autenticado
        if (session != null && session.getAttribute("profesor") != null) {
            // Sesión válida: dejamos pasar la petición
            chain.doFilter(request, response);
        } else {
            // Sin sesión: redirigimos al login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login");
        }
    }

    @Override
    public void destroy() {}
}
