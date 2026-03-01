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
import modelo_de_capas.logica.Administrador;
import modelo_de_capas.logica.Gato;
import java.util.ArrayList;
import java.util.List;
import modelo_de_capas.logica.Colonia;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class ColoniaJpaController implements Serializable {

    public ColoniaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public ColoniaJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Colonia colonia) {
        if (colonia.getListaGato() == null) {
            colonia.setListaGato(new ArrayList<Gato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Administrador administrador = colonia.getAdministrador();
            if (administrador != null) {
                administrador = em.getReference(administrador.getClass(), administrador.getId());
                colonia.setAdministrador(administrador);
            }
            List<Gato> attachedListaGato = new ArrayList<Gato>();
            for (Gato listaGatoGatoToAttach : colonia.getListaGato()) {
                listaGatoGatoToAttach = em.getReference(listaGatoGatoToAttach.getClass(), listaGatoGatoToAttach.getId());
                attachedListaGato.add(listaGatoGatoToAttach);
            }
            colonia.setListaGato(attachedListaGato);
            em.persist(colonia);
            if (administrador != null) {
                administrador.getListaColonia().add(colonia);
                administrador = em.merge(administrador);
            }
            for (Gato listaGatoGato : colonia.getListaGato()) {
                Colonia oldColoniaOfListaGatoGato = listaGatoGato.getColonia();
                listaGatoGato.setColonia(colonia);
                listaGatoGato = em.merge(listaGatoGato);
                if (oldColoniaOfListaGatoGato != null) {
                    oldColoniaOfListaGatoGato.getListaGato().remove(listaGatoGato);
                    oldColoniaOfListaGatoGato = em.merge(oldColoniaOfListaGatoGato);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Colonia colonia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Colonia persistentColonia = em.find(Colonia.class, colonia.getId());
            Administrador administradorOld = persistentColonia.getAdministrador();
            Administrador administradorNew = colonia.getAdministrador();
            List<Gato> listaGatoOld = persistentColonia.getListaGato();
            List<Gato> listaGatoNew = colonia.getListaGato();
            if (administradorNew != null) {
                administradorNew = em.getReference(administradorNew.getClass(), administradorNew.getId());
                colonia.setAdministrador(administradorNew);
            }
            List<Gato> attachedListaGatoNew = new ArrayList<Gato>();
            for (Gato listaGatoNewGatoToAttach : listaGatoNew) {
                listaGatoNewGatoToAttach = em.getReference(listaGatoNewGatoToAttach.getClass(), listaGatoNewGatoToAttach.getId());
                attachedListaGatoNew.add(listaGatoNewGatoToAttach);
            }
            listaGatoNew = attachedListaGatoNew;
            colonia.setListaGato(listaGatoNew);
            colonia = em.merge(colonia);
            if (administradorOld != null && !administradorOld.equals(administradorNew)) {
                administradorOld.getListaColonia().remove(colonia);
                administradorOld = em.merge(administradorOld);
            }
            if (administradorNew != null && !administradorNew.equals(administradorOld)) {
                administradorNew.getListaColonia().add(colonia);
                administradorNew = em.merge(administradorNew);
            }
            for (Gato listaGatoOldGato : listaGatoOld) {
                if (!listaGatoNew.contains(listaGatoOldGato)) {
                    listaGatoOldGato.setColonia(null);
                    listaGatoOldGato = em.merge(listaGatoOldGato);
                }
            }
            for (Gato listaGatoNewGato : listaGatoNew) {
                if (!listaGatoOld.contains(listaGatoNewGato)) {
                    Colonia oldColoniaOfListaGatoNewGato = listaGatoNewGato.getColonia();
                    listaGatoNewGato.setColonia(colonia);
                    listaGatoNewGato = em.merge(listaGatoNewGato);
                    if (oldColoniaOfListaGatoNewGato != null && !oldColoniaOfListaGatoNewGato.equals(colonia)) {
                        oldColoniaOfListaGatoNewGato.getListaGato().remove(listaGatoNewGato);
                        oldColoniaOfListaGatoNewGato = em.merge(oldColoniaOfListaGatoNewGato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = colonia.getId();
                if (findColonia(id) == null) {
                    throw new NonexistentEntityException("The colonia with id " + id + " no longer exists.");
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
            Colonia colonia;
            try {
                colonia = em.getReference(Colonia.class, id);
                colonia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The colonia with id " + id + " no longer exists.", enfe);
            }
            Administrador administrador = colonia.getAdministrador();
            if (administrador != null) {
                administrador.getListaColonia().remove(colonia);
                administrador = em.merge(administrador);
            }
            List<Gato> listaGato = colonia.getListaGato();
            for (Gato listaGatoGato : listaGato) {
                listaGatoGato.setColonia(null);
                listaGatoGato = em.merge(listaGatoGato);
            }
            em.remove(colonia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Colonia> findColoniaEntities() {
        return findColoniaEntities(true, -1, -1);
    }

    public List<Colonia> findColoniaEntities(int maxResults, int firstResult) {
        return findColoniaEntities(false, maxResults, firstResult);
    }

    private List<Colonia> findColoniaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Colonia.class));
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

    public Colonia findColonia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Colonia.class, id);
        } finally {
            em.close();
        }
    }

    public int getColoniaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Colonia> rt = cq.from(Colonia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
