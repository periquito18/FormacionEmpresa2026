/*
 * Recurso REST para la gestión de alumnos.
 * Base URL: /api/alumnos
 * 
 * Endpoints disponibles:
 * GET    /api/alumnos          → lista todos los alumnos
 * GET    /api/alumnos/{id}     → obtiene un alumno por ID
 * POST   /api/alumnos          → crea un nuevo alumno
 * PUT    /api/alumnos/{id}     → actualiza un alumno existente
 * DELETE /api/alumnos/{id}     → elimina un alumno
 */
package rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import modelo.entidades.Alumno;
import modelo.entidades.Curso;
import modelo.entidades.dao.DaoAlumno;
import modelo.entidades.dao.DaoCurso;
import modelo.entidades.dao.exceptions.PreexistingEntityException;
import rest.dto.AlumnoDTO;

/**
 *
 * @author SGame
 */
@Path("/alumnos")
@Produces(MediaType.APPLICATION_JSON) // Todos los métodos devuelven JSON
@Consumes(MediaType.APPLICATION_JSON) // Todos los métodos aceptan JSON
public class AlumnoREST {

    private final DaoAlumno daoAlumno = new DaoAlumno();
    private final DaoCurso daoCurso = new DaoCurso();

    // ---- GET /api/alumnos ----
    /**
     * Devuelve la lista completa de alumnos en formato JSON.
     *
     * Ejemplo de respuesta: [
     * {"id":1,"nombre":"Miguel","apellidos":"Torres","email":"...","curso":"1
     * DAW"},
     * {"id":2,"nombre":"Sara","apellidos":"Díaz","email":"...","curso":"1 DAW"}
     * ]
     */
    @GET
    public Response getAll() {
        try {
            List<Alumno> alumnos = daoAlumno.findAll();

            // Convertimos cada Alumno a AlumnoDTO para el JSON
            List<AlumnoDTO> dtos = alumnos.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return Response.ok(dtos).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("{\"error\":\"Error al obtener alumnos\"}")
                    .build();
        }
    }

    // ---- GET /api/alumnos/{id} ----
    /**
     * Devuelve un alumno concreto por su ID.
     *
     * @PathParam("id") extrae el {id} de la URL
     *
     * Ejemplo: GET /api/alumnos/3 Respuesta:
     * {"id":3,"nombre":"Javier","apellidos":"Ruiz",...}
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            Alumno alumno = daoAlumno.findById(id);
            if (alumno == null) {
                // 404 Not Found
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Alumno no encontrado\"}")
                        .build();
            }
            return Response.ok(toDTO(alumno)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("{\"error\":\"Error al obtener el alumno\"}")
                    .build();
        }
    }

    // ---- POST /api/alumnos ----
    /**
     * Crea un nuevo alumno. Recibe un AlumnoDTO en el cuerpo de la petición en
     * formato JSON.
     *
     * Ejemplo de body: { "nombre": "Nuevo", "apellidos": "Alumno", "email":
     * "nuevo@alumno.es", "fechaNacimiento": "2005-01-15", "curso": "1 DAW" }
     */
    @POST
    public Response create(AlumnoDTO dto) {
        try {
            // Validamos campos obligatorios
            if (dto.getNombre() == null || dto.getNombre().isBlank()
                    || dto.getApellidos() == null || dto.getApellidos().isBlank()
                    || dto.getEmail() == null || dto.getEmail().isBlank()
                    || dto.getFechaNacimiento() == null
                    || dto.getCurso() == null || dto.getCurso().isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Todos los campos son obligatorios\"}")
                        .build();
            }

            // Parseamos la fecha
            LocalDate fecha;
            try {
                fecha = LocalDate.parse(dto.getFechaNacimiento());
            } catch (DateTimeParseException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Formato de fecha incorrecto (yyyy-MM-dd)\"}")
                        .build();
            }

            // Buscamos el curso por nombre
            Curso curso = daoCurso.findByNombre(dto.getCurso());
            if (curso == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Curso no encontrado: " + dto.getCurso() + "\"}")
                        .build();
            }

            // Creamos el alumno
            Alumno alumno = new Alumno(dto.getNombre(), dto.getApellidos(),
                    dto.getEmail(), fecha, curso);
            daoAlumno.create(alumno);

            // 201 Created con el alumno creado en el body
            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(alumno))
                    .build();

        } catch (PreexistingEntityException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Ya existe un alumno con ese email\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("{\"error\":\"Error al crear el alumno\"}")
                    .build();
        }
    }

    // ---- PUT /api/alumnos/{id} ----
    /**
     * Actualiza un alumno existente. Solo actualiza los campos que vienen
     * informados en el DTO.
     *
     * Ejemplo: PUT /api/alumnos/3 Body: {"nombre":"Javier","apellidos":"Ruiz
     * Castillo Nuevo",...}
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, AlumnoDTO dto) {
        try {
            Alumno alumno = daoAlumno.findById(id);
            if (alumno == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Alumno no encontrado\"}")
                        .build();
            }

            // Actualizamos solo los campos que vienen informados
            if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
                alumno.setNombre(dto.getNombre());
            }
            if (dto.getApellidos() != null && !dto.getApellidos().isBlank()) {
                alumno.setApellidos(dto.getApellidos());
            }
            if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
                alumno.setEmail(dto.getEmail());
            }
            if (dto.getFechaNacimiento() != null) {
                try {
                    alumno.setFechaNacimiento(
                            LocalDate.parse(dto.getFechaNacimiento())
                    );
                } catch (DateTimeParseException e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\":\"Formato de fecha incorrecto\"}")
                            .build();
                }
            }
            if (dto.getCurso() != null && !dto.getCurso().isBlank()) {
                Curso curso = daoCurso.findByNombre(dto.getCurso());
                if (curso == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\":\"Curso no encontrado\"}")
                            .build();
                }
                alumno.setCurso(curso);
            }

            daoAlumno.edit(alumno);

            // 200 OK con el alumno actualizado
            return Response.ok(toDTO(alumno)).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("{\"error\":\"Error al actualizar el alumno\"}")
                    .build();
        }
    }

    // ---- DELETE /api/alumnos/{id} ----
    /**
     * Elimina un alumno por su ID. Fallará si el alumno tiene una práctica
     * asociada.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        try {
            Alumno alumno = daoAlumno.findById(id);
            if (alumno == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Alumno no encontrado\"}")
                        .build();
            }
            daoAlumno.destroy(id);
            // 204 No Content: éxito sin cuerpo de respuesta
            return Response.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"No se puede eliminar, tiene práctica asociada\"}")
                    .build();
        }
    }

    // ---- Método auxiliar ----
    /**
     * Convierte una entidad Alumno a su DTO para la respuesta JSON. Así no
     * exponemos la entidad JPA directamente.
     */
    private AlumnoDTO toDTO(Alumno alumno) {
        return new AlumnoDTO(
                alumno.getId(),
                alumno.getNombre(),
                alumno.getApellidos(),
                alumno.getEmail(),
                alumno.getFechaNacimiento() != null
                ? alumno.getFechaNacimiento().toString() : null,
                alumno.getCurso() != null
                ? alumno.getCurso().getNombre() : null
        );
    }
}
