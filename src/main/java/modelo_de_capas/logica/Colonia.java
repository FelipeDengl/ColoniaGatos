// Colonia.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "colonia")
public class Colonia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;

    @Basic
    private Double latMin;

    @Basic
    private Double latMax;

    @Basic
    private Double lonMin;

    @Basic
    private Double lonMax;
    @Basic
    private String descripcion;
    
    @OneToMany (mappedBy="colonia")
    private List<Gato> listaGato = new ArrayList<>();
    
    @ManyToOne 
    private Administrador administrador;

    public Colonia() {
    }

    public Colonia(Long id, String nombre, Double latMin, Double latMax, Double lonMin, Double lonMax, String descripcion, Administrador administrador) {
        this.id = id;
        this.nombre = nombre;
        this.latMin = latMin;
        this.latMax = latMax;
        this.lonMin = lonMin;
        this.lonMax = lonMax;
        this.descripcion = descripcion;
        this.administrador = administrador;
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

    public Double getLatMin() {
        return latMin;
    }

    public void setLatMin(Double latMin) {
        this.latMin = latMin;
    }

    public Double getLatMax() {
        return latMax;
    }

    public void setLatMax(Double latMax) {
        this.latMax = latMax;
    }

    public Double getLonMin() {
        return lonMin;
    }

    public void setLonMin(Double lonMin) {
        this.lonMin = lonMin;
    }

    public Double getLonMax() {
        return lonMax;
    }

    public void setLonMax(Double lonMax) {
        this.lonMax = lonMax;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Gato> getListaGato() {
        return listaGato;
    }

    public void setListaGato(List<Gato> listaGato) {
        this.listaGato = listaGato;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

}
