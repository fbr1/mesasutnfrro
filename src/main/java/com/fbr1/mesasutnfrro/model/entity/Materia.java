package com.fbr1.mesasutnfrro.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name="materias")
public class Materia {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_materia")
    private long Id;

    @Column(name="nombre")
    private String nombre;

    @Column(name="especialidad")
    private String especialidad;

    public Materia(String nombre, String especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public Materia () {} // Required for json parser

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + especialidad.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Materia materia = (Materia) o;

        if (!nombre.equals(materia.nombre)) return false;
        return especialidad.equals(materia.especialidad);

    }

    public long getId() {
        return Id;
    }

    public void setID(long ID) {
        this.Id = Id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return "Materia{" +
                "Id=" + Id +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }
}
