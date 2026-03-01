/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo_de_capas.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import modelo_de_capas.logica.Asignacion;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Familia;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class AsignacionJpaController implements Serializable {

    public AsignacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public AsignacionJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asignacion asignacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = asignacion.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                asignacion.setVoluntario(voluntario);
            }
            Familia familia = asignacion.getFamilia();
            if (familia != null) {
                familia = em.getReference(familia.getClass(), familia.getId());
                asignacion.setFamilia(familia);
            }
            em.persist(asignacion);
            if (voluntario != null) {
                voluntario.getListaAsignacion().add(asignacion);
                voluntario = em.merge(voluntario);
            }
            if (familia != null) {
                familia.getListaAsignacion().add(asignacion);
                familia = em.merge(familia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asignacion asignacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignacion persistentAsignacion = em.find(Asignacion.class, asignacion.getId());
            Voluntario voluntarioOld = persistentAsignacion.getVoluntario();
            Voluntario voluntarioNew = asignacion.getVoluntario();
            Familia familiaOld = persistentAsignacion.getFamilia();
            Familia familiaNew = asignacion.getFamilia();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                asignacion.setVoluntario(voluntarioNew);
            }
            if (familiaNew != null) {
                familiaNew = em.getReference(familiaNew.getClass(), familiaNew.getId());
                asignacion.setFamilia(familiaNew);
            }
            asignacion = em.merge(asignacion);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaAsignacion().remove(asignacion);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaAsignacion().add(asignacion);
                voluntarioNew = em.merge(voluntarioNew);
            }
            if (familiaOld != null && !familiaOld.equals(familiaNew)) {
                familiaOld.getListaAsignacion().remove(asignacion);
                familiaOld = em.merge(familiaOld);
            }
            if (familiaNew != null && !familiaNew.equals(familiaOld)) {
                familiaNew.getListaAsignacion().add(asignacion);
                familiaNew = em.merge(familiaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = asignacion.getId();
                if (findAsignacion(id) == null) {
                    throw new NonexistentEntityException("The asignacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asignacion asignacion;
            try {
                asignacion = em.getReference(Asignacion.class, id);
                asignacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asignacion with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = asignacion.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaAsignacion().remove(asignacion);
                voluntario = em.merge(voluntario);
            }
            Familia familia = asignacion.getFamilia();
            if (familia != null) {
                familia.getListaAsignacion().remove(asignacion);
                familia = em.merge(familia);
            }
            em.remove(asignacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asignacion> findAsignacionEntities() {
        return findAsignacionEntities(true, -1, -1);
    }

    public List<Asignacion> findAsignacionEntities(int maxResults, int firstResult) {
        return findAsignacionEntities(false, maxResults, firstResult);
    }

    private List<Asignacion> findAsignacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asignacion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Asignacion findAsignacion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asignacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsignacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asignacion> rt = cq.from(Asignacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
