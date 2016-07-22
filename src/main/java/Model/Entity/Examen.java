package Model.Entity;

import java.util.Date;

public class Examen extends Entity{

    private Date fecha;
    private String aula;
    private Materia materia;

    public Examen(Date fecha, String aula, Materia materia) {
        this.fecha = fecha;
        this.aula = aula;
        this.materia = materia;
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
}
