package com.fbr1.mesasutnfrro.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="llamados")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Llamado{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_llamado")
    private long Id;

    @Column(name="año")
    private int año;

    @Column(name="numero")
    private int numero;

    @Column(name="date")
    private LocalDate date;

    // This variable is the result of adding the values of the Llamado's Mesas' WeekDay
    // The goal of this is to be able to unequivocally determine the combination of days that forms the Llamado
    @Column(name="week_type")
    private int weekType;

    @OneToMany(mappedBy="llamado", fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    private List<Mesa> mesas;

    public Llamado(int año, int numero, LocalDate date, List<Mesa> mesas) {
        this.año = año;
        this.numero = numero;
        this.mesas = mesas;
        this.date = date;
    }

    public Llamado(int año, int numero, LocalDate date) {
        this.año = año;
        this.numero = numero;
        this.date = date;
    }

    public Llamado () {} // Required for json parser

    public int getWeekType() {
        return weekType;
    }

    public void setWeekType(int weekType) {
        this.weekType = weekType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {
        return Id;
    }

    public void setId(long ID) {
        this.Id = ID;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }
}
