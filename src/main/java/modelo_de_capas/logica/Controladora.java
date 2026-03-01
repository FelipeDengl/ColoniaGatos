package modelo_de_capas.logica;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelo_de_capas.persistencia.ControladoraPersistencia;

public class Controladora {

    private ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    private Usuario usuarioLogueado;

    // ===================== CREACIÓN DE USUARIOS Y ROLES =====================

    public boolean existeUsuario(String username) {
        Usuario u = controlPersis.traerUsuarioPorUsername(username);
        return u != null;
    }

    public void crearAdministradorParaUsuario(Long idUsuario) {
        controlPersis.crearAdministradorParaUsuario(idUsuario);
    }
    
    public void crearAdministrador(Administrador adm) {
        controlPersis.crearAdministrador(adm);
    }

    public void crearUsuario(Usuario u) {
        try {
            controlPersis.crearUsuario(u);
        } catch (Exception e) {
            System.out.println("❌ Error creando usuario: " + e.getMessage());
        }
    }
    
    public boolean esAdministradorActual() {
        return traerAdministradorActual() != null;
    }

    public Administrador getAdministradorDeUsuario(Usuario u) {
        if (u == null) return null;
        return controlPersis.buscarAdministradorPorUsuario(u);
    }

    // ===================== NUEVO: VOLUNTARIO ACTUAL =====================

    /** Devuelve true si el usuario logueado es un Voluntario. */
    public boolean esVoluntarioActual() {
        if (usuarioLogueado == null) return false;
        return controlPersis.esVoluntario(usuarioLogueado);
    }

    /** Devuelve el Voluntario asociado al usuario logueado (o null si no lo es). */
    public Voluntario obtenerVoluntarioActual() {
        if (usuarioLogueado == null) return null;
        return controlPersis.buscarVoluntarioPorUsuario(usuarioLogueado);
    }

    // ===================== CREACIÓN DE ROLES =====================

    public void crearVoluntario(Voluntario vol) {
        try {
            controlPersis.crearVoluntario(vol);
        } catch (Exception e) {
            System.out.println("❌ Error creando voluntario: " + e.getMessage());
        }
    }

    public void crearFamilia(Familia f) {
        try {
            controlPersis.crearFamilia(f);
        } catch (Exception e) {
            System.out.println("❌ Error creando familia: " + e.getMessage());
        }
    }

    public void crearVeterinario(Veterinario v) {
        try {
            controlPersis.crearVeterinario(v);
        } catch (Exception e) {
            System.out.println("❌ Error creando veterinario: " + e.getMessage());
        }
    }

    // ===================== LOGIN =====================

