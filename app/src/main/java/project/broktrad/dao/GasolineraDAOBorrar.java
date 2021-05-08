package project.broktrad.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import project.broktrad.bd.MiBD;
import project.broktrad.pojo.GasolineraBorrar;

public class GasolineraDAOBorrar {
    /**
     * Definimos constante con el nombre de la tabla
     */
    public static final String C_TABLA = "gasolineras" ;
    /**
     * Definimos constantes con el nombre de las columnas de la tabla
     */
    public static final String C_COLUMNA_ID_LONGITUD = "longitud";
    public static final String C_COLUMNA_ID_LATITUD = "latitud";
    public static final String C_COLUMNA_PROVINCIA = "provincia";
    public static final String C_COLUMNA_MUNICIPIO = "municipio";
    public static final String C_COLUMNA_CODPOSTAL = "codPostal";
    public static final String C_COLUMNA_DIRECCION = "direccion";
    public static final String C_COLUMNA_PRECIOGASOLINA95 = "precioGasolina95";
    public static final String C_COLUMNA_PRECIOGASOLINA98 = "precioGasolina98";
    public static final String C_COLUMNA_PRECIOGASOLEOA = "precioGasoleoA";
    public static final String C_COLUMNA_PRECIOGASOLEOPREMIUM = "precioGasoleoPremium";
    public static final String C_COLUMNA_ROTULO = "rotulo";
    public static final String C_COLUMNA_HORARIO = "horario";

    private Context contexto;
    private MiBD miBD;
    private SQLiteDatabase db;

    /**
     * Definimos lista de columnas de la tabla para utilizarla en las consultas a la base de datos
     */
    private String[] columnas = new String[]{ C_COLUMNA_ID_LONGITUD, C_COLUMNA_ID_LATITUD, C_COLUMNA_PROVINCIA,
            C_COLUMNA_MUNICIPIO, C_COLUMNA_CODPOSTAL, C_COLUMNA_DIRECCION, C_COLUMNA_PRECIOGASOLINA95,
            C_COLUMNA_PRECIOGASOLINA98, C_COLUMNA_PRECIOGASOLEOA, C_COLUMNA_PRECIOGASOLEOPREMIUM, C_COLUMNA_ROTULO,
            C_COLUMNA_HORARIO} ;

    public GasolineraDAOBorrar(Context context) {
        this.contexto = context;
    }

    public GasolineraDAOBorrar abrir(){
        miBD = new MiBD(contexto);
        db = miBD.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        miBD.close();
    }

