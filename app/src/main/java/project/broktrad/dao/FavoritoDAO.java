package project.broktrad.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import project.broktrad.bd.MiBD;
import project.broktrad.pojo.Gasolinera;
import project.broktrad.pojo.Usuario;

public class FavoritoDAO {

    /**
     * Definimos constante con el nombre de la tabla
     */
    public static final String C_TABLA = "favoritos" ;
    /**
     * Definimos constantes con el nombre de las columnas de la tabla
     */
    public static final String C_COLUMNA_ID_GASOL_LONG = "gasol_long";
    public static final String C_COLUMNA_ID_GASOL_LAT = "gasol_lat";
    public static final String C_COLUMNA_ID_USUARIO = "usuario";
    private Context contexto;
    private MiBD miBD;
    private SQLiteDatabase db;

    /**
     * Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
     */
    private String[] columnas = new String[]{ C_COLUMNA_ID_GASOL_LONG, C_COLUMNA_ID_GASOL_LAT, C_COLUMNA_ID_USUARIO } ;

    public FavoritoDAO(Context context) {
        this.contexto = context;
    }

    public FavoritoDAO abrir(){
        miBD = new MiBD(contexto);
        db = miBD.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        miBD.close();
    }

    /**
     * Devuelve un cursor con todas las filas y todas las columnas de la tabla favoritos
     * SELECT * FROM usuarios;
     */
    public Cursor getCursor() {
        Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);
        return c;
    }

    public Cursor getRegistro(String email){
        String condicion = C_COLUMNA_ID_USUARIO + "='" + email + "'";
        Cursor c = db.query(true, C_TABLA, columnas, condicion, null, null, null, null,null);
        if (c!=null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRegistroUnico(String longitud, String latitud, String email){
        String condicion = C_COLUMNA_ID_GASOL_LONG + "='" + longitud + "' AND " + C_COLUMNA_ID_GASOL_LAT + "='" + latitud + "' AND " + C_COLUMNA_ID_USUARIO + "='" + email + "'";
        Cursor c = db.query(true, C_TABLA, columnas, condicion, null, null, null, null,null);
        if (c!=null){
            c.moveToFirst();
        }
        return c;
    }

    public long add(ContentValues contentValues) {
        return db.insert(C_TABLA, null, contentValues);
    }

    public long update(ContentValues contentValues) {
        String condicion = C_COLUMNA_ID_GASOL_LONG + "='" + contentValues.getAsString(C_COLUMNA_ID_GASOL_LONG) + "' AND " + C_COLUMNA_ID_GASOL_LAT + "='" + contentValues.getAsString(C_COLUMNA_ID_GASOL_LAT) + "' AND " + C_COLUMNA_ID_USUARIO + "=" + contentValues.getAsString(C_COLUMNA_ID_USUARIO);

        int resultado = db.update(C_TABLA, contentValues, condicion, null);

        return resultado;
    }

    public void delete(String longitud, String latitud, String email) {
        String condicion = C_COLUMNA_ID_GASOL_LONG + "='" + longitud + "' AND " + C_COLUMNA_ID_GASOL_LAT + "='" + latitud + "' AND " + C_COLUMNA_ID_USUARIO + "='" + email + "'";

        //Se borra el cliente indicado en el campo de texto
        db.delete(C_TABLA, condicion, null);
    }

    public Cursor getAll() {
        Cursor cursor = db.query(C_TABLA, columnas, null, null, null, null, null);
        return cursor;
    }


}
