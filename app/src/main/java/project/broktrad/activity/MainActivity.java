package project.broktrad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import project.broktrad.R;
import project.broktrad.fragment.AccionesFragment;
import project.broktrad.fragment.OperacionesFragment;
import project.broktrad.pojo.Accion;
import project.broktrad.pojo.Operacion;
import project.broktrad.pojo.Usuario;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Usuario usuario;
    private ArrayList<Accion> accionesTodas;
    private ArrayList<Accion> accionesFavoritas;
    private ArrayList<Operacion> operaciones;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private TextView textEmail;
    private TextView textNick;

    private SharedPreferences prefs;

    private BottomNavigationView bottomNavigationView;
    private Fragment frgAcciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recibir usuario
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");

        // Obtenemos referencia a las preferencias del usuario
        prefs = getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);

        // Creación de las acciones de prueba
        accionesTodas = new ArrayList();
        accionesFavoritas = new ArrayList();
        for (int i = 1; i <= 20; i++) {
            accionesTodas.add(new Accion("Accion " + i, (float)Math.random()*20));
        }

        for (int i = 1; i <= 5; i++) {
            accionesFavoritas.add(new Accion("Accion " + i, (float)Math.random()*20));
        }

        // Creación de las operaciones de prueba
        operaciones = new ArrayList();
        for (int i = 1; i <= 10; i++) {
            operaciones.add(new Operacion(new Accion("Accion " + i, (float)Math.random()*20), (float)Math.random()*20, (float)Math.random()*50+20));
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView)findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);
        // Asignar datos usuario
        textEmail = (TextView)view.findViewById(R.id.textEmailNav);
        textEmail.setText(prefs.getString("email", "email@gmail.com"));
        textNick = (TextView)view.findViewById(R.id.textNickNav);
        textNick.setText(prefs.getString("nick", "Nick"));
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Menu inferior
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Bundle args = new Bundle();
                switch (item.getItemId()){
                    case R.id.nav_acciones:
                        fragment = new AccionesFragment();
                        args.putSerializable("Acciones", accionesTodas);
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_operaciones:
                        fragment = new OperacionesFragment();
                        args.putSerializable("Operaciones", operaciones);
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
                        bottomNavigationView.setVisibility(View.INVISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        // Creación del fragment de acciones
        frgAcciones = new AccionesFragment();
        Bundle args = new Bundle();
        args.putSerializable("Acciones", accionesTodas);
        frgAcciones.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, frgAcciones).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.action_cuenta:
                intent.setClass(MainActivity.this, CuentaActivity.class);
                intent.putExtra("Usuario", usuario);
                startActivity(intent);
                break;

            case R.id.action_ajustes:
                intent.setClass(MainActivity.this, AjustesActivity.class);
                intent.putExtra("Usuario", usuario);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f = new AccionesFragment();
        Bundle args = new Bundle();

        switch (item.getItemId()){
            case R.id.navigation_todos:
                args.putSerializable("Acciones", accionesTodas);
                break;
            case R.id.navigation_favoritos:
                args.putSerializable("Acciones", accionesFavoritas);
                break;
        }

        f.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, f).commit();

        return true;
    }
}