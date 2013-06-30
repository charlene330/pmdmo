package es.iessaladillo.pedrojoya.pr027.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper {

    // Constantes de sentencias SQL.
    private static final String TBL_ALUMNO_CREATE = "create table "
            + Instituto.Alumno.TABLA + "(" + Instituto.Alumno._ID
            + " integer primary key autoincrement, " + Instituto.Alumno.NOMBRE
            + " text not null, " + Instituto.Alumno.CURSO + " text not null, "
            + Instituto.Alumno.TELEFONO + " text not null, "
            + Instituto.Alumno.DIRECCION + " text);";
    private static final String TBL_ALUMNO_DROP = "drop table if exists "
            + Instituto.Alumno.TABLA;

    // Constructor. Recibe el contexto.
    public Helper(Context ctx) {
        // Se llama al constructor del padre, que es quien realmente crea o
        // actualiza la versi�n de BD si es necesario.
        super(ctx, Instituto.BD_NOMBRE, null, Instituto.BD_VERSION);
    }

    // Cuando es necesario crear la BD.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecuto las sentencias SQL de creaci�n de las tablas de la BD.
        db.execSQL(TBL_ALUMNO_CREATE);
    }

    // M�todo de callback para cuando la BD debe se actualizada de versi�n
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se eliminan las tablas y se recrean de nuevo.
        db.execSQL(TBL_ALUMNO_DROP);
        db.execSQL(TBL_ALUMNO_CREATE);
    }
}
