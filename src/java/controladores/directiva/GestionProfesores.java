/*
 * Controlador GestionProfesores
 * Servlet que gestiona el CRUD completo de Profesores
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
import modelo.ModeloLogin;
import modelo.entidades.Profesor;
import modelo.entidades.TipoProfesor;
import modelo.entidades.dao.DaoProfesor;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;

/**
 *
 * @author SGame
 */
@WebServlet(name = "GestionProfesores", urlPatterns = {"/directiva/GestionProfesores"})
public class GestionProfesores extends HttpServlet {

    // Rutas de las vistas
    private static final String VISTA_LISTA = "/directiva/verProfesores.jsp";
    private static final String VISTA_EDITAR = "/directiva/editarProfesor.jsp";
    private static final String URL_LISTA = "/directiva/GestionProfesores?accion=listar";

    private final DaoProfesor daoProfesor = new DaoProfesor();

    /**
     * GET: gestiona las acciones de listar, nuevo y editar Handles the HTTP
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

        // Recogemos la acción solicitada (por defecto "listar")
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

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
     * POST: procesa el formulario de crear/editar Handles the HTTP Handles the
     * HTTP <code>POST</code> method.
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
     * Carga todos los profesores y los envía a la vista de lista.
     */
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("profesores", daoProfesor.findAll());
        request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
    }

    /**
     * Muestra el formulario vacío para crear un profesor nuevo.
     */
    private void nuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Enviamos un profesor vacío para que el JSP lo use sin null checks
        request.setAttribute("profesor", new Profesor());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    /**
     * Carga un profesor existente y muestra el formulario con sus datos.
     */
    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Profesor profesor = daoProfesor.findById(id);
            if (profesor == null) {
                request.setAttribute("error", "Profesor no encontrado.");
                listar(request, response);
                return;
            }
            request.setAttribute("profesor", profesor);
            request.setAttribute("accion", "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + URL_LISTA);
        }
    }

    /**
     * Elimina un profesor por su ID.
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            // No permitiremos que un profesor se pueda eliminar a si mismo
            Profesor profesorSesion = (Profesor) request.getSession().getAttribute("profesor");
            if (profesorSesion.getId() == id) {
                request.setAttribute("error", "No puedes eliminar tu propio usuario");
                listar(request, response);
                return;
            }
            daoProfesor.destroy(id);
            request.setAttribute("exito", "Profesor eliminado correctamente.");
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "El profesor no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            // Capturamos error de integridad referencial (...)
            request.setAttribute("error", "Error inesperado al eleminar al profesor");
        }
        listar(request, response);
    }

    /**
     * Procesa el formulario: crea o edita según si el ID viene informado.
     */
    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre").trim();
        String apellidos = request.getParameter("apellidos").trim();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        String tipoProfesor = request.getParameter("tipo");
        String idParam = request.getParameter("id");

        // Validacion básica
        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
            request.setAttribute("error", "El nombre, apellidos y email del profesor son obligatorios.");
            rellenarFormulario(request, idParam, nombre, apellidos, email, tipoProfesor);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }
        
        // Al crear el profesor, la contraseña es obligatoria
        if ((idParam == null || idParam.isEmpty()) && password.isEmpty()) {
            request.setAttribute("error", "La contraseña s obligatoria al crear un profesor");
            rellenarFormulario(request, idParam, nombre, apellidos, email, tipoProfesor);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }
        
        // Convertimos el tipo a Enum
        TipoProfesor tipo = TipoProfesor.NORMAL;
        if ("DIRECTIVA".equals(tipoProfesor)) {
            tipo = TipoProfesor.DIRECTIVA;
        }

        try {
            if (idParam == null || idParam.isEmpty()) {
                // Crear nuevo profesor
                // Hasheamos la contraseña antes de guardarla
                String passwordHash = ModeloLogin.hashMD5(password);
                Profesor profesor = new Profesor(nombre, apellidos, email, passwordHash, tipo);
                daoProfesor.create(profesor);
                request.setAttribute("exito", "Profesor creado correctamente.");
            } else {
                // Editar profesor existente
                int id = Integer.parseInt(idParam);
                Profesor profesor = daoProfesor.findById(id);
                profesor.setNombre(nombre);
                profesor.setApellidos(apellidos);
                profesor.setEmail(email);
                // profesor.setPassword(password);
                profesor.setTipo(tipo);
                
                // Solo actualizamos la contraseña si se ha introducido una nueva
                if (!password.isEmpty()) {
                    profesor.setPassword(ModeloLogin.hashMD5(password));
                }
                
                daoProfesor.edit(profesor);
                request.setAttribute("exito", "Profesor actualizado correctamente.");
            }
        } catch (PreexistingEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "Ya existe un profesor con ese email.");
            rellenarFormulario(request, idParam, nombre, apellidos,email, tipoProfesor);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "El profesor no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
        }
        listar(request, response);
    }
    
    /**
     * Rellena el formulario con los datos actuales cuando hay un error de validacion,
     * para que el usuario no pierda lo que habia escrito
     */
    private void rellenarFormulario(HttpServletRequest request, String idParam, 
            String nombre, String apellidos, String email, String tipoProfesor) {
        Profesor p = new Profesor();
        p.setNombre(nombre);
        p.setApellidos(apellidos);
        p.setEmail(email);
        p.setTipo("DIRECTIVA".equals(tipoProfesor) ? TipoProfesor.DIRECTIVA : TipoProfesor.NORMAL);
        if (idParam != null && !idParam.isEmpty()) {
            p.setId(Integer.parseInt(idParam));
        }
        request.setAttribute("profesor", p);
        request.setAttribute("accion", idParam == null || idParam.isEmpty() ? "nuevo" : "editar");
    }
}
