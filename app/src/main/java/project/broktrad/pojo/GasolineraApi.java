package project.broktrad.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GasolineraApi implements Serializable {
    @SerializedName("Provincia")
    private String provincia;
    @SerializedName("Municipio")
    private String municipio;
    @SerializedName("C.P.")
    private String codPostal;
    @SerializedName("Dirección")
    private String direccion;
    @SerializedName("Longitud (WGS84)")
    private String longitud;
    @SerializedName("Latitud")
    private String latitud;
    @SerializedName("Precio Gasolina 95 E5")
    private String precioGasolina95;
    @SerializedName("Precio Gasolina 98 E5")
    private String precioGasolina98;
    @SerializedName("Precio Gasoleo A")
    private String precioGasoleoA;
    @SerializedName("Precio Gasoleo Premium")
    private String precioGasoleoPremium;
    @SerializedName("Rótulo")
    private String rotulo;
    @SerializedName("Horario")
    private String horario;
    @SerializedName("IDEESS")
    private String IDGasolinera;
    @SerializedName("IDMunicipio")
    private String IDMunicipio;

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getPrecioGasolina95() {
        return precioGasolina95;
    }

    public void setPrecioGasolina95(String precioGasolina95) {
        this.precioGasolina95 = precioGasolina95;
    }

    public String getPrecioGasolina98() {
        return precioGasolina98;
    }

    public void setPrecioGasolina98(String precioGasolina98) {
        this.precioGasolina98 = precioGasolina98;
    }

    public String getPrecioGasoleoA() {
        return precioGasoleoA;
    }

    public void setPrecioGasoleoA(String precioGasoleoA) {
        this.precioGasoleoA = precioGasoleoA;
    }

    public String getPrecioGasoleoPremium() {
        return precioGasoleoPremium;
    }

    public void setPrecioGasoleoPremium(String precioGasoleoPremium) {
        this.precioGasoleoPremium = precioGasoleoPremium;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getIDGasolinera() {
        return IDGasolinera;
    }

    public void setIDGasolinera(String IDGasolinera) {
        this.IDGasolinera = IDGasolinera;
    }

    public String getIDMunicipio() {
        return IDMunicipio;
    }

    public void setIDMunicipio(String IDMunicipio) {
        this.IDMunicipio = IDMunicipio;
    }

    @Override
    public String toString() {
        return "GasolineraApi{" +
                "provincia='" + provincia + '\'' +
                ", municipio='" + municipio + '\'' +
                ", codPostal='" + codPostal + '\'' +
                ", direccion='" + direccion + '\'' +
                ", longitud='" + longitud + '\'' +
                ", latitud='" + latitud + '\'' +
                ", precioGasolina95='" + precioGasolina95 + '\'' +
                ", precioGasolina98='" + precioGasolina98 + '\'' +
                ", precioGasoleoA='" + precioGasoleoA + '\'' +
                ", precioGasoleoPremium='" + precioGasoleoPremium + '\'' +
                ", rotulo='" + rotulo + '\'' +
                ", horario='" + horario + '\'' +
                ", IDGasolinera='" + IDGasolinera + '\'' +
                ", IDMunicipio='" + IDMunicipio + '\'' +
                '}';
    }
}
