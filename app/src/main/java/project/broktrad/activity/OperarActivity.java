package project.broktrad.activity;

import androidx.appcompat.app.AppCompatActivity;
import project.broktrad.R;
import project.broktrad.pojo.Accion;
import project.broktrad.pojo.Usuario;

import android.os.Bundle;
import android.widget.TextView;

public class OperarActivity extends AppCompatActivity {

    private Accion accion;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operar);

        texto = findViewById(R.id.textView5);

        // Recibir accion
        accion = (Accion) getIntent().getSerializableExtra("Accion");

        texto.setText(texto.getText() + " " + accion.getNombre());
    }
}