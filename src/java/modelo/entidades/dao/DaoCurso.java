/*
 * Clase DAO (Data Access Object) Curso - CRUD Curso
 * Gestiona todas las operaciones de acceso a la BD relacionadas con cursos
 */
package modelo.entidades.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import modelo.entidades.Curso;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;
import modelo.util.JPAUtil;

/**
 *
 * @author SGame
 */
public class DaoCurso {

    // ---- CREATE ----

    /**
     * Inserta un nuevo curso en la BD.
     * Verifica que no exista otro curso con el mismo nombre (es UNIQUE en BD).
     *
     * @param curso El curso a insertar
     * @throws PreexistingEntityException Si ya existe un curso con ese nombre
     */
    public void create(Curso curso) throws PreexistingEntityException {
        if (findByNombre(curso.getNombre()) != null) {
            throw new PreexistingEntityException(
                "Ya existe un curso con el nombre: " + curso.getNombre()
            );
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(curso);
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
     * Busca un curso por su ID.
     *
     * @param id El ID del curso
     * @return El curso encontrado, o null si no existe
     */
    public Curso findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Curso.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Busca un curso por su nombre.
     * Útil para validar duplicados al crear o editar.
     *
     * @param nombre El nombre del curso
     * @return El curso encontrado, o null si no existe
     */
    public Curso findByNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Curso> query = em.createQuery(
                "SELECT c FROM Curso c WHERE c.nombre = :nombre",
                Curso.class
            );
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todos los cursos ordenados alfabéticamente.
     *
     * @return Lista con todos los cursos
     */
    public List<Curso> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Curso> query = em.createQuery(
                "SELECT c FROM Curso c ORDER BY c.nombre",
                Curso.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ---- UPDATE ----

    /**
     * Actualiza los datos de un curso existente.
     *
     * @param curso El curso con los datos actualizados
     * @throws NonExistentEntityException Si el curso no existe en la BD
     */
    public void edit(Curso curso) throws NonExistentEntityException {
        if (findById(curso.getId()) == null) {
            throw new NonExistentEntityException(
                "No existe ningún curso con id: " + curso.getId()
            );
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(curso);
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
     * Elimina un curso de la BD por su ID.
     * Ojo: fallará si el curso tiene alumnos asociados (integridad referencial).
     * Esa validación la haremos en el Servlet antes de llamar a este método.
     *
     * @param id El ID del curso a eliminar
     * @throws NonExistentEntityException Si el curso no existe
     */
    public void destroy(int id) throws NonExistentEntityException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Curso curso = em.find(Curso.class, id);
            if (curso == null) {
                throw new NonExistentEntityException(
                    "No existe ningún curso con id: " + id
                );
            }
            em.remove(curso);
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
     * Cuenta el número total de cursos en la BD.
     *
     * @return El número de cursos
     */
    public int count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Curso c",
                Long.class
            );
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}
