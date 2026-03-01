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
import modelo_de_capas.logica.Alimentacion;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class AlimentacionJpaController implements Serializable {

    public AlimentacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public AlimentacionJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alimentacion alimentacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = alimentacion.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                alimentacion.setVoluntario(voluntario);
            }
            Gato gato = alimentacion.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                alimentacion.setGato(gato);
            }
            em.persist(alimentacion);
            if (voluntario != null) {
                voluntario.getListaAlimentacion().add(alimentacion);
                voluntario = em.merge(voluntario);
            }
            if (gato != null) {
                gato.getListaAlimentacion().add(alimentacion);
                gato = em.merge(gato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alimentacion alimentacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alimentacion persistentAlimentacion = em.find(Alimentacion.class, alimentacion.getId());
            Voluntario voluntarioOld = persistentAlimentacion.getVoluntario();
            Voluntario voluntarioNew = alimentacion.getVoluntario();
            Gato gatoOld = persistentAlimentacion.getGato();
            Gato gatoNew = alimentacion.getGato();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                alimentacion.setVoluntario(voluntarioNew);
            }
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                alimentacion.setGato(gatoNew);
            }
            alimentacion = em.merge(alimentacion);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaAlimentacion().remove(alimentacion);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaAlimentacion().add(alimentacion);
                voluntarioNew = em.merge(voluntarioNew);
            }
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaAlimentacion().remove(alimentacion);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaAlimentacion().add(alimentacion);
                gatoNew = em.merge(gatoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = alimentacion.getId();
                if (findAlimentacion(id) == null) {
                    throw new NonexistentEntityException("The alimentacion with id " + id + " no longer exists.");
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
            Alimentacion alimentacion;
            try {
                alimentacion = em.getReference(Alimentacion.class, id);
                alimentacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alimentacion with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = alimentacion.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaAlimentacion().remove(alimentacion);
                voluntario = em.merge(voluntario);
            }
            Gato gato = alimentacion.getGato();
            if (gato != null) {
                gato.getListaAlimentacion().remove(alimentacion);
                gato = em.merge(gato);
            }
            em.remove(alimentacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alimentacion> findAlimentacionEntities() {
        return findAlimentacionEntities(true, -1, -1);
    }

    public List<Alimentacion> findAlimentacionEntities(int maxResults, int firstResult) {
        return findAlimentacionEntities(false, maxResults, firstResult);
    }

    private List<Alimentacion> findAlimentacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alimentacion.class));
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

    public Alimentacion findAlimentacion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alimentacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlimentacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alimentacion> rt = cq.from(Alimentacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
