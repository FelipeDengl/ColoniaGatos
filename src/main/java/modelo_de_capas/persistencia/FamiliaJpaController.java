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
import modelo_de_capas.logica.Visita;
import java.util.ArrayList;
import java.util.List;
import modelo_de_capas.logica.Postulacion;
import modelo_de_capas.logica.Asignacion;
import modelo_de_capas.logica.Familia;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class FamiliaJpaController implements Serializable {

    public FamiliaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public FamiliaJpaController() {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Familia familia) {
        if (familia.getListaVisita() == null) {
            familia.setListaVisita(new ArrayList<Visita>());
        }
        if (familia.getListaPostulacion() == null) {
            familia.setListaPostulacion(new ArrayList<Postulacion>());
        }
        if (familia.getListaAsignacion() == null) {
            familia.setListaAsignacion(new ArrayList<Asignacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Visita> attachedListaVisita = new ArrayList<Visita>();
            for (Visita listaVisitaVisitaToAttach : familia.getListaVisita()) {
                listaVisitaVisitaToAttach = em.getReference(listaVisitaVisitaToAttach.getClass(), listaVisitaVisitaToAttach.getId());
                attachedListaVisita.add(listaVisitaVisitaToAttach);
            }
            familia.setListaVisita(attachedListaVisita);
            List<Postulacion> attachedListaPostulacion = new ArrayList<Postulacion>();
            for (Postulacion listaPostulacionPostulacionToAttach : familia.getListaPostulacion()) {
                listaPostulacionPostulacionToAttach = em.getReference(listaPostulacionPostulacionToAttach.getClass(), listaPostulacionPostulacionToAttach.getId());
                attachedListaPostulacion.add(listaPostulacionPostulacionToAttach);
            }
            familia.setListaPostulacion(attachedListaPostulacion);
            List<Asignacion> attachedListaAsignacion = new ArrayList<Asignacion>();
            for (Asignacion listaAsignacionAsignacionToAttach : familia.getListaAsignacion()) {
                listaAsignacionAsignacionToAttach = em.getReference(listaAsignacionAsignacionToAttach.getClass(), listaAsignacionAsignacionToAttach.getId());
                attachedListaAsignacion.add(listaAsignacionAsignacionToAttach);
            }
            familia.setListaAsignacion(attachedListaAsignacion);
            em.persist(familia);
            for (Visita listaVisitaVisita : familia.getListaVisita()) {
                Familia oldFamiliaOfListaVisitaVisita = listaVisitaVisita.getFamilia();
                listaVisitaVisita.setFamilia(familia);
                listaVisitaVisita = em.merge(listaVisitaVisita);
                if (oldFamiliaOfListaVisitaVisita != null) {
                    oldFamiliaOfListaVisitaVisita.getListaVisita().remove(listaVisitaVisita);
                    oldFamiliaOfListaVisitaVisita = em.merge(oldFamiliaOfListaVisitaVisita);
                }
            }
            for (Postulacion listaPostulacionPostulacion : familia.getListaPostulacion()) {
                Familia oldFamiliaOfListaPostulacionPostulacion = listaPostulacionPostulacion.getFamilia();
                listaPostulacionPostulacion.setFamilia(familia);
                listaPostulacionPostulacion = em.merge(listaPostulacionPostulacion);
                if (oldFamiliaOfListaPostulacionPostulacion != null) {
                    oldFamiliaOfListaPostulacionPostulacion.getListaPostulacion().remove(listaPostulacionPostulacion);
                    oldFamiliaOfListaPostulacionPostulacion = em.merge(oldFamiliaOfListaPostulacionPostulacion);
                }
            }
            for (Asignacion listaAsignacionAsignacion : familia.getListaAsignacion()) {
                Familia oldFamiliaOfListaAsignacionAsignacion = listaAsignacionAsignacion.getFamilia();
                listaAsignacionAsignacion.setFamilia(familia);
                listaAsignacionAsignacion = em.merge(listaAsignacionAsignacion);
                if (oldFamiliaOfListaAsignacionAsignacion != null) {
                    oldFamiliaOfListaAsignacionAsignacion.getListaAsignacion().remove(listaAsignacionAsignacion);
                    oldFamiliaOfListaAsignacionAsignacion = em.merge(oldFamiliaOfListaAsignacionAsignacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Familia familia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Familia persistentFamilia = em.find(Familia.class, familia.getId());
            List<Visita> listaVisitaOld = persistentFamilia.getListaVisita();
            List<Visita> listaVisitaNew = familia.getListaVisita();
            List<Postulacion> listaPostulacionOld = persistentFamilia.getListaPostulacion();
            List<Postulacion> listaPostulacionNew = familia.getListaPostulacion();
            List<Asignacion> listaAsignacionOld = persistentFamilia.getListaAsignacion();
            List<Asignacion> listaAsignacionNew = familia.getListaAsignacion();
            List<Visita> attachedListaVisitaNew = new ArrayList<Visita>();
            for (Visita listaVisitaNewVisitaToAttach : listaVisitaNew) {
                listaVisitaNewVisitaToAttach = em.getReference(listaVisitaNewVisitaToAttach.getClass(), listaVisitaNewVisitaToAttach.getId());
                attachedListaVisitaNew.add(listaVisitaNewVisitaToAttach);
            }
            listaVisitaNew = attachedListaVisitaNew;
            familia.setListaVisita(listaVisitaNew);
            List<Postulacion> attachedListaPostulacionNew = new ArrayList<Postulacion>();
            for (Postulacion listaPostulacionNewPostulacionToAttach : listaPostulacionNew) {
                listaPostulacionNewPostulacionToAttach = em.getReference(listaPostulacionNewPostulacionToAttach.getClass(), listaPostulacionNewPostulacionToAttach.getId());
                attachedListaPostulacionNew.add(listaPostulacionNewPostulacionToAttach);
            }
            listaPostulacionNew = attachedListaPostulacionNew;
            familia.setListaPostulacion(listaPostulacionNew);
            List<Asignacion> attachedListaAsignacionNew = new ArrayList<Asignacion>();
            for (Asignacion listaAsignacionNewAsignacionToAttach : listaAsignacionNew) {
                listaAsignacionNewAsignacionToAttach = em.getReference(listaAsignacionNewAsignacionToAttach.getClass(), listaAsignacionNewAsignacionToAttach.getId());
                attachedListaAsignacionNew.add(listaAsignacionNewAsignacionToAttach);
            }
            listaAsignacionNew = attachedListaAsignacionNew;
            familia.setListaAsignacion(listaAsignacionNew);
            familia = em.merge(familia);
            for (Visita listaVisitaOldVisita : listaVisitaOld) {
                if (!listaVisitaNew.contains(listaVisitaOldVisita)) {
                    listaVisitaOldVisita.setFamilia(null);
                    listaVisitaOldVisita = em.merge(listaVisitaOldVisita);
                }
            }
            for (Visita listaVisitaNewVisita : listaVisitaNew) {
                if (!listaVisitaOld.contains(listaVisitaNewVisita)) {
                    Familia oldFamiliaOfListaVisitaNewVisita = listaVisitaNewVisita.getFamilia();
                    listaVisitaNewVisita.setFamilia(familia);
                    listaVisitaNewVisita = em.merge(listaVisitaNewVisita);
                    if (oldFamiliaOfListaVisitaNewVisita != null && !oldFamiliaOfListaVisitaNewVisita.equals(familia)) {
                        oldFamiliaOfListaVisitaNewVisita.getListaVisita().remove(listaVisitaNewVisita);
                        oldFamiliaOfListaVisitaNewVisita = em.merge(oldFamiliaOfListaVisitaNewVisita);
                    }
                }
            }
            for (Postulacion listaPostulacionOldPostulacion : listaPostulacionOld) {
                if (!listaPostulacionNew.contains(listaPostulacionOldPostulacion)) {
                    listaPostulacionOldPostulacion.setFamilia(null);
                    listaPostulacionOldPostulacion = em.merge(listaPostulacionOldPostulacion);
                }
            }
            for (Postulacion listaPostulacionNewPostulacion : listaPostulacionNew) {
                if (!listaPostulacionOld.contains(listaPostulacionNewPostulacion)) {
                    Familia oldFamiliaOfListaPostulacionNewPostulacion = listaPostulacionNewPostulacion.getFamilia();
                    listaPostulacionNewPostulacion.setFamilia(familia);
                    listaPostulacionNewPostulacion = em.merge(listaPostulacionNewPostulacion);
                    if (oldFamiliaOfListaPostulacionNewPostulacion != null && !oldFamiliaOfListaPostulacionNewPostulacion.equals(familia)) {
                        oldFamiliaOfListaPostulacionNewPostulacion.getListaPostulacion().remove(listaPostulacionNewPostulacion);
                        oldFamiliaOfListaPostulacionNewPostulacion = em.merge(oldFamiliaOfListaPostulacionNewPostulacion);
                    }
                }
            }
            for (Asignacion listaAsignacionOldAsignacion : listaAsignacionOld) {
                if (!listaAsignacionNew.contains(listaAsignacionOldAsignacion)) {
                    listaAsignacionOldAsignacion.setFamilia(null);
                    listaAsignacionOldAsignacion = em.merge(listaAsignacionOldAsignacion);
                }
            }
            for (Asignacion listaAsignacionNewAsignacion : listaAsignacionNew) {
                if (!listaAsignacionOld.contains(listaAsignacionNewAsignacion)) {
                    Familia oldFamiliaOfListaAsignacionNewAsignacion = listaAsignacionNewAsignacion.getFamilia();
                    listaAsignacionNewAsignacion.setFamilia(familia);
                    listaAsignacionNewAsignacion = em.merge(listaAsignacionNewAsignacion);
                    if (oldFamiliaOfListaAsignacionNewAsignacion != null && !oldFamiliaOfListaAsignacionNewAsignacion.equals(familia)) {
                        oldFamiliaOfListaAsignacionNewAsignacion.getListaAsignacion().remove(listaAsignacionNewAsignacion);
                        oldFamiliaOfListaAsignacionNewAsignacion = em.merge(oldFamiliaOfListaAsignacionNewAsignacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = familia.getId();
                if (findFamilia(id) == null) {
                    throw new NonexistentEntityException("The familia with id " + id + " no longer exists.");
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
            Familia familia;
            try {
                familia = em.getReference(Familia.class, id);
                familia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The familia with id " + id + " no longer exists.", enfe);
            }
            List<Visita> listaVisita = familia.getListaVisita();
            for (Visita listaVisitaVisita : listaVisita) {
                listaVisitaVisita.setFamilia(null);
                listaVisitaVisita = em.merge(listaVisitaVisita);
            }
            List<Postulacion> listaPostulacion = familia.getListaPostulacion();
            for (Postulacion listaPostulacionPostulacion : listaPostulacion) {
                listaPostulacionPostulacion.setFamilia(null);
                listaPostulacionPostulacion = em.merge(listaPostulacionPostulacion);
            }
            List<Asignacion> listaAsignacion = familia.getListaAsignacion();
            for (Asignacion listaAsignacionAsignacion : listaAsignacion) {
                listaAsignacionAsignacion.setFamilia(null);
                listaAsignacionAsignacion = em.merge(listaAsignacionAsignacion);
            }
            em.remove(familia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Familia> findFamiliaEntities() {
        return findFamiliaEntities(true, -1, -1);
    }

    public List<Familia> findFamiliaEntities(int maxResults, int firstResult) {
        return findFamiliaEntities(false, maxResults, firstResult);
    }

    private List<Familia> findFamiliaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Familia.class));
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

    public Familia findFamilia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Familia.class, id);
        } finally {
            em.close();
        }
    }

    public int getFamiliaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Familia> rt = cq.from(Familia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
