// Estudio.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "estudio")
public class Estudio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;

    @Basic
    private String descripcion;

    @Basic
    private String documento; // ruta o referencia al documento
    
    @ManyToOne 
    private Diagnostico diagnostico;

    public Estudio() {
    }

    public Estudio(Long id, String nombre, String descripcion, String documento, Diagnostico diagnostico) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.documento = documento;
        this.diagnostico = diagnostico;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }
    
    
}
