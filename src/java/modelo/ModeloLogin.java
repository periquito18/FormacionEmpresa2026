/*
 * Modelo de Login
 * 
 * Clase de servicio encargada exclusivamente de la autenticación.
 * Delega el acceso a la BD en DaoProfesor, siguiendo el patrón DAO.
 * 
 * Clase de servicio para la autenticación; actúa como puente entre el formulario y la base de datos 
 * verificando credenciales para devolver el objeto Profesor si el acceso es correcto.
 */
package modelo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import modelo.entidades.Profesor;
import modelo.entidades.dao.DaoProfesor;

/**
 *
 * @author SGame
 */
public class ModeloLogin {

    // DAO que usaremos para consultar profesores en la BD
    private final DaoProfesor daoProfesor;

    /**
     * Constructor. Instancia el DAO que necesitamos.
     */
    public ModeloLogin() {
        this.daoProfesor = new DaoProfesor();
    }

    /**
     * Verifica las credenciales de un profesor.
     * Hashea la contraseña recibida y la compara con la almacenada en BD.
     *
     * @param email    El email introducido en el formulario de login
     * @param password La contraseña en texto plano introducida en el formulario
     * @return El Profesor autenticado, o null si las credenciales son incorrectas
     */
    public Profesor verificarLogin(String email, String password) {
        // Hasheamos la contraseña antes de consultar la BD
        // porque en BD están almacenadas hasheadas con MD5
        String passwordHash = hashMD5(password);

        // Delegamos la consulta en el DAO
        return daoProfesor.findByEmailAndPassword(email, passwordHash);
    }

    /**
     * Genera el hash MD5 de un texto.
     * Se usa para hashear contraseñas antes de compararlas o guardarlas.
     * Es estático para poder usarse también desde otros sitios (ej: crear profesor).
     *
     * @param texto El texto a hashear
     * @return El hash MD5 en formato hexadecimal
     */
    public static String hashMD5(String texto) {
        try {
            // MessageDigest es la clase de Java para funciones hash
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() aplica el hash y devuelve un array de bytes
            byte[] hash = md.digest(texto.getBytes());

            // Convertimos cada byte a su representación hexadecimal de 2 dígitos
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // MD5 siempre está disponible en Java, esto nunca debería ocurrir
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }
}