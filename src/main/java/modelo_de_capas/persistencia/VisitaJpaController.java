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
import modelo_de_capas.logica.Familia;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.HogarTransito;
import modelo_de_capas.logica.Visita;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class VisitaJpaController implements Serializable {

    public VisitaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public VisitaJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Visita visita) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = visita.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                visita.setVoluntario(voluntario);
            }
            Familia familia = visita.getFamilia();
            if (familia != null) {
                familia = em.getReference(familia.getClass(), familia.getId());
                visita.setFamilia(familia);
            }
            Gato gato = visita.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                visita.setGato(gato);
            }
            HogarTransito hogartransito = visita.getHogartransito();
            if (hogartransito != null) {
                hogartransito = em.getReference(hogartransito.getClass(), hogartransito.getId());
                visita.setHogartransito(hogartransito);
            }
            em.persist(visita);
            if (voluntario != null) {
                voluntario.getListaVisitas().add(visita);
                voluntario = em.merge(voluntario);
            }
            if (familia != null) {
                familia.getListaVisita().add(visita);
                familia = em.merge(familia);
            }
            if (gato != null) {
                gato.getListaVisita().add(visita);
                gato = em.merge(gato);
            }
            if (hogartransito != null) {
                hogartransito.getListaVisita().add(visita);
                hogartransito = em.merge(hogartransito);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Visita visita) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Visita persistentVisita = em.find(Visita.class, visita.getId());
            Voluntario voluntarioOld = persistentVisita.getVoluntario();
            Voluntario voluntarioNew = visita.getVoluntario();
            Familia familiaOld = persistentVisita.getFamilia();
            Familia familiaNew = visita.getFamilia();
            Gato gatoOld = persistentVisita.getGato();
            Gato gatoNew = visita.getGato();
            HogarTransito hogartransitoOld = persistentVisita.getHogartransito();
            HogarTransito hogartransitoNew = visita.getHogartransito();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                visita.setVoluntario(voluntarioNew);
            }
            if (familiaNew != null) {
                familiaNew = em.getReference(familiaNew.getClass(), familiaNew.getId());
                visita.setFamilia(familiaNew);
            }
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                visita.setGato(gatoNew);
            }
            if (hogartransitoNew != null) {
                hogartransitoNew = em.getReference(hogartransitoNew.getClass(), hogartransitoNew.getId());
                visita.setHogartransito(hogartransitoNew);
            }
            visita = em.merge(visita);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaVisitas().remove(visita);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaVisitas().add(visita);
                voluntarioNew = em.merge(voluntarioNew);
            }
            if (familiaOld != null && !familiaOld.equals(familiaNew)) {
                familiaOld.getListaVisita().remove(visita);
                familiaOld = em.merge(familiaOld);
            }
            if (familiaNew != null && !familiaNew.equals(familiaOld)) {
                familiaNew.getListaVisita().add(visita);
                familiaNew = em.merge(familiaNew);
            }
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaVisita().remove(visita);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaVisita().add(visita);
                gatoNew = em.merge(gatoNew);
            }
            if (hogartransitoOld != null && !hogartransitoOld.equals(hogartransitoNew)) {
                hogartransitoOld.getListaVisita().remove(visita);
                hogartransitoOld = em.merge(hogartransitoOld);
            }
            if (hogartransitoNew != null && !hogartransitoNew.equals(hogartransitoOld)) {
                hogartransitoNew.getListaVisita().add(visita);
                hogartransitoNew = em.merge(hogartransitoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = visita.getId();
                if (findVisita(id) == null) {
                    throw new NonexistentEntityException("The visita with id " + id + " no longer exists.");
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
            Visita visita;
            try {
                visita = em.getReference(Visita.class, id);
                visita.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The visita with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = visita.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaVisitas().remove(visita);
                voluntario = em.merge(voluntario);
            }
            Familia familia = visita.getFamilia();
            if (familia != null) {
                familia.getListaVisita().remove(visita);
                familia = em.merge(familia);
            }
            Gato gato = visita.getGato();
            if (gato != null) {
                gato.getListaVisita().remove(visita);
                gato = em.merge(gato);
            }
            HogarTransito hogartransito = visita.getHogartransito();
            if (hogartransito != null) {
                hogartransito.getListaVisita().remove(visita);
                hogartransito = em.merge(hogartransito);
            }
            em.remove(visita);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Visita> findVisitaEntities() {
        return findVisitaEntities(true, -1, -1);
    }

    public List<Visita> findVisitaEntities(int maxResults, int firstResult) {
        return findVisitaEntities(false, maxResults, firstResult);
    }

    private List<Visita> findVisitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Visita.class));
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

    public Visita findVisita(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Visita.class, id);
        } finally {
            em.close();
        }
    }

    public int getVisitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Visita> rt = cq.from(Visita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
