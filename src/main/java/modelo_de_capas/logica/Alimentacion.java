// Alimentacion.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "alimentacion")
public class Alimentacion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String tipo;

    @Basic
    private String cantidad; // "moderada/..." o una representación (ej. "50g")
    
    @Basic
    private String ubicacion;
    
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Temporal(TemporalType.TIME)
    private Date hora;
    
    @ManyToOne
    private Voluntario voluntario;
    
    @ManyToOne
    private Gato gato;

    public Alimentacion() {
    }

    public Alimentacion(Long id, String tipo, String cantidad, String ubicacion, Date fecha, Date hora, Voluntario voluntario, Gato gato) {
        this.id = id;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.hora = hora;
        this.voluntario = voluntario;
        this.gato = gato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
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
