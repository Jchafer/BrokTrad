package project.broktrad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.broktrad.R;
import project.broktrad.bd.MiBD;
import project.broktrad.bd.MiBDOperacional;
import project.broktrad.dao.UsuarioDAO;
import project.broktrad.pojo.Usuario;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText nombreUsuario;
    private EditText claveUsuario;
    private EditText nickUsuario;
    //private EditText edad;
    //private TextView txtEdad;
    private TextView txtRequisitos;
    private TextView txtNick;
    private TextView txtAceptar;
    private Button inicioSesion;
    // private RadioButton entrar;
    private RadioButton registrar;

    private ArrayList<Usuario> usuarios;
    private Usuario usuario;

    private SharedPreferences prefs;

    private MiBDOperacional miBDOperacional;
    private MiBD miBD;
    private UsuarioDAO usuarioDAO;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        miBDOperacional = MiBDOperacional.getInstance(this);
        miBD = MiBD.getInstance(this);
        usuarioDAO = new UsuarioDAO(this);
        usuarioDAO.abrir();

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
        //edad = findViewById(R.id.editTextEdad);
        //txtEdad = findViewById(R.id.txtEdad);
        txtRequisitos = findViewById(R.id.txtRequisitos);
        inicioSesion = findViewById(R.id.btAceptar);
        // entrar = findViewById(R.id.radioButtonEntrar);
        registrar = findViewById(R.id.radioButtonRegistrar);

        registrar.setOnCheckedChangeListener(this);
        inicioSesion.setOnClickListener(this);

        nombreUsuario.setText("admin@admin.com");
        claveUsuario.setText("adminA1@");

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
        //String edadPasada = edad.getText().toString();
        //int edadPasadaInt = 0;

        if (!registrar.isChecked()){
            usuario = new Usuario(emailPasada, clavePasada);
            Usuario usuEnBD = (Usuario) usuarioDAO.search(usuario);
            //usuario = comprobarUsuarioCorrecto(emailPasada, clavePasada);

            login(usuEnBD);

            /*if (usuEnBD != null) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("Usuario", usuEnBD);
                saveOnPreferences(usuEnBD.getEmail(), usuEnBD.getClave(), usuEnBD.getNick());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
            }*/
        }else{
            switch (comprobaciones(emailPasada, clavePasada)){
                case 0:
                    usuario = new Usuario(emailPasada, clavePasada, nickPasado);
                    ContentValues reg = new ContentValues();
                    reg.put(usuarioDAO.C_COLUMNA_ID_EMAIL, usuario.getEmail());
                    reg.put(usuarioDAO.C_COLUMNA_CLAVE, usuario.getClave());
                    reg.put(usuarioDAO.C_COLUMNA_NICK, usuario.getNick());
                    usuarioDAO.add(reg);

                    register(usuario);
                    break;
                case 1:
                    Toast.makeText(this, "Ya existe un usuario con ese email", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //Toast.makeText(this, "Debes ser mayor de edad", Toast.LENGTH_SHORT).show();
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

    private void login(Usuario usuario) {
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getClave())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Task completed successfully
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            intent.putExtra("Usuario", usuario);
                            saveOnPreferences(usuario.getEmail(), usuario.getClave(), usuario.getNick());
                            startActivity(intent);
                        } else {
                            // Task failed with an exception
                            Toast.makeText(LoginActivity.this, "Fallo inicio sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void register(Usuario usuario){
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getClave())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Task completed successfully
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            intent.putExtra("Usuario", usuario);
                            saveOnPreferences(usuario.getEmail(), usuario.getClave(), usuario.getNick());
                            startActivity(intent);
                        } else {
                            // Task failed with an exception
                            Toast.makeText(LoginActivity.this, "Fallo registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            //edad.setVisibility(View.VISIBLE);
            //txtEdad.setVisibility(View.VISIBLE);
            nickUsuario.setVisibility(View.VISIBLE);
            txtNick.setVisibility(View.VISIBLE);
            inicioSesion.setText("Registrar");
        }else{
            //edad.setVisibility(View.GONE);
            //txtEdad.setVisibility(View.GONE);
            nickUsuario.setVisibility(View.GONE);
            txtNick.setVisibility(View.GONE);
            txtRequisitos.setVisibility(View.GONE);
            inicioSesion.setText("Iniciar sesión");
        }

    }

    /*public Usuario comprobarUsuarioCorrecto(String nombreUsuario, String claveUsuario){
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(nombreUsuario) && usuario.getClave().equalsIgnoreCase(claveUsuario)){
                return usuario;
            }
        }
        return null;
    }*/

    public int comprobaciones(String email, String clave){
        // Si existe el usuario devuelve 1
        if (comprobarUsuarioExiste(email))
            return 1;

        // Si no es mayor de edad devuelve 2
        /*if (!comprobarMayorEdad(edad))
            return 2;*/

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

    public boolean comprobarUsuarioExiste(String email){
        Usuario usuario = new Usuario(email);
        Usuario usuDB = (Usuario)usuarioDAO.search(usuario);
        if (usuDB != null) return true;
        return false;
    }

    /*public boolean comprobarMayorEdad(int edad){
        return edad >= 18;
    }*/

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