package Model.Entity;

import java.util.ArrayList;
import java.util.Date;

public class Mesa extends Entity{

    private Date fecha;
    private ArrayList<Examen> examenes;

    public Mesa(Date fecha, ArrayList<Examen> examenes) {
        this.fecha = fecha;
        this.examenes = examenes;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Examen> getExamenes() {
        return examenes;
    }

    public void setExamenes(ArrayList<Examen> examenes) {
        this.examenes = examenes;
    }
}
