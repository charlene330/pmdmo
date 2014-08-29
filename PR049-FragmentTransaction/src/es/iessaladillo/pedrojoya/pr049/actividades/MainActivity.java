package es.iessaladillo.pedrojoya.pr049.actividades;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;
import es.iessaladillo.pedrojoya.pr049.R;
import es.iessaladillo.pedrojoya.pr049.fragmentos.DetalleFragment;
import es.iessaladillo.pedrojoya.pr049.fragmentos.DetalleFragment.OnDetalleShownListener;
import es.iessaladillo.pedrojoya.pr049.fragmentos.ListaFragment;
import es.iessaladillo.pedrojoya.pr049.fragmentos.ListaFragment.OnObraSelectedListener;
import es.iessaladillo.pedrojoya.pr049.modelos.Obra;

public class MainActivity extends Activity implements OnObraSelectedListener,
        OnDetalleShownListener, OnBackStackChangedListener {

    // Variables.
    private FragmentManager mGestorFragmentos;

    // Vistas.
    private FrameLayout flDetalle;

    // Al crear la actividad.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se obtienen las vistas.
        flDetalle = (FrameLayout) this.findViewById(R.id.flDetalle);
        // Se obtiene el gestor de fragmentos.
        mGestorFragmentos = getFragmentManager();
        // La actividad actu�ra como listener cuando se utilice la BackStack.
        mGestorFragmentos.addOnBackStackChangedListener(this);
        // Si el fragmento de lista no est� cargado ya, se carga.
        // MUY IMPORTANTE: Si no, se llama varias veces el onActivityCreated()
        // del fragmento.
        ListaFragment frgLista = (ListaFragment) mGestorFragmentos
                .findFragmentByTag("frgLista");
        if (frgLista == null) {
            // Se carga el fragmento de lista (sin a�adirlo a la BackStack, ya
            // que es un fragmento fijo que adem�s retendr� su estado entre
            // cambios de configuraci�n).
            frgLista = new ListaFragment();
            mGestorFragmentos.beginTransaction()
                    .replace(R.id.flLista, frgLista, "frgLista").commit();
        }
    }

    // Cuando en el fragmento frgLista se selecciona una obra.
    @Override
    public void onObraSelected(Obra obra, int position) {
        // Si hay FrameLayout de detalle (puede que no haya porque por el tama�o
        // del dispositivo tengamos dos actividades distintas).
        if (flDetalle != null) {
            // Se muestra el detalle de la obra.
            mostrarFragmentoDetalle(obra, position);
        } else {
            // Hay dos actividades. Se llama a la otra actividad pas�ndole la
            // obra a mostrar (cuya clase debe implementar Parcelable).
            Intent i = new Intent(this, DetalleActivity.class);
            i.putExtra(DetalleFragment.EXTRA_OBRA, obra);
            this.startActivity(i);
        }
    }

    // Carga el fragmento de detalle en el FrameLayout correspondiente.
    public void mostrarFragmentoDetalle(Obra obra, int position) {
        // Se crea una nueva instancia del fragmento de detalle pas�ndole la
        // obra como par�metro.
        DetalleFragment frgDetalle = DetalleFragment
                .newInstance(obra, position);
        // Se realiza la transacci�n y se a�ade a la BackStack especificando
        // como tag el �ndice de la lista.
        FragmentTransaction transaccion = mGestorFragmentos.beginTransaction();
        transaccion.replace(R.id.flDetalle, frgDetalle);
        transaccion.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaccion.addToBackStack(obra.getNombre());
        transaccion.commit();
    }

    // Cuando se muestra un determinado detalle (necesario para la actualizaci�n
    // de la interfaz con la BackStack.
    @Override
    public void onDetalleShown(int position) {
        // Se marca la obra cuyo detalle ha sido mostrado.
        ListaFragment frg = (ListaFragment) mGestorFragmentos
                .findFragmentById(R.id.flLista);
        if (frg != null) {
            frg.marcarObra(position);
        }
    }

    // Cuando cambia la BackStack.
    @Override
    public void onBackStackChanged() {
        // Si la BackStack queda vac�a se pulsa atr�s de nuevo para
        // que se salga de la aplicaci�n.
        int numEntradas = mGestorFragmentos.getBackStackEntryCount();
        if (numEntradas == 0) {
            onBackPressed();
        } else {
            // Si hay m�s de una entrada, se informa sobre la obra que
            // se mostrar� si se pulsa el bot�n Back.
            if (numEntradas > 1) {
                FragmentManager.BackStackEntry entrada = mGestorFragmentos
                        .getBackStackEntryAt(mGestorFragmentos
                                .getBackStackEntryCount() - 2);
                Toast toast = Toast.makeText(this,
                        "Pulsa Back para volver a \n\"" + entrada.getName()
                                + "\"", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
        }
    }
}
