// CertificadoAdopcion.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "certificado_adopcion")
public class CertificadoAdopcion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String comentario;

    @Basic
    private LocalDate fecha;
    
    @ManyToOne 
    private Gato gato;

    @ManyToOne 
    private Veterinario veterinario;

    public CertificadoAdopcion() {
    }

    public CertificadoAdopcion(Long id, String comentario, LocalDate fecha, Gato gato, Veterinario veterinario) {
        this.id = id;
        this.comentario = comentario;
        this.fecha = fecha;
        this.gato = gato;
        this.veterinario = veterinario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }
    
    
}
