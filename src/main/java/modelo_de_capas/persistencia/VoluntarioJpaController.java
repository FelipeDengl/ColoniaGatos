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
import modelo_de_capas.logica.HogarTransito;
import modelo_de_capas.logica.TransporteHogarTransito;
import modelo_de_capas.logica.CapturaCastracion;
import modelo_de_capas.logica.ControlVeterinario;
import modelo_de_capas.logica.Alimentacion;
import modelo_de_capas.logica.PuntoAvistamiento;
import modelo_de_capas.logica.Asignacion;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Felipe
 */
public class VoluntarioJpaController implements Serializable {

    public VoluntarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public VoluntarioJpaController() {
        emf = Persistence.createEntityManagerFactory("JpaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Voluntario voluntario) {
        if (voluntario.getListaVisitas() == null) {
            voluntario.setListaVisitas(new ArrayList<Visita>());
        }
        if (voluntario.getListaHogarTransito() == null) {
            voluntario.setListaHogarTransito(new ArrayList<HogarTransito>());
        }
        if (voluntario.getListaTransporteHogarTransito() == null) {
            voluntario.setListaTransporteHogarTransito(new ArrayList<TransporteHogarTransito>());
        }
        if (voluntario.getListaCapturaCastracion() == null) {
            voluntario.setListaCapturaCastracion(new ArrayList<CapturaCastracion>());
        }
        if (voluntario.getListaControlVeterinario() == null) {
            voluntario.setListaControlVeterinario(new ArrayList<ControlVeterinario>());
        }
        if (voluntario.getListaAlimentacion() == null) {
            voluntario.setListaAlimentacion(new ArrayList<Alimentacion>());
        }
        if (voluntario.getListaPuntoAvistamiento() == null) {
            voluntario.setListaPuntoAvistamiento(new ArrayList<PuntoAvistamiento>());
        }
        if (voluntario.getListaAsignacion() == null) {
            voluntario.setListaAsignacion(new ArrayList<Asignacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Visita> attachedListaVisitas = new ArrayList<Visita>();
            for (Visita listaVisitasVisitaToAttach : voluntario.getListaVisitas()) {
                listaVisitasVisitaToAttach = em.getReference(listaVisitasVisitaToAttach.getClass(), listaVisitasVisitaToAttach.getId());
                attachedListaVisitas.add(listaVisitasVisitaToAttach);
            }
            voluntario.setListaVisitas(attachedListaVisitas);
            List<HogarTransito> attachedListaHogarTransito = new ArrayList<HogarTransito>();
            for (HogarTransito listaHogarTransitoHogarTransitoToAttach : voluntario.getListaHogarTransito()) {
                listaHogarTransitoHogarTransitoToAttach = em.getReference(listaHogarTransitoHogarTransitoToAttach.getClass(), listaHogarTransitoHogarTransitoToAttach.getId());
                attachedListaHogarTransito.add(listaHogarTransitoHogarTransitoToAttach);
            }
            voluntario.setListaHogarTransito(attachedListaHogarTransito);
            List<TransporteHogarTransito> attachedListaTransporteHogarTransito = new ArrayList<TransporteHogarTransito>();
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransitoToAttach : voluntario.getListaTransporteHogarTransito()) {
                listaTransporteHogarTransitoTransporteHogarTransitoToAttach = em.getReference(listaTransporteHogarTransitoTransporteHogarTransitoToAttach.getClass(), listaTransporteHogarTransitoTransporteHogarTransitoToAttach.getId());
                attachedListaTransporteHogarTransito.add(listaTransporteHogarTransitoTransporteHogarTransitoToAttach);
            }
            voluntario.setListaTransporteHogarTransito(attachedListaTransporteHogarTransito);
            List<CapturaCastracion> attachedListaCapturaCastracion = new ArrayList<CapturaCastracion>();
            for (CapturaCastracion listaCapturaCastracionCapturaCastracionToAttach : voluntario.getListaCapturaCastracion()) {
                listaCapturaCastracionCapturaCastracionToAttach = em.getReference(listaCapturaCastracionCapturaCastracionToAttach.getClass(), listaCapturaCastracionCapturaCastracionToAttach.getId());
                attachedListaCapturaCastracion.add(listaCapturaCastracionCapturaCastracionToAttach);
            }
            voluntario.setListaCapturaCastracion(attachedListaCapturaCastracion);
            List<ControlVeterinario> attachedListaControlVeterinario = new ArrayList<ControlVeterinario>();
            for (ControlVeterinario listaControlVeterinarioControlVeterinarioToAttach : voluntario.getListaControlVeterinario()) {
                listaControlVeterinarioControlVeterinarioToAttach = em.getReference(listaControlVeterinarioControlVeterinarioToAttach.getClass(), listaControlVeterinarioControlVeterinarioToAttach.getId());
                attachedListaControlVeterinario.add(listaControlVeterinarioControlVeterinarioToAttach);
            }
            voluntario.setListaControlVeterinario(attachedListaControlVeterinario);
            List<Alimentacion> attachedListaAlimentacion = new ArrayList<Alimentacion>();
            for (Alimentacion listaAlimentacionAlimentacionToAttach : voluntario.getListaAlimentacion()) {
                listaAlimentacionAlimentacionToAttach = em.getReference(listaAlimentacionAlimentacionToAttach.getClass(), listaAlimentacionAlimentacionToAttach.getId());
                attachedListaAlimentacion.add(listaAlimentacionAlimentacionToAttach);
            }
            voluntario.setListaAlimentacion(attachedListaAlimentacion);
            List<PuntoAvistamiento> attachedListaPuntoAvistamiento = new ArrayList<PuntoAvistamiento>();
            for (PuntoAvistamiento listaPuntoAvistamientoPuntoAvistamientoToAttach : voluntario.getListaPuntoAvistamiento()) {
                listaPuntoAvistamientoPuntoAvistamientoToAttach = em.getReference(listaPuntoAvistamientoPuntoAvistamientoToAttach.getClass(), listaPuntoAvistamientoPuntoAvistamientoToAttach.getId());
                attachedListaPuntoAvistamiento.add(listaPuntoAvistamientoPuntoAvistamientoToAttach);
            }
            voluntario.setListaPuntoAvistamiento(attachedListaPuntoAvistamiento);
            List<Asignacion> attachedListaAsignacion = new ArrayList<Asignacion>();
            for (Asignacion listaAsignacionAsignacionToAttach : voluntario.getListaAsignacion()) {
                listaAsignacionAsignacionToAttach = em.getReference(listaAsignacionAsignacionToAttach.getClass(), listaAsignacionAsignacionToAttach.getId());
                attachedListaAsignacion.add(listaAsignacionAsignacionToAttach);
            }
            voluntario.setListaAsignacion(attachedListaAsignacion);
            em.persist(voluntario);
            for (Visita listaVisitasVisita : voluntario.getListaVisitas()) {
                Voluntario oldVoluntarioOfListaVisitasVisita = listaVisitasVisita.getVoluntario();
                listaVisitasVisita.setVoluntario(voluntario);
                listaVisitasVisita = em.merge(listaVisitasVisita);
                if (oldVoluntarioOfListaVisitasVisita != null) {
                    oldVoluntarioOfListaVisitasVisita.getListaVisitas().remove(listaVisitasVisita);
                    oldVoluntarioOfListaVisitasVisita = em.merge(oldVoluntarioOfListaVisitasVisita);
                }
            }
            for (HogarTransito listaHogarTransitoHogarTransito : voluntario.getListaHogarTransito()) {
                Voluntario oldVoluntarioOfListaHogarTransitoHogarTransito = listaHogarTransitoHogarTransito.getVoluntario();
                listaHogarTransitoHogarTransito.setVoluntario(voluntario);
                listaHogarTransitoHogarTransito = em.merge(listaHogarTransitoHogarTransito);
                if (oldVoluntarioOfListaHogarTransitoHogarTransito != null) {
                    oldVoluntarioOfListaHogarTransitoHogarTransito.getListaHogarTransito().remove(listaHogarTransitoHogarTransito);
                    oldVoluntarioOfListaHogarTransitoHogarTransito = em.merge(oldVoluntarioOfListaHogarTransitoHogarTransito);
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransito : voluntario.getListaTransporteHogarTransito()) {
                Voluntario oldVoluntarioOfListaTransporteHogarTransitoTransporteHogarTransito = listaTransporteHogarTransitoTransporteHogarTransito.getVoluntario();
                listaTransporteHogarTransitoTransporteHogarTransito.setVoluntario(voluntario);
                listaTransporteHogarTransitoTransporteHogarTransito = em.merge(listaTransporteHogarTransitoTransporteHogarTransito);
                if (oldVoluntarioOfListaTransporteHogarTransitoTransporteHogarTransito != null) {
                    oldVoluntarioOfListaTransporteHogarTransitoTransporteHogarTransito.getListaTransporteHogarTransito().remove(listaTransporteHogarTransitoTransporteHogarTransito);
                    oldVoluntarioOfListaTransporteHogarTransitoTransporteHogarTransito = em.merge(oldVoluntarioOfListaTransporteHogarTransitoTransporteHogarTransito);
                }
            }
            for (CapturaCastracion listaCapturaCastracionCapturaCastracion : voluntario.getListaCapturaCastracion()) {
                Voluntario oldVoluntarioOfListaCapturaCastracionCapturaCastracion = listaCapturaCastracionCapturaCastracion.getVoluntario();
                listaCapturaCastracionCapturaCastracion.setVoluntario(voluntario);
                listaCapturaCastracionCapturaCastracion = em.merge(listaCapturaCastracionCapturaCastracion);
                if (oldVoluntarioOfListaCapturaCastracionCapturaCastracion != null) {
                    oldVoluntarioOfListaCapturaCastracionCapturaCastracion.getListaCapturaCastracion().remove(listaCapturaCastracionCapturaCastracion);
                    oldVoluntarioOfListaCapturaCastracionCapturaCastracion = em.merge(oldVoluntarioOfListaCapturaCastracionCapturaCastracion);
                }
            }
            for (ControlVeterinario listaControlVeterinarioControlVeterinario : voluntario.getListaControlVeterinario()) {
                Voluntario oldVoluntarioOfListaControlVeterinarioControlVeterinario = listaControlVeterinarioControlVeterinario.getVoluntario();
                listaControlVeterinarioControlVeterinario.setVoluntario(voluntario);
                listaControlVeterinarioControlVeterinario = em.merge(listaControlVeterinarioControlVeterinario);
                if (oldVoluntarioOfListaControlVeterinarioControlVeterinario != null) {
                    oldVoluntarioOfListaControlVeterinarioControlVeterinario.getListaControlVeterinario().remove(listaControlVeterinarioControlVeterinario);
                    oldVoluntarioOfListaControlVeterinarioControlVeterinario = em.merge(oldVoluntarioOfListaControlVeterinarioControlVeterinario);
                }
            }
            for (Alimentacion listaAlimentacionAlimentacion : voluntario.getListaAlimentacion()) {
                Voluntario oldVoluntarioOfListaAlimentacionAlimentacion = listaAlimentacionAlimentacion.getVoluntario();
                listaAlimentacionAlimentacion.setVoluntario(voluntario);
                listaAlimentacionAlimentacion = em.merge(listaAlimentacionAlimentacion);
                if (oldVoluntarioOfListaAlimentacionAlimentacion != null) {
                    oldVoluntarioOfListaAlimentacionAlimentacion.getListaAlimentacion().remove(listaAlimentacionAlimentacion);
                    oldVoluntarioOfListaAlimentacionAlimentacion = em.merge(oldVoluntarioOfListaAlimentacionAlimentacion);
                }
            }
            for (PuntoAvistamiento listaPuntoAvistamientoPuntoAvistamiento : voluntario.getListaPuntoAvistamiento()) {
                Voluntario oldVoluntarioOfListaPuntoAvistamientoPuntoAvistamiento = listaPuntoAvistamientoPuntoAvistamiento.getVoluntario();
                listaPuntoAvistamientoPuntoAvistamiento.setVoluntario(voluntario);
                listaPuntoAvistamientoPuntoAvistamiento = em.merge(listaPuntoAvistamientoPuntoAvistamiento);
                if (oldVoluntarioOfListaPuntoAvistamientoPuntoAvistamiento != null) {
                    oldVoluntarioOfListaPuntoAvistamientoPuntoAvistamiento.getListaPuntoAvistamiento().remove(listaPuntoAvistamientoPuntoAvistamiento);
                    oldVoluntarioOfListaPuntoAvistamientoPuntoAvistamiento = em.merge(oldVoluntarioOfListaPuntoAvistamientoPuntoAvistamiento);
                }
            }
            for (Asignacion listaAsignacionAsignacion : voluntario.getListaAsignacion()) {
                Voluntario oldVoluntarioOfListaAsignacionAsignacion = listaAsignacionAsignacion.getVoluntario();
                listaAsignacionAsignacion.setVoluntario(voluntario);
                listaAsignacionAsignacion = em.merge(listaAsignacionAsignacion);
                if (oldVoluntarioOfListaAsignacionAsignacion != null) {
                    oldVoluntarioOfListaAsignacionAsignacion.getListaAsignacion().remove(listaAsignacionAsignacion);
                    oldVoluntarioOfListaAsignacionAsignacion = em.merge(oldVoluntarioOfListaAsignacionAsignacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Voluntario voluntario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Voluntario persistentVoluntario = em.find(Voluntario.class, voluntario.getId());
            List<Visita> listaVisitasOld = persistentVoluntario.getListaVisitas();
            List<Visita> listaVisitasNew = voluntario.getListaVisitas();
            List<HogarTransito> listaHogarTransitoOld = persistentVoluntario.getListaHogarTransito();
            List<HogarTransito> listaHogarTransitoNew = voluntario.getListaHogarTransito();
            List<TransporteHogarTransito> listaTransporteHogarTransitoOld = persistentVoluntario.getListaTransporteHogarTransito();
            List<TransporteHogarTransito> listaTransporteHogarTransitoNew = voluntario.getListaTransporteHogarTransito();
            List<CapturaCastracion> listaCapturaCastracionOld = persistentVoluntario.getListaCapturaCastracion();
            List<CapturaCastracion> listaCapturaCastracionNew = voluntario.getListaCapturaCastracion();
            List<ControlVeterinario> listaControlVeterinarioOld = persistentVoluntario.getListaControlVeterinario();
            List<ControlVeterinario> listaControlVeterinarioNew = voluntario.getListaControlVeterinario();
            List<Alimentacion> listaAlimentacionOld = persistentVoluntario.getListaAlimentacion();
            List<Alimentacion> listaAlimentacionNew = voluntario.getListaAlimentacion();
            List<PuntoAvistamiento> listaPuntoAvistamientoOld = persistentVoluntario.getListaPuntoAvistamiento();
            List<PuntoAvistamiento> listaPuntoAvistamientoNew = voluntario.getListaPuntoAvistamiento();
            List<Asignacion> listaAsignacionOld = persistentVoluntario.getListaAsignacion();
            List<Asignacion> listaAsignacionNew = voluntario.getListaAsignacion();
            List<Visita> attachedListaVisitasNew = new ArrayList<Visita>();
            for (Visita listaVisitasNewVisitaToAttach : listaVisitasNew) {
                listaVisitasNewVisitaToAttach = em.getReference(listaVisitasNewVisitaToAttach.getClass(), listaVisitasNewVisitaToAttach.getId());
                attachedListaVisitasNew.add(listaVisitasNewVisitaToAttach);
            }
            listaVisitasNew = attachedListaVisitasNew;
            voluntario.setListaVisitas(listaVisitasNew);
            List<HogarTransito> attachedListaHogarTransitoNew = new ArrayList<HogarTransito>();
            for (HogarTransito listaHogarTransitoNewHogarTransitoToAttach : listaHogarTransitoNew) {
                listaHogarTransitoNewHogarTransitoToAttach = em.getReference(listaHogarTransitoNewHogarTransitoToAttach.getClass(), listaHogarTransitoNewHogarTransitoToAttach.getId());
                attachedListaHogarTransitoNew.add(listaHogarTransitoNewHogarTransitoToAttach);
            }
            listaHogarTransitoNew = attachedListaHogarTransitoNew;
            voluntario.setListaHogarTransito(listaHogarTransitoNew);
            List<TransporteHogarTransito> attachedListaTransporteHogarTransitoNew = new ArrayList<TransporteHogarTransito>();
            for (TransporteHogarTransito listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach : listaTransporteHogarTransitoNew) {
                listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach = em.getReference(listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach.getClass(), listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach.getId());
                attachedListaTransporteHogarTransitoNew.add(listaTransporteHogarTransitoNewTransporteHogarTransitoToAttach);
            }
            listaTransporteHogarTransitoNew = attachedListaTransporteHogarTransitoNew;
            voluntario.setListaTransporteHogarTransito(listaTransporteHogarTransitoNew);
            List<CapturaCastracion> attachedListaCapturaCastracionNew = new ArrayList<CapturaCastracion>();
            for (CapturaCastracion listaCapturaCastracionNewCapturaCastracionToAttach : listaCapturaCastracionNew) {
                listaCapturaCastracionNewCapturaCastracionToAttach = em.getReference(listaCapturaCastracionNewCapturaCastracionToAttach.getClass(), listaCapturaCastracionNewCapturaCastracionToAttach.getId());
                attachedListaCapturaCastracionNew.add(listaCapturaCastracionNewCapturaCastracionToAttach);
            }
            listaCapturaCastracionNew = attachedListaCapturaCastracionNew;
            voluntario.setListaCapturaCastracion(listaCapturaCastracionNew);
            List<ControlVeterinario> attachedListaControlVeterinarioNew = new ArrayList<ControlVeterinario>();
            for (ControlVeterinario listaControlVeterinarioNewControlVeterinarioToAttach : listaControlVeterinarioNew) {
                listaControlVeterinarioNewControlVeterinarioToAttach = em.getReference(listaControlVeterinarioNewControlVeterinarioToAttach.getClass(), listaControlVeterinarioNewControlVeterinarioToAttach.getId());
                attachedListaControlVeterinarioNew.add(listaControlVeterinarioNewControlVeterinarioToAttach);
            }
            listaControlVeterinarioNew = attachedListaControlVeterinarioNew;
            voluntario.setListaControlVeterinario(listaControlVeterinarioNew);
            List<Alimentacion> attachedListaAlimentacionNew = new ArrayList<Alimentacion>();
            for (Alimentacion listaAlimentacionNewAlimentacionToAttach : listaAlimentacionNew) {
                listaAlimentacionNewAlimentacionToAttach = em.getReference(listaAlimentacionNewAlimentacionToAttach.getClass(), listaAlimentacionNewAlimentacionToAttach.getId());
                attachedListaAlimentacionNew.add(listaAlimentacionNewAlimentacionToAttach);
            }
            listaAlimentacionNew = attachedListaAlimentacionNew;
            voluntario.setListaAlimentacion(listaAlimentacionNew);
            List<PuntoAvistamiento> attachedListaPuntoAvistamientoNew = new ArrayList<PuntoAvistamiento>();
            for (PuntoAvistamiento listaPuntoAvistamientoNewPuntoAvistamientoToAttach : listaPuntoAvistamientoNew) {
                listaPuntoAvistamientoNewPuntoAvistamientoToAttach = em.getReference(listaPuntoAvistamientoNewPuntoAvistamientoToAttach.getClass(), listaPuntoAvistamientoNewPuntoAvistamientoToAttach.getId());
                attachedListaPuntoAvistamientoNew.add(listaPuntoAvistamientoNewPuntoAvistamientoToAttach);
            }
            listaPuntoAvistamientoNew = attachedListaPuntoAvistamientoNew;
            voluntario.setListaPuntoAvistamiento(listaPuntoAvistamientoNew);
            List<Asignacion> attachedListaAsignacionNew = new ArrayList<Asignacion>();
            for (Asignacion listaAsignacionNewAsignacionToAttach : listaAsignacionNew) {
                listaAsignacionNewAsignacionToAttach = em.getReference(listaAsignacionNewAsignacionToAttach.getClass(), listaAsignacionNewAsignacionToAttach.getId());
                attachedListaAsignacionNew.add(listaAsignacionNewAsignacionToAttach);
            }
            listaAsignacionNew = attachedListaAsignacionNew;
            voluntario.setListaAsignacion(listaAsignacionNew);
            voluntario = em.merge(voluntario);
            for (Visita listaVisitasOldVisita : listaVisitasOld) {
                if (!listaVisitasNew.contains(listaVisitasOldVisita)) {
                    listaVisitasOldVisita.setVoluntario(null);
                    listaVisitasOldVisita = em.merge(listaVisitasOldVisita);
                }
            }
            for (Visita listaVisitasNewVisita : listaVisitasNew) {
                if (!listaVisitasOld.contains(listaVisitasNewVisita)) {
                    Voluntario oldVoluntarioOfListaVisitasNewVisita = listaVisitasNewVisita.getVoluntario();
                    listaVisitasNewVisita.setVoluntario(voluntario);
                    listaVisitasNewVisita = em.merge(listaVisitasNewVisita);
                    if (oldVoluntarioOfListaVisitasNewVisita != null && !oldVoluntarioOfListaVisitasNewVisita.equals(voluntario)) {
                        oldVoluntarioOfListaVisitasNewVisita.getListaVisitas().remove(listaVisitasNewVisita);
                        oldVoluntarioOfListaVisitasNewVisita = em.merge(oldVoluntarioOfListaVisitasNewVisita);
                    }
                }
            }
            for (HogarTransito listaHogarTransitoOldHogarTransito : listaHogarTransitoOld) {
                if (!listaHogarTransitoNew.contains(listaHogarTransitoOldHogarTransito)) {
                    listaHogarTransitoOldHogarTransito.setVoluntario(null);
                    listaHogarTransitoOldHogarTransito = em.merge(listaHogarTransitoOldHogarTransito);
                }
            }
            for (HogarTransito listaHogarTransitoNewHogarTransito : listaHogarTransitoNew) {
                if (!listaHogarTransitoOld.contains(listaHogarTransitoNewHogarTransito)) {
                    Voluntario oldVoluntarioOfListaHogarTransitoNewHogarTransito = listaHogarTransitoNewHogarTransito.getVoluntario();
                    listaHogarTransitoNewHogarTransito.setVoluntario(voluntario);
                    listaHogarTransitoNewHogarTransito = em.merge(listaHogarTransitoNewHogarTransito);
                    if (oldVoluntarioOfListaHogarTransitoNewHogarTransito != null && !oldVoluntarioOfListaHogarTransitoNewHogarTransito.equals(voluntario)) {
                        oldVoluntarioOfListaHogarTransitoNewHogarTransito.getListaHogarTransito().remove(listaHogarTransitoNewHogarTransito);
                        oldVoluntarioOfListaHogarTransitoNewHogarTransito = em.merge(oldVoluntarioOfListaHogarTransitoNewHogarTransito);
                    }
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoOldTransporteHogarTransito : listaTransporteHogarTransitoOld) {
                if (!listaTransporteHogarTransitoNew.contains(listaTransporteHogarTransitoOldTransporteHogarTransito)) {
                    listaTransporteHogarTransitoOldTransporteHogarTransito.setVoluntario(null);
                    listaTransporteHogarTransitoOldTransporteHogarTransito = em.merge(listaTransporteHogarTransitoOldTransporteHogarTransito);
                }
            }
            for (TransporteHogarTransito listaTransporteHogarTransitoNewTransporteHogarTransito : listaTransporteHogarTransitoNew) {
                if (!listaTransporteHogarTransitoOld.contains(listaTransporteHogarTransitoNewTransporteHogarTransito)) {
                    Voluntario oldVoluntarioOfListaTransporteHogarTransitoNewTransporteHogarTransito = listaTransporteHogarTransitoNewTransporteHogarTransito.getVoluntario();
                    listaTransporteHogarTransitoNewTransporteHogarTransito.setVoluntario(voluntario);
                    listaTransporteHogarTransitoNewTransporteHogarTransito = em.merge(listaTransporteHogarTransitoNewTransporteHogarTransito);
                    if (oldVoluntarioOfListaTransporteHogarTransitoNewTransporteHogarTransito != null && !oldVoluntarioOfListaTransporteHogarTransitoNewTransporteHogarTransito.equals(voluntario)) {
                        oldVoluntarioOfListaTransporteHogarTransitoNewTransporteHogarTransito.getListaTransporteHogarTransito().remove(listaTransporteHogarTransitoNewTransporteHogarTransito);
                        oldVoluntarioOfListaTransporteHogarTransitoNewTransporteHogarTransito = em.merge(oldVoluntarioOfListaTransporteHogarTransitoNewTransporteHogarTransito);
                    }
                }
            }
            for (CapturaCastracion listaCapturaCastracionOldCapturaCastracion : listaCapturaCastracionOld) {
                if (!listaCapturaCastracionNew.contains(listaCapturaCastracionOldCapturaCastracion)) {
                    listaCapturaCastracionOldCapturaCastracion.setVoluntario(null);
                    listaCapturaCastracionOldCapturaCastracion = em.merge(listaCapturaCastracionOldCapturaCastracion);
                }
            }
            for (CapturaCastracion listaCapturaCastracionNewCapturaCastracion : listaCapturaCastracionNew) {
                if (!listaCapturaCastracionOld.contains(listaCapturaCastracionNewCapturaCastracion)) {
                    Voluntario oldVoluntarioOfListaCapturaCastracionNewCapturaCastracion = listaCapturaCastracionNewCapturaCastracion.getVoluntario();
                    listaCapturaCastracionNewCapturaCastracion.setVoluntario(voluntario);
                    listaCapturaCastracionNewCapturaCastracion = em.merge(listaCapturaCastracionNewCapturaCastracion);
                    if (oldVoluntarioOfListaCapturaCastracionNewCapturaCastracion != null && !oldVoluntarioOfListaCapturaCastracionNewCapturaCastracion.equals(voluntario)) {
                        oldVoluntarioOfListaCapturaCastracionNewCapturaCastracion.getListaCapturaCastracion().remove(listaCapturaCastracionNewCapturaCastracion);
                        oldVoluntarioOfListaCapturaCastracionNewCapturaCastracion = em.merge(oldVoluntarioOfListaCapturaCastracionNewCapturaCastracion);
                    }
                }
            }
            for (ControlVeterinario listaControlVeterinarioOldControlVeterinario : listaControlVeterinarioOld) {
                if (!listaControlVeterinarioNew.contains(listaControlVeterinarioOldControlVeterinario)) {
                    listaControlVeterinarioOldControlVeterinario.setVoluntario(null);
                    listaControlVeterinarioOldControlVeterinario = em.merge(listaControlVeterinarioOldControlVeterinario);
                }
            }
            for (ControlVeterinario listaControlVeterinarioNewControlVeterinario : listaControlVeterinarioNew) {
                if (!listaControlVeterinarioOld.contains(listaControlVeterinarioNewControlVeterinario)) {
                    Voluntario oldVoluntarioOfListaControlVeterinarioNewControlVeterinario = listaControlVeterinarioNewControlVeterinario.getVoluntario();
                    listaControlVeterinarioNewControlVeterinario.setVoluntario(voluntario);
                    listaControlVeterinarioNewControlVeterinario = em.merge(listaControlVeterinarioNewControlVeterinario);
                    if (oldVoluntarioOfListaControlVeterinarioNewControlVeterinario != null && !oldVoluntarioOfListaControlVeterinarioNewControlVeterinario.equals(voluntario)) {
                        oldVoluntarioOfListaControlVeterinarioNewControlVeterinario.getListaControlVeterinario().remove(listaControlVeterinarioNewControlVeterinario);
                        oldVoluntarioOfListaControlVeterinarioNewControlVeterinario = em.merge(oldVoluntarioOfListaControlVeterinarioNewControlVeterinario);
                    }
                }
            }
            for (Alimentacion listaAlimentacionOldAlimentacion : listaAlimentacionOld) {
                if (!listaAlimentacionNew.contains(listaAlimentacionOldAlimentacion)) {
                    listaAlimentacionOldAlimentacion.setVoluntario(null);
                    listaAlimentacionOldAlimentacion = em.merge(listaAlimentacionOldAlimentacion);
                }
            }
            for (Alimentacion listaAlimentacionNewAlimentacion : listaAlimentacionNew) {
                if (!listaAlimentacionOld.contains(listaAlimentacionNewAlimentacion)) {
                    Voluntario oldVoluntarioOfListaAlimentacionNewAlimentacion = listaAlimentacionNewAlimentacion.getVoluntario();
                    listaAlimentacionNewAlimentacion.setVoluntario(voluntario);
                    listaAlimentacionNewAlimentacion = em.merge(listaAlimentacionNewAlimentacion);
                    if (oldVoluntarioOfListaAlimentacionNewAlimentacion != null && !oldVoluntarioOfListaAlimentacionNewAlimentacion.equals(voluntario)) {
                        oldVoluntarioOfListaAlimentacionNewAlimentacion.getListaAlimentacion().remove(listaAlimentacionNewAlimentacion);
                        oldVoluntarioOfListaAlimentacionNewAlimentacion = em.merge(oldVoluntarioOfListaAlimentacionNewAlimentacion);
                    }
                }
            }
            for (PuntoAvistamiento listaPuntoAvistamientoOldPuntoAvistamiento : listaPuntoAvistamientoOld) {
                if (!listaPuntoAvistamientoNew.contains(listaPuntoAvistamientoOldPuntoAvistamiento)) {
                    listaPuntoAvistamientoOldPuntoAvistamiento.setVoluntario(null);
                    listaPuntoAvistamientoOldPuntoAvistamiento = em.merge(listaPuntoAvistamientoOldPuntoAvistamiento);
                }
            }
            for (PuntoAvistamiento listaPuntoAvistamientoNewPuntoAvistamiento : listaPuntoAvistamientoNew) {
                if (!listaPuntoAvistamientoOld.contains(listaPuntoAvistamientoNewPuntoAvistamiento)) {
                    Voluntario oldVoluntarioOfListaPuntoAvistamientoNewPuntoAvistamiento = listaPuntoAvistamientoNewPuntoAvistamiento.getVoluntario();
                    listaPuntoAvistamientoNewPuntoAvistamiento.setVoluntario(voluntario);
                    listaPuntoAvistamientoNewPuntoAvistamiento = em.merge(listaPuntoAvistamientoNewPuntoAvistamiento);
                    if (oldVoluntarioOfListaPuntoAvistamientoNewPuntoAvistamiento != null && !oldVoluntarioOfListaPuntoAvistamientoNewPuntoAvistamiento.equals(voluntario)) {
                        oldVoluntarioOfListaPuntoAvistamientoNewPuntoAvistamiento.getListaPuntoAvistamiento().remove(listaPuntoAvistamientoNewPuntoAvistamiento);
                        oldVoluntarioOfListaPuntoAvistamientoNewPuntoAvistamiento = em.merge(oldVoluntarioOfListaPuntoAvistamientoNewPuntoAvistamiento);
                    }
                }
            }
            for (Asignacion listaAsignacionOldAsignacion : listaAsignacionOld) {
                if (!listaAsignacionNew.contains(listaAsignacionOldAsignacion)) {
                    listaAsignacionOldAsignacion.setVoluntario(null);
                    listaAsignacionOldAsignacion = em.merge(listaAsignacionOldAsignacion);
                }
            }
            for (Asignacion listaAsignacionNewAsignacion : listaAsignacionNew) {
                if (!listaAsignacionOld.contains(listaAsignacionNewAsignacion)) {
                    Voluntario oldVoluntarioOfListaAsignacionNewAsignacion = listaAsignacionNewAsignacion.getVoluntario();
                    listaAsignacionNewAsignacion.setVoluntario(voluntario);
                    listaAsignacionNewAsignacion = em.merge(listaAsignacionNewAsignacion);
                    if (oldVoluntarioOfListaAsignacionNewAsignacion != null && !oldVoluntarioOfListaAsignacionNewAsignacion.equals(voluntario)) {
                        oldVoluntarioOfListaAsignacionNewAsignacion.getListaAsignacion().remove(listaAsignacionNewAsignacion);
                        oldVoluntarioOfListaAsignacionNewAsignacion = em.merge(oldVoluntarioOfListaAsignacionNewAsignacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = voluntario.getId();
                if (findVoluntario(id) == null) {
                    throw new NonexistentEntityException("The voluntario with id " + id + " no longer exists.");
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
            Voluntario voluntario;
            try {
                voluntario = em.getReference(Voluntario.class, id);
                voluntario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The voluntario with id " + id + " no longer exists.", enfe);
            }
            List<Visita> listaVisitas = voluntario.getListaVisitas();
            for (Visita listaVisitasVisita : listaVisitas) {
                listaVisitasVisita.setVoluntario(null);
                listaVisitasVisita = em.merge(listaVisitasVisita);
            }
            List<HogarTransito> listaHogarTransito = voluntario.getListaHogarTransito();
            for (HogarTransito listaHogarTransitoHogarTransito : listaHogarTransito) {
                listaHogarTransitoHogarTransito.setVoluntario(null);
                listaHogarTransitoHogarTransito = em.merge(listaHogarTransitoHogarTransito);
            }
            List<TransporteHogarTransito> listaTransporteHogarTransito = voluntario.getListaTransporteHogarTransito();
            for (TransporteHogarTransito listaTransporteHogarTransitoTransporteHogarTransito : listaTransporteHogarTransito) {
                listaTransporteHogarTransitoTransporteHogarTransito.setVoluntario(null);
                listaTransporteHogarTransitoTransporteHogarTransito = em.merge(listaTransporteHogarTransitoTransporteHogarTransito);
            }
            List<CapturaCastracion> listaCapturaCastracion = voluntario.getListaCapturaCastracion();
            for (CapturaCastracion listaCapturaCastracionCapturaCastracion : listaCapturaCastracion) {
                listaCapturaCastracionCapturaCastracion.setVoluntario(null);
                listaCapturaCastracionCapturaCastracion = em.merge(listaCapturaCastracionCapturaCastracion);
            }
            List<ControlVeterinario> listaControlVeterinario = voluntario.getListaControlVeterinario();
            for (ControlVeterinario listaControlVeterinarioControlVeterinario : listaControlVeterinario) {
                listaControlVeterinarioControlVeterinario.setVoluntario(null);
                listaControlVeterinarioControlVeterinario = em.merge(listaControlVeterinarioControlVeterinario);
            }
            List<Alimentacion> listaAlimentacion = voluntario.getListaAlimentacion();
            for (Alimentacion listaAlimentacionAlimentacion : listaAlimentacion) {
                listaAlimentacionAlimentacion.setVoluntario(null);
                listaAlimentacionAlimentacion = em.merge(listaAlimentacionAlimentacion);
            }
            List<PuntoAvistamiento> listaPuntoAvistamiento = voluntario.getListaPuntoAvistamiento();
            for (PuntoAvistamiento listaPuntoAvistamientoPuntoAvistamiento : listaPuntoAvistamiento) {
                listaPuntoAvistamientoPuntoAvistamiento.setVoluntario(null);
                listaPuntoAvistamientoPuntoAvistamiento = em.merge(listaPuntoAvistamientoPuntoAvistamiento);
            }
            List<Asignacion> listaAsignacion = voluntario.getListaAsignacion();
            for (Asignacion listaAsignacionAsignacion : listaAsignacion) {
                listaAsignacionAsignacion.setVoluntario(null);
                listaAsignacionAsignacion = em.merge(listaAsignacionAsignacion);
            }
            em.remove(voluntario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Voluntario> findVoluntarioEntities() {
        return findVoluntarioEntities(true, -1, -1);
    }

    public List<Voluntario> findVoluntarioEntities(int maxResults, int firstResult) {
        return findVoluntarioEntities(false, maxResults, firstResult);
    }

    private List<Voluntario> findVoluntarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Voluntario.class));
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

    public Voluntario findVoluntario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Voluntario.class, id);
        } finally {
            em.close();
        }
    }

    public int getVoluntarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Voluntario> rt = cq.from(Voluntario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
