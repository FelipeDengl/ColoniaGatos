// Gato.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "gato")
public class Gato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;

    @Basic
    private String color;

    @Basic
    private String caracteristicas;

    @Basic
    private String foto; // ruta o nombre de archivo

    @Basic
    private String estadoSalud;
    
    @Column(name = "foto_path")
    private String fotoPath;
    
    @Basic
    private String qr;

    @Basic
    private Boolean aptoAdopcion;

    @Basic
    private LocalDate fechaRegistro; // ejemplo de fecha

    @OneToMany (mappedBy="gato")
    private List<PuntoAvistamiento> listaPuntoAvistamiento = new ArrayList<>();
    
    @OneToMany (mappedBy="gato")
    private List<Postulacion> listaPostulacion = new ArrayList<>();
    
    @OneToMany (mappedBy="gato")
    private List<CertificadoAdopcion> listaCertificadoAdopcion = new ArrayList<>();
    
    @OneToMany (mappedBy="gato")
    private List<Visita> listaVisita = new ArrayList<>();
    
    @OneToMany (mappedBy="gato")
    private List<TransporteHogarTransito> listaTransporteHogarTransito = new ArrayList<>();
    
    @OneToMany (mappedBy="gato")
    private List<CapturaCastracion> listaCapturaCastracion = new ArrayList<>();
    
    @OneToMany (mappedBy="gato")
    private List<ControlVeterinario> listaControlVeterinario = new ArrayList<>();
    
    @OneToMany (mappedBy="gato")
    private List<Alimentacion> listaAlimentacion = new ArrayList<>();
    
    @ManyToOne 
    private Colonia colonia;

    public Gato() {
    }

    public Gato(Long id, String nombre, String color, String caracteristicas, String foto, String estadoSalud, String fotoPath, String qr, Boolean aptoAdopcion, LocalDate fechaRegistro, Colonia colonia) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.caracteristicas = caracteristicas;
        this.foto = foto;
        this.estadoSalud = estadoSalud;
        this.fotoPath = fotoPath;
        this.qr = qr;
        this.aptoAdopcion = aptoAdopcion;
        this.fechaRegistro = fechaRegistro;
        this.colonia = colonia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public Boolean getAptoAdopcion() {
        return aptoAdopcion;
    }

    public void setAptoAdopcion(Boolean aptoAdopcion) {
        this.aptoAdopcion = aptoAdopcion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<PuntoAvistamiento> getListaPuntoAvistamiento() {
        return listaPuntoAvistamiento;
    }

    public void setListaPuntoAvistamiento(List<PuntoAvistamiento> listaPuntoAvistamiento) {
        this.listaPuntoAvistamiento = listaPuntoAvistamiento;
    }

    public List<Postulacion> getListaPostulacion() {
        return listaPostulacion;
    }

    public void setListaPostulacion(List<Postulacion> listaPostulacion) {
        this.listaPostulacion = listaPostulacion;
    }

    public List<CertificadoAdopcion> getListaCertificadoAdopcion() {
        return listaCertificadoAdopcion;
    }

    public void setListaCertificadoAdopcion(List<CertificadoAdopcion> listaCertificadoAdopcion) {
        this.listaCertificadoAdopcion = listaCertificadoAdopcion;
    }

    public List<Visita> getListaVisita() {
        return listaVisita;
    }

    public void setListaVisita(List<Visita> listaVisita) {
        this.listaVisita = listaVisita;
    }

    public List<TransporteHogarTransito> getListaTransporteHogarTransito() {
        return listaTransporteHogarTransito;
    }

    public void setListaTransporteHogarTransito(List<TransporteHogarTransito> listaTransporteHogarTransito) {
        this.listaTransporteHogarTransito = listaTransporteHogarTransito;
    }

    public List<CapturaCastracion> getListaCapturaCastracion() {
        return listaCapturaCastracion;
    }

    public void setListaCapturaCastracion(List<CapturaCastracion> listaCapturaCastracion) {
        this.listaCapturaCastracion = listaCapturaCastracion;
    }

    public List<ControlVeterinario> getListaControlVeterinario() {
        return listaControlVeterinario;
    }

    public void setListaControlVeterinario(List<ControlVeterinario> listaControlVeterinario) {
        this.listaControlVeterinario = listaControlVeterinario;
    }

    public List<Alimentacion> getListaAlimentacion() {
        return listaAlimentacion;
    }

    public void setListaAlimentacion(List<Alimentacion> listaAlimentacion) {
        this.listaAlimentacion = listaAlimentacion;
    }

    public Colonia getColonia() {
        return colonia;
    }

    public void setColonia(Colonia colonia) {
        this.colonia = colonia;
    }

    public String toString() {
        return nombre + " (" + color + ")";
    }
    
}
