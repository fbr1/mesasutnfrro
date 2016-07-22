package Model.Entity;

public class Materia extends Entity{

    public enum Especialidades {
        ISI,IC,IQ,IM,IE
    }

    private String nombre;
    private String especialidad;

    public Materia(String nombre, String especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
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
}
