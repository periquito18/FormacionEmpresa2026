/*
 * Controlador GestionEmpresas
 * Servlet que gestiona el CRUD completo de Empresas
 * Accesible para todos los profesores autenticados
 */
package controladores.comun;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.entidades.Empresa;
import modelo.entidades.dao.DaoEmpresa;
import modelo.entidades.dao.exceptions.NonExistentEntityException;

/**
 *
 * @author SGame
 */
@WebServlet(name = "GestionEmpresas", urlPatterns = {"/comun/GestionEmpresas"})
public class GestionEmpresas extends HttpServlet {

    // Rutas de las vistas
    private static final String VISTA_LISTA = "/comun/verEmpresas.jsp";
    private static final String VISTA_EDITAR = "/comun/editarEmpresa.jsp";
    private static final String URL_LISTA = "/comun/GestionEmpresas?accion=listar";
    
    private final DaoEmpresa daoEmpresa = new DaoEmpresa();
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
     * POST: procesa el formulario de crear/editar Handles the HTTP
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
    
    // ---- Acciones privadas ----
    
    /**
     * Carga todas las empresas y las envía a la vista de lista.
     */
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("empresas", daoEmpresa.findAll());
        request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
    }

    /**
     * Muestra el formulario vacío para añadir una empresa nueva.
     */
    private void nuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Enviamos una empresa vacía para que el JSP lo use sin null checks
        request.setAttribute("empresa", new Empresa());
        request.setAttribute("accion", "nuevo");
        request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
    }

    /**
     * Carga una empresa existente y muestra el formulario con sus datos.
     */
    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Empresa empresa = daoEmpresa.findById(id);
            if (empresa == null) {
                request.setAttribute("error", "Empresa no encontrada.");
                listar(request, response);
                return;
            }
            request.setAttribute("empresa", empresa);
            request.setAttribute("accion", "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + URL_LISTA);
        }
    }

    /**
     * Elimina los datos de una empresa por su ID.
     */
    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            daoEmpresa.destroy(id);
            request.setAttribute("exito", "Empresa eliminada correctamente.");
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "La empresa no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            // Capturamos error de integridad referencial (tiene prácticas asociadas)
            request.setAttribute("error",
                    "No se puede eliminar la empresa porque tiene prácticas asociadas.");
        }
        listar(request, response);
    }

    /**
     * Procesa el formulario: crea o edita según si el ID viene informado.
     */
    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre").trim();
        String descripcion = request.getParameter("descripcion").trim();
        String tutorNombre = request.getParameter("tutorNombre").trim();
        String tutorEmail = request.getParameter("tutorEmail").trim();
        String idParam = request.getParameter("id");

        // Validación básica
        if (nombre.isEmpty() ||tutorNombre.isEmpty() ||tutorEmail.isEmpty()) {
            request.setAttribute("error", "El nombre de la empresa, tutor y email del tutor son obligatorios.");
            request.setAttribute("empresa", new Empresa(nombre, descripcion, tutorNombre, tutorEmail));
            request.setAttribute("accion", idParam == null || idParam.isEmpty()
                    ? "nuevo" : "editar");
            request.getRequestDispatcher(VISTA_EDITAR).forward(request, response);
            return;
        }

        try {
            if (idParam == null || idParam.isEmpty()) {
                // Crear nuevo registro de empresa
                Empresa empresa = new Empresa(nombre, descripcion, tutorNombre, tutorEmail);
                daoEmpresa.create(empresa);
                request.setAttribute("exito", "Empresa creada correctamente.");
            } else {
                // Editar empresa existente
                int id = Integer.parseInt(idParam);
                Empresa empresa = daoEmpresa.findById(id);
                empresa.setNombre(nombre);
                empresa.setDescripcion(descripcion);
                empresa.setTutorNombre(tutorNombre);
                empresa.setTutorEmail(tutorEmail);
                daoEmpresa.edit(empresa);
                request.setAttribute("exito", "Empresa actualizada correctamente.");
            }
        } catch (NonExistentEntityException e) {
            e.printStackTrace();
            request.setAttribute("error", "La empresa no existe.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
        }
        listar(request, response);
    }

}
