package es.iessaladillo.pedrojoya.pr050;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Cuando se crea la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Se llama al constructor del padre.
        super.onCreate(savedInstanceState);
        // Se establece el layout que mostrar� la actividad.
        setContentView(R.layout.activity_main);
        // Se muestra el icono de navegaci�n junto al icono de la aplicaci�n.
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Se activa el �tem de overflow en dispositivos con bot�n f�sico de
        // men�.
        activarOverflow();
    }

    // Activa el �tem de overflow en dispositivos con bot�n f�sico de men�.
    private void activarOverflow() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignorar
        }
    }

    // Al crear la primera vez el men�.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se infla el men� a partir del XML.
        getMenuInflater().inflate(R.menu.activity_main, menu);
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
            case R.id.mnuAgregar:
                mostrarTostada(getString(R.string.agregar));
                break;
            case R.id.mnuCargar:
                mostrarTostada(getString(R.string.cargar));
                break;
            case R.id.mnuEditar:
                mostrarTostada(item.getTitle().toString());
                break;
            case R.id.mnuEliminar:
                mostrarTostada(getString(R.string.eliminar));
                break;
            case R.id.mnuBuscar:
                mostrarTostada(getString(R.string.buscar));
                break;
            case R.id.mnuCompartir:
                mostrarTostada(getString(R.string.compartir));
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
