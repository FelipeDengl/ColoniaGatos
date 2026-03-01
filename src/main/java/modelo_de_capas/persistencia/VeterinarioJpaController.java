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
import modelo_de_capas.logica.CertificadoAdopcion;
import java.util.ArrayList;
import java.util.List;
import modelo_de_capas.logica.Veterinario;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class VeterinarioJpaController implements Serializable {

    public VeterinarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public VeterinarioJpaController() {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Veterinario veterinario) {
        if (veterinario.getListaCertificadoAdopcion() == null) {
            veterinario.setListaCertificadoAdopcion(new ArrayList<CertificadoAdopcion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CertificadoAdopcion> attachedListaCertificadoAdopcion = new ArrayList<CertificadoAdopcion>();
            for (CertificadoAdopcion listaCertificadoAdopcionCertificadoAdopcionToAttach : veterinario.getListaCertificadoAdopcion()) {
                listaCertificadoAdopcionCertificadoAdopcionToAttach = em.getReference(listaCertificadoAdopcionCertificadoAdopcionToAttach.getClass(), listaCertificadoAdopcionCertificadoAdopcionToAttach.getId());
                attachedListaCertificadoAdopcion.add(listaCertificadoAdopcionCertificadoAdopcionToAttach);
            }
            veterinario.setListaCertificadoAdopcion(attachedListaCertificadoAdopcion);
            em.persist(veterinario);
            for (CertificadoAdopcion listaCertificadoAdopcionCertificadoAdopcion : veterinario.getListaCertificadoAdopcion()) {
                Veterinario oldVeterinarioOfListaCertificadoAdopcionCertificadoAdopcion = listaCertificadoAdopcionCertificadoAdopcion.getVeterinario();
                listaCertificadoAdopcionCertificadoAdopcion.setVeterinario(veterinario);
                listaCertificadoAdopcionCertificadoAdopcion = em.merge(listaCertificadoAdopcionCertificadoAdopcion);
                if (oldVeterinarioOfListaCertificadoAdopcionCertificadoAdopcion != null) {
                    oldVeterinarioOfListaCertificadoAdopcionCertificadoAdopcion.getListaCertificadoAdopcion().remove(listaCertificadoAdopcionCertificadoAdopcion);
                    oldVeterinarioOfListaCertificadoAdopcionCertificadoAdopcion = em.merge(oldVeterinarioOfListaCertificadoAdopcionCertificadoAdopcion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Veterinario veterinario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Veterinario persistentVeterinario = em.find(Veterinario.class, veterinario.getId());
            List<CertificadoAdopcion> listaCertificadoAdopcionOld = persistentVeterinario.getListaCertificadoAdopcion();
            List<CertificadoAdopcion> listaCertificadoAdopcionNew = veterinario.getListaCertificadoAdopcion();
            List<CertificadoAdopcion> attachedListaCertificadoAdopcionNew = new ArrayList<CertificadoAdopcion>();
            for (CertificadoAdopcion listaCertificadoAdopcionNewCertificadoAdopcionToAttach : listaCertificadoAdopcionNew) {
                listaCertificadoAdopcionNewCertificadoAdopcionToAttach = em.getReference(listaCertificadoAdopcionNewCertificadoAdopcionToAttach.getClass(), listaCertificadoAdopcionNewCertificadoAdopcionToAttach.getId());
                attachedListaCertificadoAdopcionNew.add(listaCertificadoAdopcionNewCertificadoAdopcionToAttach);
            }
            listaCertificadoAdopcionNew = attachedListaCertificadoAdopcionNew;
            veterinario.setListaCertificadoAdopcion(listaCertificadoAdopcionNew);
            veterinario = em.merge(veterinario);
            for (CertificadoAdopcion listaCertificadoAdopcionOldCertificadoAdopcion : listaCertificadoAdopcionOld) {
                if (!listaCertificadoAdopcionNew.contains(listaCertificadoAdopcionOldCertificadoAdopcion)) {
                    listaCertificadoAdopcionOldCertificadoAdopcion.setVeterinario(null);
                    listaCertificadoAdopcionOldCertificadoAdopcion = em.merge(listaCertificadoAdopcionOldCertificadoAdopcion);
                }
            }
            for (CertificadoAdopcion listaCertificadoAdopcionNewCertificadoAdopcion : listaCertificadoAdopcionNew) {
                if (!listaCertificadoAdopcionOld.contains(listaCertificadoAdopcionNewCertificadoAdopcion)) {
                    Veterinario oldVeterinarioOfListaCertificadoAdopcionNewCertificadoAdopcion = listaCertificadoAdopcionNewCertificadoAdopcion.getVeterinario();
                    listaCertificadoAdopcionNewCertificadoAdopcion.setVeterinario(veterinario);
                    listaCertificadoAdopcionNewCertificadoAdopcion = em.merge(listaCertificadoAdopcionNewCertificadoAdopcion);
                    if (oldVeterinarioOfListaCertificadoAdopcionNewCertificadoAdopcion != null && !oldVeterinarioOfListaCertificadoAdopcionNewCertificadoAdopcion.equals(veterinario)) {
                        oldVeterinarioOfListaCertificadoAdopcionNewCertificadoAdopcion.getListaCertificadoAdopcion().remove(listaCertificadoAdopcionNewCertificadoAdopcion);
                        oldVeterinarioOfListaCertificadoAdopcionNewCertificadoAdopcion = em.merge(oldVeterinarioOfListaCertificadoAdopcionNewCertificadoAdopcion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = veterinario.getId();
                if (findVeterinario(id) == null) {
                    throw new NonexistentEntityException("The veterinario with id " + id + " no longer exists.");
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
            Veterinario veterinario;
            try {
                veterinario = em.getReference(Veterinario.class, id);
                veterinario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The veterinario with id " + id + " no longer exists.", enfe);
            }
            List<CertificadoAdopcion> listaCertificadoAdopcion = veterinario.getListaCertificadoAdopcion();
            for (CertificadoAdopcion listaCertificadoAdopcionCertificadoAdopcion : listaCertificadoAdopcion) {
                listaCertificadoAdopcionCertificadoAdopcion.setVeterinario(null);
                listaCertificadoAdopcionCertificadoAdopcion = em.merge(listaCertificadoAdopcionCertificadoAdopcion);
            }
            em.remove(veterinario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Veterinario> findVeterinarioEntities() {
        return findVeterinarioEntities(true, -1, -1);
    }

    public List<Veterinario> findVeterinarioEntities(int maxResults, int firstResult) {
        return findVeterinarioEntities(false, maxResults, firstResult);
    }

    private List<Veterinario> findVeterinarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Veterinario.class));
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

    public Veterinario findVeterinario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Veterinario.class, id);
        } finally {
            em.close();
        }
    }

    public int getVeterinarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Veterinario> rt = cq.from(Veterinario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
