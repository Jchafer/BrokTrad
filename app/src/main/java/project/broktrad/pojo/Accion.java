package project.broktrad.pojo;

import java.io.Serializable;

public class Accion implements Serializable {
    private String nombre;
    private float valor;

    public Accion() {
    }

    public Accion(String nombre, float valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Accion{" +
                "nombre='" + nombre + '\'' +
                ", valor=" + valor +
                '}';
    }
}
