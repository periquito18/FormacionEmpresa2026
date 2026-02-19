/*
 * Entidad Alumno
 * 
 */
package modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 *
 * @author SGame
 */
@Entity
@Table(name = "alumnos")
public class Alumno implements Serializable{
    
    // Define la versión de la clase para garantizar la compatibilidad durante la serialización y deserialización.
    private static final long serialVersionUID = 1L;
    
    // ---- Datos / Atributos ----
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 150, message = "Los apellidos no pueden superar los 150 caracteres")
    @Column(name = "apellidos", nullable = false, length = 150)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email incorrecto")
    @Size(max = 150)
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    // @NotNull(message = "El alumno debe estar matriculado en un curso")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToOne(mappedBy = "alumno", fetch = FetchType.LAZY)
    private Practica practica;
    
    // ---- Constructores ----
    
    public Alumno() {}

    public Alumno(String nombre, String apellidos, String email,
                  LocalDate fechaNacimiento, Curso curso) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.curso = curso;
    }
    
    // ---- Getters y Setters ----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public Practica getPractica() { return practica; }
    public void setPractica(Practica practica) { this.practica = practica; }

    // ---- Utilidades ----

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return id == alumno.id;
    }
    
    @Override
    public String toString() {
        // Cálculo de edad: diferencia entre fechaNacimiento y fecha actual
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        
        // Obtenemos el nombre del curso y de la práctica de forma segura
        String nombreCurso = (curso != null) ? curso.getNombre() : "Sin curso";
        String infoPractica = (practica != null) ? "Asignada" : "Ninguna";

        return "Alumno{" + "id=" + id + 
               ", nombre=" + nombre + " " + apellidos + 
               ", edad=" + edad + 
               ", curso=" + nombreCurso + 
               ", práctica=" + infoPractica + "}";
    }
    
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }
}
