package es.iessaladillo.pedrojoya.pr047.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import es.iessaladillo.pedrojoya.pr047.R;
import es.iessaladillo.pedrojoya.pr047.fragmentos.DetalleFragment;
import es.iessaladillo.pedrojoya.pr047.interfaces.OnAlbumSelectedListener;
import es.iessaladillo.pedrojoya.pr047.modelos.Album;

public class MainActivity extends FragmentActivity implements
        OnAlbumSelectedListener {

    // Constantes.
    public static final String EXTRA_ALBUM = "es.iessaladillo.pr047.ALBUM";

    // Variables miembro.
    private DetalleFragment frgDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtiene la referencia al fragmento de detalle.
        frgDetalle = (DetalleFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.frgDetalle);
    }

    // Cuando en el fragmento frgLista se selecciona un �lbum.
    @Override
    public void onAlbumSelected(Album album) {
        // Si hay fragmento de detalle (puede que no haya porque por el tama�o
        // del dispositivo tengamos dos actividades distintas).
        if (frgDetalle != null) {
            // Muestro el detalle del �lbum.
            frgDetalle.mostrarDetalle(album);
        }
        else {
            // Hay dos actividades. Llamo a la otra actividad pas�ndole el �lbum
            // que debe mostrar (cuya clase debe implementar Parcelable).
            Intent i = new Intent(this, DetalleActivity.class);
            i.putExtra(EXTRA_ALBUM, album);
            this.startActivity(i);
        }
    }
}
