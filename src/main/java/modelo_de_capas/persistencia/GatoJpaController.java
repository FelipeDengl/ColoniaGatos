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
import modelo_de_capas.logica.PuntoAvistamiento;
import java.util.ArrayList;
import java.util.List;
import modelo_de_capas.logica.Postulacion;
import modelo_de_capas.logica.CertificadoAdopcion;
import modelo_de_capas.logica.Visita;
import modelo_de_capas.logica.TransporteHogarTransito;
import modelo_de_capas.logica.CapturaCastracion;
import modelo_de_capas.logica.ControlVeterinario;
import modelo_de_capas.logica.Alimentacion;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class GatoJpaController implements Serializable {

    public GatoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public GatoJpaController () {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Gato gato) {
        if (gato.getListaPuntoAvistamiento() == null) {
            gato.setListaPuntoAvistamiento(new ArrayList<PuntoAvistamiento>());
        }
        if (gato.getListaPostulacion() == null) {
            gato.setListaPostulacion(new ArrayList<Postulacion>());
        }
        if (gato.getListaCertificadoAdopcion() == null) {
            gato.setListaCertificadoAdopcion(new ArrayList<CertificadoAdopcion>());
        }
        if (gato.getListaVisita() == null) {
            gato.setListaVisita(new ArrayList<Visita>());
        }
        if (gato.getListaTransporteHogarTransito() == null) {
            gato.setListaTransporteHogarTransito(new ArrayList<TransporteHogarTransito>());
        }
        if (gato.getListaCapturaCastracion() == null) {
            gato.setListaCapturaCastracion(new ArrayList<CapturaCastracion>());
        }
        if (gato.getListaControlVeterinario() == null) {
            gato.setListaControlVeterinario(new ArrayList<ControlVeterinario>());
        }
        if (gato.getListaAlimentacion() == null) {
            gato.setListaAlimentacion(new ArrayList<Alimentacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Colonia colonia = gato.getColonia();
            if (colonia != null) {
                colonia = em.getReference(colonia.getClass(), colonia.getId());
                gato.setColonia(colonia);
            }
            List<PuntoAvistamiento> attachedListaPuntoAvistamiento = new ArrayList<PuntoAvistamiento>();
            for (PuntoAvistamiento listaPuntoAvistamientoPuntoAvistamientoToAttach : gato.getListaPuntoAvistamiento()) {
                listaPuntoAvistamientoPuntoAvistamientoToAttach = em.getReference(listaPuntoAvistamientoPuntoAvistamientoToAttach.getClass(), listaPuntoAvistamientoPuntoAvistamientoToAttach.getId());
                attachedListaPuntoAvistamiento.add(listaPuntoAvistamientoPuntoAvistamientoToAttach);
            }
            gato.setListaPuntoAvistamiento(attachedListaPuntoAvistamiento);
            List<Postulacion> attachedListaPostulacion = new ArrayList<Postulacion>();
            for (Postulacion listaPostulacionPostulacionToAttach : gato.getListaPostulacion()) {
                listaPostulacionPostulacionToAttach = em.getReference(listaPostulacionPostulacionToAttach.getClass(), listaPostulacionPostulacionToAttach.getId());
                attachedListaPostulacion.add(listaPostulacionPostulacionToAttach);
            }
            gato.setListaPostulacion(attachedListaPostulacion);
            List<CertificadoAdopcion> attachedListaCertificadoAdopcion = new ArrayList<CertificadoAdopcion>();
            for (CertificadoAdopcion listaCertificadoAdopcionCertificadoAdopcionToAttach : gato.getListaCertificadoAdopcion()) {
                listaCertificadoAdopcionCertificadoAdopcionToAttach = em.getReference(listaCertificadoAdopcionCertificadoAdopcionToAttach.getClass(), listaCertificadoAdopcionCertificadoAdopcionToAttach.getId());
                attachedListaCertificadoAdopcion.add(listaCertificadoAdopcionCertificadoAdopcionToAttach);
            }
            gato.setListaCertificadoAdopcion(attachedListaCertificadoAdopcion);
            List<Visita> attachedListaVisita = new ArrayList<Visita>();
            for (Visita listaVisitaVisitaToAttach : gato.getListaVisita()) {
                listaVisitaVisitaToAttach = em.getReference(listaVisitaVisitaToAttach.getClass(), listaVisitaVisitaToAttach.getId());
                attachedListaVisita.add(listaVisitaVisitaToAttach);
            }
            gato.setListaVisita(attachedListaVisita);
            List<TransporteHogarTransito> attachedListaTransporteHogarTransito = new ArrayList<TransporteHogarTransito>();
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransitoToAttach : gato.getListaTransporteHogarTransito()) {
                listaTransporteHogarTransitoTransporteHogarTransitoToAttach = em.getReference(listaTransporteHogarTransitoTransporteHogarTransitoToAttach.getClass(), listaTransporteHogarTransitoTransporteHogarTransitoToAttach.getId());
                attachedListaTransporteHogarTransito.add(listaTransporteHogarTransitoTransporteHogarTransitoToAttach);
            }
            gato.setListaTransporteHogarTransito(attachedListaTransporteHogarTransito);
            List<CapturaCastracion> attachedListaCapturaCastracion = new ArrayList<CapturaCastracion>();
            for (CapturaCastracion listaCapturaCastracionCapturaCastracionToAttach : gato.getListaCapturaCastracion()) {
                listaCapturaCastracionCapturaCastracionToAttach = em.getReference(listaCapturaCastracionCapturaCastracionToAttach.getClass(), listaCapturaCastracionCapturaCastracionToAttach.getId());
                attachedListaCapturaCastracion.add(listaCapturaCastracionCapturaCastracionToAttach);
            }
            gato.setListaCapturaCastracion(attachedListaCapturaCastracion);
            List<ControlVeterinario> attachedListaControlVeterinario = new ArrayList<ControlVeterinario>();
            for (ControlVeterinario listaControlVeterinarioControlVeterinarioToAttach : gato.getListaControlVeterinario()) {
                listaControlVeterinarioControlVeterinarioToAttach = em.getReference(listaControlVeterinarioControlVeterinarioToAttach.getClass(), listaControlVeterinarioControlVeterinarioToAttach.getId());
                attachedListaControlVeterinario.add(listaControlVeterinarioControlVeterinarioToAttach);
            }
            gato.setListaControlVeterinario(attachedListaControlVeterinario);
            List<Alimentacion> attachedListaAlimentacion = new ArrayList<Alimentacion>();
            for (Alimentacion listaAlimentacionAlimentacionToAttach : gato.getListaAlimentacion()) {
                listaAlimentacionAlimentacionToAttach = em.getReference(listaAlimentacionAlimentacionToAttach.getClass(), listaAlimentacionAlimentacionToAttach.getId());
                attachedListaAlimentacion.add(listaAlimentacionAlimentacionToAttach);
            }
            gato.setListaAlimentacion(attachedListaAlimentacion);
            em.persist(gato);
            if (colonia != null) {
                colonia.getListaGato().add(gato);
                colonia = em.merge(colonia);
            }
            for (PuntoAvistamiento listaPuntoAvistamientoPuntoAvistamiento : gato.getListaPuntoAvistamiento()) {
                Gato oldGatoOfListaPuntoAvistamientoPuntoAvistamiento = listaPuntoAvistamientoPuntoAvistamiento.getGato();
                listaPuntoAvistamientoPuntoAvistamiento.setGato(gato);
                listaPuntoAvistamientoPuntoAvistamiento = em.merge(listaPuntoAvistamientoPuntoAvistamiento);
                if (oldGatoOfListaPuntoAvistamientoPuntoAvistamiento != null) {
                    oldGatoOfListaPuntoAvistamientoPuntoAvistamiento.getListaPuntoAvistamiento().remove(listaPuntoAvistamientoPuntoAvistamiento);
                    oldGatoOfListaPuntoAvistamientoPuntoAvistamiento = em.merge(oldGatoOfListaPuntoAvistamientoPuntoAvistamiento);
                }
            }
            for (Postulacion listaPostulacionPostulacion : gato.getListaPostulacion()) {
                Gato oldGatoOfListaPostulacionPostulacion = listaPostulacionPostulacion.getGato();
                listaPostulacionPostulacion.setGato(gato);
                listaPostulacionPostulacion = em.merge(listaPostulacionPostulacion);
                if (oldGatoOfListaPostulacionPostulacion != null) {
                    oldGatoOfListaPostulacionPostulacion.getListaPostulacion().remove(listaPostulacionPostulacion);
                    oldGatoOfListaPostulacionPostulacion = em.merge(oldGatoOfListaPostulacionPostulacion);
                }
            }
            for (CertificadoAdopcion listaCertificadoAdopcionCertificadoAdopcion : gato.getListaCertificadoAdopcion()) {
                Gato oldGatoOfListaCertificadoAdopcionCertificadoAdopcion = listaCertificadoAdopcionCertificadoAdopcion.getGato();
                listaCertificadoAdopcionCertificadoAdopcion.setGato(gato);
                listaCertificadoAdopcionCertificadoAdopcion = em.merge(listaCertificadoAdopcionCertificadoAdopcion);
                if (oldGatoOfListaCertificadoAdopcionCertificadoAdopcion != null) {
                    oldGatoOfListaCertificadoAdopcionCertificadoAdopcion.getListaCertificadoAdopcion().remove(listaCertificadoAdopcionCertificadoAdopcion);
                    oldGatoOfListaCertificadoAdopcionCertificadoAdopcion = em.merge(oldGatoOfListaCertificadoAdopcionCertificadoAdopcion);
                }
            }
            for (Visita listaVisitaVisita : gato.getListaVisita()) {
                Gato oldGatoOfListaVisitaVisita = listaVisitaVisita.getGato();
                listaVisitaVisita.setGato(gato);
                listaVisitaVisita = em.merge(listaVisitaVisita);
                if (oldGatoOfListaVisitaVisita != null) {
                    oldGatoOfListaVisitaVisita.getListaVisita().remove(listaVisitaVisita);
                    oldGatoOfListaVisitaVisita = em.merge(oldGatoOfListaVisitaVisita);
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransito : gato.getListaTransporteHogarTransito()) {
                Gato oldGatoOfListaTransporteHogarTransitoTransporteHogarTransito = listaTransporteHogarTransitoTransporteHogarTransito.getGato();
                listaTransporteHogarTransitoTransporteHogarTransito.setGato(gato);
                listaTransporteHogarTransitoTransporteHogarTransito = em.merge(listaTransporteHogarTransitoTransporteHogarTransito);
                if (oldGatoOfListaTransporteHogarTransitoTransporteHogarTransito != null) {
                    oldGatoOfListaTransporteHogarTransitoTransporteHogarTransito.getListaTransporteHogarTransito().remove(listaTransporteHogarTransitoTransporteHogarTransito);
                    oldGatoOfListaTransporteHogarTransitoTransporteHogarTransito = em.merge(oldGatoOfListaTransporteHogarTransitoTransporteHogarTransito);
                }
            }
            for (CapturaCastracion listaCapturaCastracionCapturaCastracion : gato.getListaCapturaCastracion()) {
                Gato oldGatoOfListaCapturaCastracionCapturaCastracion = listaCapturaCastracionCapturaCastracion.getGato();
                listaCapturaCastracionCapturaCastracion.setGato(gato);
                listaCapturaCastracionCapturaCastracion = em.merge(listaCapturaCastracionCapturaCastracion);
                if (oldGatoOfListaCapturaCastracionCapturaCastracion != null) {
                    oldGatoOfListaCapturaCastracionCapturaCastracion.getListaCapturaCastracion().remove(listaCapturaCastracionCapturaCastracion);
                    oldGatoOfListaCapturaCastracionCapturaCastracion = em.merge(oldGatoOfListaCapturaCastracionCapturaCastracion);
                }
            }
            for (ControlVeterinario listaControlVeterinarioControlVeterinario : gato.getListaControlVeterinario()) {
                Gato oldGatoOfListaControlVeterinarioControlVeterinario = listaControlVeterinarioControlVeterinario.getGato();
                listaControlVeterinarioControlVeterinario.setGato(gato);
                listaControlVeterinarioControlVeterinario = em.merge(listaControlVeterinarioControlVeterinario);
                if (oldGatoOfListaControlVeterinarioControlVeterinario != null) {
                    oldGatoOfListaControlVeterinarioControlVeterinario.getListaControlVeterinario().remove(listaControlVeterinarioControlVeterinario);
                    oldGatoOfListaControlVeterinarioControlVeterinario = em.merge(oldGatoOfListaControlVeterinarioControlVeterinario);
                }
            }
            for (Alimentacion listaAlimentacionAlimentacion : gato.getListaAlimentacion()) {
                Gato oldGatoOfListaAlimentacionAlimentacion = listaAlimentacionAlimentacion.getGato();
                listaAlimentacionAlimentacion.setGato(gato);
                listaAlimentacionAlimentacion = em.merge(listaAlimentacionAlimentacion);
                if (oldGatoOfListaAlimentacionAlimentacion != null) {
                    oldGatoOfListaAlimentacionAlimentacion.getListaAlimentacion().remove(listaAlimentacionAlimentacion);
                    oldGatoOfListaAlimentacionAlimentacion = em.merge(oldGatoOfListaAlimentacionAlimentacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gato gato) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gato persistentGato = em.find(Gato.class, gato.getId());
            Colonia coloniaOld = persistentGato.getColonia();
            Colonia coloniaNew = gato.getColonia();
            List<PuntoAvistamiento> listaPuntoAvistamientoOld = persistentGato.getListaPuntoAvistamiento();
            List<PuntoAvistamiento> listaPuntoAvistamientoNew = gato.getListaPuntoAvistamiento();
            List<Postulacion> listaPostulacionOld = persistentGato.getListaPostulacion();
            List<Postulacion> listaPostulacionNew = gato.getListaPostulacion();
            List<CertificadoAdopcion> listaCertificadoAdopcionOld = persistentGato.getListaCertificadoAdopcion();
            List<CertificadoAdopcion> listaCertificadoAdopcionNew = gato.getListaCertificadoAdopcion();
            List<Visita> listaVisitaOld = persistentGato.getListaVisita();
            List<Visita> listaVisitaNew = gato.getListaVisita();
            List<TransporteHogarTransito> listaTransporteHogarTransitoOld = persistentGato.getListaTransporteHogarTransito();
            List<TransporteHogarTransito> listaTransporteHogarTransitoNew = gato.getListaTransporteHogarTransito();
            List<CapturaCastracion> listaCapturaCastracionOld = persistentGato.getListaCapturaCastracion();
            List<CapturaCastracion> listaCapturaCastracionNew = gato.getListaCapturaCastracion();
            List<ControlVeterinario> listaControlVeterinarioOld = persistentGato.getListaControlVeterinario();
            List<ControlVeterinario> listaControlVeterinarioNew = gato.getListaControlVeterinario();
            List<Alimentacion> listaAlimentacionOld = persistentGato.getListaAlimentacion();
            List<Alimentacion> listaAlimentacionNew = gato.getListaAlimentacion();
            if (coloniaNew != null) {
                coloniaNew = em.getReference(coloniaNew.getClass(), coloniaNew.getId());
                gato.setColonia(coloniaNew);
            }
            List<PuntoAvistamiento> attachedListaPuntoAvistamientoNew = new ArrayList<PuntoAvistamiento>();
            for (PuntoAvistamiento listaPuntoAvistamientoNewPuntoAvistamientoToAttach : listaPuntoAvistamientoNew) {
                listaPuntoAvistamientoNewPuntoAvistamientoToAttach = em.getReference(listaPuntoAvistamientoNewPuntoAvistamientoToAttach.getClass(), listaPuntoAvistamientoNewPuntoAvistamientoToAttach.getId());
                attachedListaPuntoAvistamientoNew.add(listaPuntoAvistamientoNewPuntoAvistamientoToAttach);
            }
            listaPuntoAvistamientoNew = attachedListaPuntoAvistamientoNew;
            gato.setListaPuntoAvistamiento(listaPuntoAvistamientoNew);
            List<Postulacion> attachedListaPostulacionNew = new ArrayList<Postulacion>();
            for (Postulacion listaPostulacionNewPostulacionToAttach : listaPostulacionNew) {
                listaPostulacionNewPostulacionToAttach = em.getReference(listaPostulacionNewPostulacionToAttach.getClass(), listaPostulacionNewPostulacionToAttach.getId());
                attachedListaPostulacionNew.add(listaPostulacionNewPostulacionToAttach);
            }
            listaPostulacionNew = attachedListaPostulacionNew;
            gato.setListaPostulacion(listaPostulacionNew);
            List<CertificadoAdopcion> attachedListaCertificadoAdopcionNew = new ArrayList<CertificadoAdopcion>();
            for (CertificadoAdopcion listaCertificadoAdopcionNewCertificadoAdopcionToAttach : listaCertificadoAdopcionNew) {
                listaCertificadoAdopcionNewCertificadoAdopcionToAttach = em.getReference(listaCertificadoAdopcionNewCertificadoAdopcionToAttach.getClass(), listaCertificadoAdopcionNewCertificadoAdopcionToAttach.getId());
                attachedListaCertificadoAdopcionNew.add(listaCertificadoAdopcionNewCertificadoAdopcionToAttach);
            }
            listaCertificadoAdopcionNew = attachedListaCertificadoAdopcionNew;
            gato.setListaCertificadoAdopcion(listaCertificadoAdopcionNew);
            List<Visita> attachedListaVisitaNew = new ArrayList<Visita>();
            for (Visita listaVisitaNewVisitaToAttach : listaVisitaNew) {
                listaVisitaNewVisitaToAttach = em.getReference(listaVisitaNewVisitaToAttach.getClass(), listaVisitaNewVisitaToAttach.getId());
                attachedListaVisitaNew.add(listaVisitaNewVisitaToAttach);
            }
            listaVisitaNew = attachedListaVisitaNew;
            gato.setListaVisita(listaVisitaNew);
            List<TransporteHogarTransito> attachedListaTransporteHogarTransitoNew = new ArrayList<TransporteHogarTransito>();
            for (TransporteHogarTransito listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach : listaTransporteHogarTransitoNew) {
                listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach = em.getReference(listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach.getClass(), listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach.getId());
                attachedListaTransporteHogarTransitoNew.add(listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach);
            }
            listaTransporteHogarTransitoNew = attachedListaTransporteHogarTransitoNew;
            gato.setListaTransporteHogarTransito(listaTransporteHogarTransitoNew);
            List<CapturaCastracion> attachedListaCapturaCastracionNew = new ArrayList<CapturaCastracion>();
            for (CapturaCastracion listaCapturaCastracionNewCapturaCastracionToAttach : listaCapturaCastracionNew) {
                listaCapturaCastracionNewCapturaCastracionToAttach = em.getReference(listaCapturaCastracionNewCapturaCastracionToAttach.getClass(), listaCapturaCastracionNewCapturaCastracionToAttach.getId());
                attachedListaCapturaCastracionNew.add(listaCapturaCastracionNewCapturaCastracionToAttach);
            }
            listaCapturaCastracionNew = attachedListaCapturaCastracionNew;
            gato.setListaCapturaCastracion(listaCapturaCastracionNew);
            List<ControlVeterinario> attachedListaControlVeterinarioNew = new ArrayList<ControlVeterinario>();
            for (ControlVeterinario listaControlVeterinarioNewControlVeterinarioToAttach : listaControlVeterinarioNew) {
                listaControlVeterinarioNewControlVeterinarioToAttach = em.getReference(listaControlVeterinarioNewControlVeterinarioToAttach.getClass(), listaControlVeterinarioNewControlVeterinarioToAttach.getId());
                attachedListaControlVeterinarioNew.add(listaControlVeterinarioNewControlVeterinarioToAttach);
            }
            listaControlVeterinarioNew = attachedListaControlVeterinarioNew;
            gato.setListaControlVeterinario(listaControlVeterinarioNew);
            List<Alimentacion> attachedListaAlimentacionNew = new ArrayList<Alimentacion>();
            for (Alimentacion listaAlimentacionNewAlimentacionToAttach : listaAlimentacionNew) {
                listaAlimentacionNewAlimentacionToAttach = em.getReference(listaAlimentacionNewAlimentacionToAttach.getClass(), listaAlimentacionNewAlimentacionToAttach.getId());
                attachedListaAlimentacionNew.add(listaAlimentacionNewAlimentacionToAttach);
            }
            listaAlimentacionNew = attachedListaAlimentacionNew;
            gato.setListaAlimentacion(listaAlimentacionNew);
            gato = em.merge(gato);
            if (coloniaOld != null && !coloniaOld.equals(coloniaNew)) {
                coloniaOld.getListaGato().remove(gato);
                coloniaOld = em.merge(coloniaOld);
            }
            if (coloniaNew != null && !coloniaNew.equals(coloniaOld)) {
                coloniaNew.getListaGato().add(gato);
                coloniaNew = em.merge(coloniaNew);
            }
            for (PuntoAvistamiento listaPuntoAvistamientoOldPuntoAvistamiento : listaPuntoAvistamientoOld) {
                if (!listaPuntoAvistamientoNew.contains(listaPuntoAvistamientoOldPuntoAvistamiento)) {
                    listaPuntoAvistamientoOldPuntoAvistamiento.setGato(null);
                    listaPuntoAvistamientoOldPuntoAvistamiento = em.merge(listaPuntoAvistamientoOldPuntoAvistamiento);
                }
            }
            for (PuntoAvistamiento listaPuntoAvistamientoNewPuntoAvistamiento : listaPuntoAvistamientoNew) {
                if (!listaPuntoAvistamientoOld.contains(listaPuntoAvistamientoNewPuntoAvistamiento)) {
                    Gato oldGatoOfListaPuntoAvistamientoNewPuntoAvistamiento = listaPuntoAvistamientoNewPuntoAvistamiento.getGato();
                    listaPuntoAvistamientoNewPuntoAvistamiento.setGato(gato);
                    listaPuntoAvistamientoNewPuntoAvistamiento = em.merge(listaPuntoAvistamientoNewPuntoAvistamiento);
                    if (oldGatoOfListaPuntoAvistamientoNewPuntoAvistamiento != null && !oldGatoOfListaPuntoAvistamientoNewPuntoAvistamiento.equals(gato)) {
                        oldGatoOfListaPuntoAvistamientoNewPuntoAvistamiento.getListaPuntoAvistamiento().remove(listaPuntoAvistamientoNewPuntoAvistamiento);
                        oldGatoOfListaPuntoAvistamientoNewPuntoAvistamiento = em.merge(oldGatoOfListaPuntoAvistamientoNewPuntoAvistamiento);
                    }
                }
            }
            for (Postulacion listaPostulacionOldPostulacion : listaPostulacionOld) {
                if (!listaPostulacionNew.contains(listaPostulacionOldPostulacion)) {
                    listaPostulacionOldPostulacion.setGato(null);
                    listaPostulacionOldPostulacion = em.merge(listaPostulacionOldPostulacion);
                }
            }
            for (Postulacion listaPostulacionNewPostulacion : listaPostulacionNew) {
                if (!listaPostulacionOld.contains(listaPostulacionNewPostulacion)) {
                    Gato oldGatoOfListaPostulacionNewPostulacion = listaPostulacionNewPostulacion.getGato();
                    listaPostulacionNewPostulacion.setGato(gato);
                    listaPostulacionNewPostulacion = em.merge(listaPostulacionNewPostulacion);
                    if (oldGatoOfListaPostulacionNewPostulacion != null && !oldGatoOfListaPostulacionNewPostulacion.equals(gato)) {
                        oldGatoOfListaPostulacionNewPostulacion.getListaPostulacion().remove(listaPostulacionNewPostulacion);
                        oldGatoOfListaPostulacionNewPostulacion = em.merge(oldGatoOfListaPostulacionNewPostulacion);
                    }
                }
            }
            for (CertificadoAdopcion listaCertificadoAdopcionOldCertificadoAdopcion : listaCertificadoAdopcionOld) {
                if (!listaCertificadoAdopcionNew.contains(listaCertificadoAdopcionOldCertificadoAdopcion)) {
                    listaCertificadoAdopcionOldCertificadoAdopcion.setGato(null);
                    listaCertificadoAdopcionOldCertificadoAdopcion = em.merge(listaCertificadoAdopcionOldCertificadoAdopcion);
                }
            }
            for (CertificadoAdopcion listaCertificadoAdopcionNewCertificadoAdopcion : listaCertificadoAdopcionNew) {
                if (!listaCertificadoAdopcionOld.contains(listaCertificadoAdopcionNewCertificadoAdopcion)) {
                    Gato oldGatoOfListaCertificadoAdopcionNewCertificadoAdopcion = listaCertificadoAdopcionNewCertificadoAdopcion.getGato();
                    listaCertificadoAdopcionNewCertificadoAdopcion.setGato(gato);
                    listaCertificadoAdopcionNewCertificadoAdopcion = em.merge(listaCertificadoAdopcionNewCertificadoAdopcion);
                    if (oldGatoOfListaCertificadoAdopcionNewCertificadoAdopcion != null && !oldGatoOfListaCertificadoAdopcionNewCertificadoAdopcion.equals(gato)) {
                        oldGatoOfListaCertificadoAdopcionNewCertificadoAdopcion.getListaCertificadoAdopcion().remove(listaCertificadoAdopcionNewCertificadoAdopcion);
                        oldGatoOfListaCertificadoAdopcionNewCertificadoAdopcion = em.merge(oldGatoOfListaCertificadoAdopcionNewCertificadoAdopcion);
                    }
                }
            }
            for (Visita listaVisitaOldVisita : listaVisitaOld) {
                if (!listaVisitaNew.contains(listaVisitaOldVisita)) {
                    listaVisitaOldVisita.setGato(null);
                    listaVisitaOldVisita = em.merge(listaVisitaOldVisita);
                }
            }
            for (Visita listaVisitaNewVisita : listaVisitaNew) {
                if (!listaVisitaOld.contains(listaVisitaNewVisita)) {
                    Gato oldGatoOfListaVisitaNewVisita = listaVisitaNewVisita.getGato();
                    listaVisitaNewVisita.setGato(gato);
                    listaVisitaNewVisita = em.merge(listaVisitaNewVisita);
                    if (oldGatoOfListaVisitaNewVisita != null && !oldGatoOfListaVisitaNewVisita.equals(gato)) {
                        oldGatoOfListaVisitaNewVisita.getListaVisita().remove(listaVisitaNewVisita);
                        oldGatoOfListaVisitaNewVisita = em.merge(oldGatoOfListaVisitaNewVisita);
                    }
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoOldTransporteHogarTransito : listaTransporteHogarTransitoOld) {
                if (!listaTransporteHogarTransitoNew.contains(listaTransporteHogarTransitoOldTransporteHogarTransito)) {
                    listaTransporteHogarTransitoOldTransporteHogarTransito.setGato(null);
                    listaTransporteHogarTransitoOldTransporteHogarTransito = em.merge(listaTransporteHogarTransitoOldTransporteHogarTransito);
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoNewTransporteHogarTransito : listaTransporteHogarTransitoNew) {
                if (!listaTransporteHogarTransitoOld.contains(listaTransporteHogarTransitoNewTransporteHogarTransito)) {
                    Gato oldGatoOfListaTransporteHogarTransitoNewTransporteHogarTransito = listaTransporteHogarTransitoNewTransporteHogarTransito.getGato();
                    listaTransporteHogarTransitoNewTransporteHogarTransito.setGato(gato);
                    listaTransporteHogarTransitoNewTransporteHogarTransito = em.merge(listaTransporteHogarTransitoNewTransporteHogarTransito);
                    if (oldGatoOfListaTransporteHogarTransitoNewTransporteHogarTransito != null && !oldGatoOfListaTransporteHogarTransitoNewTransporteHogarTransito.equals(gato)) {
                        oldGatoOfListaTransporteHogarTransitoNewTransporteHogarTransito.getListaTransporteHogarTransito().remove(listaTransporteHogarTransitoNewTransporteHogarTransito);
                        oldGatoOfListaTransporteHogarTransitoNewTransporteHogarTransito = em.merge(oldGatoOfListaTransporteHogarTransitoNewTransporteHogarTransito);
                    }
                }
            }
            for (CapturaCastracion listaCapturaCastracionOldCapturaCastracion : listaCapturaCastracionOld) {
                if (!listaCapturaCastracionNew.contains(listaCapturaCastracionOldCapturaCastracion)) {
                    listaCapturaCastracionOldCapturaCastracion.setGato(null);
                    listaCapturaCastracionOldCapturaCastracion = em.merge(listaCapturaCastracionOldCapturaCastracion);
                }
            }
            for (CapturaCastracion listaCapturaCastracionNewCapturaCastracion : listaCapturaCastracionNew) {
                if (!listaCapturaCastracionOld.contains(listaCapturaCastracionNewCapturaCastracion)) {
                    Gato oldGatoOfListaCapturaCastracionNewCapturaCastracion = listaCapturaCastracionNewCapturaCastracion.getGato();
                    listaCapturaCastracionNewCapturaCastracion.setGato(gato);
                    listaCapturaCastracionNewCapturaCastracion = em.merge(listaCapturaCastracionNewCapturaCastracion);
                    if (oldGatoOfListaCapturaCastracionNewCapturaCastracion != null && !oldGatoOfListaCapturaCastracionNewCapturaCastracion.equals(gato)) {
                        oldGatoOfListaCapturaCastracionNewCapturaCastracion.getListaCapturaCastracion().remove(listaCapturaCastracionNewCapturaCastracion);
                        oldGatoOfListaCapturaCastracionNewCapturaCastracion = em.merge(oldGatoOfListaCapturaCastracionNewCapturaCastracion);
                    }
                }
            }
            for (ControlVeterinario listaControlVeterinarioOldControlVeterinario : listaControlVeterinarioOld) {
                if (!listaControlVeterinarioNew.contains(listaControlVeterinarioOldControlVeterinario)) {
                    listaControlVeterinarioOldControlVeterinario.setGato(null);
                    listaControlVeterinarioOldControlVeterinario = em.merge(listaControlVeterinarioOldControlVeterinario);
                }
            }
            for (ControlVeterinario listaControlVeterinarioNewControlVeterinario : listaControlVeterinarioNew) {
                if (!listaControlVeterinarioOld.contains(listaControlVeterinarioNewControlVeterinario)) {
                    Gato oldGatoOfListaControlVeterinarioNewControlVeterinario = listaControlVeterinarioNewControlVeterinario.getGato();
                    listaControlVeterinarioNewControlVeterinario.setGato(gato);
                    listaControlVeterinarioNewControlVeterinario = em.merge(listaControlVeterinarioNewControlVeterinario);
                    if (oldGatoOfListaControlVeterinarioNewControlVeterinario != null && !oldGatoOfListaControlVeterinarioNewControlVeterinario.equals(gato)) {
                        oldGatoOfListaControlVeterinarioNewControlVeterinario.getListaControlVeterinario().remove(listaControlVeterinarioNewControlVeterinario);
                        oldGatoOfListaControlVeterinarioNewControlVeterinario = em.merge(oldGatoOfListaControlVeterinarioNewControlVeterinario);
                    }
                }
            }
            for (Alimentacion listaAlimentacionOldAlimentacion : listaAlimentacionOld) {
                if (!listaAlimentacionNew.contains(listaAlimentacionOldAlimentacion)) {
                    listaAlimentacionOldAlimentacion.setGato(null);
                    listaAlimentacionOldAlimentacion = em.merge(listaAlimentacionOldAlimentacion);
                }
            }
            for (Alimentacion listaAlimentacionNewAlimentacion : listaAlimentacionNew) {
                if (!listaAlimentacionOld.contains(listaAlimentacionNewAlimentacion)) {
                    Gato oldGatoOfListaAlimentacionNewAlimentacion = listaAlimentacionNewAlimentacion.getGato();
                    listaAlimentacionNewAlimentacion.setGato(gato);
                    listaAlimentacionNewAlimentacion = em.merge(listaAlimentacionNewAlimentacion);
                    if (oldGatoOfListaAlimentacionNewAlimentacion != null && !oldGatoOfListaAlimentacionNewAlimentacion.equals(gato)) {
                        oldGatoOfListaAlimentacionNewAlimentacion.getListaAlimentacion().remove(listaAlimentacionNewAlimentacion);
                        oldGatoOfListaAlimentacionNewAlimentacion = em.merge(oldGatoOfListaAlimentacionNewAlimentacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gato.getId();
                if (findGato(id) == null) {
                    throw new NonexistentEntityException("The gato with id " + id + " no longer exists.");
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
            Gato gato;
            try {
                gato = em.getReference(Gato.class, id);
                gato.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gato with id " + id + " no longer exists.", enfe);
            }
            Colonia colonia = gato.getColonia();
            if (colonia != null) {
                colonia.getListaGato().remove(gato);
                colonia = em.merge(colonia);
            }
            List<PuntoAvistamiento> listaPuntoAvistamiento = gato.getListaPuntoAvistamiento();
            for (PuntoAvistamiento listaPuntoAvistamientoPuntoAvistamiento : listaPuntoAvistamiento) {
                listaPuntoAvistamientoPuntoAvistamiento.setGato(null);
                listaPuntoAvistamientoPuntoAvistamiento = em.merge(listaPuntoAvistamientoPuntoAvistamiento);
            }
            List<Postulacion> listaPostulacion = gato.getListaPostulacion();
            for (Postulacion listaPostulacionPostulacion : listaPostulacion) {
                listaPostulacionPostulacion.setGato(null);
                listaPostulacionPostulacion = em.merge(listaPostulacionPostulacion);
            }
            List<CertificadoAdopcion> listaCertificadoAdopcion = gato.getListaCertificadoAdopcion();
            for (CertificadoAdopcion listaCertificadoAdopcionCertificadoAdopcion : listaCertificadoAdopcion) {
                listaCertificadoAdopcionCertificadoAdopcion.setGato(null);
                listaCertificadoAdopcionCertificadoAdopcion = em.merge(listaCertificadoAdopcionCertificadoAdopcion);
            }
            List<Visita> listaVisita = gato.getListaVisita();
            for (Visita listaVisitaVisita : listaVisita) {
                listaVisitaVisita.setGato(null);
                listaVisitaVisita = em.merge(listaVisitaVisita);
            }
            List<TransporteHogarTransito> listaTransporteHogarTransito = gato.getListaTransporteHogarTransito();
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransito : listaTransporteHogarTransito) {
                listaTransporteHogarTransitoTransporteHogarTransito.setGato(null);
                listaTransporteHogarTransitoTransporteHogarTransito = em.merge(listaTransporteHogarTransitoTransporteHogarTransito);
            }
            List<CapturaCastracion> listaCapturaCastracion = gato.getListaCapturaCastracion();
            for (CapturaCastracion listaCapturaCastracionCapturaCastracion : listaCapturaCastracion) {
                listaCapturaCastracionCapturaCastracion.setGato(null);
                listaCapturaCastracionCapturaCastracion = em.merge(listaCapturaCastracionCapturaCastracion);
            }
            List<ControlVeterinario> listaControlVeterinario = gato.getListaControlVeterinario();
            for (ControlVeterinario listaControlVeterinarioControlVeterinario : listaControlVeterinario) {
                listaControlVeterinarioControlVeterinario.setGato(null);
                listaControlVeterinarioControlVeterinario = em.merge(listaControlVeterinarioControlVeterinario);
            }
            List<Alimentacion> listaAlimentacion = gato.getListaAlimentacion();
            for (Alimentacion listaAlimentacionAlimentacion : listaAlimentacion) {
                listaAlimentacionAlimentacion.setGato(null);
                listaAlimentacionAlimentacion = em.merge(listaAlimentacionAlimentacion);
            }
            em.remove(gato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Gato> findGatoEntities() {
        return findGatoEntities(true, -1, -1);
    }

    public List<Gato> findGatoEntities(int maxResults, int firstResult) {
        return findGatoEntities(false, maxResults, firstResult);
    }

    private List<Gato> findGatoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gato.class));
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

    public Gato findGato(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gato.class, id);
        } finally {
            em.close();
        }
    }

    public int getGatoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gato> rt = cq.from(Gato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
