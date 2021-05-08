package project.broktrad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import project.broktrad.R;
import project.broktrad.bd.MiBD;
import project.broktrad.dao.FavoritoDAO;
import project.broktrad.dao.GasolineraDAOBorrar;
import project.broktrad.fragment.BuscadorFragment;
import project.broktrad.fragment.DatosFragment;
import project.broktrad.fragment.GasolinerasFragment;
import project.broktrad.pojo.Gasolinera;
import project.broktrad.pojo.GasolinerasJson;
import project.broktrad.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //private Usuario usuario;
    //private ArrayList<Gasolinera> gasolinerasTodas;
    //private ArrayList<GasolineraApi> gasolinerasFavoritas;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private TextView textEmail;
    private TextView textNick;
    //private TextView textFecha;

    private SharedPreferences prefs;

    //private BottomNavigationView bottomNavigationView;
    private Fragment frgBuscador;
    private Fragment datosFragment;

    //private GasolineraDAO gasolineraDAO;
    private FavoritoDAO favoritoDAO;
    //private Cursor cursor;
    private MiBD miBD;

    SharedPreferences prefsManager;
    Locale localizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miBD = MiBD.getInstance(this);
        favoritoDAO = new FavoritoDAO(this);

        try {
            // Abrimos la base de datos
            favoritoDAO.abrir();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), R.string.error_bd,
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Recibir usuario
        //usuario = (Usuario) getIntent().getSerializableExtra("Usuario");

        // Obtenemos referencia a las preferencias del usuario
        prefs = getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);

        // Creación y gestión del navigationView
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView)findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);
        textEmail = (TextView) view.findViewById(R.id.textEmailNav);
        textNick = (TextView) view.findViewById(R.id.textNickNav);
        //textFecha = (TextView) view.findViewById(R.id.textFechaNav);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        cargaDatos();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Menu inferior
        //bottomNavigationView = (BottomNavigationView) findViewById(R.id.menu_navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Bundle args = new Bundle();
                switch (item.getItemId()){
                    case R.id.nav_buscador:
                        fragment = new BuscadorFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_favoritas:
                        fragment = new GasolinerasFragment();
                        ArrayList<Gasolinera> gasolinerasFavoritas = new ArrayList();
                        ArrayList<String> idGasolineras = favoritoDAO.gasolinerasFavoritas((String) textEmail.getText());
                        if (!idGasolineras.isEmpty()){
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://sedeaplicaciones.minetur.gob.es/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            ApiService apiService = retrofit.create(ApiService.class);
                            Call<GasolinerasJson> call = apiService.getGasolinerasValencia();
                            call.enqueue(new Callback<GasolinerasJson>() {

                                @Override
                                public void onResponse(Call<GasolinerasJson> call, Response<GasolinerasJson> response) {
                                    for (int i = 0; i < idGasolineras.size(); i++) {
                                        for(Gasolinera gasolinera : response.body().getListaGasolineras()) {
                                            if (idGasolineras.get(i).equalsIgnoreCase(gasolinera.getIDGasolinera()))
                                                gasolinerasFavoritas.add(gasolinera);
                                        }
                                    }

                                    args.putSerializable("Gasolineras", gasolinerasFavoritas);
                                    fragment.setArguments(args);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).addToBackStack(null).commit();

                                }

                                @Override
                                public void onFailure(Call<GasolinerasJson> call, Throwable t) {
                                }
                            });
                        }else{
                            Toast.makeText(MainActivity.this, R.string.sin_favoritos, Toast.LENGTH_SHORT).show();
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    /*case R.id.nav_actualizar:
                        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                        dialogo.setTitle("Actualización");
                        dialogo.setMessage("¿Quieres actualizar la base de datos? Esto conllevará un tiempo");
                        dialogo.setCancelable(false);
                        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                guardar();
                                leer();
                                Toast.makeText(MainActivity.this, R.string.datos_actualizados, Toast.LENGTH_LONG).show();
                                Toast.makeText(MainActivity.this, prefs.getString("fecha_Actualizacion", String.valueOf(R.string.no_disponible)), Toast.LENGTH_LONG).show();
                                cargaDatos();
                            }
                        });
                        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(MainActivity.this, R.string.actualizacion_cancelada, Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogo.show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;*/
                    case R.id.nav_ajustes:
                        Intent intentAjustes = new Intent();
                        intentAjustes.setClass(MainActivity.this, AjustesActivity.class);
                        //intentAjustes.putExtra("Usuario", usuario);
                        startActivity(intentAjustes);
                        break;
                    case R.id.nav_cerrar_sersion:
                        Intent intentSesion = new Intent();
                        intentSesion.setClass(MainActivity.this, LoginActivity.class);
                        startActivity(intentSesion);
                        break;
                }
                return true;
            }
        });

        datosFragment = new DatosFragment();

        // Creación del fragment de gasolineras
        frgBuscador = new BuscadorFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, frgBuscador).commit();

    }

    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            Intent refrescar = new Intent(this, InicialActivity.class);
            startActivity(refrescar);
            finish();
        }else
            super.onBackPressed();
    }

    @Override
    public void onResume(){
        super.onResume();
        cargaDatos();
    }

    // Carga datos en el navigationView y cambia las preferencias del usuario
    private void cargaDatos(){
        // Asignar datos usuario
        textEmail.setText(prefs.getString("email", "email@gmail.com"));
        textNick.setText(prefs.getString("nick", "Nick"));
        //extFecha.setText(getResources().getString(R.string.ultima_busqueda) + " " + prefs.getString("fecha_Actualizacion", "Fecha"));

        prefsManager = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (!prefsManager.getString("idioma", "").isEmpty()){
            String idioma = prefsManager.getString("idioma", "");

            if (idioma.equalsIgnoreCase("ESP"))
                localizacion = new Locale("es", "ES");
            else if (idioma.equalsIgnoreCase("ENG"))
                localizacion = new Locale("en", "US");

            Locale.setDefault(localizacion);
            Configuration config = new Configuration();
            config.locale = localizacion;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    // A través de la url se descarga el fichero
    public void guardar() {

        String url = "https://geoportalgasolineras.es/resources/files/preciosEESS_es.xls";
        File direc = new File(getFilesDir(),"preciosEESS_es.xls");

        downloadFile(url, direc);
    }

    private static void downloadFile(String url, File outputFile) {
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL u = new URL(url);
            URLConnection conn = u.openConnection();

            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }

    // Una vez descargado el fichero, obtenemos los datos para almacenarlos en la BBDD
    public void leer() {
        ContentValues reg = new ContentValues();

        try {
            InputStream myInput;
            // Inicializar asset manager
            // AssetManager assetManager = getAssets();
            // abrir archivo excel file name as myexcelfile.xls
            //myInput = assetManager.open("preciosEESS_es.xls");
            myInput = getBaseContext().openFileInput("preciosEESS_es.xls");
            // Crear un File System object POI
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Crear un Workbook usando el File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Obtener la primera hoja del workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            Iterator<Row> rowIterator = mySheet.rowIterator();

            int countRow = 0;
            int countColumn = 0;
            int primeraFila = 5;

            String fecha = "";
            while(rowIterator.hasNext()){
                countRow ++;
                HSSFRow row = (HSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                if (countRow == 1){
                    cellIterator.next();
                    fecha = ((HSSFCell)cellIterator.next()).toString();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("fecha_Actualizacion", fecha);
                    editor.apply();
                }
                // Primera linea de gasolinera
                if (countRow >= primeraFila){
                    while(cellIterator.hasNext()) {
                        HSSFCell cell = (HSSFCell) cellIterator.next();
                        countColumn ++;
                        switch (countColumn){
                            case 1:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_PROVINCIA, cell.toString());
                                break;

                            case 2:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_MUNICIPIO, cell.toString());
                                break;

                            case 4:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_CODPOSTAL, cell.toString());
                                break;

                            case 5:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_DIRECCION, cell.toString());
                                break;

                            case 7:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_ID_LONGITUD, cell.toString());
                                break;

                            case 8:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_ID_LATITUD, cell.toString());
                                break;

                            case 9:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_PRECIOGASOLINA95, cell.toString());
                                break;

                            case 12:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_PRECIOGASOLINA98, cell.toString());
                                break;

                            case 14:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_PRECIOGASOLEOA, cell.toString());
                                break;

                            case 15:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_PRECIOGASOLEOPREMIUM, cell.toString());
                                break;

                            case 26:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_ROTULO, cell.toString());
                                break;

                            case 29:
                                reg.put(GasolineraDAOBorrar.C_COLUMNA_HORARIO, cell.toString());
                                break;

                            default:
                                break;
                        }
                    }
                    countColumn = 0;
                    //Cursor c = gasolineraDAO.getRegistro(reg.getAsString(GasolineraDAO.C_COLUMNA_ID_LONGITUD), reg.getAsString(GasolineraDAO.C_COLUMNA_ID_LATITUD));

                    // Si getCount() devuelve 0, no hay registro con esos ids
                    // Si getCount() devuelve 1, hay un registro con esos ids
                    if (reg.getAsString(GasolineraDAOBorrar.C_COLUMNA_PROVINCIA).contains("VALENCIA")){
                        //if (c.getCount() == 0) gasolineraDAO.add(reg);
                        //else gasolineraDAO.update(reg);
                    }
                    //c.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String cambiarComaPunt(String conComa){
        String conPunto = conComa.replaceAll(",", ".");
        return conPunto;

    }

}