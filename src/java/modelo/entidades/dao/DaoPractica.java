/*
 * Clase DAO (Data Access Object) Practica - CRUD Practica
 * Gestiona todas las operaciones de acceso a la BD relacionadas con prácticas.
 * Incluye métodos específicos para las estadísticas y para verificar
 * si un alumno ya tiene una práctica asignada.
 */
package modelo.entidades.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import modelo.entidades.Alumno;
import modelo.entidades.Empresa;
import modelo.entidades.Practica;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.entidades.dao.exceptions.PreexistingEntityException;
import modelo.util.JPAUtil;

/**
 *
 * @author SGame
 */
public class DaoPractica {

    // ---- CREATE ----

    /**
     * Inserta una nueva práctica en la BD.
     * Verifica que el alumno no tenga ya una práctica activa.
     *
     * @param practica La práctica a insertar
     * @throws PreexistingEntityException Si el alumno ya tiene una práctica
     */
    public void create(Practica practica) throws PreexistingEntityException {
        // Un alumno solo puede tener una práctica activa
        if (findByAlumno(practica.getAlumno()) != null) {
            throw new PreexistingEntityException(
                "El alumno " + practica.getAlumno().getNombreCompleto() +
                " ya tiene una práctica asignada."
            );
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(practica);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error completo en la consola
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ---- READ ----

    /**
     * Busca una práctica por su ID.
     *
     * @param id El ID de la práctica
     * @return La práctica encontrada, o null si no existe
     */
    public Practica findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Practica.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Busca la práctica de un alumno concreto.
     * Como un alumno solo puede tener una práctica, devuelve un único objeto.
     *
     * @param alumno El alumno cuya práctica queremos obtener
     * @return La práctica del alumno, o null si no tiene ninguna
     */
    public Practica findByAlumno(Alumno alumno) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Practica> query = em.createQuery(
                "SELECT p FROM Practica p WHERE p.alumno = :alumno",
                Practica.class
            );
            query.setParameter("alumno", alumno);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error completo en la consola
            return null; // El alumno no tiene práctica asignada
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todas las prácticas de una empresa concreta.
     * Útil para las estadísticas de alumnos por empresa.
     *
     * @param empresa La empresa cuyas prácticas queremos obtener
     * @return Lista de prácticas de esa empresa
     */
    public List<Practica> findByEmpresa(Empresa empresa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Practica> query = em.createQuery(
                "SELECT p FROM Practica p WHERE p.empresa = :empresa",
                Practica.class
            );
            query.setParameter("empresa", empresa);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todas las prácticas ordenadas por fecha de inicio.
     *
     * @return Lista con todas las prácticas
     */
    public List<Practica> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Practica> query = em.createQuery(
                "SELECT p FROM Practica p ORDER BY p.fechaInicio DESC",
                Practica.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta el número de alumnos en prácticas por empresa.
     * Devuelve una lista de arrays Object[] donde:
     * - [0] es el nombre de la empresa (String)
     * - [1] es el número de prácticas (Long)
     * Usado para las estadísticas y gráficas.
     *
     * @return Lista de pares [nombreEmpresa, cantidadAlumnos]
     */
    public List<Object[]> countAlumnosPorEmpresa() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT p.empresa.nombre, COUNT(p) FROM Practica p " +
                "GROUP BY p.empresa.nombre ORDER BY COUNT(p) DESC",
                Object[].class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta el número de alumnos en prácticas por curso.
     * Devuelve una lista de arrays Object[] donde:
     * - [0] es el nombre del curso (String)
     * - [1] es el número de prácticas (Long)
     * Usado para las estadísticas y gráficas.
     *
     * @return Lista de pares [nombreCurso, cantidadAlumnos]
     */
    public List<Object[]> countAlumnosPorCurso() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT p.alumno.curso.nombre, COUNT(p) FROM Practica p " +
                "GROUP BY p.alumno.curso.nombre ORDER BY p.alumno.curso.nombre",
                Object[].class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ---- UPDATE ----

    /**
     * Actualiza los datos de una práctica existente.
     * Principalmente usado para añadir o editar comentarios.
     *
     * @param practica La práctica con los datos actualizados
     * @throws NonExistentEntityException Si la práctica no existe en la BD
     */
    public void edit(Practica practica) throws NonExistentEntityException {
        if (findById(practica.getId()) == null) {
            throw new NonExistentEntityException(
                "No existe ninguna práctica con id: " + practica.getId()
            );
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(practica);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error completo en la consola
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ---- DELETE ----

    /**
     * Elimina una práctica de la BD por su ID.
     *
     * @param id El ID de la práctica a eliminar
     * @throws NonExistentEntityException Si la práctica no existe
     */
    public void destroy(int id) throws NonExistentEntityException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Practica practica = em.find(Practica.class, id);
            if (practica == null) {
                throw new NonExistentEntityException(
                    "No existe ninguna práctica con id: " + id
                );
            }
            em.remove(practica);
            em.getTransaction().commit();
        } catch (NonExistentEntityException e) {
            e.printStackTrace(); // Muestra el error completo en la consola
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error completo en la consola
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ---- UTILIDADES ----

    /**
     * Cuenta el número total de prácticas en la BD.
     *
     * @return El número de prácticas
     */
    public int count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Practica p",
                Long.class
            );
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}
