/*
 * Controlador Login
 * Servlet que gestiona la autenticación de profesores
 * GET  → muestra el formulario de login
 * POST → procesa las credenciales y redirige según resultado
 */
package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.ModeloLogin;
import modelo.entidades.Profesor;

/**
 *
 * @author SGame
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private static final String VISTA = "/login.jsp";

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * GET: muestra el formulario de login
     * También redirige al inicio si ya hay sesión activa
     * (para que un usuario logueado no vea el login)
     * 
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

        // Si ya hay sesión, redirige al inicio directamente
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("profesor") != null) {
            response.sendRedirect(request.getContextPath() + "/Inicio");
            return;
        }

        request.getRequestDispatcher(VISTA).forward(request, response);
        // getServletContext().getRequestDispatcher(VISTA).forward(request, response);
    }

    /**
     * POST: procesa el formulario de login
     * Recoge email y password, verifica credenciales,
     * y redirige al inicio o vuelve al login con error
     * 
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Recogemos los datos del formulario
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validación básica: campos no vacíos
        if (email == null || email.isBlank() || 
            password == null || password.isBlank()) {
            request.setAttribute("error", "Por favor, introduce email y contraseña");
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        // Verificamos las credenciales con el modelo
        ModeloLogin modeloLogin = new ModeloLogin();
        Profesor profesor = modeloLogin.verificarLogin(email, password);

        if (profesor != null) {
            // Login correcto: creamos sesión y guardamos el profesor
            HttpSession session = request.getSession(true);
            session.setAttribute("profesor", profesor);

            // Redirigimos al inicio (redirect evita reenvío del formulario)
            response.sendRedirect(request.getContextPath() + "/Inicio");
        } else {
            // Login incorrecto: volvemos al login con mensaje de error
            request.setAttribute("error", "Email o contraseña incorrectos");
            request.getRequestDispatcher(VISTA).forward(request, response);
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
