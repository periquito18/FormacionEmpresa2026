/*
 * Controlador Inicio
 * Servlet que muestra el dashboard principal tras el login
 * Carga datos de resumen para mostrar en el inicio
 */
package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.entidades.dao.DaoAlumno;
import modelo.entidades.dao.DaoEmpresa;
import modelo.entidades.dao.DaoPractica;

/**
 *
 * @author SGame
 */
@WebServlet(name = "Inicio", urlPatterns = {"/Inicio"})
public class Inicio extends HttpServlet {

    private static final String VISTA = "/inicio.jsp";
    
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
        
        // Cargamos algunos datos de resumen para el dashboard
        DaoAlumno daoAlumno = new DaoAlumno();
        DaoEmpresa daoEmpresa = new DaoEmpresa();
        DaoPractica daoPractica = new DaoPractica();
        
        // Enviamos los contadores a la vista como atributos del request
        request.setAttribute("totalAlumnos", daoAlumno.count());
        request.setAttribute("totalEmpresas", daoEmpresa.count());
        request.setAttribute("totalPracticas", daoPractica.count());
        
        request.getRequestDispatcher(VISTA).forward(request, response);
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
