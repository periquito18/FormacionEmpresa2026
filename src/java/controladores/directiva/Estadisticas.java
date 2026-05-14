/*
 * Servlet que genera los datos para la página de estadísticas.
 * Solo accesible para profesores de la Directiva (protegido por FiltroDirectiva).
 * Carga los datos de prácticas agrupados por empresa y por curso.
 */
package controladores.directiva;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import modelo.entidades.dao.DaoPractica;

/**
 *
 * @author SGame
 */
@WebServlet(name = "Estadisticas", urlPatterns = {"/directiva/Estadisticas"})
public class Estadisticas extends HttpServlet {
    
    // Creamos la instancia del DAO
    private final DaoPractica daoPractica = new DaoPractica();

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

        // Llamamos a los métodos desde la instancia
        // Datos agrupados por empresa: [[nombreEmpresa, count], ...]
        List<Object[]> alumnosPorEmpresa = daoPractica.countAlumnosPorEmpresa();

        // Datos agrupados por curso: [[nombreCurso, count], ...]
        List<Object[]> alumnosPorCurso = daoPractica.countAlumnosPorCurso();

        // Construimos los arrays JSON para Chart.js directamente en el Servlet
        // para no hacer lógica compleja en el JSP
        StringBuilder labelsEmpresa = new StringBuilder();
        StringBuilder valuesEmpresa = new StringBuilder();
        StringBuilder labelsCurso = new StringBuilder();
        StringBuilder valuesCurso = new StringBuilder();

        // Procesamos datos por empresa
        for (int i = 0; i < alumnosPorEmpresa.size(); i++) {
            Object[] fila = alumnosPorEmpresa.get(i);
            labelsEmpresa.append("\"").append(fila[0]).append("\"");
            valuesEmpresa.append(fila[1]);
            if (i < alumnosPorEmpresa.size() - 1) {
                labelsEmpresa.append(",");
                valuesEmpresa.append(",");
            }
        }

        // Procesamos datos por curso
        for (int i = 0; i < alumnosPorCurso.size(); i++) {
            Object[] fila = alumnosPorCurso.get(i);
            labelsCurso.append("\"").append(fila[0]).append("\"");
            valuesCurso.append(fila[1]);
            if (i < alumnosPorCurso.size() - 1) {
                labelsCurso.append(",");
                valuesCurso.append(",");
            }
        }

        // Enviamos los datos a la vista
        request.setAttribute("alumnosPorEmpresa", alumnosPorEmpresa);
        request.setAttribute("alumnosPorCurso", alumnosPorCurso);
        request.setAttribute("labelsEmpresa", labelsEmpresa.toString());
        request.setAttribute("valuesEmpresa", valuesEmpresa.toString());
        request.setAttribute("labelsCurso", labelsCurso.toString());
        request.setAttribute("valuesCurso", valuesCurso.toString());

        request.getRequestDispatcher("/directiva/estadisticas.jsp")
                .forward(request, response);
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