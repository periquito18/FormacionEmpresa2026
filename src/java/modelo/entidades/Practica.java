/*
 * Entidad Practica
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
/**
 *
 * @author SGame
 */
@Entity
@Table(name = "practicas")
public class Practica {
    
    // Define la versión de la clase para garantizar la compatibilidad durante la serialización y deserialización.
    private static final long serialVersionUID = 1L;
    
    // ---- Datos / Atributos ----
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "La práctica debe estar asignada a un alumno")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false, unique = true)
    private Alumno alumno;

    @NotNull(message = "La práctica debe estar vinculada a una empresa")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser posterior a hoy")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Size(max = 2000, message = "Los comentarios no pueden exceder los 2000 caracteres")
    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comentarios;

    // ---- Constructores ----

    public Practica() {}

    public Practica(Alumno alumno, Empresa empresa,
                    LocalDate fechaInicio, LocalDate fechaFin) {
        this.alumno = alumno;
        this.empresa = empresa;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // ---- Getters y Setters ----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    // ---- Utilidades ----

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Practica practica = (Practica) o;
        return id == practica.id;
    }
    
    @Override
    public String toString() {
        return "Practica{id=" + id + ", alumno=" + alumno.getNombreCompleto() +
               ", empresa=" + empresa.getNombre() + ", fechaInicio=" +
               fechaInicio + ", fechaFin=" + fechaFin + "}";
    }
    
    /*
    @Override
    public String toString() {
        // Usamos comprobaciones de seguridad para evitar NullPointerException al imprimir
        String nombreAlumno = (alumno != null) ? alumno.getNombreCompleto() : "Sin asignar";
        String nombreEmpresa = (empresa != null) ? empresa.getNombre() : "Sin asignar";
        
        return "Practica{" + "id=" + id + 
               ", alumno=" + nombreAlumno + 
               ", empresa=" + nombreEmpresa + 
               ", periodo=" + fechaInicio + " a " + fechaFin + "}";
    }
    */
    
    /**
     * Validación de lógica de negocio
     * Comprueba que la fecha de fin sea posterior a la de inicio
     */
    @AssertTrue(message = "La fecha de fin debe ser posterior a la fecha de inicio")
    public boolean isFechaValida() {
        if (fechaInicio == null || fechaFin == null) return true; // Lo capturan los @NotNull
        return fechaFin.isAfter(fechaInicio);
    }
}
