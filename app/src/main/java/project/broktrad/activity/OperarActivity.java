package project.broktrad.activity;

import androidx.appcompat.app.AppCompatActivity;
import project.broktrad.R;
import project.broktrad.pojo.Gasolinera;

import android.os.Bundle;
import android.widget.TextView;

public class OperarActivity extends AppCompatActivity {

    private Gasolinera gasolinera;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operar);

        texto = findViewById(R.id.textView5);

        // Recibir gasolinera
        gasolinera = (Gasolinera) getIntent().getSerializableExtra("Gasolinera");

        texto.setText(texto.getText() + " " + gasolinera.getDireccion());
    }
}