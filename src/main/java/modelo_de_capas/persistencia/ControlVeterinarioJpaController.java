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
import modelo_de_capas.logica.ControlVeterinario;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class ControlVeterinarioJpaController implements Serializable {

    public ControlVeterinarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ControlVeterinarioJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ControlVeterinario controlVeterinario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = controlVeterinario.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                controlVeterinario.setVoluntario(voluntario);
            }
            Gato gato = controlVeterinario.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                controlVeterinario.setGato(gato);
            }
            em.persist(controlVeterinario);
            if (voluntario != null) {
                voluntario.getListaControlVeterinario().add(controlVeterinario);
                voluntario = em.merge(voluntario);
            }
            if (gato != null) {
                gato.getListaControlVeterinario().add(controlVeterinario);
                gato = em.merge(gato);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ControlVeterinario controlVeterinario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ControlVeterinario persistentControlVeterinario = em.find(ControlVeterinario.class, controlVeterinario.getId());
            Voluntario voluntarioOld = persistentControlVeterinario.getVoluntario();
            Voluntario voluntarioNew = controlVeterinario.getVoluntario();
            Gato gatoOld = persistentControlVeterinario.getGato();
            Gato gatoNew = controlVeterinario.getGato();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                controlVeterinario.setVoluntario(voluntarioNew);
            }
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                controlVeterinario.setGato(gatoNew);
            }
            controlVeterinario = em.merge(controlVeterinario);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaControlVeterinario().remove(controlVeterinario);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaControlVeterinario().add(controlVeterinario);
                voluntarioNew = em.merge(voluntarioNew);
            }
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaControlVeterinario().remove(controlVeterinario);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaControlVeterinario().add(controlVeterinario);
                gatoNew = em.merge(gatoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = controlVeterinario.getId();
                if (findControlVeterinario(id) == null) {
                    throw new NonexistentEntityException("The controlVeterinario with id " + id + " no longer exists.");
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
            ControlVeterinario controlVeterinario;
            try {
                controlVeterinario = em.getReference(ControlVeterinario.class, id);
                controlVeterinario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The controlVeterinario with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = controlVeterinario.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaControlVeterinario().remove(controlVeterinario);
                voluntario = em.merge(voluntario);
            }
            Gato gato = controlVeterinario.getGato();
            if (gato != null) {
                gato.getListaControlVeterinario().remove(controlVeterinario);
                gato = em.merge(gato);
            }
            em.remove(controlVeterinario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ControlVeterinario> findControlVeterinarioEntities() {
        return findControlVeterinarioEntities(true, -1, -1);
    }

    public List<ControlVeterinario> findControlVeterinarioEntities(int maxResults, int firstResult) {
        return findControlVeterinarioEntities(false, maxResults, firstResult);
    }

    private List<ControlVeterinario> findControlVeterinarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ControlVeterinario.class));
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

    public ControlVeterinario findControlVeterinario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ControlVeterinario.class, id);
        } finally {
            em.close();
        }
    }

    public int getControlVeterinarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ControlVeterinario> rt = cq.from(ControlVeterinario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
