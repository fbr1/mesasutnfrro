package com.fbr1.mesasutnfrro.model.entity;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="mesas")
public class Mesa {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_mesa")
    private int ID;

    @Column(name="fecha")
    private Date fecha;

    @OneToMany(mappedBy = "mesa", fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    private List<Examen> examenes;

    @ManyToOne
    @JoinColumn(name = "id_llamado")
    private Llamado llamado;

    public Mesa(Date fecha, List<Examen> examenes) {
        this.fecha = fecha;
        this.examenes = examenes;
    }

    public Mesa () {} // Required for json parser

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Examen> getExamenes() {
        return examenes;
    }

    public void setExamenes(List<Examen> examenes) {
        this.examenes = examenes;
    }

    public Llamado getLlamado() {
        return llamado;
    }

    public void setLlamado(Llamado llamado) {
        this.llamado = llamado;
    }
}
