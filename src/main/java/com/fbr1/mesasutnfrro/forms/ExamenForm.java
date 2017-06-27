package com.fbr1.mesasutnfrro.forms;

import org.hibernate.validator.constraints.NotEmpty;

public class ExamenForm {

    @NotEmpty
    private String materia;

    @NotEmpty
    private String aula;

    @NotEmpty
    private String especialidad;

    @NotEmpty
    private String hora;

    @NotEmpty
    private String selected_mesa;

    @Override
    public String toString() {
        return "ExamenForm{" +
                "materia='" + materia + '\'' +
                ", aula='" + aula + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", hora='" + hora + '\'' +
                ", selected_mesa='" + selected_mesa + '\'' +
                '}';
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSelected_mesa() {
        return selected_mesa;
    }

    public void setSelected_mesa(String selected_mesa) {
        this.selected_mesa = selected_mesa;
    }
}
