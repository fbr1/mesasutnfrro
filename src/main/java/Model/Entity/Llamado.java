package Model.Entity;

import java.util.ArrayList;

public class Llamado extends Entity{

    private int año;
    private int numero;
    private ArrayList<Mesa> mesas;

    public Llamado(int año, int numero, ArrayList<Mesa> mesas) {
        this.año = año;
        this.numero = numero;
        this.mesas = mesas;
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

    public ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(ArrayList<Mesa> mesas) {
        this.mesas = mesas;
    }
}