    public boolean login(String usernameOrEmail, String password) {

        Usuario u = controlPersis.buscarUsuarioPorUsernameOEmail(usernameOrEmail);

        if (u != null && u.getPassword() != null && u.getPassword().equals(password)) {
            usuarioLogueado = u;
            return true;
        }
        return false;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void logout() {
        usuarioLogueado = null;
    }

    // ===================== ROLES ESPECIALES (ADMIN) =====================

    /** Devuelve el Administrador asociado al usuario logueado (o null). */
    public Administrador obtenerAdministradorActual() {
        if (usuarioLogueado == null) return null;
        return controlPersis.buscarAdministradorPorUsuario(usuarioLogueado);
    }
    
    public Administrador traerAdministradorActual() {
        if (usuarioLogueado == null) return null;
        return controlPersis.traerAdministradorPorUsuario(usuarioLogueado);
    }

    // ===================== GATOS =====================

    public void crearGato(Gato gato, File fotoSeleccionada) {
        try {
            if (fotoSeleccionada != null) {
                String ruta = FileStorage.saveImage(fotoSeleccionada);
                gato.setFotoPath(ruta);
            }
            controlPersis.crearGato(gato);
        } catch (Exception e) {
            System.out.println("❌ Error guardando gato: " + e.getMessage());
        }
    }

    public List<Gato> listarGatos() {
        return controlPersis.listarGatos();
    }

    // ===================== AVISTAMIENTOS =====================

    public List<PuntoAvistamiento> listarAvistamientosPorGato(Gato gato) {
        return controlPersis.listarAvistamientosPorGato(gato);
    }

    /**
     * NUEVO:
     * Crea un punto de avistamiento para el voluntario logueado.
     * - coordena: string "lat,long" o similar
     * - gato: puede ser uno existente o uno recién creado
     * - estadoSalud: se guarda en el PuntoAvistamiento y, si viene gato != null,
     *                se actualiza también en el Gato.
     */
    public PuntoAvistamiento crearPuntoAvistamiento(String coordena, Gato gato, String estadoSalud) {
    Voluntario vol = obtenerVoluntarioActual();
    if (vol == null) {
        throw new IllegalStateException("Solo un voluntario logueado puede registrar un avistamiento.");
    }

    // Si hay gato pero sin id, mejor avisar claro
    if (gato != null && gato.getId() == null) {
        throw new IllegalStateException("El gato debe estar guardado en la base de datos antes de crear el avistamiento.");
    }

    PuntoAvistamiento pa = new PuntoAvistamiento();
    pa.setCoordena(coordena);
    pa.setFecha(LocalDate.now());
    pa.setHora(LocalTime.now());
    pa.setEstadosalud(estadoSalud);
    pa.setVoluntario(vol);
    pa.setGato(gato);

    // actualizar estado de salud del gato si corresponde
    if (gato != null && estadoSalud != null && !estadoSalud.isBlank()) {
        gato.setEstadoSalud(estadoSalud);
        controlPersis.editarGato(gato);
    }

    controlPersis.crearPuntoAvistamiento(pa);
    return pa;
}


    /**
     * NUEVO:
     * Devuelve las colonias que contienen (o que se quieren considerar para) una coordenada.
     * Por ahora, como no sabemos el nombre de los campos de Colonia,
     * se devuelve la lista completa como fallback.
     *
     * Cuando tengas definidos los límites de la Colonia (x1,y1,x2,y2 o latMin/latMax, etc.),
     * se puede completar la función puntoDentroDeColonia().
     */
    public List<Colonia> buscarColoniasPorCoordenada(String coordena) {
        List<Colonia> todas = listarColonias();
        List<Colonia> resultado = new ArrayList<>();

        if (todas == null) return resultado;

        if (coordena == null || coordena.isBlank()) {
            // sin coordenada -> no matchea ninguna
            return resultado;
        }

        String[] partes = coordena.split(",");
        if (partes.length != 2) {
            // formato raro -> ninguna
            return resultado;
        }

        try {
            double lat = Double.parseDouble(partes[0].trim());
            double lon = Double.parseDouble(partes[1].trim());

            for (Colonia c : todas) {
                if (puntoDentroDeColonia(c, lat, lon)) {
                    resultado.add(c);
                }
            }

            return resultado; // si está vacío, así se queda

        } catch (NumberFormatException e) {
            // coordenadas no numéricas -> ninguna
            return resultado;
        }
    }


    /**
     * NUEVO (helper):
     * Lógica de si un punto está dentro de una colonia.
     *
     * IMPORTANTE:
     * - De momento devuelve true para todas las colonias para no romper.
     * - Cuando me pases la clase Colonia con sus campos de límites,
     *   lo ajustamos, por ejemplo:
     *
     *   double xMin = c.getLatMin();
     *   double xMax = c.getLatMax();
     *   double yMin = c.getLonMin();
     *   double yMax = c.getLonMax();
     *   return lat >= xMin && lat <= xMax && lon >= yMin && lon <= yMax;
     */
    private boolean puntoDentroDeColonia(Colonia c, double lat, double lon) {
        // TODO: reemplazar por la lógica real usando los atributos de Colonia
        return true;
    }

    // ===================== COLONIAS =====================

    public void crearColonia(Colonia c) {
        controlPersis.crearColonia(c);
    }

    public List<Colonia> listarColonias() {
        return controlPersis.listarColonias();
    }
    
    public List<Colonia> buscarColoniasQueContienenPunto(double lat, double lon) {
        // acá ya no pasamos por el string, vamos directo
        List<Colonia> todas = listarColonias();
        List<Colonia> resultado = new ArrayList<>();

        if (todas == null) return resultado;

        for (Colonia c : todas) {
            if (puntoDentroDeColonia(c, lat, lon)) {
                resultado.add(c);
            }
        }
        return resultado; // si no hay ninguna, vuelve lista vacía
    }
    public PuntoAvistamiento registrarAvistamientoConGatoExistente(
        double lat,
        double lon,
        Gato gatoSeleccionado,
        String estadoSalud
    ) {
        String coord = lat + "," + lon;
        return crearPuntoAvistamiento(coord, gatoSeleccionado, estadoSalud);
    }
    
    public PuntoAvistamiento registrarAvistamientoConGatoExistente(
        String coordena,
        Gato gatoSeleccionado,
        Colonia coloniaAsociada,
        String nuevoEstadoSalud,
        java.io.File fotoNueva
) {
    if (gatoSeleccionado == null) {
        throw new IllegalArgumentException("El gato seleccionado no puede ser null.");
    }

    // Asociar colonia si corresponde
    if (coloniaAsociada != null && gatoSeleccionado.getColonia() == null) {
        gatoSeleccionado.setColonia(coloniaAsociada);
    }

    // Actualizar estado de salud del gato si se indicó
    if (nuevoEstadoSalud != null && !nuevoEstadoSalud.isBlank()) {
        gatoSeleccionado.setEstadoSalud(nuevoEstadoSalud);
    }

    // 🔴 CASO CLAVE: ¿el gato ya está en la BD o es nuevo?
    if (gatoSeleccionado.getId() == null) {
        // 👉 Es un gato nuevo: lo creamos en la base antes de usarlo en el avistamiento.
        // Usamos el mismo flujo que cuando creás gatos normalmente.
        // crearGato ya se encarga de guardar fotoPath si fotoNueva != null.
        try {
            crearGato(gatoSeleccionado, fotoNueva);
        } catch (Exception e) {
            System.out.println("❌ Error guardando gato nuevo desde avistamiento: " + e.getMessage());
        }

    } else {
        // 👉 Gato ya existente: solo actualizamos foto si mandaron una nueva
        if (fotoNueva != null) {
            try {
                String ruta = FileStorage.saveImage(fotoNueva);
                gatoSeleccionado.setFotoPath(ruta);
            } catch (Exception e) {
                System.out.println("⚠ No se pudo guardar la nueva foto del gato: " + e.getMessage());
            }
        }

        // Guardar cambios del gato existente
        controlPersis.editarGato(gatoSeleccionado);
    }

    // Ahora sí, el gato DEBE tener un id válido → se puede crear el avistamiento.
    return crearPuntoAvistamiento(
            coordena,
            gatoSeleccionado,
            nuevoEstadoSalud
    );
}

    
    public PuntoAvistamiento registrarAvistamientoConGatoNuevo(
        String coordena,
        Colonia coloniaAsociada,
        String nombre,
        String color,
        String caracteristicas,
        String zona,
        String estadoSalud,
        java.io.File fotoSeleccionada
) {
    Gato g = new Gato();
        // Nombre opcional → si viene vacío, se usa "Gato"
        if (nombre == null || nombre.isBlank()) {
            g.setNombre("Gato");
        } else {
            g.setNombre(nombre.trim());
        }

        g.setColor(color != null ? color.trim() : null);
        g.setCaracteristicas(caracteristicas != null ? caracteristicas.trim() : null);
        g.setEstadoSalud(estadoSalud != null ? estadoSalud.trim() : null);

        // Si tu entidad Gato tiene campo zona, descomentá esto:
        // g.setZona(zona != null ? zona.trim() : null);

        if (coloniaAsociada != null) {
            g.setColonia(coloniaAsociada);
        }

        // Usa tu método existente (guarda fotoPath si fotoSeleccionada != null)
        crearGato(g, fotoSeleccionada);

        // Crear el punto de avistamiento con ese gato nuevo
        return crearPuntoAvistamiento(
                coordena,
                g,
                estadoSalud
        );
    }
    
    // ======================================================
    // ACTUALIZAR GATO
    // ======================================================
    public void actualizarGato(Gato gato, File fotoSeleccionada) {

        try {
            if (fotoSeleccionada != null) {
                String ruta = FileStorage.saveImage(fotoSeleccionada);
                gato.setFotoPath(ruta);
            }

            controlPersis.actualizarGato(gato);

        } catch (Exception e) {
            System.out.println("❌ Error actualizando gato: " + e.getMessage());
        }
    }
    
    public void crearHogarTransito(String nombre, Boolean disponible, String direccion, String caracteristica) {

        Voluntario vol = obtenerVoluntarioActual();
        if (vol == null) {
            throw new IllegalStateException("Solo un voluntario logueado puede crear un hogar transitorio.");
        }

        HogarTransito ht = new HogarTransito();
        ht.setNombre(nombre);
        ht.setDisponible(disponible);
        ht.setDireccion(direccion);
        ht.setCaracteristica(caracteristica);
        ht.setVoluntario(vol);

        controlPersis.crearHogarTransito(ht);
    }

    public Voluntario obtenerVoluntarioPorUsuario(Usuario u) {
        return controlPersis.obtenerVoluntarioPorUsuario(u);
    }

    public List<HogarTransito> listarHogaresTransito() {
        return controlPersis.listarHogarTransito();
    }
    public void guardarHogarTransito (HogarTransito h){
        controlPersis.guardarHogarTransito(h);
    }
    // ===================== VOLUNTARIOS / HOGARES =====================

    public List<Voluntario> listarVoluntarios() {
        return controlPersis.listarVoluntarios();
    }

    public List<HogarTransito> listarHogaresTransitoPorVoluntario(Voluntario v) {
        return controlPersis.listarHogaresTransitoPorVoluntario(v);
    }

      public void registrarAlimentacion(Gato gato, String tipo, String cantidad, String ubicacion) {
        Voluntario vol = obtenerVoluntarioActual();
        if (vol == null) {
            throw new IllegalStateException("Solo un voluntario logueado puede registrar tareas.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }

        Date ahora = new Date();

        Alimentacion a = new Alimentacion();
        a.setTipo(tipo);
        a.setCantidad(cantidad);
        a.setUbicacion(ubicacion);
        a.setFecha(ahora);
        a.setHora(ahora);
        a.setVoluntario(vol);
        a.setGato(gato);

        controlPersis.crearAlimentacion(a);
    }

    public void registrarCapturaCastracion(Gato gato, String nombreVet, String numeroVet, String documento, String ubicacion) {
        Voluntario vol = obtenerVoluntarioActual();
        if (vol == null) {
            throw new IllegalStateException("Solo un voluntario logueado puede registrar tareas.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }

        Date ahora = new Date();

        CapturaCastracion c = new CapturaCastracion();
        c.setNombreVeterinario(nombreVet);
        c.setNumeroVeterinario(numeroVet);
        c.setDocumento(documento);
        c.setUbicacion(ubicacion);
        c.setFecha(ahora);
        c.setHora(ahora);
        c.setVoluntario(vol);
        c.setGato(gato);

        controlPersis.crearCapturaCastracion(c);
    }

    public void registrarControlVeterinario(Gato gato, String nombreVet, String numeroVet, String tipo, String documento, String ubicacion) {
        Voluntario vol = obtenerVoluntarioActual();
        if (vol == null) {
            throw new IllegalStateException("Solo un voluntario logueado puede registrar tareas.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }

        Date ahora = new Date();

        ControlVeterinario cv = new ControlVeterinario();
        cv.setNombreVeterinario(nombreVet);
        cv.setNumeroVeterinario(numeroVet);
        cv.setTipo(tipo);
        cv.setDocumento(documento);
        cv.setUbicacion(ubicacion);
        cv.setFecha(ahora);
        cv.setHora(ahora);
        cv.setVoluntario(vol);
        cv.setGato(gato);

        controlPersis.crearControlVeterinario(cv);
    }

    public void registrarTransporteHogarTransito(Gato gato,
                                                 String ubicacion,
                                                 String aclaracion,
                                                 LocalTime horaEntrega,
                                                 HogarTransito hogar) {
        Voluntario vol = obtenerVoluntarioActual();
        if (vol == null) {
            throw new IllegalStateException("Solo un voluntario logueado puede registrar tareas.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }
        if (hogar == null) {
            throw new IllegalArgumentException("El hogar de tránsito no puede ser null.");
        }

        Date ahora = new Date();

        TransporteHogarTransito t = new TransporteHogarTransito();
        t.setHoraEntrega(horaEntrega);
        t.setAclaracion(aclaracion);
        t.setUbicacion(ubicacion);
        t.setFecha(ahora);
        t.setHora(ahora);
        t.setVoluntario(vol);
        t.setGato(gato);
        t.setHogartransito(hogar);

        controlPersis.crearTransporteHogarTransito(t);
    }

    public List<HogarTransito> listarHogaresTransitoDisponibles() {
        List<HogarTransito> todos = listarHogaresTransito();
        java.util.List<HogarTransito> res = new java.util.ArrayList<>();
        if (todos == null) return res;

        for (HogarTransito h : todos) {
            if (h.getDisponible() != null && h.getDisponible()) {
                res.add(h);
            }
        }
        return res;
    }
    
    public List<Familia> listarFamilias() {
        return controlPersis.listarFamilias();
    }
    
    public boolean gatoTieneAsignacion(Gato g) {
        return controlPersis.existeAsignacionParaGato(g);
    }

    public List<Gato> listarGatosSinAsignacion() {
        List<Gato> todos = listarGatos();
        List<Gato> res = new ArrayList<>();
        if (todos == null) return res;

        for (Gato g : todos) {
            if (!gatoTieneAsignacion(g)) {
                res.add(g);
            }
        }
        return res;
    }
    public List<Gato> listarGatosEnMisHogaresNoAsignados() {
        Voluntario vol = obtenerVoluntarioActual();
        List<Gato> res = new ArrayList<>();
        if (vol == null) return res;

        List<Gato> enHogares = controlPersis.listarGatosEnHogaresDeVoluntario(vol);
        if (enHogares == null) return res;

        for (Gato g : enHogares) {
            if (!gatoTieneAsignacion(g)) {
                res.add(g);
            }
        }
        return res;
    }
    
    public void registrarAsignacionFamilia(Gato gato, Familia familia) {
        Voluntario vol = obtenerVoluntarioActual();
        if (vol == null) {
            throw new IllegalStateException("Solo un voluntario logueado puede asignar un gato a una familia.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }
        if (familia == null) {
            throw new IllegalArgumentException("La familia no puede ser null.");
        }

        if (gatoTieneAsignacion(gato)) {
            throw new IllegalStateException("Este gato ya está asignado a una familia.");
        }

        Asignacion a = new Asignacion();
        a.setFecha(LocalDate.now());
        a.setHora(LocalTime.now());
        a.setVoluntario(vol);
        a.setFamilia(familia);
        a.setGato(gato);

        controlPersis.crearAsignacion(a);
    }
    
    public void registrarVisitaSeguimiento(Gato gato, Familia familia, String descripcion) {
        Voluntario vol = obtenerVoluntarioActual();
        if (vol == null) {
            throw new IllegalStateException("Solo un voluntario logueado puede registrar visitas.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }
        if (familia == null) {
            throw new IllegalArgumentException("La familia no puede ser null.");
        }

        Visita v = new Visita();
        v.setFecha(LocalDate.now());
        v.setHora(LocalTime.now());
        v.setDescripcion(descripcion);
        v.setVoluntario(vol);
        v.setFamilia(familia);
        v.setGato(gato);
        v.setHogartransito(null); // por ahora sin hogar

        controlPersis.crearVisita(v);
    }
    
    // ===================== FAMILIA ACTUAL =====================

    /** Devuelve true si el usuario logueado es una Familia. */
    public boolean esFamiliaActual() {
        if (usuarioLogueado == null) return false;
        return controlPersis.esFamilia(usuarioLogueado);
    }

    /** Devuelve la Familia asociada al usuario logueado (o null). */
    public Familia obtenerFamiliaActual() {
        if (usuarioLogueado == null) return null;
        return controlPersis.buscarFamiliaPorUsuario(usuarioLogueado);
    }
    
    public HogarTransito obtenerHogarActualDeGato(Gato g) {
        return controlPersis.buscarUltimoHogarDeGato(g);
    }
    
    
    /**
    * Devuelve los gatos que:
    *  - no están asignados a una familia
    *  - tienen al menos un transporte a hogar de tránsito (es decir, están / estuvieron en hogar)
    *  - si soloAptos == true, además tienen aptoAdopcion == true
    */
   public List<Gato> listarGatosEnHogarParaAdopcion(boolean soloAptos) {
       List<Gato> todos = listarGatosSinAsignacion();
       List<Gato> res = new ArrayList<>();
       if (todos == null) return res;

       for (Gato g : todos) {
           // si solo queremos aptos
           if (soloAptos) {
               Boolean apto = g.getAptoAdopcion();
               if (apto == null || !apto) {
                   continue;
               }
           }

           // verificamos que tenga un hogar de tránsito asociado (último transporte)
           HogarTransito hogar = obtenerHogarActualDeGato(g);
           if (hogar != null) {
               res.add(g);
           }
       }

       return res;
   }
   
   public void crearPostulacionParaFamiliaActual(Gato gato) {
        Familia fam = obtenerFamiliaActual();
        if (fam == null) {
            throw new IllegalStateException("Solo una familia logueada puede postularse para adopción.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }

        Postulacion p = new Postulacion();
        p.setFecha(LocalDate.now());
        p.setEstado("Pendiente");  // como pediste
        p.setFamilia(fam);
        p.setGato(gato);

        controlPersis.crearPostulacion(p);
    }
   
       // ===================== NUEVO: VETERINARIO ACTUAL =====================

    public boolean esVeterinarioActual() {
        if (usuarioLogueado == null) return false;
        return controlPersis.esVeterinario(usuarioLogueado);
    }

    public Veterinario obtenerVeterinarioActual() {
        if (usuarioLogueado == null) return null;
        return controlPersis.buscarVeterinarioPorUsuario(usuarioLogueado);
    }

        // ===================== HISTORIAL MÉDICO / DIAGNÓSTICOS =====================

    public HistorialMedico obtenerOCrearHistorialMedico(Gato gato) {
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }

        HistorialMedico hm = controlPersis.buscarHistorialPorGato(gato);
        if (hm == null) {
            hm = new HistorialMedico();
            hm.setGato(gato);
            hm.setNotas(null);
            controlPersis.crearHistorialMedico(hm);
        }
        return hm;
    }

    public List<Diagnostico> listarDiagnosticosPorGato(Gato gato) {
        HistorialMedico hm = controlPersis.buscarHistorialPorGato(gato);
        if (hm == null) return new ArrayList<>();
        return controlPersis.listarDiagnosticosPorHistorial(hm);
    }

    public Diagnostico crearDiagnosticoParaGato(Gato gato, String nombre) {
        Veterinario vet = obtenerVeterinarioActual();
        if (vet == null) {
            throw new IllegalStateException("Solo un veterinario logueado puede crear diagnósticos.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }

        HistorialMedico hm = obtenerOCrearHistorialMedico(gato);

        Diagnostico d = new Diagnostico();
        d.setNombre(nombre);
        d.setFecha(LocalDate.now());
        d.setHistorialmedico(hm);
        d.setVeterinario(vet);

        controlPersis.crearDiagnostico(d);
        return d;
    }

    public void actualizarDiagnostico(Diagnostico d) {
        controlPersis.editarDiagnostico(d);
    }

    public Estudio crearEstudioParaDiagnostico(Diagnostico diag, String nombre, String descripcion, String documento) {
        if (diag == null) {
            throw new IllegalArgumentException("El diagnóstico no puede ser null.");
        }

        Estudio e = new Estudio();
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        e.setDocumento(documento);
        e.setDiagnostico(diag);

        controlPersis.crearEstudio(e);
        return e;
    }

    public void actualizarEstudio(Estudio e) {
        controlPersis.editarEstudio(e);
    }

    public CertificadoAdopcion emitirCertificadoAptitud(Gato gato, String comentario) {
        Veterinario vet = obtenerVeterinarioActual();
        if (vet == null) {
            throw new IllegalStateException("Solo un veterinario logueado puede emitir certificados.");
        }
        if (gato == null) {
            throw new IllegalArgumentException("El gato no puede ser null.");
        }

        CertificadoAdopcion cert = new CertificadoAdopcion();
        cert.setComentario(comentario);
        cert.setFecha(LocalDate.now());
        cert.setGato(gato);
        cert.setVeterinario(vet);

        controlPersis.crearCertificadoAdopcion(cert);

        // marcar gato como apto para adopción
        gato.setAptoAdopcion(Boolean.TRUE);
        controlPersis.editarGato(gato);

        return cert;
    }
    
    public List<Estudio> listarEstudiosPorDiagnostico(Diagnostico diag) {
        return controlPersis.listarEstudiosPorDiagnostico(diag);
    }

    // --- TRATAMIENTOS ---

    public List<Tratamiento> listarTratamientosPorDiagnostico(Diagnostico diag) {
        return controlPersis.listarTratamientosPorDiagnostico(diag);
    }
    public Tratamiento crearTratamientoParaDiagnostico(Diagnostico diag,
                                                   String nombre,
                                                   LocalDate fecha,
                                                   String descripcion) {
        Tratamiento t = new Tratamiento();
        t.setNombre(nombre);
        t.setFecha(fecha);
        t.setDescripcion(descripcion);
        t.setDiagnostico(diag);

        controlPersis.crearTratamiento(t);
        return t;
    }

    public void actualizarTratamiento(Tratamiento t) {
        controlPersis.actualizarTratamiento(t);
    }


}   
