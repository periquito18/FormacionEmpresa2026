/*
 * Clase DAO (Data Access Object) Empresa - CRUD Empresa
 * Gestiona todas las operaciones de acceso a la BD relacionadas con empresas
 */
package modelo.entidades.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import modelo.entidades.Empresa;
import modelo.entidades.dao.exceptions.NonExistentEntityException;
import modelo.util.JPAUtil;

/**
 *
 * @author SGame
 */
public class DaoEmpresa {

    // ---- CREATE ----
    /**
     * Inserta una nueva empresa en la BD.
     *
     * @param empresa La empresa a insertar
     */
    public void create(Empresa empresa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(empresa);
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
     * Busca una empresa por su ID.
     *
     * @param id El ID de la empresa
     * @return La empresa encontrada, o null si no existe
     */
    public Empresa findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve todas las empresas ordenadas alfabéticamente por nombre.
     *
     * @return Lista con todas las empresas
     */
    public List<Empresa> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Empresa> query = em.createQuery(
                    "SELECT e FROM Empresa e ORDER BY e.nombre",
                    Empresa.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ---- UPDATE ----
    /**
     * Actualiza los datos de una empresa existente.
     *
     * @param empresa La empresa con los datos actualizados
     * @throws NonExistentEntityException Si la empresa no existe en la BD
     */
    public void edit(Empresa empresa) throws NonExistentEntityException {
        if (findById(empresa.getId()) == null) {
            throw new NonExistentEntityException(
                    "No existe ninguna empresa con id: " + empresa.getId()
            );
        }
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(empresa);
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
     * Elimina una empresa de la BD por su ID. Fallará si la empresa tiene
     * prácticas asociadas (integridad referencial). Validar en el Servlet antes
     * de llamar a este método.
     *
     * @param id El ID de la empresa a eliminar
     * @throws NonExistentEntityException Si la empresa no existe
     */
    public void destroy(int id) throws NonExistentEntityException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Empresa empresa = em.find(Empresa.class, id);
            if (empresa == null) {
                throw new NonExistentEntityException(
                        "No existe ninguna empresa con id: " + id
                );
            }
            em.remove(empresa);
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
     * Cuenta el número total de empresas en la BD.
     *
     * @return El número de empresas
     */
    public int count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM Empresa e",
                    Long.class
            );
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}
