/*
 * Clase FiltroSesion
 * Filtro que protege la zona exclusiva de la Directiva
 * Permite el acceso solo si el profesor autenticado es de tipo DIRECTIVA
 * Si no lo es, redirige al inicio con mensaje de acceso denegado
 * Este filtro actúa DESPUÉS de FiltroSesion, por lo que si llega aquí
 * ya sabemos que hay sesión activa con profesor autenticado
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
import modelo.entidades.Profesor;
import modelo.entidades.TipoProfesor;

/**
 *
 * @author SGame
 */
@WebFilter(filterName = "FiltroDirectiva",
    urlPatterns = {"/Directiva/*"},
    dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD,
                       DispatcherType.ERROR, DispatcherType.INCLUDE})
public class FiltroDirectiva implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Recuperamos el profesor de la sesión (sabemos que existe por FiltroSesion)
        HttpSession session = httpRequest.getSession(false);
        Profesor profesor = (Profesor) session.getAttribute("profesor");

        // Comprobamos si es de tipo DIRECTIVA
        if (profesor != null && profesor.getTipo() == TipoProfesor.DIRECTIVA) {
            // Es directiva: dejamos pasar
            chain.doFilter(request, response);
        } else {
            // No es directiva: avisamos y redirigimos al inicio
            session.setAttribute("errorAcceso",
                "No tienes permisos para acceder a esa sección");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Inicio");
        }
    }

    @Override
    public void destroy() {}
}
