package es.iessaladillo.pedrojoya.galileo.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper {

    // Constructor. Recibe el contexto.
    public BDHelper(Context ctx) {
        // Se llama al constructor del padre, que es quien realmente crea o
        // actualiza la versi�n de BD si es necesario.
        super(ctx, BD.DB_NAME, null, BD.DB_VERSION);
    }

    // Se llama autom�ticamente al intentar abrir una base de datos que
    // no exista a�n.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecutan las sentencias SQL de creaci�n de las tablas.
        db.execSQL(BD.Tienda.CREATE);
        db.execSQL(BD.Foto.CREATE);
        db.execSQL(BD.Comentario.CREATE);
        db.execSQL(BD.Imagen.CREATE);
    }

    // Se llama autom�ticamente al intentar abrir una base de datos con una
    // versi�n
    // distinta a la que tiene actualmente.
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
            int versionNueva) {
        // Por simplicidad, se eliminan las tablas existentes y se vuelven a
        // crear,
        db.execSQL(BD.Tienda.DROP);
        db.execSQL(BD.Tienda.CREATE);
        db.execSQL(BD.Foto.DROP);
        db.execSQL(BD.Foto.CREATE);
        db.execSQL(BD.Comentario.DROP);
        db.execSQL(BD.Comentario.CREATE);
        db.execSQL(BD.Imagen.DROP);
        db.execSQL(BD.Imagen.CREATE);
    }
}
