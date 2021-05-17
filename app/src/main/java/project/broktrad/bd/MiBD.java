package project.broktrad.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

import project.broktrad.dao.FavoritoDAO;

public class MiBD extends SQLiteOpenHelper implements Serializable {

    private static SQLiteDatabase db;
    // Nombre de la base de datos
    private static final String database = "Gasolineras_precios";
    // Versión de la base de datos
    private static final int version = 14;
    private String sqlCreacionFavoritos = "CREATE TABLE favoritos (id_gasol TEXT PRIMARY KEY, email TEXT);";

    private static MiBD instance = null;

    private static FavoritoDAO favoritoDAO;

    public FavoritoDAO getFavoritoDAO() {
        return favoritoDAO;
    }

    public static MiBD getInstance(Context context) {
        if(instance == null) {
            instance = new MiBD(context);
            db = instance.getWritableDatabase();
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
        db.execSQL(sqlCreacionFavoritos);

        insercionDatos(db);

        Log.i("SQLite", "Se crea la base de datos " + database + " version " + version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ) {
        Log.i("SQLite", "Control de versiones: Old Version=" + oldVersion + " New Version= " + newVersion  );
        if ( newVersion > oldVersion )
        {
            // Elimina tabla
            db.execSQL( "DROP TABLE IF EXISTS favoritos" );
            // Creamos la nueva tabla
            db.execSQL(sqlCreacionFavoritos);

            insercionDatos(db);

            Log.i("SQLite", "Se actualiza versión de la base de datos, New version= " + newVersion  );
        }
    }

    private void insercionDatos(SQLiteDatabase db){
        // Insertamos los datos de prueba
        db.execSQL("INSERT INTO favoritos (id_gasol, email) VALUES ('4061', 'admin@admin.com');");
        db.execSQL("INSERT INTO favoritos (id_gasol, email) VALUES ('11954', 'admin@admin.com');");
    }

}