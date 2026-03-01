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
import modelo_de_capas.logica.Colonia;
import java.util.ArrayList;
import java.util.List;
import modelo_de_capas.logica.Administrador;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class AdministradorJpaController implements Serializable {

    public AdministradorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public AdministradorJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrador administrador) {
        if (administrador.getListaColonia() == null) {
            administrador.setListaColonia(new ArrayList<Colonia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Colonia> attachedListaColonia = new ArrayList<Colonia>();
            for (Colonia listaColoniaColoniaToAttach : administrador.getListaColonia()) {
                listaColoniaColoniaToAttach = em.getReference(listaColoniaColoniaToAttach.getClass(), listaColoniaColoniaToAttach.getId());
                attachedListaColonia.add(listaColoniaColoniaToAttach);
            }
            administrador.setListaColonia(attachedListaColonia);
            em.persist(administrador);
            for (Colonia listaColoniaColonia : administrador.getListaColonia()) {
                Administrador oldAdministradorOfListaColoniaColonia = listaColoniaColonia.getAdministrador();
                listaColoniaColonia.setAdministrador(administrador);
                listaColoniaColonia = em.merge(listaColoniaColonia);
                if (oldAdministradorOfListaColoniaColonia != null) {
                    oldAdministradorOfListaColoniaColonia.getListaColonia().remove(listaColoniaColonia);
                    oldAdministradorOfListaColoniaColonia = em.merge(oldAdministradorOfListaColoniaColonia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Administrador administrador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrador persistentAdministrador = em.find(Administrador.class, administrador.getId());
            List<Colonia> listaColoniaOld = persistentAdministrador.getListaColonia();
            List<Colonia> listaColoniaNew = administrador.getListaColonia();
            List<Colonia> attachedListaColoniaNew = new ArrayList<Colonia>();
            for (Colonia listaColoniaNewColoniaToAttach : listaColoniaNew) {
                listaColoniaNewColoniaToAttach = em.getReference(listaColoniaNewColoniaToAttach.getClass(), listaColoniaNewColoniaToAttach.getId());
                attachedListaColoniaNew.add(listaColoniaNewColoniaToAttach);
            }
            listaColoniaNew = attachedListaColoniaNew;
            administrador.setListaColonia(listaColoniaNew);
            administrador = em.merge(administrador);
            for (Colonia listaColoniaOldColonia : listaColoniaOld) {
                if (!listaColoniaNew.contains(listaColoniaOldColonia)) {
                    listaColoniaOldColonia.setAdministrador(null);
                    listaColoniaOldColonia = em.merge(listaColoniaOldColonia);
                }
            }
            for (Colonia listaColoniaNewColonia : listaColoniaNew) {
                if (!listaColoniaOld.contains(listaColoniaNewColonia)) {
                    Administrador oldAdministradorOfListaColoniaNewColonia = listaColoniaNewColonia.getAdministrador();
                    listaColoniaNewColonia.setAdministrador(administrador);
                    listaColoniaNewColonia = em.merge(listaColoniaNewColonia);
                    if (oldAdministradorOfListaColoniaNewColonia != null && !oldAdministradorOfListaColoniaNewColonia.equals(administrador)) {
                        oldAdministradorOfListaColoniaNewColonia.getListaColonia().remove(listaColoniaNewColonia);
                        oldAdministradorOfListaColoniaNewColonia = em.merge(oldAdministradorOfListaColoniaNewColonia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = administrador.getId();
                if (findAdministrador(id) == null) {
                    throw new NonexistentEntityException("The administrador with id " + id + " no longer exists.");
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
            Administrador administrador;
            try {
                administrador = em.getReference(Administrador.class, id);
                administrador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The administrador with id " + id + " no longer exists.", enfe);
            }
            List<Colonia> listaColonia = administrador.getListaColonia();
            for (Colonia listaColoniaColonia : listaColonia) {
                listaColoniaColonia.setAdministrador(null);
                listaColoniaColonia = em.merge(listaColoniaColonia);
            }
            em.remove(administrador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Administrador> findAdministradorEntities() {
        return findAdministradorEntities(true, -1, -1);
    }

    public List<Administrador> findAdministradorEntities(int maxResults, int firstResult) {
        return findAdministradorEntities(false, maxResults, firstResult);
    }

    private List<Administrador> findAdministradorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Administrador.class));
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

    public Administrador findAdministrador(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Administrador.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdministradorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Administrador> rt = cq.from(Administrador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
