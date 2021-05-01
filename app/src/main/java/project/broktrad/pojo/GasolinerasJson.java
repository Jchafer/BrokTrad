package project.broktrad.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GasolinerasJson {
    @SerializedName("Fecha")
    private String fecha;
    @SerializedName("ListaEESSPrecio")
    private List<GasolineraApi> listaGasolineras;
    @SerializedName("Nota")
    private String nota;
    @SerializedName("ResultadoConsulta")
    private String resultadoConsulta;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<GasolineraApi> getListaGasolineras() {
        return listaGasolineras;
    }

    public void setListaGasolineras(List<GasolineraApi> listaGasolineras) {
        this.listaGasolineras = listaGasolineras;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getResultadoConsulta() {
        return resultadoConsulta;
    }

    public void setResultadoConsulta(String resultadoConsulta) {
        this.resultadoConsulta = resultadoConsulta;
    }

    @Override
    public String toString() {
        return "GasolinerasJson{" +
                "fecha='" + fecha + '\'' +
                ", listaGasolineras=" + listaGasolineras +
                ", nota='" + nota + '\'' +
                ", resultadoConsulta='" + resultadoConsulta + '\'' +
                '}';
    }
}
