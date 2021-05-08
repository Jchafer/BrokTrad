package project.broktrad.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

import project.broktrad.dao.FavoritoDAO;

public class MiBD extends SQLiteOpenHelper implements Serializable {

    private static SQLiteDatabase db;
    //nombre de la base de datos
    private static final String database = "Gasolineras_precios";
    //versión de la base de datos
    private static final int version = 14;
    //Instruccion SQL para crear la tabla de gasolineras
    /*private String sqlCreacionGasolineras = "CREATE TABLE gasolineras ( codPostal INTEGER PRIMARY KEY, provincia TEXT, municipio TEXT, direccion TEXT, longitud LONG, latitud LONG," +
            "precioGasolina95 LONG, precioGasolina98 LONG, precioGasoleoA LONG, precioGasoleoPremium LONG, rotulo TEXT, horario TEXT);";*/
    /*private String sqlCreacionGasolineras = "CREATE TABLE gasolineras (longitud LONG, latitud LONG, provincia TEXT, municipio TEXT, codPostal TEXT NOT NULL, direccion TEXT," +
            "precioGasolina95 LONG, precioGasolina98 LONG, precioGasoleoA LONG, precioGasoleoPremium LONG, rotulo TEXT, horario TEXT, PRIMARY KEY(longitud, latitud));";*/
    //private String sqlCreacionUsuarios = "CREATE TABLE usuarios (email TEXT PRIMARY KEY, clave TEXT, nick TEXT);";
    //private String sqlCreacionFavoritos = "CREATE TABLE favoritos (gasol_long LONG, gasol_lat LONG, usuario TEXT, PRIMARY KEY(gasol_long, gasol_lat, usuario), FOREIGN KEY (gasol_long) REFERENCES gasolineras(longitud), FOREIGN KEY (gasol_lat) REFERENCES gasolineras(latitud), FOREIGN KEY (usuario) REFERENCES usuarios(email));";
    private String sqlCreacionFavoritos = "CREATE TABLE favoritos (id_gasol TEXT PRIMARY KEY, email TEXT);";


    private static MiBD instance = null;

    //private static GasolineraDAO gasolineraDAO;
    //private static UsuarioDAO usuarioDAO;
    private static FavoritoDAO favoritoDAO;

    /*public GasolineraDAO getGasolineraDAO() {
        return gasolineraDAO;
    }*/
    /*public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }*/
    public FavoritoDAO getFavoritoDAO() {
        return favoritoDAO;
    }

    public static MiBD getInstance(Context context) {
        if(instance == null) {
            instance = new MiBD(context);
            db = instance.getWritableDatabase();
            //gasolineraDAO = new GasolineraDAO(context);
            //usuarioDAO = new UsuarioDAO(context);
            favoritoDAO = new FavoritoDAO(context);
        }
        return instance;
    }

    public static SQLiteDatabase getDB(){
        return db;
    }
    public static void closeDB(){db.close();};

    /**
     * Constructor de clase
     * */
    public MiBD(Context context) {
        super( context, database, null, version );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(sqlCreacionGasolineras);
        //db.execSQL(sqlCreacionUsuarios);
        db.execSQL(sqlCreacionFavoritos);

        insercionDatos(db);

        //upgrade_2(db);

        Log.i("SQLite", "Se crea la base de datos " + database + " version " + version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ) {
        Log.i("SQLite", "Control de versiones: Old Version=" + oldVersion + " New Version= " + newVersion  );
        if ( newVersion > oldVersion )
        {
            //elimina tablas
            //db.execSQL( "DROP TABLE IF EXISTS gasolineras" );
            db.execSQL( "DROP TABLE IF EXISTS usuarios" );
            db.execSQL( "DROP TABLE IF EXISTS favoritos" );
            //y luego creamos las nuevas tablas
            //db.execSQL(sqlCreacionGasolineras);
            //db.execSQL(sqlCreacionUsuarios);
            db.execSQL(sqlCreacionFavoritos);

            //insercionDatos(db);

            /*if (oldVersion > 2)
            {
                upgrade_2(db);
            }*/

            Log.i("SQLite", "Se actualiza versión de la base de datos, New version= " + newVersion  );
        }
    }

    private void insercionDatos(SQLiteDatabase db){
        // Insertamos los datos de prueba
        //db.execSQL("INSERT INTO gasolineras (provincia, municipio, codPostal, direccion, longitud, latitud, precioGasolina95, precioGasolina98, precioGasoleoA, precioGasoleoPremium, rotulo, horario) VALUES ('ALBACETE', 'ALBACETE', '02001', 'CALLE FEDERICO GARCIA LORCA, 1', '-1,849833', '39,000861', '1.079', null, '0.969', null, 'PLENOIL', 'L-D: 24H');");
        //db.execSQL("INSERT INTO gasolineras (provincia, municipio, codPostal, direccion, longitud, latitud, precioGasolina95, precioGasolina98, precioGasoleoA, precioGasoleoPremium, rotulo, horario) VALUES ('ALBACETETE', 'ALBACETE', '02001', 'CALLE FEDERICO GARCIA LORCA, 1', '-1,849834', '39,000862', '1.079', null, '0.969', null, 'PLENOIL', 'L-D: 24H');");
        //db.execSQL("INSERT INTO usuarios (email, clave, nick) VALUES ('prueba@prueba.com', 'pruebaA1@', 'prueba');");
        //db.execSQL("INSERT INTO favoritos (gasol_long, gasol_lat, usuario) VALUES ('-1,849833', '39,000861', 'admin@admin.com');");
        //db.execSQL("INSERT INTO favoritos (gasol_long, gasol_lat, usuario) VALUES ('-1,849835', '39,000862', 'admin@admin.com');");
        db.execSQL("INSERT INTO favoritos (id_gasol, email) VALUES ('4061', 'admin@admin.com');");
        db.execSQL("INSERT INTO favoritos (id_gasol, email) VALUES ('11954', 'admin@admin.com');");
    }

    /*private void upgrade_2(SQLiteDatabase db) {
        //
        // Upgrade versión 2: ***
        //
        db.execSQL("ALTER TABLE usuarios ADD admin BOOLEAN NOT NULL DEFAULT 'false'");
        db.execSQL("INSERT INTO usuarios (email, clave, nick, admin) VALUES ('admin@admin.com', 'adminA1@', 'admin', 'true');");
        Log.i(this.getClass().toString(), "Actualización versión 2 finalizada");
    }*/

}