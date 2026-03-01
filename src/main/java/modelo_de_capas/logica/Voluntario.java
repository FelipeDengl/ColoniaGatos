// Voluntario.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "voluntario")
public class Voluntario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    private String nombre;

    @Basic
    private String dni;

    @Basic
    private Double reputacion;

    @Basic
    private String username;

    @Basic
    private String password;
    
    @OneToOne
    private Usuario usuario;
    
    @OneToMany (mappedBy="voluntario")
    private List<Visita> listaVisitas = new ArrayList<>();
    
    @OneToMany (mappedBy="voluntario")
    private List<HogarTransito> listaHogarTransito = new ArrayList<>();
    
    @OneToMany (mappedBy="voluntario")
    private List<TransporteHogarTransito> listaTransporteHogarTransito = new ArrayList<>();
    
    @OneToMany (mappedBy="voluntario")
    private List<CapturaCastracion> listaCapturaCastracion = new ArrayList<>();
    
    @OneToMany (mappedBy="voluntario")
    private List<ControlVeterinario> listaControlVeterinario = new ArrayList<>();
    
    @OneToMany (mappedBy="voluntario")
    private List<Alimentacion> listaAlimentacion = new ArrayList<>();
    
    @OneToMany (mappedBy="voluntario")
    private List<PuntoAvistamiento> listaPuntoAvistamiento = new ArrayList<>();
    
    @OneToMany (mappedBy="voluntario")
    private List<Asignacion> listaAsignacion = new ArrayList<>();

    public Voluntario() {
    }

    public Voluntario(Long id, String nombre, String dni, Double reputacion, String username, String password, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.reputacion = reputacion;
        this.username = username;
        this.password = password;
        this.usuario = usuario;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Double getReputacion() {
        return reputacion;
    }

    public void setReputacion(Double reputacion) {
        this.reputacion = reputacion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Visita> getListaVisitas() {
        return listaVisitas;
    }

    public void setListaVisitas(List<Visita> listaVisitas) {
        this.listaVisitas = listaVisitas;
    }

    public List<HogarTransito> getListaHogarTransito() {
        return listaHogarTransito;
    }

    public void setListaHogarTransito(List<HogarTransito> listaHogarTransito) {
        this.listaHogarTransito = listaHogarTransito;
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

    public List<PuntoAvistamiento> getListaPuntoAvistamiento() {
        return listaPuntoAvistamiento;
    }

    public void setListaPuntoAvistamiento(List<PuntoAvistamiento> listaPuntoAvistamiento) {
        this.listaPuntoAvistamiento = listaPuntoAvistamiento;
    }

    public List<Asignacion> getListaAsignacion() {
        return listaAsignacion;
    }

    public void setListaAsignacion(List<Asignacion> listaAsignacion) {
        this.listaAsignacion = listaAsignacion;
    }
    
    
}
