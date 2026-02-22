/*
 * Controlador GestionCursos
 * Servlet que gestiona el CRUD completo de Cursos
 * Solo accesible para profesores de la Directiva (protegido por FiltroDirectiva)
 * Recibe el parámetro "accion" para decidir qué operación realizar
 */
package controladores.directiva;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.entidades.Curso;
import modelo.entidades.dao.DaoCurso;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;

/**
 *
 * @author SGame
 */
@WebServlet(name = "GestionCursos", urlPatterns = {"/directiva/GestionCursos"})
public class GestionCursos extends HttpServlet {

    // Rutas de las vistas
    private static final String VISTA_LISTA = "/directiva/verCursos.jsp";
    private static final String VISTA_EDITAR = "/directiva/editarCurso.jsp";
    private static final String URL_LISTA = "/directiva/GestionCursos?accion=listar";

    private final DaoCurso daoCurso = new DaoCurso();

    /**
     * GET: gestiona las acciones de listar, nuevo y editar Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Recogemos la acción solicitada (por defecto "listar")
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        /**
         * switch (accion) { 
         *  case "listar" -> listar(request, response); 
         *  case "nuevo" -> nuevo(request, response); 
         *  case "editar" -> editar(request, response); 
         *  case "eliminar" -> eliminar(request, response); 
         *  default -> listar(request, response); 
         * }
         */
        
        switch (accion) {
            case "listar":
                listar(request, response);
                break;
            case "nuevo":
                nuevo(request, response);
                break;
            case "editar":
                editar(request, response);
                break;
            case "eliminar":
                eliminar(request, response);
                break;
            default:
                listar(request, response);
        }
    }

    /**
     * POST: procesa el formulario de crear/editar Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         guardar(request, response);
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

    // ---- Acciones privadas ----
    
    /**
     * Carga todos los cursos y los envía a la vista de lista.
     */
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cursos", daoCurso.findAll());
        request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
    }

    /**
     * Muestra el formulario vacío para crear un curso nuevo.
     */
    private void nuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Enviamos un curso vacío para que el JSP lo use sin null checks
        request.setAttribute("curso", new Curso());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    /**
     * Carga un curso existente y muestra el formulario con sus datos.
     */
    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Curso curso = daoCurso.findById(id);
            if (curso == null) {
                request.setAttribute("error", "Curso no encontrado.");
                listar(request, response);
                return;
            }
            request.setAttribute("curso", curso);
            request.setAttribute("accion", "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + URL_LISTA);
        }
    }

    /**
     * Elimina un curso por su ID. Si tiene alumnos asociados, muestra un error.
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            daoCurso.destroy(id);
            request.setAttribute("exito", "Curso eliminado correctamente.");
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "El curso no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            // Capturamos error de integridad referencial (tiene alumnos)
            request.setAttribute("error",
                    "No se puede eliminar el curso porque tiene alumnos asociados.");
        }
        listar(request, response);
    }

    /**
     * Procesa el formulario: crea o edita según si el ID viene informado.
     */
    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre").trim();
        String idParam = request.getParameter("id");

        // Validación básica
        if (nombre.isEmpty()) {
            request.setAttribute("error", "El nombre del curso es obligatorio.");
            request.setAttribute("curso", new Curso());
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        try {
            if (idParam == null || idParam.isEmpty()) {
                // Crear nuevo curso
                Curso curso = new Curso(nombre);
                daoCurso.create(curso);
                request.setAttribute("exito", "Curso creado correctamente.");
            } else {
                // Editar curso existente
                int id = Integer.parseInt(idParam);
                Curso curso = daoCurso.findById(id);
                curso.setNombre(nombre);
                daoCurso.edit(curso);
                request.setAttribute("exito", "Curso actualizado correctamente.");
            }
        } catch (PreexistingEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "Ya existe un curso con ese nombre.");
            request.setAttribute("curso", new Curso());
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "El curso no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
        }
        listar(request, response);
    }
}
