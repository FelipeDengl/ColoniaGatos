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
import modelo_de_capas.logica.Tratamiento;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class TratamientoJpaController implements Serializable {

    public TratamientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public TratamientoJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tratamiento tratamiento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Diagnostico diagnostico = tratamiento.getDiagnostico();
            if (diagnostico != null) {
                diagnostico = em.getReference(diagnostico.getClass(), diagnostico.getId());
                tratamiento.setDiagnostico(diagnostico);
            }
            em.persist(tratamiento);
            if (diagnostico != null) {
                diagnostico.getListaTratamiento().add(tratamiento);
                diagnostico = em.merge(diagnostico);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tratamiento tratamiento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tratamiento persistentTratamiento = em.find(Tratamiento.class, tratamiento.getId());
            Diagnostico diagnosticoOld = persistentTratamiento.getDiagnostico();
            Diagnostico diagnosticoNew = tratamiento.getDiagnostico();
            if (diagnosticoNew != null) {
                diagnosticoNew = em.getReference(diagnosticoNew.getClass(), diagnosticoNew.getId());
                tratamiento.setDiagnostico(diagnosticoNew);
            }
            tratamiento = em.merge(tratamiento);
            if (diagnosticoOld != null && !diagnosticoOld.equals(diagnosticoNew)) {
                diagnosticoOld.getListaTratamiento().remove(tratamiento);
                diagnosticoOld = em.merge(diagnosticoOld);
            }
            if (diagnosticoNew != null && !diagnosticoNew.equals(diagnosticoOld)) {
                diagnosticoNew.getListaTratamiento().add(tratamiento);
                diagnosticoNew = em.merge(diagnosticoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tratamiento.getId();
                if (findTratamiento(id) == null) {
                    throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.");
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
            Tratamiento tratamiento;
            try {
                tratamiento = em.getReference(Tratamiento.class, id);
                tratamiento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tratamiento with id " + id + " no longer exists.", enfe);
            }
            Diagnostico diagnostico = tratamiento.getDiagnostico();
            if (diagnostico != null) {
                diagnostico.getListaTratamiento().remove(tratamiento);
                diagnostico = em.merge(diagnostico);
            }
            em.remove(tratamiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tratamiento> findTratamientoEntities() {
        return findTratamientoEntities(true, -1, -1);
    }

    public List<Tratamiento> findTratamientoEntities(int maxResults, int firstResult) {
        return findTratamientoEntities(false, maxResults, firstResult);
    }

    private List<Tratamiento> findTratamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tratamiento.class));
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

    public Tratamiento findTratamiento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tratamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getTratamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tratamiento> rt = cq.from(Tratamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
