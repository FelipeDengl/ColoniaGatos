// HistorialMedico.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "historial_medico")
public class HistorialMedico implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String notas; // resumen o texto libre
    
    @OneToOne
    private Gato gato;

    public HistorialMedico() {
    }

    public HistorialMedico(Long id, String notas, Gato gato) {
        this.id = id;
        this.notas = notas;
        this.gato = gato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }
    
    
}
