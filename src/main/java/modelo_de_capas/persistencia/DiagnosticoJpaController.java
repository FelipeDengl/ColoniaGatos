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
import modelo_de_capas.logica.Estudio;
import java.util.ArrayList;
import java.util.List;
import modelo_de_capas.logica.Diagnostico;
import modelo_de_capas.logica.Tratamiento;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class DiagnosticoJpaController implements Serializable {

    public DiagnosticoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public DiagnosticoJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Diagnostico diagnostico) {
        if (diagnostico.getListaEstudio() == null) {
            diagnostico.setListaEstudio(new ArrayList<Estudio>());
        }
        if (diagnostico.getListaTratamiento() == null) {
            diagnostico.setListaTratamiento(new ArrayList<Tratamiento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudio> attachedListaEstudio = new ArrayList<Estudio>();
            for (Estudio listaEstudioEstudioToAttach : diagnostico.getListaEstudio()) {
                listaEstudioEstudioToAttach = em.getReference(listaEstudioEstudioToAttach.getClass(), listaEstudioEstudioToAttach.getId());
                attachedListaEstudio.add(listaEstudioEstudioToAttach);
            }
            diagnostico.setListaEstudio(attachedListaEstudio);
            List<Tratamiento> attachedListaTratamiento = new ArrayList<Tratamiento>();
            for (Tratamiento listaTratamientoTratamientoToAttach : diagnostico.getListaTratamiento()) {
                listaTratamientoTratamientoToAttach = em.getReference(listaTratamientoTratamientoToAttach.getClass(), listaTratamientoTratamientoToAttach.getId());
                attachedListaTratamiento.add(listaTratamientoTratamientoToAttach);
            }
            diagnostico.setListaTratamiento(attachedListaTratamiento);
            em.persist(diagnostico);
            for (Estudio listaEstudioEstudio : diagnostico.getListaEstudio()) {
                Diagnostico oldDiagnosticoOfListaEstudioEstudio = listaEstudioEstudio.getDiagnostico();
                listaEstudioEstudio.setDiagnostico(diagnostico);
                listaEstudioEstudio = em.merge(listaEstudioEstudio);
                if (oldDiagnosticoOfListaEstudioEstudio != null) {
                    oldDiagnosticoOfListaEstudioEstudio.getListaEstudio().remove(listaEstudioEstudio);
                    oldDiagnosticoOfListaEstudioEstudio = em.merge(oldDiagnosticoOfListaEstudioEstudio);
                }
            }
            for (Tratamiento listaTratamientoTratamiento : diagnostico.getListaTratamiento()) {
                Diagnostico oldDiagnosticoOfListaTratamientoTratamiento = listaTratamientoTratamiento.getDiagnostico();
                listaTratamientoTratamiento.setDiagnostico(diagnostico);
                listaTratamientoTratamiento = em.merge(listaTratamientoTratamiento);
                if (oldDiagnosticoOfListaTratamientoTratamiento != null) {
                    oldDiagnosticoOfListaTratamientoTratamiento.getListaTratamiento().remove(listaTratamientoTratamiento);
                    oldDiagnosticoOfListaTratamientoTratamiento = em.merge(oldDiagnosticoOfListaTratamientoTratamiento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Diagnostico diagnostico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Diagnostico persistentDiagnostico = em.find(Diagnostico.class, diagnostico.getId());
            List<Estudio> listaEstudioOld = persistentDiagnostico.getListaEstudio();
            List<Estudio> listaEstudioNew = diagnostico.getListaEstudio();
            List<Tratamiento> listaTratamientoOld = persistentDiagnostico.getListaTratamiento();
            List<Tratamiento> listaTratamientoNew = diagnostico.getListaTratamiento();
            List<Estudio> attachedListaEstudioNew = new ArrayList<Estudio>();
            for (Estudio listaEstudioNewEstudioToAttach : listaEstudioNew) {
                listaEstudioNewEstudioToAttach = em.getReference(listaEstudioNewEstudioToAttach.getClass(), listaEstudioNewEstudioToAttach.getId());
                attachedListaEstudioNew.add(listaEstudioNewEstudioToAttach);
            }
            listaEstudioNew = attachedListaEstudioNew;
            diagnostico.setListaEstudio(listaEstudioNew);
            List<Tratamiento> attachedListaTratamientoNew = new ArrayList<Tratamiento>();
            for (Tratamiento listaTratamientoNewTratamientoToAttach : listaTratamientoNew) {
                listaTratamientoNewTratamientoToAttach = em.getReference(listaTratamientoNewTratamientoToAttach.getClass(), listaTratamientoNewTratamientoToAttach.getId());
                attachedListaTratamientoNew.add(listaTratamientoNewTratamientoToAttach);
            }
            listaTratamientoNew = attachedListaTratamientoNew;
            diagnostico.setListaTratamiento(listaTratamientoNew);
            diagnostico = em.merge(diagnostico);
            for (Estudio listaEstudioOldEstudio : listaEstudioOld) {
                if (!listaEstudioNew.contains(listaEstudioOldEstudio)) {
                    listaEstudioOldEstudio.setDiagnostico(null);
                    listaEstudioOldEstudio = em.merge(listaEstudioOldEstudio);
                }
            }
            for (Estudio listaEstudioNewEstudio : listaEstudioNew) {
                if (!listaEstudioOld.contains(listaEstudioNewEstudio)) {
                    Diagnostico oldDiagnosticoOfListaEstudioNewEstudio = listaEstudioNewEstudio.getDiagnostico();
                    listaEstudioNewEstudio.setDiagnostico(diagnostico);
                    listaEstudioNewEstudio = em.merge(listaEstudioNewEstudio);
                    if (oldDiagnosticoOfListaEstudioNewEstudio != null && !oldDiagnosticoOfListaEstudioNewEstudio.equals(diagnostico)) {
                        oldDiagnosticoOfListaEstudioNewEstudio.getListaEstudio().remove(listaEstudioNewEstudio);
                        oldDiagnosticoOfListaEstudioNewEstudio = em.merge(oldDiagnosticoOfListaEstudioNewEstudio);
                    }
                }
            }
            for (Tratamiento listaTratamientoOldTratamiento : listaTratamientoOld) {
                if (!listaTratamientoNew.contains(listaTratamientoOldTratamiento)) {
                    listaTratamientoOldTratamiento.setDiagnostico(null);
                    listaTratamientoOldTratamiento = em.merge(listaTratamientoOldTratamiento);
                }
            }
            for (Tratamiento listaTratamientoNewTratamiento : listaTratamientoNew) {
                if (!listaTratamientoOld.contains(listaTratamientoNewTratamiento)) {
                    Diagnostico oldDiagnosticoOfListaTratamientoNewTratamiento = listaTratamientoNewTratamiento.getDiagnostico();
                    listaTratamientoNewTratamiento.setDiagnostico(diagnostico);
                    listaTratamientoNewTratamiento = em.merge(listaTratamientoNewTratamiento);
                    if (oldDiagnosticoOfListaTratamientoNewTratamiento != null && !oldDiagnosticoOfListaTratamientoNewTratamiento.equals(diagnostico)) {
                        oldDiagnosticoOfListaTratamientoNewTratamiento.getListaTratamiento().remove(listaTratamientoNewTratamiento);
                        oldDiagnosticoOfListaTratamientoNewTratamiento = em.merge(oldDiagnosticoOfListaTratamientoNewTratamiento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = diagnostico.getId();
                if (findDiagnostico(id) == null) {
                    throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.");
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
            Diagnostico diagnostico;
            try {
                diagnostico = em.getReference(Diagnostico.class, id);
                diagnostico.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diagnostico with id " + id + " no longer exists.", enfe);
            }
            List<Estudio> listaEstudio = diagnostico.getListaEstudio();
            for (Estudio listaEstudioEstudio : listaEstudio) {
                listaEstudioEstudio.setDiagnostico(null);
                listaEstudioEstudio = em.merge(listaEstudioEstudio);
            }
            List<Tratamiento> listaTratamiento = diagnostico.getListaTratamiento();
            for (Tratamiento listaTratamientoTratamiento : listaTratamiento) {
                listaTratamientoTratamiento.setDiagnostico(null);
                listaTratamientoTratamiento = em.merge(listaTratamientoTratamiento);
            }
            em.remove(diagnostico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Diagnostico> findDiagnosticoEntities() {
        return findDiagnosticoEntities(true, -1, -1);
    }

    public List<Diagnostico> findDiagnosticoEntities(int maxResults, int firstResult) {
        return findDiagnosticoEntities(false, maxResults, firstResult);
    }

    private List<Diagnostico> findDiagnosticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diagnostico.class));
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

    public Diagnostico findDiagnostico(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diagnostico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiagnosticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diagnostico> rt = cq.from(Diagnostico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
