/*
 * Servlet que gestiona el cambio de idioma de la aplicación.
 * Guarda el idioma seleccionado en la sesión y redirige
 * a la página desde la que se solicitó el cambio.
 */
package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *
 * @author SGame
 */
@WebServlet(name = "CambiarIdioma", urlPatterns = {"/cambiarIdioma"})
public class CambiarIdioma extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recogemos el idioma solicitado (es o en)
        String idioma = request.getParameter("idioma");

        // Validamos que sea un idioma soportado
        if ("es".equals(idioma) || "en".equals(idioma)) {
            // Guardamos el locale en la sesión
            Locale locale = new Locale(idioma);
            request.getSession().setAttribute("locale", locale);
        }

        // Redirigimos a la página anterior (o a inicio si no hay referencia)
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/inicio");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}