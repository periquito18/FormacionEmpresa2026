/*
 * Clase Excepcion "NonExistentEntityException"
 */
package modelo.entidades.dao.exceptions;

/**
 *
 * @author SGame
 */
public class NonExistentEntityException extends Exception {

    public NonExistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistentEntityException(String message) {
        super(message);
    }
}
