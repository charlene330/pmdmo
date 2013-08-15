package es.iessaladillo.pedrojoya.pr054.actividades;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import es.iessaladillo.pedrojoya.pr054.R;
import es.iessaladillo.pedrojoya.pr054.fragmentos.AlumnoFragment;
import es.iessaladillo.pedrojoya.pr054.fragmentos.NotasFragment;

public class MainActivity extends Activity implements
        ActionBar.OnNavigationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se configura para action bar para que tenga un spinner de navegaci�n,
        // NO muestre el t�tulo (para que haya m�s espacio), pero s� muestre el
        // icono de navegaci�n junto al icono de la aplicaci�n.
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        // Se crea el adaptador para el Spinner a partir de un array de
        // constantes de cadena. Se usar� como layout uno similar a
        // android.R.layout.simple_spinner_dropdown_item pero con el texto en
        // blanco.
        SpinnerAdapter adaptador = ArrayAdapter.createFromResource(this,
                R.array.opciones, R.layout.dark_actionbar_spinner);
        // Se establece el adaptador y el listener para el spinner (que ser� la
        // propia actividad).
        ab.setListNavigationCallbacks(adaptador, this);
        // Si venimos de un estado anterior.
        if (savedInstanceState != null) {
            // Se coloca en la opci�n en la que estaba.
            ab.setSelectedNavigationItem(savedInstanceState.getInt("opcion"));
        }
    }

    // Al seleccionar un elemento del spinner de la action bar.
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Se crea el fragmento correspondiente al elemento seleccionado.
        Fragment frg = null;
        switch (itemPosition) {
        case 0:
            frg = new AlumnoFragment();
            break;
        case 1:
            frg = new NotasFragment();
            break;
        }
        // Se muestra el fragmento en el contenedor del layout de la actividad.
        FragmentTransaction transaccion = getFragmentManager()
                .beginTransaction();
        transaccion.replace(android.R.id.content, frg);
        transaccion.commit();
        // Se retorna que ya ha sido procesada la selecci�n.
        return true;
    }

    // Al crear el men�.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se infla el men� a partir del XML.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Cuando se pulsa un elemento del men�.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dependiendo del item pulsado se realiza la acci�n deseada.
        switch (item.getItemId()) {
        case android.R.id.home:
            Toast.makeText(getApplicationContext(),
                    R.string.ir_a_la_actividad_superior, Toast.LENGTH_SHORT)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Almacena qu� pesta�a tenemos seleccionada.
        outState.putInt("opcion", getActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

}
