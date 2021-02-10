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
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import project.broktrad.R;
import project.broktrad.bd.MiBD;
import project.broktrad.bd.MiBDOperacional;
import project.broktrad.dao.FavoritoDAO;
import project.broktrad.dao.GasolineraDAO;
import project.broktrad.fragment.BuscadorFragment;
import project.broktrad.fragment.DatosFragment;
import project.broktrad.fragment.GasolinerasFragment;
import project.broktrad.fragment.GasolinerasFavoritasFragment;
import project.broktrad.pojo.Gasolinera;
import project.broktrad.pojo.Usuario;

public class MainActivity extends AppCompatActivity {

    private Usuario usuario;
    private ArrayList<Gasolinera> gasolinerasTodas;
    private ArrayList<Gasolinera> gasolinerasFavoritas;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private TextView textEmail;
    private TextView textNick;

    private SharedPreferences prefs;

    private BottomNavigationView bottomNavigationView;
    private Fragment frgBuscador;
    private Fragment datosFragment;

    private GasolineraDAO gasolineraDAO;
    private FavoritoDAO favoritoDAO;
    private Cursor cursor;
    private MiBDOperacional miBDOperacional;
    private MiBD miBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miBDOperacional = MiBDOperacional.getInstance(this);
        miBD = MiBD.getInstance(this);
        gasolineraDAO = new GasolineraDAO(this);
        favoritoDAO = new FavoritoDAO(this);

        try {
            // Abrimos la base de datos
            gasolineraDAO.abrir();
            // Obtenemos el cursor
            cursor = gasolineraDAO.getCursor();
            // Se indica que a la Actividad principal que controle los recursos
            // cursor. Es decir, si se termina la Actividad, se elimina este cursor de la memoria
            startManagingCursor(cursor);

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Se ha producido un error al abrir la base de datos.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

        // Recibir usuario
        usuario = (Usuario) getIntent().getSerializableExtra("Usuario");

        // Obtenemos referencia a las preferencias del usuario
        prefs = getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);

        // Creación de las acciones de prueba
        gasolinerasTodas = gasolineraDAO.getGasolinerasTodas();

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
                        //bottomNavigationView.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_favoritas:
                        fragment = new GasolinerasFavoritasFragment();
                        ArrayList<Gasolinera> gasolinerasFavoritas = gasolineraDAO.getGasolineras(prefs.getString("email", "email@gmail.com"));

                        args.putSerializable("GasolinerasFavoritas", gasolinerasFavoritas);
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
                        //bottomNavigationView.setVisibility(View.INVISIBLE);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_actualizar:
                        guardar();
                        leer();
                        Toast.makeText(MainActivity.this, "Datos actualizados a fecha: " + prefs.getString("fecha_Actualizacion", "No disponible"), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        datosFragment = new DatosFragment();

        // Creación del fragment de gasolineras
        frgBuscador = new BuscadorFragment();
        //Bundle args = new Bundle();
        //args.putSerializable("Gasolineras", gasolinerasTodas);
        //frgAcciones.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, frgBuscador).commit();

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

    //@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f = new GasolinerasFragment();
        Bundle args = new Bundle();

        switch (item.getItemId()){
            case R.id.navigation_todos:
                args.putSerializable("Gasolineras", gasolinerasTodas);
                break;
            case R.id.navigation_favoritos:
                args.putSerializable("GasolinerasFavoritas", gasolinerasFavoritas);
                break;
        }

        f.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, f).commit();

