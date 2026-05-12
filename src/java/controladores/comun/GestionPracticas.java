/*
 * Controlador GestionPracticas
 * Servlet que gestiona el CRUD completo de Prácticas
 * Al crear una práctica envía automáticamente un email al alumno
 * Accesible para todos los profesores autenticados
 */
package controladores.comun;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import modelo.entidades.Alumno;
import modelo.entidades.Empresa;
import modelo.entidades.Practica;
import modelo.entidades.dao.DaoAlumno;
import modelo.entidades.dao.DaoEmpresa;
import modelo.entidades.dao.DaoPractica;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;
import modelo.util.Email;

/**
 *
 * @author SGame
 */
@WebServlet(name = "GestionPracticas", urlPatterns = {"/comun/GestionPracticas"})
public class GestionPracticas extends HttpServlet {

    private static final String VISTA_LISTA = "/comun/verPracticas.jsp";
    private static final String VISTA_EDITAR = "/comun/editarPractica.jsp";
    private static final String URL_LISTA = "/comun/GestionPracticas?accion=listar";

    private final DaoPractica daoPractica = new DaoPractica();
    private final DaoAlumno daoAlumno = new DaoAlumno();
    private final DaoEmpresa daoEmpresa = new DaoEmpresa();

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

    // ---- Acciones Privadas ----
    /**
     * Carga todas las practicas y las envia a la vista de lista
     */
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("practicas", daoPractica.findAll());
        request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
    }

    /**
     * Muestra el formulario vacio para crear una practica nueva
     */
    private void nuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargamos alumnos SIN práctica asignada para el selector
        request.setAttribute("alumnosSinPractica",
                daoAlumno.findAll().stream().filter(a -> daoPractica.findByAlumno(a) == null).toList()
        );
        request.setAttribute("empresas", daoEmpresa.findAll());
        request.setAttribute("practica", new Practica());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    /**
     * Carga una practica existente y muestra el formulario con sus datos
     */
    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Practica practica = daoPractica.findById(id);
            if (practica == null) {
                request.setAttribute("error", "Práctica no encontrada.");
                listar(request, response);
                return;
            }
            request.setAttribute("practica", practica);
            request.setAttribute("empresas", daoEmpresa.findAll());
            request.setAttribute("accion", "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + URL_LISTA);
        }
    }

    /**
     * Elimina los datos de una practica por su ID
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            daoPractica.destroy(id);
            request.setAttribute("exito", "Práctica eliminada correctamente.");
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "La práctica no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "Error inesperado al eliminar la práctica.");
        }
        listar(request, response);
    }

    /**
     * Procesa el formulario: crea o edita según si el ID viene informado
     */
    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String alumnoIdParam = request.getParameter("alumnoId");
        String empresaIdParam = request.getParameter("empresaId");
        String fechaInicioStr = request.getParameter("fechaInicio");
        String fechaFinStr = request.getParameter("fechaFin");
        String comentarios = request.getParameter("comentarios");
        String idParam = request.getParameter("id");

        // Validación de campos obligatorios
        if (empresaIdParam == null || empresaIdParam.isEmpty()
                || fechaInicioStr.isEmpty() || fechaFinStr.isEmpty()) {
            request.setAttribute("error",
                    "Empresa y fechas son obligatorios.");
            cargarDatosFormulario(request, idParam);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        // Parseamos las fechas
        LocalDate fechaInicio, fechaFin;
        try {
            fechaInicio = LocalDate.parse(fechaInicioStr);
            fechaFin = LocalDate.parse(fechaFinStr);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Formato de fecha incorrecto.");
            cargarDatosFormulario(request, idParam);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        // Validamos que la fecha de fin sea posterior a la de inicio
        if (fechaFin.isBefore(fechaInicio)) {
            request.setAttribute("error",
                    "La fecha de fin debe ser posterior a la fecha de inicio.");
            cargarDatosFormulario(request, idParam);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        // Buscamos empresa
        Empresa empresa = daoEmpresa.findById(Integer.parseInt(empresaIdParam));
        if (empresa == null) {
            request.setAttribute("error", "Empresa no válida.");
            cargarDatosFormulario(request, idParam);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        try {
            if (idParam == null || idParam.isEmpty()) {
                // ---- CREAR nueva práctica ----
                if (alumnoIdParam == null || alumnoIdParam.isEmpty()) {
                    request.setAttribute("error", "Debes seleccionar un alumno.");
                    cargarDatosFormulario(request, idParam);
                    request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
                    return;
                }

                Alumno alumno = daoAlumno.findById(Integer.parseInt(alumnoIdParam));
                if (alumno == null) {
                    request.setAttribute("error", "Alumno no válido.");
                    cargarDatosFormulario(request, idParam);
                    request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
                    return;
                }

                Practica practica = new Practica(alumno, empresa,
                        fechaInicio, fechaFin);
                if (comentarios != null && !comentarios.trim().isEmpty()) {
                    practica.setComentarios(comentarios.trim());
                }

                daoPractica.create(practica);

                // Enviamos email al alumno con los datos de la práctica
                // Lo hacemos en un hilo separado para no bloquear la respuesta
                // si el servidor de correo tarda en responder
                final Alumno alumnoFinal = alumno;
                final Empresa empresaFinal = empresa;
                final LocalDate inicioFinal = fechaInicio;
                final LocalDate finFinal = fechaFin;

                new Thread(() -> Email.enviarNotificacionPractica(
                        alumnoFinal.getEmail(),
                        alumnoFinal.getNombreCompleto(),
                        empresaFinal.getNombre(),
                        empresaFinal.getTutorNombre(),
                        empresaFinal.getTutorEmail(),
                        inicioFinal.toString(),
                        finFinal.toString()
                )).start();

                request.setAttribute("exito",
                        "Práctica creada correctamente. "
                        + "Se ha enviado un email de notificación al alumno.");

            } else {
                // ---- EDITAR práctica existente ----
                // En edición solo se pueden cambiar empresa, fechas y comentarios
                // El alumno no cambia
                int id = Integer.parseInt(idParam);
                Practica practica = daoPractica.findById(id);
                practica.setEmpresa(empresa);
                practica.setFechaInicio(fechaInicio);
                practica.setFechaFin(fechaFin);
                practica.setComentarios(
                        comentarios != null ? comentarios.trim() : null
                );
                daoPractica.edit(practica);
                request.setAttribute("exito", "Práctica actualizada correctamente.");
            }

        } catch (PreexistingEntityException e) {
            e.printStackTrace();
            request.setAttribute("error",
                    "Este alumno ya tiene una práctica asignada.");
            cargarDatosFormulario(request, idParam);
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "La práctica no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
        }

        listar(request, response);
    }

    /**
     * Carga los datos necesarios para mostrar el formulario. Reutilizado tanto
     * en nuevo como en editar para no repetir código.
     */
    private void cargarDatosFormulario(HttpServletRequest request, String idParam)
            throws ServletException {
        request.setAttribute("empresas", daoEmpresa.findAll());
        if (idParam == null || idParam.isEmpty()) {
            // Formulario de nuevo: cargamos alumnos sin práctica
            request.setAttribute("alumnosSinPractica",
                    daoAlumno.findAll().stream().filter(a -> daoPractica.findByAlumno(a) == null).toList());
            request.setAttribute("practica", new Practica());
            request.setAttribute("accion", "nuevo");
        } else {
            // Formulario de editar: cargamos la práctica existente
            Practica practica = daoPractica.findById(Integer.parseInt(idParam));
            request.setAttribute("practica", practica);
            request.setAttribute("accion", "editar");
        }
    }
}
