package com.fbr1.mesasutnfrro.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="examenes")
public class Examen {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_examen")
    private int ID;

    @Column(name="hora")
    private Date fecha;

    @Column(name="aula")
    private String aula;

    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "id_materia" )
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_mesa")
    private Mesa mesa;

    public Examen(Date fecha, String aula, Materia materia) {
        this.fecha = fecha;
        this.aula = aula;
        this.materia = materia;
    }

    public Examen() {} // Required for json parser

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

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