        return true;
    }

    public void guardar() {

        String url = "https://geoportalgasolineras.es/resources/files/preciosEESS_es.xls";
        File direc = new File(getFilesDir(),"preciosEESS_es.xls");

        downloadFile(url, direc);

        /*usuario = new Usuario("prueba@prueba.com", "pruebaA1@");
        FavoritoDAO favoritoDAO = new FavoritoDAO(this);
        favoritoDAO.abrir();
        ArrayList<Gasolinera> gasolineras = gasolineraDAO.getGasolineras(usuario);
        for (Gasolinera gasolinera : gasolineras){
            Log.i("/////////", " GAS " + gasolinera.toString());
        }*/

        //Gasolinera gasolinera = new Gasolinera("Prov", "Munic", "CodPostal", "Direc", "Long", "Lat", "Precio95", "Precio98", "PrecioA", "PrecioP", "Rot", "Hor");
        /*ContentValues reg = new ContentValues();
        reg.put(GasolineraDAO.C_COLUMNA_PROVINCIA, gasolinera.getProvincia());
        reg.put(GasolineraDAO.C_COLUMNA_MUNICIPIO, gasolinera.getMunicipio());
        reg.put(GasolineraDAO.C_COLUMNA_CODPOSTAL, gasolinera.getCodPostal());
        reg.put(GasolineraDAO.C_COLUMNA_DIRECCION, gasolinera.getDireccion());
        reg.put(GasolineraDAO.C_COLUMNA_ID_LONGITUD, gasolinera.getLongitud());
        reg.put(GasolineraDAO.C_COLUMNA_ID_LATITUD, gasolinera.getLatitud());
        reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLINA95, gasolinera.getPrecioGasolina95());
        reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLINA98, gasolinera.getPrecioGasolina98());
        reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLEOA, gasolinera.getPrecioGasoleoA());
        reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLEOPREMIUM, gasolinera.getPrecioGasoleoPremium());
        reg.put(GasolineraDAO.C_COLUMNA_ROTULO, gasolinera.getRotulo());
        reg.put(GasolineraDAO.C_COLUMNA_HORARIO, gasolinera.getHorario());
        gasolineraDAO.add(reg);*/

        /*FavoritoDAO favoritoDAO = new FavoritoDAO(this);
        favoritoDAO.abrir();
        ContentValues reg = new ContentValues();
        reg.put(FavoritoDAO.C_COLUMNA_ID_GASOL_LONG, gasolinera.getLongitud());
        reg.put(FavoritoDAO.C_COLUMNA_ID_GASOL_LAT, gasolinera.getLatitud());
        reg.put(FavoritoDAO.C_COLUMNA_ID_USUARIO, usu1.getEmail());
        Cursor cursorFavorito = favoritoDAO.getRegistro(usu1.getEmail());
        if (cursorFavorito == null)
            favoritoDAO.add(reg);

        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        usuarioDAO.abrir();
        Usuario usuEnBD = (Usuario) usuarioDAO.search(usu1);
        if (usuEnBD != null) {
            usuEnBD.setFavoritos(gasolineraDAO.getGasolineras(usuEnBD));
            for (Gasolinera gasolinera1 : usuEnBD.getFavoritos()) {
                Log.i("/////////", " GAS 1 " + gasolinera1.toString());
            }
        }

        favoritoDAO.delete(gasolinera.getLongitud(), gasolinera.getLatitud(), usuEnBD.getEmail());
        if (usuEnBD != null) {
            usuEnBD.setFavoritos(gasolineraDAO.getGasolineras(usuEnBD));
            for (Gasolinera gasolinera1 : usuEnBD.getFavoritos()) {
                Log.i("/////////", " GAS 2 " + gasolinera1.toString());
            }
        }*/

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

    public void leer() {
        ContentValues reg = new ContentValues();

        try {
            InputStream myInput;
            // Inicializar asset manager
            // AssetManager assetManager = getAssets();
            //  abrir archivo excel file name as myexcelfile.xls
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
                                reg.put(GasolineraDAO.C_COLUMNA_PROVINCIA, cell.toString());
                                break;

                            case 2:
                                reg.put(GasolineraDAO.C_COLUMNA_MUNICIPIO, cell.toString());
                                break;

                            case 4:
                                reg.put(GasolineraDAO.C_COLUMNA_CODPOSTAL, cell.toString());
                                break;

                            case 5:
                                reg.put(GasolineraDAO.C_COLUMNA_DIRECCION, cell.toString());
                                break;

                            case 7:
                                reg.put(GasolineraDAO.C_COLUMNA_ID_LONGITUD, cell.toString());
                                break;

                            case 8:
                                reg.put(GasolineraDAO.C_COLUMNA_ID_LATITUD, cell.toString());
                                break;

                            case 9:
                                reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLINA95, cell.toString());
                                break;

                            case 12:
                                reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLINA98, cell.toString());
                                break;

                            case 14:
                                reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLEOA, cell.toString());
                                break;

                            case 15:
                                reg.put(GasolineraDAO.C_COLUMNA_PRECIOGASOLEOPREMIUM, cell.toString());
                                break;

                            case 26:
                                reg.put(GasolineraDAO.C_COLUMNA_ROTULO, cell.toString());
                                break;

                            case 29:
                                reg.put(GasolineraDAO.C_COLUMNA_HORARIO, cell.toString());
                                break;

                            default:
                                break;
                        }
                    }
                    countColumn = 0;
                    Cursor c = gasolineraDAO.getRegistro(reg.getAsString(GasolineraDAO.C_COLUMNA_ID_LONGITUD), reg.getAsString(GasolineraDAO.C_COLUMNA_ID_LATITUD));

                    // Si getCount() devuelve 0, no hay registro con esos ids
                    // Si getCount() devuelve 1, hay un registro con esos ids
                    if (reg.getAsString(GasolineraDAO.C_COLUMNA_PROVINCIA).contains("VALENCIA")){
                        if (c.getCount() == 0) gasolineraDAO.add(reg);
                        else gasolineraDAO.update(reg);
                    }
                    c.close();
                    Log.i("///////////", " FECHA " + fecha);
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