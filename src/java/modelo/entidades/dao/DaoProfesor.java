/*
 * Clase DAO (Data Access Object) Profesor - CRUD Profesor
 * Centraliza todas las operaciones de acceso a la BD relacionadas con profesores.
 * Utiliza JPAUtil para obtener EntityManagers sin gestionar el EMF directamente.
 */
package modelo.entidades.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import modelo.entidades.Profesor;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;
import modelo.util.JPAUtil;

/**
 *
 * @author SGame
 */
public class DaoProfesor {
    
    // ---- CREATE ----
    
    /**
     * Inserta un nuevo profesor en la base de datos
     * Verifica antes que no exista otro profesor con el mismo email
     * 
     * @param profesor El profesor a insertar
     * @throws PreexistingEntityException Si ya existe un profesor con ese email
     */
    public void create(Profesor profesor) throws PreexistingEntityException {
        
        // Comprobamos si ya existe un profesor con ese email
        if (findByEmail(profesor.getEmail()) != null) {
            throw new PreexistingEntityException(
                "Ya existe un profesor con el email: " + profesor.getEmail()
            );
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();    // Iniciamos la transacción
            em.persist(profesor);           // INSESRT en la BD
            em.getTransaction().commit();   // Confirmamos la transacción
        } catch (Exception e) {
            em.getTransaction().rollback(); // Si algo falla, deshacemos los cambios
            throw e;
        } finally {
            em.close(); // Siempre cerramos el EM, haya error o no
        }
    }
    
    // ---- READ ----
    
    /**
     * Busca un profesor por su ID
     * 
     * @param id El ID del profesor
     * @return El profesor encontrado, o null si no existe
     */
    public Profesor findById(int id) {
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // em.find devuelve null automáticamente si no encuentra el registro
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca un profesor por su email
     * Útil para validar duplicados y para el login
     * 
     * @param email El email a buscar
     * @return El profesor encontrado, o null si no existe
     */
    public Profesor findByEmail(String email) {
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL: como SQL pero con nombres de clase y atributos de Java
            TypedQuery<Profesor> query = em.createQuery(
                "SELECT p FROM Profesor p WHERE p.email = :email",
                Profesor.class
            );
            query.setParameter("email", email);
            return query.getSingleResult(); // Lanza excepción si no encuentra nada
        } catch (Exception e) {
            return null; // No existe ningún profesor con ese email
        } finally {
            em.close();
        }
    }
    
    public Profesor findByEmailAndPassword(String email, String password) {
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Profesor> query = em.createQuery(
                "SELECT p FROM Profesor p " +
                "WHERE p.eamil = :email AND p.password = :password",
                Profesor.class
            );
            query.setParameter("email", email);
            query.setParameter("password", password);
            
            return query.getSingleResult();
        } catch (Exception e) {
            return null; // No existe ningún profesor con ese email
        } finally {
            em.close();
        }
    }
    
    /**
     * Devuelve todos los profesores de la bd
     * Usado por la Directiva para gestionar profesores
     * 
     * @return Lista con todos los profesores 
     */
    public List<Profesor> findAll() {
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Profesor> query = em.createQuery(
                "SELECT p FROM Profesor p ORDER BY p.apellidos, p.nombre",
                Profesor.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    // ---- UPDATE ----
    
    public void edit(Profesor profesor) throws NonExistentEntityException {
        
        // Verificamos que el profesor existe antes de intentar actualizarlo
        if (findById(profesor.getId()) == null) {
            throw new NonExistentEntityException(
                "No existe ningún profesor con id: " + profesor.getId()
            );
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(profesor);             // UPDATE en la BD (merge fusiona los cambios)
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    // ---- DELETE ----
    
    /**
     * Elimina un profesor de la base de datos por su ID
     * 
     * @param id El ID del profesor a eliminar
     * @throws NonExistentEntityException  Si el profesor no existe en la BD
     */
    public void destroy(int id) throws NonExistentEntityException {
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // find dentro de la misma transacción para que el objeto
            // esté en estado "managed" y podamos hacer remove
            Profesor profesor = em.find(Profesor.class, id);
            if (profesor == null) {
                throw new NonExistentEntityException(
                    "No existe ningún profesor con id: " + id
                );
            }
            
            em.remove(profesor);    // DELETE en la BD
            em.getTransaction().commit();
        } catch (NonExistentEntityException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    // ---- UTILIDADES ----
    
    /**
     * Cuenta el número total de profesores en la BD
     * 
     * @return El número de profesores
     */
    public int count() {
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Profesor p",
                Long.class
            );
            // getSingleResult devuelve Long, lo convertimos a "int"
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}
