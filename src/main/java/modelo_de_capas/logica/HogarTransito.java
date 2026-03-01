// HogarTransito.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "hogar_transito")
public class HogarTransito implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;

    @Basic
    private Boolean disponible;

    @Basic
    private String direccion;

    @Basic
    private String caracteristica;

    @ManyToOne
    private Voluntario voluntario;
    
    @OneToMany (mappedBy="hogartransito")
    private List<Visita> listaVisita = new ArrayList<>();
    
    @OneToMany (mappedBy="hogartransito")
    private List<TransporteHogarTransito> listaTransporteHogarTransito = new ArrayList<>();

    public HogarTransito() {
    }

    public HogarTransito(Long id, String nombre, Boolean disponible, String direccion, String caracteristica, Voluntario voluntario) {
        this.id = id;
        this.nombre = nombre;
        this.disponible = disponible;
        this.direccion = direccion;
        this.caracteristica = caracteristica;
        this.voluntario = voluntario;
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

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Voluntario getVoluntario() {
        return voluntario;
    }

    public void setVoluntario(Voluntario voluntario) {
        this.voluntario = voluntario;
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

    
    
}
