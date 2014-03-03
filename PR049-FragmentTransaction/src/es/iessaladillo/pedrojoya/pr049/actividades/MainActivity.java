package es.iessaladillo.pedrojoya.pr049.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import es.iessaladillo.pedrojoya.pr049.R;
import es.iessaladillo.pedrojoya.pr049.fragmentos.DetalleFragment;
import es.iessaladillo.pedrojoya.pr049.fragmentos.ListaFragment.OnAlbumSelectedListener;
import es.iessaladillo.pedrojoya.pr049.modelos.Album;

public class MainActivity extends FragmentActivity implements
        OnAlbumSelectedListener {

    // Constantes.
    public static final String EXTRA_ALBUM = "es.iessaladillo.pr049.ALBUM";

    // Variables miembro.
    private FragmentManager gestor;
    private FrameLayout flDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtengo las referencias a los objetos.
        flDetalle = (FrameLayout) this.findViewById(R.id.flDetalle);
        gestor = getSupportFragmentManager();
    }

    // Cuando en el fragmento frgLista se selecciona un �lbum.
    @Override
    public void onAlbumSelected(Album album) {
        // Si hay FrameLayout de detalle (puede que no haya porque por el tama�o
        // del dispositivo tengamos dos actividades distintas).
        if (flDetalle != null) {
            // Muestro el detalle del �lbum.
            mostrarFragmentoDetalle(album);
        } else {
            // Hay dos actividades. Llamo a la otra actividad pas�ndole el �lbum
            // que debe mostrar (cuya clase debe implementar Parcelable).
            Intent i = new Intent(this, DetalleActivity.class);
            i.putExtra(EXTRA_ALBUM, album);
            this.startActivity(i);
        }
    }

    public void mostrarFragmentoDetalle(Album album) {
        // Inicio la transacci�n.
        FragmentTransaction transaccion = gestor.beginTransaction();
        // Creo una nueva instancia del fragmento de detalle pas�ndole el �lbum
        // como par�metro.
        DetalleFragment frgDetalle = DetalleFragment.newInstance(album);
        // A�ado a la transacci�n el colocar el fragmento en el FrameLayout.
        transaccion.replace(R.id.flDetalle, frgDetalle);
        transaccion.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaccion.addToBackStack("prueba");
        // Finalizo la transacci�n.
        transaccion.commit();
    }
}
