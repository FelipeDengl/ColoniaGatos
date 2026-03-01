package modelo_de_capas.persistencia;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import modelo_de_capas.logica.Colonia;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Familia;
import modelo_de_capas.logica.Veterinario;
import modelo_de_capas.logica.Usuario;
import modelo_de_capas.logica.PuntoAvistamiento;
import modelo_de_capas.logica.Administrador;
import modelo_de_capas.logica.Alimentacion;
import modelo_de_capas.logica.Asignacion;
import modelo_de_capas.logica.CapturaCastracion;
import modelo_de_capas.logica.CertificadoAdopcion;
import modelo_de_capas.logica.ControlVeterinario;
import modelo_de_capas.logica.Diagnostico;
import modelo_de_capas.logica.Estudio;
import modelo_de_capas.logica.HistorialMedico;
import modelo_de_capas.logica.HogarTransito;
import modelo_de_capas.logica.Postulacion;
import modelo_de_capas.logica.TransporteHogarTransito;
import modelo_de_capas.logica.Tratamiento;
import modelo_de_capas.logica.Visita;

public class ControladoraPersistencia {

    AdministradorJpaController admJpa = new AdministradorJpaController();
    AlimentacionJpaController aliJpa = new AlimentacionJpaController();
    AsignacionJpaController asigJpa = new AsignacionJpaController();
    CapturaCastracionJpaController captCastJpa = new CapturaCastracionJpaController();
    CertificadoAdopcionJpaController certfJpa = new CertificadoAdopcionJpaController();
    ColoniaJpaController colJpa = new ColoniaJpaController();
    ControlVeterinarioJpaController contVetJpa = new ControlVeterinarioJpaController();
    DiagnosticoJpaController diagJpa = new DiagnosticoJpaController();
    EstudioJpaController estJpa = new EstudioJpaController();
    FamiliaJpaController famJpa = new FamiliaJpaController();
    GatoJpaController gatoJpa = new GatoJpaController();
    HistorialMedicoJpaController histMedJpa = new HistorialMedicoJpaController();
    HogarTransitoJpaController hogTransJpa = new HogarTransitoJpaController();
    PostulacionJpaController postJpa = new PostulacionJpaController();
    PuntoAvistamientoJpaController puntoAvistJpa = new PuntoAvistamientoJpaController();
    TransporteHogarTransitoJpaController transHogarTransJpa = new TransporteHogarTransitoJpaController();
    TratamientoJpaController tratamientoJpa = new TratamientoJpaController();
    VeterinarioJpaController vetJpa = new VeterinarioJpaController();
    VisitaJpaController visJpa = new VisitaJpaController();
    VoluntarioJpaController volJpa = new VoluntarioJpaController();

    // controlador de Usuario
    UsuarioJpaController usuarioJpa = new UsuarioJpaController();

    // ================== CRUD básicos ==================

    public void crearVoluntario(Voluntario v) {
        volJpa.create(v);
    }

    public void crearGato(Gato g) {
        gatoJpa.create(g);
    }

    public void crearUsuario(Usuario u) throws Exception {
        usuarioJpa.create(u);
    }

    public void crearFamilia(Familia f) {
        famJpa.create(f);
    }

    public void crearVeterinario(Veterinario v) {
        vetJpa.create(v);
    }

    public List<Gato> listarGatos() {
        return gatoJpa.findGatoEntities();
    }

    // ================== Usuarios (login / búsqueda) ==================

    public Usuario buscarUsuarioPorUsernameOEmail(String valor) {
        // intenta por username
        try {
            Usuario u = usuarioJpa.findUsuarioByUsername(valor);
            if (u != null) return u;
        } catch (Exception e) { /* ignora */ }

        // intenta por gmail
        try {
            Usuario u2 = usuarioJpa.findUsuarioByGmail(valor);
            if (u2 != null) return u2;
        } catch (Exception e) { /* ignora */ }

        // fallback
        return null;
    }

    public Usuario traerUsuarioPorUsername(String username) {
        return usuarioJpa.findUsuarioByUsername(username);
    }

    public Usuario traerUsuarioPorGmail(String gmail) {
        return usuarioJpa.findUsuarioByGmail(gmail);
    }

    public Usuario traerUsuarioPorUserOGmail(String valor) {
        Usuario u = usuarioJpa.findUsuarioByUsername(valor);
        if (u == null) {
            u = usuarioJpa.findUsuarioByGmail(valor);
        }
        return u;
    }

