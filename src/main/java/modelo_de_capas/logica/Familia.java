// Familia.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "familia")
public class Familia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;

    @Basic
    private String numeroTelefono;

    @Basic
    private Integer cantIntegrantes;

    @Basic
    private String direccion;

    @Basic
    private String responsable;

    @Basic
    private Double reputacion;

    @Basic
    private String username;

    @Basic
    private String password;

    @OneToMany (mappedBy="familia")
    private List<Visita> listaVisita = new ArrayList<>();
    
    @OneToMany (mappedBy="familia")
    private List<Postulacion> listaPostulacion = new ArrayList<>();
    
    @OneToMany (mappedBy="familia")
    private List<Asignacion> listaAsignacion = new ArrayList<>();
    
    @OneToOne
    private Usuario usuario;

    public Familia() {
    }

    public Familia(Long id, String nombre, String numeroTelefono, Integer cantIntegrantes, String direccion, String responsable, Double reputacion, String username, String password, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.numeroTelefono = numeroTelefono;
        this.cantIntegrantes = cantIntegrantes;
        this.direccion = direccion;
        this.responsable = responsable;
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

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public Integer getCantIntegrantes() {
        return cantIntegrantes;
    }

    public void setCantIntegrantes(Integer cantIntegrantes) {
        this.cantIntegrantes = cantIntegrantes;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
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

    public List<Visita> getListaVisita() {
        return listaVisita;
    }

    public void setListaVisita(List<Visita> listaVisita) {
        this.listaVisita = listaVisita;
    }

    public List<Postulacion> getListaPostulacion() {
        return listaPostulacion;
    }

    public void setListaPostulacion(List<Postulacion> listaPostulacion) {
        this.listaPostulacion = listaPostulacion;
    }

    public List<Asignacion> getListaAsignacion() {
        return listaAsignacion;
    }

    public void setListaAsignacion(List<Asignacion> listaAsignacion) {
        this.listaAsignacion = listaAsignacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
}
