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
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Visita;
import java.util.ArrayList;
import java.util.List;
import modelo_de_capas.logica.HogarTransito;
import modelo_de_capas.logica.TransporteHogarTransito;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class HogarTransitoJpaController implements Serializable {

    public HogarTransitoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public HogarTransitoJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HogarTransito hogarTransito) {
        if (hogarTransito.getListaVisita() == null) {
            hogarTransito.setListaVisita(new ArrayList<Visita>());
        }
        if (hogarTransito.getListaTransporteHogarTransito() == null) {
            hogarTransito.setListaTransporteHogarTransito(new ArrayList<TransporteHogarTransito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario voluntario = hogarTransito.getVoluntario();
            if (voluntario != null) {
                voluntario = em.getReference(voluntario.getClass(), voluntario.getId());
                hogarTransito.setVoluntario(voluntario);
            }
            List<Visita> attachedListaVisita = new ArrayList<Visita>();
            for (Visita listaVisitaVisitaToAttach : hogarTransito.getListaVisita()) {
                listaVisitaVisitaToAttach = em.getReference(listaVisitaVisitaToAttach.getClass(), listaVisitaVisitaToAttach.getId());
                attachedListaVisita.add(listaVisitaVisitaToAttach);
            }
            hogarTransito.setListaVisita(attachedListaVisita);
            List<TransporteHogarTransito> attachedListaTransporteHogarTransito = new ArrayList<TransporteHogarTransito>();
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransitoToAttach : hogarTransito.getListaTransporteHogarTransito()) {
                listaTransporteHogarTransitoTransporteHogarTransitoToAttach = em.getReference(listaTransporteHogarTransitoTransporteHogarTransitoToAttach.getClass(), listaTransporteHogarTransitoTransporteHogarTransitoToAttach.getId());
                attachedListaTransporteHogarTransito.add(listaTransporteHogarTransitoTransporteHogarTransitoToAttach);
            }
            hogarTransito.setListaTransporteHogarTransito(attachedListaTransporteHogarTransito);
            em.persist(hogarTransito);
            if (voluntario != null) {
                voluntario.getListaHogarTransito().add(hogarTransito);
                voluntario = em.merge(voluntario);
            }
            for (Visita listaVisitaVisita : hogarTransito.getListaVisita()) {
                HogarTransito oldHogartransitoOfListaVisitaVisita = listaVisitaVisita.getHogartransito();
                listaVisitaVisita.setHogartransito(hogarTransito);
                listaVisitaVisita = em.merge(listaVisitaVisita);
                if (oldHogartransitoOfListaVisitaVisita != null) {
                    oldHogartransitoOfListaVisitaVisita.getListaVisita().remove(listaVisitaVisita);
                    oldHogartransitoOfListaVisitaVisita = em.merge(oldHogartransitoOfListaVisitaVisita);
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransito : hogarTransito.getListaTransporteHogarTransito()) {
                HogarTransito oldHogartransitoOfListaTransporteHogarTransitoTransporteHogarTransito = listaTransporteHogarTransitoTransporteHogarTransito.getHogartransito();
                listaTransporteHogarTransitoTransporteHogarTransito.setHogartransito(hogarTransito);
                listaTransporteHogarTransitoTransporteHogarTransito = em.merge(listaTransporteHogarTransitoTransporteHogarTransito);
                if (oldHogartransitoOfListaTransporteHogarTransitoTransporteHogarTransito != null) {
                    oldHogartransitoOfListaTransporteHogarTransitoTransporteHogarTransito.getListaTransporteHogarTransito().remove(listaTransporteHogarTransitoTransporteHogarTransito);
                    oldHogartransitoOfListaTransporteHogarTransitoTransporteHogarTransito = em.merge(oldHogartransitoOfListaTransporteHogarTransitoTransporteHogarTransito);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HogarTransito hogarTransito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HogarTransito persistentHogarTransito = em.find(HogarTransito.class, hogarTransito.getId());
            Voluntario voluntarioOld = persistentHogarTransito.getVoluntario();
            Voluntario voluntarioNew = hogarTransito.getVoluntario();
            List<Visita> listaVisitaOld = persistentHogarTransito.getListaVisita();
            List<Visita> listaVisitaNew = hogarTransito.getListaVisita();
            List<TransporteHogarTransito> listaTransporteHogarTransitoOld = persistentHogarTransito.getListaTransporteHogarTransito();
            List<TransporteHogarTransito> listaTransporteHogarTransitoNew = hogarTransito.getListaTransporteHogarTransito();
            if (voluntarioNew != null) {
                voluntarioNew = em.getReference(voluntarioNew.getClass(), voluntarioNew.getId());
                hogarTransito.setVoluntario(voluntarioNew);
            }
            List<Visita> attachedListaVisitaNew = new ArrayList<Visita>();
            for (Visita listaVisitaNewVisitaToAttach : listaVisitaNew) {
                listaVisitaNewVisitaToAttach = em.getReference(listaVisitaNewVisitaToAttach.getClass(), listaVisitaNewVisitaToAttach.getId());
                attachedListaVisitaNew.add(listaVisitaNewVisitaToAttach);
            }
            listaVisitaNew = attachedListaVisitaNew;
            hogarTransito.setListaVisita(listaVisitaNew);
            List<TransporteHogarTransito> attachedListaTransporteHogarTransitoNew = new ArrayList<TransporteHogarTransito>();
            for (TransporteHogarTransito listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach : listaTransporteHogarTransitoNew) {
                listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach = em.getReference(listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach.getClass(), listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach.getId());
                attachedListaTransporteHogarTransitoNew.add(listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach);
            }
            listaTransporteHogarTransitoNew = attachedListaTransporteHogarTransitoNew;
            hogarTransito.setListaTransporteHogarTransito(listaTransporteHogarTransitoNew);
            hogarTransito = em.merge(hogarTransito);
            if (voluntarioOld != null && !voluntarioOld.equals(voluntarioNew)) {
                voluntarioOld.getListaHogarTransito().remove(hogarTransito);
                voluntarioOld = em.merge(voluntarioOld);
            }
            if (voluntarioNew != null && !voluntarioNew.equals(voluntarioOld)) {
                voluntarioNew.getListaHogarTransito().add(hogarTransito);
                voluntarioNew = em.merge(voluntarioNew);
            }
            for (Visita listaVisitaOldVisita : listaVisitaOld) {
                if (!listaVisitaNew.contains(listaVisitaOldVisita)) {
                    listaVisitaOldVisita.setHogartransito(null);
                    listaVisitaOldVisita = em.merge(listaVisitaOldVisita);
                }
            }
            for (Visita listaVisitaNewVisita : listaVisitaNew) {
                if (!listaVisitaOld.contains(listaVisitaNewVisita)) {
                    HogarTransito oldHogartransitoOfListaVisitaNewVisita = listaVisitaNewVisita.getHogartransito();
                    listaVisitaNewVisita.setHogartransito(hogarTransito);
                    listaVisitaNewVisita = em.merge(listaVisitaNewVisita);
                    if (oldHogartransitoOfListaVisitaNewVisita != null && !oldHogartransitoOfListaVisitaNewVisita.equals(hogarTransito)) {
                        oldHogartransitoOfListaVisitaNewVisita.getListaVisita().remove(listaVisitaNewVisita);
                        oldHogartransitoOfListaVisitaNewVisita = em.merge(oldHogartransitoOfListaVisitaNewVisita);
                    }
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoOldTransporteHogarTransito : listaTransporteHogarTransitoOld) {
                if (!listaTransporteHogarTransitoNew.contains(listaTransporteHogarTransitoOldTransporteHogarTransito)) {
                    listaTransporteHogarTransitoOldTransporteHogarTransito.setHogartransito(null);
                    listaTransporteHogarTransitoOldTransporteHogarTransito = em.merge(listaTransporteHogarTransitoOldTransporteHogarTransito);
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoNewTransporteHogarTransito : listaTransporteHogarTransitoNew) {
                if (!listaTransporteHogarTransitoOld.contains(listaTransporteHogarTransitoNewTransporteHogarTransito)) {
                    HogarTransito oldHogartransitoOfListaTransporteHogarTransitoNewTransporteHogarTransito = listaTransporteHogarTransitoNewTransporteHogarTransito.getHogartransito();
                    listaTransporteHogarTransitoNewTransporteHogarTransito.setHogartransito(hogarTransito);
                    listaTransporteHogarTransitoNewTransporteHogarTransito = em.merge(listaTransporteHogarTransitoNewTransporteHogarTransito);
                    if (oldHogartransitoOfListaTransporteHogarTransitoNewTransporteHogarTransito != null && !oldHogartransitoOfListaTransporteHogarTransitoNewTransporteHogarTransito.equals(hogarTransito)) {
                        oldHogartransitoOfListaTransporteHogarTransitoNewTransporteHogarTransito.getListaTransporteHogarTransito().remove(listaTransporteHogarTransitoNewTransporteHogarTransito);
                        oldHogartransitoOfListaTransporteHogarTransitoNewTransporteHogarTransito = em.merge(oldHogartransitoOfListaTransporteHogarTransitoNewTransporteHogarTransito);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = hogarTransito.getId();
                if (findHogarTransito(id) == null) {
                    throw new NonexistentEntityException("The hogarTransito with id " + id + " no longer exists.");
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
            HogarTransito hogarTransito;
            try {
                hogarTransito = em.getReference(HogarTransito.class, id);
                hogarTransito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hogarTransito with id " + id + " no longer exists.", enfe);
            }
            Voluntario voluntario = hogarTransito.getVoluntario();
            if (voluntario != null) {
                voluntario.getListaHogarTransito().remove(hogarTransito);
                voluntario = em.merge(voluntario);
            }
            List<Visita> listaVisita = hogarTransito.getListaVisita();
            for (Visita listaVisitaVisita : listaVisita) {
                listaVisitaVisita.setHogartransito(null);
                listaVisitaVisita = em.merge(listaVisitaVisita);
            }
            List<TransporteHogarTransito> listaTransporteHogarTransito = hogarTransito.getListaTransporteHogarTransito();
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransito : listaTransporteHogarTransito) {
                listaTransporteHogarTransitoTransporteHogarTransito.setHogartransito(null);
                listaTransporteHogarTransitoTransporteHogarTransito = em.merge(listaTransporteHogarTransitoTransporteHogarTransito);
            }
            em.remove(hogarTransito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HogarTransito> findHogarTransitoEntities() {
        return findHogarTransitoEntities(true, -1, -1);
    }

    public List<HogarTransito> findHogarTransitoEntities(int maxResults, int firstResult) {
        return findHogarTransitoEntities(false, maxResults, firstResult);
    }

    private List<HogarTransito> findHogarTransitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HogarTransito.class));
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

    public HogarTransito findHogarTransito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HogarTransito.class, id);
        } finally {
            em.close();
        }
    }

    public int getHogarTransitoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HogarTransito> rt = cq.from(HogarTransito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
    