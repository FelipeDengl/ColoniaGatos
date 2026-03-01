// TransporteHogarTransito.java
package modelo_de_capas.logica;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "transporte_hogar_transito")
public class TransporteHogarTransito implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private LocalTime horaEntrega;

    @Basic
    private String aclaracion;

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
    
    @ManyToOne
    private HogarTransito hogartransito;

    public TransporteHogarTransito() {
    }

    public TransporteHogarTransito(Long id, LocalTime horaEntrega, String aclaracion, String ubicacion, Date fecha, Date hora, Voluntario voluntario, Gato gato, HogarTransito hogartransito) {
        this.id = id;
        this.horaEntrega = horaEntrega;
        this.aclaracion = aclaracion;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.hora = hora;
        this.voluntario = voluntario;
        this.gato = gato;
        this.hogartransito = hogartransito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getAclaracion() {
        return aclaracion;
    }

    public void setAclaracion(String aclaracion) {
        this.aclaracion = aclaracion;
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

    public HogarTransito getHogartransito() {
        return hogartransito;
    }

    public void setHogartransito(HogarTransito hogartransito) {
        this.hogartransito = hogartransito;
    }
    
    
}
