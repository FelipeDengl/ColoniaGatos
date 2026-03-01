// Administrador.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "administrador")
public class Administrador implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String nombre;
    
    @OneToOne
    private Usuario usuario;
    
    @OneToMany (mappedBy="administrador")
    private List<Colonia> listaColonia = new ArrayList<>();

    public Administrador() {
    }

    public Administrador(Long id, String nombre, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Colonia> getListaColonia() {
        return listaColonia;
    }

    public void setListaColonia(List<Colonia> listaColonia) {
        this.listaColonia = listaColonia;
    }

    
}
