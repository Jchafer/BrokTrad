package project.broktrad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import project.broktrad.R;
import project.broktrad.utilities.Validaciones;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText nombreUsuario;
    private EditText claveUsuario;
    private EditText nickUsuario;
    private TextView txtRequisitos;
    private TextView txtNick;
    private Button inicioSesion;
    private RadioButton registrar;
    private SharedPreferences prefs;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

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
        txtRequisitos = findViewById(R.id.txtRequisitos);
        inicioSesion = findViewById(R.id.btAceptar);
        registrar = findViewById(R.id.radioButtonRegistrar);

        registrar.setOnCheckedChangeListener(this);
        inicioSesion.setOnClickListener(this);

        // Datos de acceso rápido
        nombreUsuario.setText("admin@admin.com");
        claveUsuario.setText("adminA1@");

        prefs = getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {

        String emailPasada = nombreUsuario.getText().toString();
        String clavePasada = claveUsuario.getText().toString();
        String nickPasado = nickUsuario.getText().toString();

        if (!registrar.isChecked()){
            // Login en Firebase
            login(emailPasada, clavePasada);
        }else{
            switch (comprobaciones(emailPasada, clavePasada)){
                case 0:
                    register(emailPasada, clavePasada, nickPasado);
                    break;
                case 1:
                    Toast.makeText(this, R.string.usuario_existe, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, R.string.email_incorrecto, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, R.string.clave_incorrecta, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    // Inicio de sesión de usuario a traves de Firebase
    private void login(String emailPasada, String clavePasada) {
        auth.signInWithEmailAndPassword(emailPasada, clavePasada)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Se cambia el email de las preferencias del usuario
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", emailPasada);
                        editor.apply();
                        Intent intent = new Intent(getApplication(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Task failed with an exception
                        Toast.makeText(LoginActivity.this, R.string.usuario_incorrecto, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    // Nuevo registro de usuario a traves de Firebase
    private void register(String emailPasada, String clavePasada, String nickPasado){
        auth.createUserWithEmailAndPassword(emailPasada, clavePasada)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Se cambia el nick de las preferencias del usuario
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nick", nickPasado);
                        editor.apply();
                        auth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            createSimpleDialog();
                                        }
                                    }
                                });
                    } else {
                        // Task failed with an exception
                        Toast.makeText(LoginActivity.this, R.string.fallo_registro, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            nickUsuario.setVisibility(View.VISIBLE);
            txtNick.setVisibility(View.VISIBLE);
            inicioSesion.setText("Registrar");
        }else{
            nickUsuario.setVisibility(View.GONE);
            txtNick.setVisibility(View.GONE);
            txtRequisitos.setVisibility(View.GONE);
            inicioSesion.setText("Iniciar sesión");
        }
    }

    public void createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.registrar);
        builder.setMessage(R.string.confirmacion);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Correct", "Email sent.");
            }
        });
        builder.show();
    }

    public int comprobaciones(String email, String clave){
        // Si no valida email devuelve 3
        if (!Validaciones.validaEmail(email))
            return 2;

        // Si no valida clave devuelve 4
        if(!Validaciones.validaClave(clave)){
            txtRequisitos.setVisibility(View.VISIBLE);
            return 3;
        }else
            txtRequisitos.setVisibility(View.GONE);

        return 0;
    }

}