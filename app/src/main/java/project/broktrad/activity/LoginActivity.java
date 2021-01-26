package project.broktrad.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.broktrad.R;
import project.broktrad.pojo.Usuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText nombreUsuario;
    private EditText claveUsuario;
    private EditText nickUsuario;
    private EditText edad;
    private TextView txtEdad;
    private TextView txtRequisitos;
    private TextView txtNick;
    private TextView txtAceptar;
    private ImageButton inicioSesion;
    // private RadioButton entrar;
    private RadioButton registrar;

    private ArrayList<Usuario> usuarios;
    private Usuario usuario;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Agregar animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_izquierda);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);

        ImageView logoImage = findViewById(R.id.logoImage);
        ImageView logoTexto = findViewById(R.id.logoTexto);

        logoImage.setAnimation(animacion1);
        logoTexto.setAnimation(animacion2);

        nombreUsuario = findViewById(R.id.editTextUsuario);
        claveUsuario = findViewById(R.id.editTextClave);
        nickUsuario = findViewById(R.id.editTextNick);
        txtNick = findViewById(R.id.txtNick);
        edad = findViewById(R.id.editTextEdad);
        txtEdad = findViewById(R.id.txtEdad);
        txtRequisitos = findViewById(R.id.txtRequisitos);
        txtAceptar = findViewById(R.id.txtAceptar);
        inicioSesion = findViewById(R.id.btAceptar);
        // entrar = findViewById(R.id.radioButtonEntrar);
        registrar = findViewById(R.id.radioButtonRegistrar);

        registrar.setOnCheckedChangeListener(this);
        inicioSesion.setOnClickListener(this);

        nombreUsuario.setText("admin@gmail.com");
        claveUsuario.setText("adminA1@");

        // Creación de datos usuarios
        usuarios = new ArrayList();
        usuarios.add(new Usuario("admin@gmail.com", "adminA1@", "admin", 18));
        usuarios.add(new Usuario("sara@hotmail.com", "sara", "Sary", 20));
        usuarios.add(new Usuario("pepe@gmail.com", "pepe", "Pep", 32));
        usuarios.add(new Usuario("marta@hotmail.es", "marta", "Martuchi", 25));
        usuarios.add(new Usuario("raul@hotmail.com", "raul", "Raul", 38));

        prefs = getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);

    }

    private void saveOnPreferences(String email, String password, String nick) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("nick", nick);
        editor.apply();
    }

    @Override
    public void onClick(View view) {

        String emailPasada = nombreUsuario.getText().toString();
        String clavePasada = claveUsuario.getText().toString();
        String nickPasado = nickUsuario.getText().toString();
        String edadPasada = edad.getText().toString();
        int edadPasadaInt = 0;

        if (!registrar.isChecked()){
            usuario = comprobarUsuarioCorrecto(emailPasada, clavePasada);

            if (usuario != null) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("Usuario", usuario);
                saveOnPreferences(usuario.getEmail(), usuario.getClave(), usuario.getNick());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
            }
        }else{
            if (!edadPasada.isEmpty()){
                edadPasadaInt = Integer.parseInt(edadPasada);
            }

            switch (comprobaciones(emailPasada, clavePasada, edadPasadaInt)){
                case 0:
                    usuario = new Usuario(emailPasada, clavePasada, nickPasado, edadPasadaInt);
                    usuarios.add(usuario);
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra("Usuario", usuario);
                    startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(this, "Ya existe un usuario con ese email", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, "Debes ser mayor de edad", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, "Debes introducir correctamente el email", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(this, "Debes introducir correctamente la clave", Toast.LENGTH_SHORT).show();
                    break;

            }
            /*if (comprobarMayorEdad(Integer.parseInt(edad.getText().toString()))){
                if (!comprobarUsuarioExiste(nombreUsuario.getText().toString())){
                    usuario = new Usuario(nombreUsuario.getText().toString(), claveUsuario.getText().toString(), Integer.parseInt(edad.getText().toString()));
                    usuarios.add(usuario);
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra("Usuario", usuario);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Ya existe un usuario con ese email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Debes ser mayor de edad", Toast.LENGTH_SHORT).show();
            }*/
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            edad.setVisibility(View.VISIBLE);
            txtEdad.setVisibility(View.VISIBLE);
            nickUsuario.setVisibility(View.VISIBLE);
            txtNick.setVisibility(View.VISIBLE);
        }else{
            edad.setVisibility(View.GONE);
            txtEdad.setVisibility(View.GONE);
            nickUsuario.setVisibility(View.GONE);
            txtNick.setVisibility(View.GONE);
            txtRequisitos.setVisibility(View.GONE);
        }

    }

    public Usuario comprobarUsuarioCorrecto(String nombreUsuario, String claveUsuario){
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(nombreUsuario) && usuario.getClave().equalsIgnoreCase(claveUsuario)){
                return usuario;
            }
        }
        return null;
    }

    public int comprobaciones(String email, String clave, int edad){
        // Si existe el usuario devuelve 1
        if (comprobarUsuarioExiste(email))
            return 1;

        // Si no es mayor de edad devuelve 2
        if (!comprobarMayorEdad(edad))
            return 2;

        // Si no valida email devuelve 3
        if (!validaEmail(email))
            return 3;

        // Si no valida clave devuelve 4
        if(!validaClave(clave)){
            txtRequisitos.setVisibility(View.VISIBLE);
            return 4;
        }else
            txtRequisitos.setVisibility(View.GONE);

        return 0;
    }

    public boolean comprobarUsuarioExiste(String nombreUsuario){
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(nombreUsuario)){
                return true;
            }
        }
        return false;
    }

    public boolean comprobarMayorEdad(int edad){
        return edad >= 18;
    }

    public boolean validaEmail(String email){
        // Patrón para validar el email
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");

        Matcher mather = pattern.matcher(email);

        if (mather.find() == true)
            return true;

        return false;

    }

    public boolean validaClave(String clave){
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
}