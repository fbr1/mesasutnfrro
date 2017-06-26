package com.fbr1.mesasutnfrro.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="examenes")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Examen {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_examen")
    private long Id;

    @Column(name="hora")
    private LocalDateTime fecha;

    @Column(name="aula")
    private String aula;

    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "id_materia" )
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "id_mesa")
    @JsonBackReference
    private Mesa mesa;

    public Examen(LocalDateTime fecha, String aula, Materia materia) {
        this.fecha = fecha;
        this.aula = aula;
        this.materia = materia;
    }

    public Examen() {} // Required for json parser

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
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

    @Override
    public String toString() {
        return "Examen{" +
                "Id=" + Id +
                ", fecha=" + fecha +
                ", aula='" + aula + '\'' +
                ", materia=" + materia +
                ", mesa=" + mesa +
                '}';
    }
}
