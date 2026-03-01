// Asignacion.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "asignacion")
public class Asignacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private LocalDate fecha;

    @Basic
    private LocalTime hora;

    @ManyToOne
    private Voluntario voluntario;
    
    @ManyToOne
    private Familia familia;
    
    @OneToOne
    private Gato gato;

    public Asignacion() {
    }

    public Asignacion(Long id, LocalDate fecha, LocalTime hora, Voluntario voluntario, Familia familia, Gato gato) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.voluntario = voluntario;
        this.familia = familia;
        this.gato = gato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Voluntario getVoluntario() {
        return voluntario;
    }

    public void setVoluntario(Voluntario voluntario) {
        this.voluntario = voluntario;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }
    
    
}
