/*
 * Entidad Empresa
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author SGame
 */
@Entity
@Table(name = "empresas")
public class Empresa implements Serializable{
    
    // Define la versión de la clase para garantizar la compatibilidad durante la serialización y deserialización.
    private static final long serialVersionUID = 1L;
    
    // ---- Datos / Atributos ----
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Size(max = 2000, message = "La descripción es demasiado larga")
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank(message = "El nombre del tutor es obligatorio")
    @Size(max = 200, message = "El nombre del tutor no puede superar los 200 caracteres")
    @Column(name = "tutor_nombre", nullable = false, length = 200)
    private String tutorNombre;

    @NotBlank(message = "El email del tutor es obligatorio")
    @Email(message = "El formato del email del tutor no es válido")
    @Size(max = 150)
    @Column(name = "tutor_email", nullable = false, length = 150)
    private String tutorEmail;

    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    private List<Practica> practicas;
    
    // ---- Constructores ----

    public Empresa() {}

    public Empresa(String nombre, String descripcion,
                   String tutorNombre, String tutorEmail) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tutorNombre = tutorNombre;
        this.tutorEmail = tutorEmail;
    }

    // ---- Getters y Setters ----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTutorNombre() { return tutorNombre; }
    public void setTutorNombre(String tutorNombre) { this.tutorNombre = tutorNombre; }

    public String getTutorEmail() { return tutorEmail; }
    public void setTutorEmail(String tutorEmail) { this.tutorEmail = tutorEmail; }

    public List<Practica> getPracticas() { return practicas; }
    public void setPracticas(List<Practica> practicas) { this.practicas = practicas; }

    // ---- Utilidades ----

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return id == empresa.id;
    }
    
    @Override
    public String toString() {
        return "Empresa{id=" + id + ", nombre=" + nombre +
               ", tutorNombre=" + tutorNombre + ", tutorEmail=" + tutorEmail + "}";
    }
    
    public String getInfoContacto() {
        return "Empresa: " + nombre + " | Tutor: " + tutorNombre + 
                " - " + tutorEmail;
    }
}
