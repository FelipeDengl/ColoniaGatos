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
import modelo_de_capas.logica.CertificadoAdopcion;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.Veterinario;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class CertificadoAdopcionJpaController implements Serializable {

    public CertificadoAdopcionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public CertificadoAdopcionJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CertificadoAdopcion certificadoAdopcion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gato gato = certificadoAdopcion.getGato();
            if (gato != null) {
                gato = em.getReference(gato.getClass(), gato.getId());
                certificadoAdopcion.setGato(gato);
            }
            Veterinario veterinario = certificadoAdopcion.getVeterinario();
            if (veterinario != null) {
                veterinario = em.getReference(veterinario.getClass(), veterinario.getId());
                certificadoAdopcion.setVeterinario(veterinario);
            }
            em.persist(certificadoAdopcion);
            if (gato != null) {
                gato.getListaCertificadoAdopcion().add(certificadoAdopcion);
                gato = em.merge(gato);
            }
            if (veterinario != null) {
                veterinario.getListaCertificadoAdopcion().add(certificadoAdopcion);
                veterinario = em.merge(veterinario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CertificadoAdopcion certificadoAdopcion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CertificadoAdopcion persistentCertificadoAdopcion = em.find(CertificadoAdopcion.class, certificadoAdopcion.getId());
            Gato gatoOld = persistentCertificadoAdopcion.getGato();
            Gato gatoNew = certificadoAdopcion.getGato();
            Veterinario veterinarioOld = persistentCertificadoAdopcion.getVeterinario();
            Veterinario veterinarioNew = certificadoAdopcion.getVeterinario();
            if (gatoNew != null) {
                gatoNew = em.getReference(gatoNew.getClass(), gatoNew.getId());
                certificadoAdopcion.setGato(gatoNew);
            }
            if (veterinarioNew != null) {
                veterinarioNew = em.getReference(veterinarioNew.getClass(), veterinarioNew.getId());
                certificadoAdopcion.setVeterinario(veterinarioNew);
            }
            certificadoAdopcion = em.merge(certificadoAdopcion);
            if (gatoOld != null && !gatoOld.equals(gatoNew)) {
                gatoOld.getListaCertificadoAdopcion().remove(certificadoAdopcion);
                gatoOld = em.merge(gatoOld);
            }
            if (gatoNew != null && !gatoNew.equals(gatoOld)) {
                gatoNew.getListaCertificadoAdopcion().add(certificadoAdopcion);
                gatoNew = em.merge(gatoNew);
            }
            if (veterinarioOld != null && !veterinarioOld.equals(veterinarioNew)) {
                veterinarioOld.getListaCertificadoAdopcion().remove(certificadoAdopcion);
                veterinarioOld = em.merge(veterinarioOld);
            }
            if (veterinarioNew != null && !veterinarioNew.equals(veterinarioOld)) {
                veterinarioNew.getListaCertificadoAdopcion().add(certificadoAdopcion);
                veterinarioNew = em.merge(veterinarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = certificadoAdopcion.getId();
                if (findCertificadoAdopcion(id) == null) {
                    throw new NonexistentEntityException("The certificadoAdopcion with id " + id + " no longer exists.");
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
            CertificadoAdopcion certificadoAdopcion;
            try {
                certificadoAdopcion = em.getReference(CertificadoAdopcion.class, id);
                certificadoAdopcion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The certificadoAdopcion with id " + id + " no longer exists.", enfe);
            }
            Gato gato = certificadoAdopcion.getGato();
            if (gato != null) {
                gato.getListaCertificadoAdopcion().remove(certificadoAdopcion);
                gato = em.merge(gato);
            }
            Veterinario veterinario = certificadoAdopcion.getVeterinario();
            if (veterinario != null) {
                veterinario.getListaCertificadoAdopcion().remove(certificadoAdopcion);
                veterinario = em.merge(veterinario);
            }
            em.remove(certificadoAdopcion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CertificadoAdopcion> findCertificadoAdopcionEntities() {
        return findCertificadoAdopcionEntities(true, -1, -1);
    }

    public List<CertificadoAdopcion> findCertificadoAdopcionEntities(int maxResults, int firstResult) {
        return findCertificadoAdopcionEntities(false, maxResults, firstResult);
    }

    private List<CertificadoAdopcion> findCertificadoAdopcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CertificadoAdopcion.class));
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

    public CertificadoAdopcion findCertificadoAdopcion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CertificadoAdopcion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCertificadoAdopcionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CertificadoAdopcion> rt = cq.from(CertificadoAdopcion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
