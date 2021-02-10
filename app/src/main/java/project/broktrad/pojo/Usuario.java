package project.broktrad.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String email;
    private String clave;
    private String nick;
    private boolean admin;
    private ArrayList<Gasolinera> favoritos;

    public Usuario() {
        this.favoritos = new ArrayList();
    }

    public Usuario(String email) {
        this.email = email;
        this.clave = null;
        this.nick = null;
        this.admin = false;
        this.favoritos = new ArrayList();
    }

    public Usuario(String email, String clave, String nick) {
        this.email = email;
        this.clave = clave;
        this.nick = nick;
        this.admin = false;
        this.favoritos = new ArrayList();
    }

    public Usuario(String email, String clave) {
        this.email = email;
        this.clave = clave;
        this.nick = null;
        this.admin = false;
        this.favoritos = new ArrayList();
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public ArrayList<Gasolinera> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(ArrayList<Gasolinera> favoritos) {
        this.favoritos = favoritos;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", clave='" + clave + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
