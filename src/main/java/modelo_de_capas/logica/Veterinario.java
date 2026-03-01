// Veterinario.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "veterinario")
public class Veterinario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;

    @Basic
    private String numeroTelefono;

    @Basic
    private String dni;
    
    @OneToOne
    private Usuario usuario;
    
    @OneToMany (mappedBy="veterinario")
    private List<CertificadoAdopcion> listaCertificadoAdopcion = new ArrayList<>();
    
    @OneToMany (mappedBy="veterinario")
    private List<Diagnostico> listaDiagnostico = new ArrayList<>();

    public Veterinario() {
    }

    public Veterinario(Long id, String nombre, String numeroTelefono, String dni, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.numeroTelefono = numeroTelefono;
        this.dni = dni;
        this.usuario = usuario;
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

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<CertificadoAdopcion> getListaCertificadoAdopcion() {
        return listaCertificadoAdopcion;
    }

    public void setListaCertificadoAdopcion(List<CertificadoAdopcion> listaCertificadoAdopcion) {
        this.listaCertificadoAdopcion = listaCertificadoAdopcion;
    }

    public List<Diagnostico> getListaDiagnostico() {
        return listaDiagnostico;
    }

    public void setListaDiagnostico(List<Diagnostico> listaDiagnostico) {
        this.listaDiagnostico = listaDiagnostico;
    }

    
    
}
