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
import modelo_de_capas.logica.Familia;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.Postulacion;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class PostulacionJpaController implements Serializable {

    public PostulacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public PostulacionJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Postulacion postulacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia familia = postulacion.getFamilia();
            if (familia != null) {
                familia = em.getReference(familia.getClass(), familia.getId());
                postulacion.setFamilia(familia);
            }
            Gato gato = postulacion.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                postulacion.setGato(gato);
            }
            em.persist(postulacion);
            if (familia != null) {
                familia.getListaPostulacion().add(postulacion);
                familia = em.merge(familia);
            }
            if (gato != null) {
                gato.getListaPostulacion().add(postulacion);
                gato = em.merge(gato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Postulacion postulacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Postulacion persistentPostulacion = em.find(Postulacion.class, postulacion.getId());
            Familia familiaOld = persistentPostulacion.getFamilia();
            Familia familiaNew = postulacion.getFamilia();
            Gato gatoOld = persistentPostulacion.getGato();
            Gato gatoNew = postulacion.getGato();
            if (familiaNew != null) {
                familiaNew = em.getReference(familiaNew.getClass(), familiaNew.getId());
                postulacion.setFamilia(familiaNew);
            }
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                postulacion.setGato(gatoNew);
            }
            postulacion = em.merge(postulacion);
            if (familiaOld != null && !familiaOld.equals(familiaNew)) {
                familiaOld.getListaPostulacion().remove(postulacion);
                familiaOld = em.merge(familiaOld);
            }
            if (familiaNew != null && !familiaNew.equals(familiaOld)) {
                familiaNew.getListaPostulacion().add(postulacion);
                familiaNew = em.merge(familiaNew);
            }
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaPostulacion().remove(postulacion);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaPostulacion().add(postulacion);
                gatoNew = em.merge(gatoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = postulacion.getId();
                if (findPostulacion(id) == null) {
                    throw new NonexistentEntityException("The postulacion with id " + id + " no longer exists.");
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
            Postulacion postulacion;
            try {
                postulacion = em.getReference(Postulacion.class, id);
                postulacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The postulacion with id " + id + " no longer exists.", enfe);
            }
            Familia familia = postulacion.getFamilia();
            if (familia != null) {
                familia.getListaPostulacion().remove(postulacion);
                familia = em.merge(familia);
            }
            Gato gato = postulacion.getGato();
            if (gato != null) {
                gato.getListaPostulacion().remove(postulacion);
                gato = em.merge(gato);
            }
            em.remove(postulacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Postulacion> findPostulacionEntities() {
        return findPostulacionEntities(true, -1, -1);
    }

    public List<Postulacion> findPostulacionEntities(int maxResults, int firstResult) {
        return findPostulacionEntities(false, maxResults, firstResult);
    }

    private List<Postulacion> findPostulacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Postulacion.class));
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

    public Postulacion findPostulacion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Postulacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPostulacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Postulacion> rt = cq.from(Postulacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