    /**
     * Devuelve un cursor con todas las filas y todas las columnas de la tabla gasolineras
     * SELECT * FROM gasolineras;
     */
    public Cursor getCursor() {
        Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);
        return c;
    }

    public Cursor getRegistro(String longitud, String latitud){
        String condicion = C_COLUMNA_ID_LONGITUD + "='" + longitud + "' AND " + C_COLUMNA_ID_LATITUD + "='" + latitud + "'";
        Cursor c = db.query(true, C_TABLA, columnas, condicion, null, null, null, null,null);
        if (c!=null){
            c.moveToFirst();
        }
        return c;
    }

    public long add(ContentValues contentValues) {
        if (db == null)
            abrir();
        return db.insert(C_TABLA, null, contentValues);
    }

    public long update(ContentValues contentValues) {
        String condicion = C_COLUMNA_ID_LONGITUD + "='" + contentValues.getAsString(C_COLUMNA_ID_LONGITUD) + "' and " + C_COLUMNA_ID_LATITUD + "='" + contentValues.getAsString(C_COLUMNA_ID_LATITUD) + "'";

        int resultado = db.update(C_TABLA, contentValues, condicion, null);

        return resultado;
    }

    public void delete(String longitud, String latitud) {
        String condicion = C_COLUMNA_ID_LONGITUD + "='" + longitud + "' AND " + C_COLUMNA_ID_LATITUD + "='" + latitud + "'";

        //Se borra el cliente indicado en el campo de texto
        db.delete(C_TABLA, condicion, null);
    }

    public Cursor getAll() {
        Cursor cursor = db.query(C_TABLA, columnas, null, null, null, null, null);
        return cursor;
    }

    public ArrayList getGasolineras(String email) {
        ArrayList<GasolineraBorrar> listaGasolineras = new ArrayList();

        FavoritoDAO favoritoDAO = miBD.getFavoritoDAO();
        favoritoDAO.abrir();
        Cursor cursorFavoritos = favoritoDAO.getRegistro(email);
        if (cursorFavoritos.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Cursor cursorGasolinera = getRegistro(cursorFavoritos.getString(0), cursorFavoritos.getString(1));
                if (cursorGasolinera.moveToFirst()) {
                    do {
                        GasolineraBorrar g = new GasolineraBorrar();
                        //g.setLongitud(Long.parseLong(cursorGasolinera.getString(0)));
                        g.setLongitud(cursorGasolinera.getString(0));
                        g.setLatitud(cursorGasolinera.getString(1));
                        g.setProvincia(cursorGasolinera.getString(2));
                        g.setMunicipio(cursorGasolinera.getString(3));
                        g.setCodPostal(cursorGasolinera.getString(4));
                        g.setDireccion(cursorGasolinera.getString(5));
                        g.setPrecioGasolina95(cursorGasolinera.getString(6));
                        g.setPrecioGasolina98(cursorGasolinera.getString(7));
                        g.setPrecioGasoleoA(cursorGasolinera.getString(8));
                        g.setPrecioGasoleoPremium(cursorGasolinera.getString(9));
                        g.setRotulo(cursorGasolinera.getString(10));
                        g.setHorario(cursorGasolinera.getString(11));

                        listaGasolineras.add(g);

                    } while (cursorGasolinera.moveToNext());
                }
            } while (cursorFavoritos.moveToNext());
        }
        return listaGasolineras;
    }

    public ArrayList getGasolinerasTodas() {
        ArrayList<GasolineraBorrar> listaGasolineras = new ArrayList();

        Cursor cursor = getCursor();
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Cursor cursorGasolinera = getRegistro(cursor.getString(0), cursor.getString(1));
                if (cursorGasolinera.moveToFirst()) {
                    do {
                        GasolineraBorrar g = new GasolineraBorrar();
                        //g.setLongitud(Long.parseLong(cursorGasolinera.getString(0)));
                        g.setLongitud(cursorGasolinera.getString(0));
                        g.setLatitud(cursorGasolinera.getString(1));
                        g.setProvincia(cursorGasolinera.getString(2));
                        g.setMunicipio(cursorGasolinera.getString(3));
                        g.setCodPostal(cursorGasolinera.getString(4));
                        g.setDireccion(cursorGasolinera.getString(5));
                        g.setPrecioGasolina95(cursorGasolinera.getString(6));
                        g.setPrecioGasolina98(cursorGasolinera.getString(7));
                        g.setPrecioGasoleoA(cursorGasolinera.getString(8));
                        g.setPrecioGasoleoPremium(cursorGasolinera.getString(9));
                        g.setRotulo(cursorGasolinera.getString(10));
                        g.setHorario(cursorGasolinera.getString(11));

                        listaGasolineras.add(g);

                    } while (cursorGasolinera.moveToNext());
                }
            } while (cursor.moveToNext());
        }
        return listaGasolineras;
    }

    public Cursor obtenerFavoritos(String email){
        String[] columnas = new String[]{ "gasol_long", "gasol_lat", "usuario" } ;
        String condicion = "usuario" + "='" + email + "'";
        Log.i("/////////", " HOLA " + email);
        Cursor c = db.query(true, "favoritos", columnas, condicion, null, null, null, null,null);
        //Log.i("/////////", " HOLA " + c.getString(1));
        if (c!=null){
            c.moveToFirst();
        }
        return c;
    }

    public  ArrayList<GasolineraBorrar> gasolinerasPorMunicipio(String municipio){
        ArrayList<GasolineraBorrar> gasolineras = new ArrayList();

        String condicion = C_COLUMNA_MUNICIPIO + "='" + municipio.toUpperCase() + "'";
        Cursor cursor = db.query(true, C_TABLA, columnas, condicion, null, null, null, null,null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Cursor cursorGasolinera = getRegistro(cursor.getString(0), cursor.getString(1));
                if (cursorGasolinera.moveToFirst()) {
                    do {
                        GasolineraBorrar g = new GasolineraBorrar();
                        //g.setLongitud(Long.parseLong(cursorGasolinera.getString(0)));
                        g.setLongitud(cursorGasolinera.getString(0));
                        g.setLatitud(cursorGasolinera.getString(1));
                        g.setProvincia(cursorGasolinera.getString(2));
                        g.setMunicipio(cursorGasolinera.getString(3));
                        g.setCodPostal(cursorGasolinera.getString(4));
                        g.setDireccion(cursorGasolinera.getString(5));
                        g.setPrecioGasolina95(cursorGasolinera.getString(6));
                        g.setPrecioGasolina98(cursorGasolinera.getString(7));
                        g.setPrecioGasoleoA(cursorGasolinera.getString(8));
                        g.setPrecioGasoleoPremium(cursorGasolinera.getString(9));
                        g.setRotulo(cursorGasolinera.getString(10));
                        g.setHorario(cursorGasolinera.getString(11));

                        gasolineras.add(g);

                    } while (cursorGasolinera.moveToNext());
                }
            } while (cursor.moveToNext());
        }

        if (gasolineras.isEmpty())
            return null;
        else
            return gasolineras;
    }
}
