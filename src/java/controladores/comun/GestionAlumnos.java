/*
 * Controlador GestionAlumnos
 * Servlet que gestiona el CRUD completo de Alumnos
 * Accesible para todos los profesores autenticados
 * Incluye importacion masiva desde fichero CSV
 * @MultipartConfig es obligatorio para poder recibir ficheros subidos
 * desde formularios con enctyp="multipart/form-data"
 */
package controladores.comun;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import modelo.entidades.Alumno;
import modelo.entidades.Curso;
import modelo.entidades.dao.DaoAlumno;
import modelo.entidades.dao.DaoCurso;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;
import modelo.util.CSVImporter;

/**
 *
 * @author SGame
 */
@WebServlet(name = "GestionAlumnos", urlPatterns = {"/comun/GestionAlumnos"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // Máximo 5MB por fichero
public class GestionAlumnos extends HttpServlet {

    private static final String VISTA_LISTA   = "/comun/verAlumnos.jsp";
    private static final String VISTA_EDITAR  = "/comun/editarAlumno.jsp";
    private static final String URL_LISTA     = "/comun/GestionAlumnos?accion=listar";

    private final DaoAlumno daoAlumno = new DaoAlumno();
    private final DaoCurso  daoCurso  = new DaoCurso();
    
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
        String accion = request.getParameter("accion");
        if ("importar".equals(accion)) {
            importarCSV(request, response);
        } else {
            guardar(request, response);
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

    // ---- Acciones privadas ----
    /**
     * Lista los alumnos filtrados por curso si se especifica,
     * o todos los alumnos si no hay filtro.
     * Carga todos los alumnos y los envia a la vista de lista
     */
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Cargamos todos los cursos para el selector de filtro
        request.setAttribute("cursos", daoCurso.findAll());

        // Comprobamos si hay filtro por curso
        String cursoIdParam = request.getParameter("cursoId");
        if (cursoIdParam != null && !cursoIdParam.isEmpty()) {
            try {
                int cursoId = Integer.parseInt(cursoIdParam);
                Curso curso = daoCurso.findById(cursoId);
                if (curso != null) {
                    // Alumnos filtrados por curso
                    request.setAttribute("alumnos", daoAlumno.findByCurso(curso));
                    request.setAttribute("cursoSeleccionado", curso);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            // Sin filtro: todos los alumnos
            request.setAttribute("alumnos", daoAlumno.findAll());
        }

        request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
    }
    
    /**
     * Muestra el formulario vacío para crear un alumno nuevo
     */
    private void nuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("alumno", new Alumno());
        request.setAttribute("cursos", daoCurso.findAll());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }
    
    /**
     * Carga un alumno existente y muestra el formulario con sus datos.
     */
    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Alumno alumno = daoAlumno.findById(id);
            if (alumno == null) {
                request.setAttribute("error", "Alumno no encontrado.");
                listar(request, response);
                return;
            }
            request.setAttribute("alumno", alumno);
            request.setAttribute("cursos", daoCurso.findAll());
            request.setAttribute("accion", "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + URL_LISTA);
        }
    }
    
    /**
     * Elimina un alumno por su ID.
     * Fallará si el alumno tiene una práctica asociada.
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            daoAlumno.destroy(id);
            request.setAttribute("exito", "Alumno eliminado correctamente.");
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "El alumno no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error",
                "No se puede eliminar el alumno porque tiene una práctica asociada.");
        }
        listar(request, response);
    }
    
    /**
     * Procesa el formulario de crear o editar alumno.
     */
    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre       = request.getParameter("nombre").trim();
        String apellidos    = request.getParameter("apellidos").trim();
        String email        = request.getParameter("email").trim();
        String fechaStr     = request.getParameter("fechaNacimiento");
        String cursoIdParam = request.getParameter("cursoId");
        String idParam      = request.getParameter("id");

        // Validación básica de campos obligatorios
        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty()
                || fechaStr.isEmpty() || cursoIdParam.isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.setAttribute("cursos", daoCurso.findAll());
            request.setAttribute("accion", 
                idParam == null || idParam.isEmpty() ? "nuevo" : "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        // Parseamos la fecha (viene como String "yyyy-MM-dd" desde el input date)
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Formato de fecha incorrecto.");
            request.setAttribute("cursos", daoCurso.findAll());
            request.setAttribute("accion",
                idParam == null || idParam.isEmpty() ? "nuevo" : "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        // Buscamos el curso seleccionado
        Curso curso = daoCurso.findById(Integer.parseInt(cursoIdParam));
        if (curso == null) {
            request.setAttribute("error", "Curso no válido.");
            request.setAttribute("cursos", daoCurso.findAll());
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        try {
            if (idParam == null || idParam.isEmpty()) {
                // ---- CREAR nuevo alumno ----
                Alumno alumno = new Alumno(nombre, apellidos, email, fechaNacimiento, curso);
                daoAlumno.create(alumno);
                request.setAttribute("exito", "Alumno creado correctamente.");
            } else {
                // ---- EDITAR alumno existente ----
                int id = Integer.parseInt(idParam);
                Alumno alumno = daoAlumno.findById(id);
                alumno.setNombre(nombre);
                alumno.setApellidos(apellidos);
                alumno.setEmail(email);
                alumno.setFechaNacimiento(fechaNacimiento);
                alumno.setCurso(curso);
                daoAlumno.edit(alumno);
                request.setAttribute("exito", "Alumno actualizado correctamente.");
            }
        } catch (PreexistingEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "Ya existe un alumno con ese email.");
            request.setAttribute("cursos", daoCurso.findAll());
            request.setAttribute("accion",
                idParam == null || idParam.isEmpty() ? "nuevo" : "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "El alumno no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
        }

        listar(request, response);
    }
    
    /**
     * Importa alumnos masivamente desde un fichero CSV.
     * El CSV debe tener el formato:
     * nombre,apellidos,email,fecha_nacimiento (yyyy-MM-dd)
     * La primera línea puede ser una cabecera (se detecta automáticamente).
     */
    private void importarCSV(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recogemos el curso destino y el fichero subido
        String cursoIdParam = request.getParameter("cursoId");

        if (cursoIdParam == null || cursoIdParam.isEmpty()) {
            request.setAttribute("error", "Debes seleccionar un curso para importar.");
            listar(request, response);
            return;
        }

        Curso curso = daoCurso.findById(Integer.parseInt(cursoIdParam));
        if (curso == null) {
            request.setAttribute("error", "Curso no válido.");
            listar(request, response);
            return;
        }

        // Obtenemos el fichero subido mediante la API de Servlet 3.0+
        Part fichero = request.getPart("ficheroCSV");
        if (fichero == null || fichero.getSize() == 0) {
            request.setAttribute("error", "Debes seleccionar un fichero CSV.");
            listar(request, response);
            return;
        }

        // Pasamos el stream del fichero a nuestra clase utilitaria CSVImporter
        try (InputStream inputStream = fichero.getInputStream()) {
            CSVImporter.ResultadoImportacion resultado =
                CSVImporter.importarAlumnos(inputStream, curso, daoAlumno);

            // Mostramos el resumen de la importación
            request.setAttribute("exito",
                "Importación completada: " + resultado.getImportados() +
                " alumnos importados, " + resultado.getErrores() + " errores.");

            // Si hubo errores los mostramos en detalle
            if (!resultado.getMensajesError().isEmpty()) {
                request.setAttribute("erroresCSV", resultado.getMensajesError());
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el fichero CSV: "
                + e.getMessage());
        }

        listar(request, response);
    }
}
