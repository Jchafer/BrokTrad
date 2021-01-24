package project.broktrad.pojo;

import java.io.Serializable;

public class Operacion implements Serializable {
    private Accion accion;
    private float valorAccion;
    private float inversion;

    public Operacion() {
    }

    public Operacion(Accion accion, float valorAccion, float inversion) {
        this.accion = accion;
        this.valorAccion = valorAccion;
        this.inversion = inversion;
    }

    public Accion getAccion() {
        return accion;
    }

    public void setAccion(Accion accion) {
        this.accion = accion;
    }

    public float getValorAccion() {
        return valorAccion;
    }

    public void setValorAccion(float valorAccion) {
        this.valorAccion = valorAccion;
    }

    public float getInversion() {
        return inversion;
    }

    public void setInversion(float inversion) {
        this.inversion = inversion;
    }

    @Override
    public String toString() {
        return "Operacion{" +
                "accion=" + accion +
                ", valorAccion=" + valorAccion +
                ", inversion=" + inversion +
                '}';
    }
}
