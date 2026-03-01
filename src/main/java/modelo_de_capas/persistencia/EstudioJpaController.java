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
import modelo_de_capas.logica.Diagnostico;
import modelo_de_capas.logica.Estudio;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class EstudioJpaController implements Serializable {

    public EstudioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EstudioJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudio estudio) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Diagnostico diagnostico = estudio.getDiagnostico();
            if (diagnostico != null) {
                diagnostico = em.getReference(diagnostico.getClass(), diagnostico.getId());
                estudio.setDiagnostico(diagnostico);
            }
            em.persist(estudio);
            if (diagnostico != null) {
                diagnostico.getListaEstudio().add(estudio);
                diagnostico = em.merge(diagnostico);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudio estudio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudio persistentEstudio = em.find(Estudio.class, estudio.getId());
            Diagnostico diagnosticoOld = persistentEstudio.getDiagnostico();
            Diagnostico diagnosticoNew = estudio.getDiagnostico();
            if (diagnosticoNew != null) {
                diagnosticoNew = em.getReference(diagnosticoNew.getClass(), diagnosticoNew.getId());
                estudio.setDiagnostico(diagnosticoNew);
            }
            estudio = em.merge(estudio);
            if (diagnosticoOld != null && !diagnosticoOld.equals(diagnosticoNew)) {
                diagnosticoOld.getListaEstudio().remove(estudio);
                diagnosticoOld = em.merge(diagnosticoOld);
            }
            if (diagnosticoNew != null && !diagnosticoNew.equals(diagnosticoOld)) {
                diagnosticoNew.getListaEstudio().add(estudio);
                diagnosticoNew = em.merge(diagnosticoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = estudio.getId();
                if (findEstudio(id) == null) {
                    throw new NonexistentEntityException("The estudio with id " + id + " no longer exists.");
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
            Estudio estudio;
            try {
                estudio = em.getReference(Estudio.class, id);
                estudio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudio with id " + id + " no longer exists.", enfe);
            }
            Diagnostico diagnostico = estudio.getDiagnostico();
            if (diagnostico != null) {
                diagnostico.getListaEstudio().remove(estudio);
                diagnostico = em.merge(diagnostico);
            }
            em.remove(estudio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estudio> findEstudioEntities() {
        return findEstudioEntities(true, -1, -1);
    }

    public List<Estudio> findEstudioEntities(int maxResults, int firstResult) {
        return findEstudioEntities(false, maxResults, firstResult);
    }

    private List<Estudio> findEstudioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudio.class));
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

    public Estudio findEstudio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudio> rt = cq.from(Estudio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
