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
import modelo_de_capas.logica.HistorialMedico;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class HistorialMedicoJpaController implements Serializable {

    public HistorialMedicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public HistorialMedicoJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistorialMedico historialMedico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(historialMedico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistorialMedico historialMedico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            historialMedico = em.merge(historialMedico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = historialMedico.getId();
                if (findHistorialMedico(id) == null) {
                    throw new NonexistentEntityException("The historialMedico with id " + id + " no longer exists.");
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
            HistorialMedico historialMedico;
            try {
                historialMedico = em.getReference(HistorialMedico.class, id);
                historialMedico.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialMedico with id " + id + " no longer exists.", enfe);
            }
            em.remove(historialMedico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistorialMedico> findHistorialMedicoEntities() {
        return findHistorialMedicoEntities(true, -1, -1);
    }

    public List<HistorialMedico> findHistorialMedicoEntities(int maxResults, int firstResult) {
        return findHistorialMedicoEntities(false, maxResults, firstResult);
    }

    private List<HistorialMedico> findHistorialMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistorialMedico.class));
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

    public HistorialMedico findHistorialMedico(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistorialMedico.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistorialMedico> rt = cq.from(HistorialMedico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
