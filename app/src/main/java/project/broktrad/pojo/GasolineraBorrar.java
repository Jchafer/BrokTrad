package project.broktrad.pojo;

import java.io.Serializable;

public class GasolineraBorrar implements Serializable {

    private String provincia;
    private String municipio;
    private String codPostal;
    private String direccion;
    private String longitud;
    private String latitud;
    private String precioGasolina95;
    private String precioGasolina98;
    private String precioGasoleoA;
    private String precioGasoleoPremium;
    private String rotulo;
    private String horario;

    public GasolineraBorrar() {
    }

    public GasolineraBorrar(String provincia, String municipio, String codPostal, String direccion, String longitud, String latitud, String precioGasolina95, String precioGasolina98, String precioGasoleoA, String precioGasoleoPremium, String rotulo, String horario) {
        this.provincia = provincia;
        this.municipio = municipio;
        this.codPostal = codPostal;
        this.direccion = direccion;
        this.longitud = longitud;
        this.latitud = latitud;
        this.precioGasolina95 = precioGasolina95;
        this.precioGasolina98 = precioGasolina98;
        this.precioGasoleoA = precioGasoleoA;
        this.precioGasoleoPremium = precioGasoleoPremium;
        this.rotulo = rotulo;
        this.horario = horario;
    }

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

    @Override
    public String toString() {
        return "Gasolinera{" +
                "provincia='" + provincia + '\'' +
                ", municipio='" + municipio + '\'' +
                ", codPostal='" + codPostal + '\'' +
                ", direccion='" + direccion + '\'' +
                ", longitud=" + longitud +
                ", latitud=" + latitud +
                ", precioGasolina95=" + precioGasolina95 +
                ", precioGasolina98=" + precioGasolina98 +
                ", precioGasoleoA=" + precioGasoleoA +
                ", precioGasoleoPremium=" + precioGasoleoPremium +
                ", rotulo='" + rotulo + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }
}
