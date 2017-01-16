package com.fbr1.mesasutnfrro.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fbr1.mesasutnfrro.util.WeekDayConverter;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="mesas")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Mesa {

    public enum WeekDay {

        LUNES(1),
        MARTES(1<<1),
        MIERCOLES(1<<2),
        JUEVES(1<<3),
        VIERNES(1<<4),
        SABADO(1<<5),
        DOMINGO(1<<6);

        private final int weekDayValue;

        WeekDay(int weekDayValue) {
            this.weekDayValue = weekDayValue;
        }

        public int getWeekDayValue(){
            return weekDayValue;
        }

    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id_mesa")
    private long Id;

    @Column(name="fecha")
    private Date fecha;

    @Column(name="week_day")
    @Convert(converter = WeekDayConverter.class)
    private WeekDay weekDay;

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

    public long getID() {
        return Id;
    }

    public void setID(long Id) {
        this.Id = Id;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
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
