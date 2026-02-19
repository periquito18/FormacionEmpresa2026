/*
 * Clase Excepcion "PreexistingEntityException"
 */
package modelo.entidades.dao.exceptions;

/**
 *
 * @author SGame
 */
public class PreexistingEntityException extends Exception {
    
    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    public PreexistingEntityException(String message) {
        super(message);
    }
}
