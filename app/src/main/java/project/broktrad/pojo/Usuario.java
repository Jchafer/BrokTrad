package project.broktrad.pojo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String email;
    private String clave;
    private int edad;
    private float saldo;

    public Usuario() {
    }

    public Usuario(String email, String clave, int edad) {
        this.email = email;
        this.clave = clave;
        this.edad = edad;
        this.saldo = 10000;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", clave='" + clave + '\'' +
                ", edad=" + edad +
                ", saldo=" + saldo +
                '}';
    }
}
