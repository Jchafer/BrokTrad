package project.broktrad.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Validaciones {
    // Valida email pasado como String mediante un pattern
    public static boolean validaEmail(String email){
        // Patrón para validar el email
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");

        Matcher mather = pattern.matcher(email);

        if (mather.find() == true)
            return true;

        return false;

    }

    // Valida clave pasada como String mediante un pattern
    public static boolean validaClave(String clave){
        /* Patrón para validar la clave
        Mínimo 1 número
        Mínimo 1 letra minúscula
        Mínimo 1 letra mayùscula
        Mínimo 1 caracter especial
        Sin espacios
        Mínimo 8 caracteres*/
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

        Matcher mather = pattern.matcher(clave);

        if (mather.find() == true)
            return true;

        return false;
    }

    // Cambiar las comas por puntos
    public String cambiarComaPunt(String conComa){
        String conPunto = conComa.replaceAll(",", ".");
        return conPunto;
    }
}
