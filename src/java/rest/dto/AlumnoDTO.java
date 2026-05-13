/*
 * DTO (Data Transfer Object) para Alumno.
 * Representa los datos del alumno que se exponen en la API REST.
 */
package rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author SGame
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluye campos null en el JSON
public class AlumnoDTO {
    
    // --- Atributos ---
    private Integer id;
    private String nombre;
    private String apellidos;
    private String email;
    private String fechaNacimiento; // String para control del formato
    private String curso;           // Solo el nombre del curso, no el objeto completo

    // ---- Constructor vacío (necesario para Jackson) ----
    public AlumnoDTO() {}

    // ---- Constructor completo ----
    public AlumnoDTO(Integer id, String nombre, String apellidos,
                     String email, String fechaNacimiento, String curso) {
        this.id              = id;
        this.nombre          = nombre;
        this.apellidos       = apellidos;
        this.email           = email;
        this.fechaNacimiento = fechaNacimiento;
        this.curso           = curso;
    }

    // ---- Getters y Setters ----
    public Integer getId()                      { return id; }
    public void setId(Integer id)               { this.id = id; }

    public String getNombre()                   { return nombre; }
    public void setNombre(String nombre)        { this.nombre = nombre; }

    public String getApellidos()                { return apellidos; }
    public void setApellidos(String apellidos)  { this.apellidos = apellidos; }

    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }

    public String getFechaNacimiento()          { return fechaNacimiento; }
    public void setFechaNacimiento(String f)    { this.fechaNacimiento = f; }

    public String getCurso()                    { return curso; }
    public void setCurso(String curso)          { this.curso = curso; }
}
