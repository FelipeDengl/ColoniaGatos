// Diagnostico.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "diagnostico")
public class Diagnostico implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;

    @Basic
    private LocalDate fecha;
   
    @OneToMany (mappedBy="diagnostico")
    private List<Estudio> listaEstudio = new ArrayList<>();
    
    @OneToMany (mappedBy="diagnostico")
    private List<Tratamiento> listaTratamiento = new ArrayList<>();
    
    @ManyToOne 
    private HistorialMedico historialmedico;
    
    @ManyToOne 
    private Veterinario veterinario;

    public Diagnostico() {
    }

    public Diagnostico(Long id, String nombre, LocalDate fecha, HistorialMedico historialmedico, Veterinario veterinario) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.historialmedico = historialmedico;
        this.veterinario = veterinario;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<Estudio> getListaEstudio() {
        return listaEstudio;
    }

    public void setListaEstudio(List<Estudio> listaEstudio) {
        this.listaEstudio = listaEstudio;
    }

    public List<Tratamiento> getListaTratamiento() {
        return listaTratamiento;
    }

    public void setListaTratamiento(List<Tratamiento> listaTratamiento) {
        this.listaTratamiento = listaTratamiento;
    }

    public HistorialMedico getHistorialmedico() {
        return historialmedico;
    }

    public void setHistorialmedico(HistorialMedico historialmedico) {
        this.historialmedico = historialmedico;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
    
    

    
}
