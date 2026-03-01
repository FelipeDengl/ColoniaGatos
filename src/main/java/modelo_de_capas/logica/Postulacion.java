// Postulacion.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "postulacion")
public class Postulacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private LocalDate fecha;

    @Basic
    private String estado;
    
    @ManyToOne
    private Familia familia;
    
    @ManyToOne
    private Gato gato;

    public Postulacion() {
    }

    public Postulacion(Long id, LocalDate fecha, String estado, Familia familia, Gato gato) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
