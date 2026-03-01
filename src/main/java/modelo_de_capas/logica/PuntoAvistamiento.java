// PuntoAvistamiento.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "punto_avistamiento")
public class PuntoAvistamiento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String coordena; // string con lat,long u otro formato
    
    @Basic
    private LocalDate fecha;

    @Basic
    private LocalTime hora;
    
    @Basic
    private String estadosalud;
    
    @ManyToOne
    private Voluntario voluntario;
    
    @ManyToOne
    private Gato gato;

    public PuntoAvistamiento() {
    }

    public PuntoAvistamiento(Long id, String coordena, LocalDate fecha, LocalTime hora, String estadosalud, Voluntario voluntario, Gato gato) {
        this.id = id;
        this.coordena = coordena;
        this.fecha = fecha;
        this.hora = hora;
        this.estadosalud = estadosalud;
        this.voluntario = voluntario;
        this.gato = gato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoordena() {
        return coordena;
    }

    public void setCoordena(String coordena) {
        this.coordena = coordena;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstadosalud() {
        return estadosalud;
    }

    public void setEstadosalud(String estadosalud) {
        this.estadosalud = estadosalud;
    }

    public Voluntario getVoluntario() {
        return voluntario;
    }

    public void setVoluntario(Voluntario voluntario) {
        this.voluntario = voluntario;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }

}
