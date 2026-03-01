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
import modelo_de_capas.logica.HogarTransito;
import modelo_de_capas.logica.TransporteHogarTransito;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class TransporteHogarTransitoJpaController implements Serializable {

    public TransporteHogarTransitoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public TransporteHogarTransitoJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TransporteHogarTransito transporteHogarTransito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = transporteHogarTransito.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                transporteHogarTransito.setVoluntario(voluntario);
            }
            Gato gato = transporteHogarTransito.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                transporteHogarTransito.setGato(gato);
            }
            HogarTransito hogartransito = transporteHogarTransito.getHogartransito();
            if (hogartransito != null) {
                hogartransito = em.getReference(hogartransito.getClass(), hogartransito.getId());
                transporteHogarTransito.setHogartransito(hogartransito);
            }
            em.persist(transporteHogarTransito);
            if (voluntario != null) {
                voluntario.getListaTransporteHogarTransito().add(transporteHogarTransito);
                voluntario = em.merge(voluntario);
            }
            if (gato != null) {
                gato.getListaTransporteHogarTransito().add(transporteHogarTransito);
                gato = em.merge(gato);
            }
            if (hogartransito != null) {
                hogartransito.getListaTransporteHogarTransito().add(transporteHogarTransito);
                hogartransito = em.merge(hogartransito);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TransporteHogarTransito transporteHogarTransito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TransporteHogarTransito persistentTransporteHogarTransito = em.find(TransporteHogarTransito.class, transporteHogarTransito.getId());
            Voluntario voluntarioOld = persistentTransporteHogarTransito.getVoluntario();
            Voluntario voluntarioNew = transporteHogarTransito.getVoluntario();
            Gato gatoOld = persistentTransporteHogarTransito.getGato();
            Gato gatoNew = transporteHogarTransito.getGato();
            HogarTransito hogartransitoOld = persistentTransporteHogarTransito.getHogartransito();
            HogarTransito hogartransitoNew = transporteHogarTransito.getHogartransito();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                transporteHogarTransito.setVoluntario(voluntarioNew);
            }
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                transporteHogarTransito.setGato(gatoNew);
            }
            if (hogartransitoNew != null) {
                hogartransitoNew = em.getReference(hogartransitoNew.getClass(), hogartransitoNew.getId());
                transporteHogarTransito.setHogartransito(hogartransitoNew);
            }
            transporteHogarTransito = em.merge(transporteHogarTransito);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaTransporteHogarTransito().remove(transporteHogarTransito);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaTransporteHogarTransito().add(transporteHogarTransito);
                voluntarioNew = em.merge(voluntarioNew);
            }
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaTransporteHogarTransito().remove(transporteHogarTransito);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaTransporteHogarTransito().add(transporteHogarTransito);
                gatoNew = em.merge(gatoNew);
            }
            if (hogartransitoOld != null && !hogartransitoOld.equals(hogartransitoNew)) {
                hogartransitoOld.getListaTransporteHogarTransito().remove(transporteHogarTransito);
                hogartransitoOld = em.merge(hogartransitoOld);
            }
            if (hogartransitoNew != null && !hogartransitoNew.equals(hogartransitoOld)) {
                hogartransitoNew.getListaTransporteHogarTransito().add(transporteHogarTransito);
                hogartransitoNew = em.merge(hogartransitoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = transporteHogarTransito.getId();
                if (findTransporteHogarTransito(id) == null) {
                    throw new NonexistentEntityException("The transporteHogarTransito with id " + id + " no longer exists.");
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
            TransporteHogarTransito transporteHogarTransito;
            try {
                transporteHogarTransito = em.getReference(TransporteHogarTransito.class, id);
                transporteHogarTransito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transporteHogarTransito with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = transporteHogarTransito.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaTransporteHogarTransito().remove(transporteHogarTransito);
                voluntario = em.merge(voluntario);
            }
            Gato gato = transporteHogarTransito.getGato();
            if (gato != null) {
                gato.getListaTransporteHogarTransito().remove(transporteHogarTransito);
                gato = em.merge(gato);
            }
            HogarTransito hogartransito = transporteHogarTransito.getHogartransito();
            if (hogartransito != null) {
                hogartransito.getListaTransporteHogarTransito().remove(transporteHogarTransito);
                hogartransito = em.merge(hogartransito);
            }
            em.remove(transporteHogarTransito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TransporteHogarTransito> findTransporteHogarTransitoEntities() {
        return findTransporteHogarTransitoEntities(true, -1, -1);
    }

    public List<TransporteHogarTransito> findTransporteHogarTransitoEntities(int maxResults, int firstResult) {
        return findTransporteHogarTransitoEntities(false, maxResults, firstResult);
    }

    private List<TransporteHogarTransito> findTransporteHogarTransitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TransporteHogarTransito.class));
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

    public TransporteHogarTransito findTransporteHogarTransito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TransporteHogarTransito.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransporteHogarTransitoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TransporteHogarTransito> rt = cq.from(TransporteHogarTransito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
