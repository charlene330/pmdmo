package es.iessaladillo.pedrojoya.pr074;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private SearchView svBuscar;

    // Cuando se crea la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Se llama al constructor del padre.
        super.onCreate(savedInstanceState);
        // Se establece el layout que mostrar� la actividad.
        setContentView(R.layout.activity_main);
        // Se muestra el icono de navegaci�n junto al icono de la aplicaci�n.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // Al crear la primera vez el men�.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se infla el men� a partir del XML.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // Se obtiene la referencia a la SearchView.
        MenuItem item = menu.findItem(R.id.mnuBuscar);
        svBuscar = (SearchView) MenuItemCompat.getActionView(item);
        // Cuando de realice la b�squeda.
        svBuscar.setOnQueryTextListener(new OnQueryTextListener() {

            // Cuando se env�a el t�rmino a la b�squeda.
            @Override
            public boolean onQueryTextSubmit(String query) {
                mostrarTostada(getString(R.string.buscar) + query);
                return true;
            }

            // Cuando cambia el texto.
            @Override
            public boolean onQueryTextChange(String query) {
                // No se hace nada.
                return false;
            }
        });
        // Se retorna lo que devuelva la actividad.
        return super.onCreateOptionsMenu(menu);
    }

    // Cuando se pulsa un elemento del men�.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dependiendo del item pulsado se realiza la acci�n deseada.
        switch (item.getItemId()) {
        case android.R.id.home:
            mostrarTostada(getString(R.string.ir_a_la_actividad_superior));
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        // Retorna que ya ha sido gestionado.
        return true;
    }

    // Muestra una tostada.
    private void mostrarTostada(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT)
                .show();
    }

}
