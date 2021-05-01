package project.broktrad.pojo;

import com.google.gson.annotations.SerializedName;

public class Municipio {
    @SerializedName("IDMunicipio")
    private String ID;
    @SerializedName("Municipio")
    private String nombre;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Municipio{" +
                "ID='" + ID + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
