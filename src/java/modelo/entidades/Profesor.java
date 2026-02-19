/*
 * Entidad Profesor
 * 
 */
package modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author SGame
 */
@Entity
@Table(name = "profesores")
public class Profesor implements Serializable {

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
    @Email(message = "El formato del email no es válido")
    @Size(max = 150)
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @NotNull(message = "El tipo de profesor debe ser asignado")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoProfesor tipo;

    // ---- Constructores ----
    
    public Profesor() {}
    
    public Profesor(String nombre, String apellidos, String email,
            String password, TipoProfesor tipo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    // ---- Getters y Setters ----
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoProfesor getTipo() {
        return tipo;
    }

    public void setTipo(TipoProfesor tipo) {
        this.tipo = tipo;
    }

    // ---- Utilidades ----
    // ---- Métodos de Identidad y Representación ----
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
        // return 31 + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Profesor other = (Profesor) obj;
        // Si el ID es 0 (nuevo objeto), solo es igual a sí mismo
        if (id == 0) {
            return false;
        }
        return id == other.id;
    }

    @Override
    public String toString() {
        return "Profesor{id=" + id + ", nombre=" + nombre
                + ", apellidos=" + apellidos + ", email=" + email
                + ", tipo=" + tipo + "}";
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    public String getInfoContacto() {
        return "Profesor: " + nombre + " | Email: " + email;
    }
}
