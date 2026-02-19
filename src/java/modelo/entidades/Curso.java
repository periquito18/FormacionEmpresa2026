/*
 * Entidad Curso
 * 
 */
package modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author SGame
 */
@Entity
@Table(name = "cursos")
public class Curso implements Serializable{
    
    // Define la versión de la clase para garantizar la compatibilidad durante la serialización y deserialización.
    private static final long serialVersionUID = 1L;
    
    // ---- Datos / Atributos ----
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    // @NotNull(message = "La lista de alumnos no puede ser nula")
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Alumno> alumnos;
    
    // ---- Constructores ----

    public Curso() {}

    public Curso(String nombre) {
        this.nombre = nombre;
    }
    
    // ---- Getters y Setters ----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Alumno> getAlumnos() { return alumnos; }
    public void setAlumnos(List<Alumno> alumnos) { this.alumnos = alumnos; }
    
    // ---- Utilidades ----
    // ---- Métodos de Identidad y Representación ----
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return id == curso.id;
    }
    
    @Override
    public String toString() {
        return "Curso{id=" + id + ", nombre=" + nombre + "}";
    }
}
