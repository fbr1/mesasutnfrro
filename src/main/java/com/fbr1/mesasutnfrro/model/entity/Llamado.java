package com.fbr1.mesasutnfrro.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="llamados")
public class Llamado{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_llamado")
    private int ID;

    @Column(name="año")
    private int año;

    @Column(name="numero")
    private int numero;

    @Column(name="date")
    private Date date;

    @OneToMany(mappedBy="llamado", fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @JsonManagedReference
    private List<Mesa> mesas;

    public Llamado(int año, int numero, List<Mesa> mesas, Date date) {
        this.año = año;
        this.numero = numero;
        this.mesas = mesas;
        this.date = date;
    }

    public Llamado () {} // Required for json parser

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