    public Administrador traerAdministradorPorUsuario(Usuario u) {
        EntityManager em = admJpa.getEntityManager();
        try {
            TypedQuery<Administrador> q = em.createQuery(
                "SELECT a FROM Administrador a WHERE a.usuario = :usu",
                Administrador.class
            );
            q.setParameter("usu", u);
            List<Administrador> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }
    
    public Administrador buscarAdministradorPorUsuario(Usuario u) {
        EntityManager em = admJpa.getEntityManager();
        try {
            TypedQuery<Administrador> q = em.createQuery(
                "SELECT a FROM Administrador a WHERE a.usuario = :user",
                Administrador.class
            );
            q.setParameter("user", u);
            List<Administrador> lista = q.getResultList();

            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    
    public void crearAdministrador(Administrador a) {
        admJpa.create(a);
    }

    // ================== Avistamientos ==================

    public List<PuntoAvistamiento> listarAvistamientosPorGato(Gato gato) {
        EntityManager em = puntoAvistJpa.getEntityManager();
        try {
            TypedQuery<PuntoAvistamiento> q = em.createQuery(
                "SELECT p FROM PuntoAvistamiento p " +
                "WHERE p.gato = :gato " +
                "ORDER BY p.fecha DESC, p.hora DESC",
                PuntoAvistamiento.class
            );
            q.setParameter("gato", gato);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // ⭐ NUEVO: crear PuntoAvistamiento
    public void crearPuntoAvistamiento(PuntoAvistamiento p) {
        puntoAvistJpa.create(p);
    }

    // ================== Administrador ==================

    /** Devuelve true si existe un Administrador asociado a ese Usuario. */
    public boolean esAdministrador(Usuario u) {
        if (u == null) return false;
        EntityManager em = admJpa.getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(a) FROM Administrador a WHERE a.usuario = :usuario",
                Long.class
            );
            q.setParameter("usuario", u);
            Long count = q.getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    // ================== Voluntario (nuevo soporte para "esVoluntarioActual") ==================

    /** Devuelve true si el Usuario tiene un Voluntario asociado. */
    public boolean esVoluntario(Usuario u) {
        if (u == null) return false;
        EntityManager em = volJpa.getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(v) FROM Voluntario v WHERE v.usuario = :usuario",
                Long.class
            );
            q.setParameter("usuario", u);
            Long count = q.getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    /** Devuelve el Voluntario asociado a ese Usuario (o null si no hay). */
    public Voluntario buscarVoluntarioPorUsuario(Usuario u) {
        if (u == null) return null;
        EntityManager em = volJpa.getEntityManager();
        try {
            TypedQuery<Voluntario> q = em.createQuery(
                "SELECT v FROM Voluntario v WHERE v.usuario = :usuario",
                Voluntario.class
            );
            q.setParameter("usuario", u);
            List<Voluntario> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    // ================== Gato (update) ==================

    /** Edita un gato existente (se usa cuando se actualiza estado de salud / foto desde un avistamiento). */
    public void editarGato(Gato g) {
        try {
            gatoJpa.edit(g);
        } catch (Exception e) {
            System.out.println("❌ Error editando gato: " + e.getMessage());
        }
    }

    // ================== Colonias ==================

    public void crearColonia(Colonia c) {
        colJpa.create(c);
    }

    public List<Colonia> listarColonias() {
        return colJpa.findColoniaEntities();
    }
    
    public void crearAdministradorParaUsuario(Long idUsuario) {

        Usuario usuario = usuarioJpa.findUsuario(idUsuario);
        if (usuario == null) {
            System.out.println("❌ No existe el usuario con ID " + idUsuario);
            return;
        }

        try {
            Administrador admin = new Administrador();
            admin.setUsuario(usuario);
            admin.setListaColonia(new java.util.ArrayList<>());

            admJpa.create(admin);

            System.out.println("✅ Administrador creado para usuario: " + usuario.getUsername());

        } catch (Exception e) {
            System.out.println("❌ Error creando administrador: " + e.getMessage());
        }
    }
    public void actualizarGato(Gato g) {
        try {
            gatoJpa.edit(g);
        } catch (Exception e) {
            System.out.println("❌ Error guardando edición de gato: " + e.getMessage());
        }
    }
    
    

    public Voluntario obtenerVoluntarioPorUsuario(Usuario u) {
        if (u == null) return null;

        EntityManager em = volJpa.getEntityManager();
        try {
            TypedQuery<Voluntario> q = em.createQuery(
                "SELECT v FROM Voluntario v WHERE v.usuario = :user",
                Voluntario.class
            );
            q.setParameter("user", u);

            List<Voluntario> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);

        } finally {
            em.close();
        }
    }
    
    public void crearHogarTransito(HogarTransito h) {
        hogTransJpa.create(h);
    }

    public List<HogarTransito> listarHogarTransito() {
        return hogTransJpa.findHogarTransitoEntities();
    }

    public void guardarHogarTransito(HogarTransito h) {
       hogTransJpa.create(h);
    }
    
        public List<Voluntario> listarVoluntarios() {
        return volJpa.findVoluntarioEntities();
    }

    public List<HogarTransito> listarHogaresTransitoPorVoluntario(Voluntario v) {
        if (v == null) return java.util.Collections.emptyList();

        EntityManager em = hogTransJpa.getEntityManager();
        try {
            TypedQuery<HogarTransito> q = em.createQuery(
                "SELECT h FROM HogarTransito h WHERE h.voluntario = :vol",
                HogarTransito.class
            );
            q.setParameter("vol", v);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
        public void crearAlimentacion(Alimentacion a) {
        aliJpa.create(a);
    }

    public void crearCapturaCastracion(CapturaCastracion c) {
        captCastJpa.create(c);
    }

    public void crearControlVeterinario(ControlVeterinario cv) {
        contVetJpa.create(cv);
    }

    public void crearTransporteHogarTransito(TransporteHogarTransito t) {
        transHogarTransJpa.create(t);
    }

    public List<Familia> listarFamilias() {
        return famJpa.findFamiliaEntities();
    }
    
    public void crearAsignacion(Asignacion a) {
        asigJpa.create(a);
    }

    public void crearVisita(Visita v) {
        visJpa.create(v);
    }
    
    
    public boolean existeAsignacionParaGato(Gato g) {
        if (g == null) return false;

        EntityManager em = asigJpa.getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(a) FROM Asignacion a WHERE a.gato = :gato",
                Long.class
            );
            q.setParameter("gato", g);
            Long count = q.getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
    
    public List<Gato> listarGatosEnHogaresDeVoluntario(Voluntario v) {
        if (v == null) return java.util.Collections.emptyList();

        EntityManager em = transHogarTransJpa.getEntityManager();
        try {
            TypedQuery<Gato> q = em.createQuery(
                "SELECT DISTINCT t.gato " +
                "FROM TransporteHogarTransito t " +
                "WHERE t.hogartransito.voluntario = :vol",
                Gato.class
            );
            q.setParameter("vol", v);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    // ================== Familia (para saber si el usuario actual es familia) ==================

    /** Devuelve true si el Usuario tiene una Familia asociada. */
    public boolean esFamilia(Usuario u) {
        if (u == null) return false;
        EntityManager em = famJpa.getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(f) FROM Familia f WHERE f.usuario = :usuario",
                Long.class
            );
            q.setParameter("usuario", u);
            Long count = q.getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    /** Devuelve la Familia asociada a ese Usuario (o null si no hay). */
    public Familia buscarFamiliaPorUsuario(Usuario u) {
        if (u == null) return null;
        EntityManager em = famJpa.getEntityManager();
        try {
            TypedQuery<Familia> q = em.createQuery(
                "SELECT f FROM Familia f WHERE f.usuario = :usuario",
                Familia.class
            );
            q.setParameter("usuario", u);
            List<Familia> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    public void crearPostulacion(Postulacion p) {
        postJpa.create(p);
    }
    
    public HogarTransito buscarUltimoHogarDeGato(Gato g) {
        if (g == null) return null;

        EntityManager em = transHogarTransJpa.getEntityManager();
        try {
            TypedQuery<TransporteHogarTransito> q = em.createQuery(
                "SELECT t FROM TransporteHogarTransito t " +
                "WHERE t.gato = :gato " +
                "ORDER BY t.fecha DESC, t.hora DESC",
                TransporteHogarTransito.class
            );
            q.setParameter("gato", g);
            q.setMaxResults(1);
            List<TransporteHogarTransito> lista = q.getResultList();
            if (lista.isEmpty()) return null;
            return lista.get(0).getHogartransito();
        } finally {
            em.close();
        }
    }
    
        // ================== Veterinario ==================

    /** Devuelve true si el Usuario tiene un Veterinario asociado. */
    public boolean esVeterinario(Usuario u) {
        if (u == null) return false;
        EntityManager em = vetJpa.getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(v) FROM Veterinario v WHERE v.usuario = :usuario",
                Long.class
            );
            q.setParameter("usuario", u);
            Long count = q.getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    /** Devuelve el Veterinario asociado a ese Usuario (o null si no hay). */
    public Veterinario buscarVeterinarioPorUsuario(Usuario u) {
        if (u == null) return null;
        EntityManager em = vetJpa.getEntityManager();
        try {
            TypedQuery<Veterinario> q = em.createQuery(
                "SELECT v FROM Veterinario v WHERE v.usuario = :usuario",
                Veterinario.class
            );
            q.setParameter("usuario", u);
            List<Veterinario> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    // ================== Historial médico / Diagnósticos ==================

    public HistorialMedico buscarHistorialPorGato(Gato gato) {
        if (gato == null) return null;
        EntityManager em = histMedJpa.getEntityManager();
        try {
            TypedQuery<HistorialMedico> q = em.createQuery(
                "SELECT h FROM HistorialMedico h WHERE h.gato = :gato",
                HistorialMedico.class
            );
            q.setParameter("gato", gato);
            List<HistorialMedico> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        } finally {
            em.close();
        }
    }

    public void crearHistorialMedico(HistorialMedico h) {
        histMedJpa.create(h);
    }

    public void editarHistorialMedico(HistorialMedico h) {
        try {
            histMedJpa.edit(h);
        } catch (Exception e) {
            System.out.println("❌ Error editando historial médico: " + e.getMessage());
        }
    }

    public void crearDiagnostico(Diagnostico d) {
        diagJpa.create(d);
    }

    public void editarDiagnostico(Diagnostico d) {
        try {
            diagJpa.edit(d);
        } catch (Exception e) {
            System.out.println("❌ Error editando diagnóstico: " + e.getMessage());
        }
    }

    public List<Diagnostico> listarDiagnosticosPorHistorial(HistorialMedico hm) {
        if (hm == null) return java.util.Collections.emptyList();
        EntityManager em = diagJpa.getEntityManager();
        try {
            TypedQuery<Diagnostico> q = em.createQuery(
                "SELECT d FROM Diagnostico d " +
                "WHERE d.historialmedico = :hm " +
                "ORDER BY d.fecha DESC, d.id DESC",
                Diagnostico.class
            );
            q.setParameter("hm", hm);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // ================== Estudios ==================

    public void crearEstudio(Estudio e) {
        estJpa.create(e);
    }

    public void editarEstudio(Estudio e) {
        try {
            estJpa.edit(e);
        } catch (Exception ex) {
            System.out.println("❌ Error editando estudio: " + ex.getMessage());
        }
    }

    // ================== Certificados de adopción ==================

    public void crearCertificadoAdopcion(CertificadoAdopcion c) {
        certfJpa.create(c);
    }
        public List<Estudio> listarEstudiosPorDiagnostico(Diagnostico diag) {
        if (diag == null) return java.util.Collections.emptyList();

        EntityManager em = estJpa.getEntityManager();
        try {
            TypedQuery<Estudio> q = em.createQuery(
                "SELECT e FROM Estudio e WHERE e.diagnostico = :diag",
                Estudio.class
            );
            q.setParameter("diag", diag);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    public List<Tratamiento> listarTratamientosPorDiagnostico(Diagnostico diag){
        if (diag == null) return java.util.Collections.emptyList();

        EntityManager em = tratamientoJpa.getEntityManager();
        try {
            TypedQuery<Tratamiento> q = em.createQuery(
                "SELECT t FROM Tratamiento t WHERE t.diagnostico = :diag",
                Tratamiento.class
            );
            q.setParameter("diag", diag);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public void crearTratamiento(Tratamiento t) {
        tratamientoJpa.create(t);
    }

    public void actualizarTratamiento(Tratamiento t) {
        try {
            tratamientoJpa.edit(t);
        } catch (Exception e) {
            System.out.println("❌ Error editando tratamiento: " + e.getMessage());
        }
    }
}
