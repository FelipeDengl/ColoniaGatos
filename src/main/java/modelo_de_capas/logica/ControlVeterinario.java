// ControlVeterinario.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "control_veterinario")
public class ControlVeterinario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombreVeterinario;

    @Basic
    private String numeroVeterinario;

    @Basic
    private String tipo;

    @Basic
    private String documento;
    
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

    public ControlVeterinario() {
    }

    public ControlVeterinario(Long id, String nombreVeterinario, String numeroVeterinario, String tipo, String documento, String ubicacion, Date fecha, Date hora, Voluntario voluntario, Gato gato) {
        this.id = id;
        this.nombreVeterinario = nombreVeterinario;
        this.numeroVeterinario = numeroVeterinario;
        this.tipo = tipo;
        this.documento = documento;
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

    public String getNombreVeterinario() {
        return nombreVeterinario;
    }

    public void setNombreVeterinario(String nombreVeterinario) {
        this.nombreVeterinario = nombreVeterinario;
    }

    public String getNumeroVeterinario() {
        return numeroVeterinario;
    }

    public void setNumeroVeterinario(String numeroVeterinario) {
        this.numeroVeterinario = numeroVeterinario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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
