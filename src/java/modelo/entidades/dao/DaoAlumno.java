/*
 * Clase DAO (Data Access Object) Alumno - CRUD Alumno
 * Gestiona todas las operaciones de acceso a la BD relacionadas con alumnos.
 * Incluye métodos específicos para buscar por curso y por email,
 * necesarios para la importación CSV y la consulta por cursos.
 */
package modelo.entidades.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import modelo.entidades.Alumno;
import modelo.entidades.Curso;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;
import modelo.util.JPAUtil;

/**
 *
 * @author SGame
 */
public class DaoAlumno {

    // ---- CREATE ----
    /**
     * Inserta un nuevo alumno en la BD. Verifica que no exista otro alumno con
     * el mismo email.
     *
     * @param alumno El alumno a insertar
     * @throws PreexistingEntityException Si ya existe un alumno con ese email
     */
    public void create(Alumno alumno) throws PreexistingEntityException {
        if (findByEmail(alumno.getEmail()) != null) {
            throw new PreexistingEntityException(
                    "Ya existe un alumno con el email: " + alumno.getEmail()
            );
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(alumno);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ---- READ ----
    /**
     * Busca un alumno por su ID.
     *
     * @param id El ID del alumno
     * @return El alumno encontrado, o null si no existe
     */
    public Alumno findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Busca un alumno por su email. Útil para validar duplicados y durante la
     * importación CSV.
     *
     * @param email El email a buscar
     * @return El alumno encontrado, o null si no existe
     */
    public Alumno findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Alumno> query = em.createQuery(
                    "SELECT a FROM Alumno a WHERE a.email = :email",
                    Alumno.class
            );
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todos los alumnos de un curso concreto. El enunciado indica que
     * los alumnos se consultarán por cursos.
     *
     * @param curso El curso cuyos alumnos queremos obtener
     * @return Lista de alumnos matriculados en ese curso
     */
    public List<Alumno> findByCurso(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Alumno> query = em.createQuery(
                    "SELECT a FROM Alumno a WHERE a.curso = :curso "
                    + "ORDER BY a.apellidos, a.nombre",
                    Alumno.class
            );
            query.setParameter("curso", curso);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todos los alumnos ordenados por apellidos y nombre.
     *
     * @return Lista con todos los alumnos
     */
    public List<Alumno> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Alumno> query = em.createQuery(
                    "SELECT a FROM Alumno a ORDER BY a.apellidos, a.nombre",
                    Alumno.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ---- UPDATE ----
    /**
     * Actualiza los datos de un alumno existente.
     *
     * @param alumno El alumno con los datos actualizados
     * @throws NonExistentEntityException Si el alumno no existe en la BD
     */
    public void edit(Alumno alumno) throws NonExistentEntityException {
        if (findById(alumno.getId()) == null) {
            throw new NonExistentEntityException(
                    "No existe ningún alumno con id: " + alumno.getId()
            );
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(alumno);
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
     * Elimina un alumno de la BD por su ID. Fallará si el alumno tiene una
     * práctica asociada (integridad referencial). Validar en el Servlet antes
     * de llamar a este método.
     *
     * @param id El ID del alumno a eliminar
     * @throws NonExistentEntityException Si el alumno no existe
     */
    public void destroy(int id) throws NonExistentEntityException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Alumno alumno = em.find(Alumno.class, id);
            if (alumno == null) {
                throw new NonExistentEntityException(
                        "No existe ningún alumno con id: " + id
                );
            }
            em.remove(alumno);
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
     * Cuenta el número total de alumnos en la BD.
     *
     * @return El número de alumnos
     */
    public int count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Alumno a",
                    Long.class
            );
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta el número de alumnos de un curso concreto. Útil para las
     * estadísticas.
     *
     * @param curso El curso a contar
     * @return El número de alumnos en ese curso
     */
    public int countByCurso(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Alumno a WHERE a.curso = :curso",
                    Long.class
            );
            query.setParameter("curso", curso);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}
