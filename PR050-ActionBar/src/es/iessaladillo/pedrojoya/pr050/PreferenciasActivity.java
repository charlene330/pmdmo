package es.iessaladillo.pedrojoya.pr050;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class PreferenciasActivity extends Activity {

    // Al crear la actividad.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        // Se muestra el icono de navegaci�n junto al icono de la aplicaci�n.
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Al pulsar sobre un �tem del men� se opciones.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dependiendo del �tem seleccionado.
        switch (item.getItemId()) {
        case android.R.id.home: // Icono de navegaci�n.
            // Se muestra la actividad principal de la aplicaci�n.
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        default:
            // Se propaga el evento porque no ha sido resuelto.
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
