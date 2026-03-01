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
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.PuntoAvistamiento;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class PuntoAvistamientoJpaController implements Serializable {

    public PuntoAvistamientoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public PuntoAvistamientoJpaController() {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PuntoAvistamiento puntoAvistamiento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = puntoAvistamiento.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                puntoAvistamiento.setVoluntario(voluntario);
            }
            Gato gato = puntoAvistamiento.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                puntoAvistamiento.setGato(gato);
            }
            em.persist(puntoAvistamiento);
            if (voluntario != null) {
                voluntario.getListaPuntoAvistamiento().add(puntoAvistamiento);
                voluntario = em.merge(voluntario);
            }
            if (gato != null) {
                gato.getListaPuntoAvistamiento().add(puntoAvistamiento);
                gato = em.merge(gato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PuntoAvistamiento puntoAvistamiento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuntoAvistamiento persistentPuntoAvistamiento = em.find(PuntoAvistamiento.class, puntoAvistamiento.getId());
            Voluntario voluntarioOld = persistentPuntoAvistamiento.getVoluntario();
            Voluntario voluntarioNew = puntoAvistamiento.getVoluntario();
            Gato gatoOld = persistentPuntoAvistamiento.getGato();
            Gato gatoNew = puntoAvistamiento.getGato();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                puntoAvistamiento.setVoluntario(voluntarioNew);
            }
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                puntoAvistamiento.setGato(gatoNew);
            }
            puntoAvistamiento = em.merge(puntoAvistamiento);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaPuntoAvistamiento().remove(puntoAvistamiento);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaPuntoAvistamiento().add(puntoAvistamiento);
                voluntarioNew = em.merge(voluntarioNew);
            }
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaPuntoAvistamiento().remove(puntoAvistamiento);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaPuntoAvistamiento().add(puntoAvistamiento);
                gatoNew = em.merge(gatoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = puntoAvistamiento.getId();
                if (findPuntoAvistamiento(id) == null) {
                    throw new NonexistentEntityException("The puntoAvistamiento with id " + id + " no longer exists.");
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
            PuntoAvistamiento puntoAvistamiento;
            try {
                puntoAvistamiento = em.getReference(PuntoAvistamiento.class, id);
                puntoAvistamiento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puntoAvistamiento with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = puntoAvistamiento.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaPuntoAvistamiento().remove(puntoAvistamiento);
                voluntario = em.merge(voluntario);
            }
            Gato gato = puntoAvistamiento.getGato();
            if (gato != null) {
                gato.getListaPuntoAvistamiento().remove(puntoAvistamiento);
                gato = em.merge(gato);
            }
            em.remove(puntoAvistamiento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PuntoAvistamiento> findPuntoAvistamientoEntities() {
        return findPuntoAvistamientoEntities(true, -1, -1);
    }

    public List<PuntoAvistamiento> findPuntoAvistamientoEntities(int maxResults, int firstResult) {
        return findPuntoAvistamientoEntities(false, maxResults, firstResult);
    }

    private List<PuntoAvistamiento> findPuntoAvistamientoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PuntoAvistamiento.class));
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

    public PuntoAvistamiento findPuntoAvistamiento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PuntoAvistamiento.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuntoAvistamientoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PuntoAvistamiento> rt = cq.from(PuntoAvistamiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
