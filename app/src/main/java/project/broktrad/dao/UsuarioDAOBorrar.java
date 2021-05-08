package project.broktrad.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import project.broktrad.bd.MiBD;
import project.broktrad.pojo.UsuarioBorrar;

public class UsuarioDAOBorrar {

    /**
     * Definimos constante con el nombre de la tabla
     */
    public static final String C_TABLA = "usuarios" ;
    /**
     * Definimos constantes con el nombre de las columnas de la tabla
     */
    public static final String C_COLUMNA_ID_EMAIL = "email";
    public static final String C_COLUMNA_CLAVE = "clave";
    public static final String C_COLUMNA_NICK = "nick";
    public static final String C_COLUMNA_ADMIN = "admin";

    private Context contexto;
    private MiBD miBD;
    private SQLiteDatabase db;

    /**
     * Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
     */
    private String[] columnas = new String[]{ C_COLUMNA_ID_EMAIL, C_COLUMNA_CLAVE, C_COLUMNA_NICK, C_COLUMNA_ADMIN } ;

    public UsuarioDAOBorrar(Context context) {
        this.contexto = context;
    }

    public UsuarioDAOBorrar abrir(){
        miBD = new MiBD(contexto);
        db = miBD.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        miBD.close();
    }

    /**
     * Devuelve un cursor con todas las filas y todas las columnas de la tabla usuarios
     * SELECT * FROM usuarios;
     */
    public Cursor getCursor() {
        Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);
        return c;
    }

    public Cursor getRegistro(String id){
        String condicion = C_COLUMNA_ID_EMAIL + "=" + id;
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
        String condicion = C_COLUMNA_ID_EMAIL + "='" + contentValues.getAsString(C_COLUMNA_ID_EMAIL) + "'";

        int resultado = db.update(C_TABLA, contentValues, condicion, null);

        return resultado;
    }

    public void delete(String _id) {
        String condicion = C_COLUMNA_ID_EMAIL + "=" + _id;

        //Se borra el cliente indicado en el campo de texto
        db.delete(C_TABLA, condicion, null);
    }

    public Cursor getAll() {
        Cursor cursor = db.query(C_TABLA, columnas, null, null, null, null, null);
        return cursor;
    }

    public Object search(Object obj) {
        UsuarioBorrar c = (UsuarioBorrar) obj;
        String condicion = C_COLUMNA_ID_EMAIL + "='" + c.getEmail() + "'";

        Cursor cursor = db.query("usuarios", columnas, condicion, null, null, null, null);
        UsuarioBorrar nuevoUsuario = null;
        if (cursor.moveToFirst()) {
            nuevoUsuario = new UsuarioBorrar();
            nuevoUsuario.setEmail(cursor.getString(0));
            nuevoUsuario.setClave(cursor.getString(1));
            nuevoUsuario.setNick(cursor.getString(2));
            nuevoUsuario.setAdmin(cursor.getInt(3) > 0);

            // Obtenemos la lista de gasolineras que tiene el cliente
            //Cursor cursorNuevo = miBD.getGasolineraDAO().obtenerFavoritos(c.getEmail());
            //nuevoUsuario.setFavoritos(miBD.getGasolineraDAO().getGasolineras(nuevoUsuario));

        }
        return nuevoUsuario;
    }

}
