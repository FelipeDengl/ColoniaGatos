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
import modelo_de_capas.logica.CapturaCastracion;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class CapturaCastracionJpaController implements Serializable {

    public CapturaCastracionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public CapturaCastracionJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CapturaCastracion capturaCastracion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = capturaCastracion.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                capturaCastracion.setVoluntario(voluntario);
            }
            Gato gato = capturaCastracion.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                capturaCastracion.setGato(gato);
            }
            em.persist(capturaCastracion);
            if (voluntario != null) {
                voluntario.getListaCapturaCastracion().add(capturaCastracion);
                voluntario = em.merge(voluntario);
            }
            if (gato != null) {
                gato.getListaCapturaCastracion().add(capturaCastracion);
                gato = em.merge(gato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CapturaCastracion capturaCastracion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CapturaCastracion persistentCapturaCastracion = em.find(CapturaCastracion.class, capturaCastracion.getId());
            Voluntario voluntarioOld = persistentCapturaCastracion.getVoluntario();
            Voluntario voluntarioNew = capturaCastracion.getVoluntario();
            Gato gatoOld = persistentCapturaCastracion.getGato();
            Gato gatoNew = capturaCastracion.getGato();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                capturaCastracion.setVoluntario(voluntarioNew);
            }
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                capturaCastracion.setGato(gatoNew);
            }
            capturaCastracion = em.merge(capturaCastracion);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaCapturaCastracion().remove(capturaCastracion);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaCapturaCastracion().add(capturaCastracion);
                voluntarioNew = em.merge(voluntarioNew);
            }
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaCapturaCastracion().remove(capturaCastracion);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaCapturaCastracion().add(capturaCastracion);
                gatoNew = em.merge(gatoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = capturaCastracion.getId();
                if (findCapturaCastracion(id) == null) {
                    throw new NonexistentEntityException("The capturaCastracion with id " + id + " no longer exists.");
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
            CapturaCastracion capturaCastracion;
            try {
                capturaCastracion = em.getReference(CapturaCastracion.class, id);
                capturaCastracion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The capturaCastracion with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = capturaCastracion.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaCapturaCastracion().remove(capturaCastracion);
                voluntario = em.merge(voluntario);
            }
            Gato gato = capturaCastracion.getGato();
            if (gato != null) {
                gato.getListaCapturaCastracion().remove(capturaCastracion);
                gato = em.merge(gato);
            }
            em.remove(capturaCastracion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CapturaCastracion> findCapturaCastracionEntities() {
        return findCapturaCastracionEntities(true, -1, -1);
    }

    public List<CapturaCastracion> findCapturaCastracionEntities(int maxResults, int firstResult) {
        return findCapturaCastracionEntities(false, maxResults, firstResult);
    }

    private List<CapturaCastracion> findCapturaCastracionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CapturaCastracion.class));
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

    public CapturaCastracion findCapturaCastracion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CapturaCastracion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCapturaCastracionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CapturaCastracion> rt = cq.from(CapturaCastracion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
