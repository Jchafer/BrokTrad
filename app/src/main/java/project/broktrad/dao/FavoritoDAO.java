package project.broktrad.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import project.broktrad.bd.MiBD;

public class FavoritoDAO {

    /**
     * Definimos constante con el nombre de la tabla
     */
    public static final String C_TABLA = "favoritos" ;
    /**
     * Definimos constantes con el nombre de las columnas de la tabla
     */
    public static final String C_COLUMNA_ID_GASOL = "id_gasol";
    public static final String C_COLUMNA_EMAIL = "email";
    private Context contexto;
    private MiBD miBD;
    private SQLiteDatabase db;

    /**
     * Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
     */
    private String[] columnas = new String[]{ C_COLUMNA_ID_GASOL, C_COLUMNA_EMAIL };

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
     * SELECT * FROM favoritos;
     */
    public Cursor getCursor() {
        Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);
        return c;
    }

    public Cursor getRegistro(String email){
        String condicion = C_COLUMNA_EMAIL + "='" + email + "'";
        Cursor c = db.query(true, C_TABLA, columnas, condicion, null, null, null, null,null);
        if (c!=null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getRegistroUnico(String id_gasol, String email){
        String condicion = C_COLUMNA_ID_GASOL + "='" + id_gasol + "' AND " + C_COLUMNA_EMAIL + "='" + email + "'";
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
        String condicion = C_COLUMNA_ID_GASOL + "='" + contentValues.getAsString(C_COLUMNA_ID_GASOL) + "' AND " + C_COLUMNA_EMAIL + "='" + contentValues.getAsString(C_COLUMNA_EMAIL);

        int resultado = db.update(C_TABLA, contentValues, condicion, null);

        return resultado;
    }

    public void delete(String id_gasol, String email) {
        String condicion = C_COLUMNA_ID_GASOL + "='" + id_gasol + "' AND " + C_COLUMNA_EMAIL + "='" + email + "'";

        //Se borra el registro favorito indicado en los campos de texto
        db.delete(C_TABLA, condicion, null);
    }

    public Cursor getAll() {
        Cursor cursor = db.query(C_TABLA, columnas, null, null, null, null, null);
        return cursor;
    }

    public ArrayList<String> gasolinerasFavoritas(String email) {
        ArrayList<String> idGasolineras = new ArrayList<>();
        String condicion = C_COLUMNA_EMAIL + "='" + email + "'";
        // Obtenemos un cursor con los registros que tenga el mismo email
        Cursor cursor = db.query(true, C_TABLA, columnas, condicion, null, null, null, null,null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Cursor cursorGasolinera = getRegistroUnico(cursor.getString(0), cursor.getString(1));
                if (cursorGasolinera.moveToFirst()) {
                    do {
                        // Añadimos el id de la gasolinera a la lista
                        idGasolineras.add(cursorGasolinera.getString(0));
                    } while (cursorGasolinera.moveToNext());
                }
            } while (cursor.moveToNext());
        }
        return idGasolineras;
    }
}
