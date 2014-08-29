package es.iessaladillo.pedrojoya.pr050;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public class MainActivity extends Activity implements FotoFragment.Listener,
        InfoFragment.Listener {

    // Constantes.
    private static final String TAG_FOTO_FRAGMENT = "fotoFragment";
    private static final String TAG_INFO_FRAGMENT = "infoFragment";

    // Cuando se crea la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se activa el �tem de overflow en dispositivos con bot�n f�sico de
        // men�.
        overflowEnDispositivoConTeclaMenu();
        // Se carga el fragmento con la foto (s�lo si no est� ya).
        FotoFragment frg = (FotoFragment) getFragmentManager()
                .findFragmentByTag(TAG_FOTO_FRAGMENT);
        if (frg == null) {
            frg = FotoFragment.newInstance(R.drawable.bench);
            getFragmentManager().beginTransaction()
                    .replace(R.id.frmFragmento, frg, TAG_FOTO_FRAGMENT)
                    .commit();
        }
    }

    // Al crearse el men� de opciones.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se infla el men� a partir del XML.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // Se retorna lo que devuelva la actividad.
        return super.onCreateOptionsMenu(menu);
    }

    // Cuando se pulsa un �tem del men� de opciones.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dependiendo del item pulsado se realiza la acci�n deseada.
        switch (item.getItemId()) {
        case R.id.mnuPreferencias:
            mostrarPreferencias();
            break;
        default:
            // Se propaga el evento porque no ha sido resuelto.
            return super.onOptionsItemSelected(item);
        }
        // Retorna que el evento ya ha sido gestionado.
        return true;
    }

    // Muestra la actividad de preferencias.
    private void mostrarPreferencias() {
        Intent intent = new Intent(this, PreferenciasActivity.class);
        startActivity(intent);
    }

    // Cuando se solicita la Info.
    @Override
    public void onInfo(int fotoResId) {
        // Se carga el fragmento Info en la actividad, agreg�ndolo a la
        // BackStack.
        InfoFragment frg = (InfoFragment) getFragmentManager()
                .findFragmentByTag(TAG_INFO_FRAGMENT);
        if (frg == null) {
            frg = new InfoFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.frmFragmento, frg, TAG_INFO_FRAGMENT)
                .addToBackStack(TAG_INFO_FRAGMENT).commit();
    }

    // Cuando se solicita la foto.
    @Override
    public void onFoto(int fotoResId) {
        // Se carga el fragmento Foto en la actividad.
        FotoFragment frg = (FotoFragment) getFragmentManager()
                .findFragmentByTag(TAG_FOTO_FRAGMENT);
        if (frg == null) {
            frg = FotoFragment.newInstance(fotoResId);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.frmFragmento, frg, TAG_FOTO_FRAGMENT).commit();
    }

    // Activa el �tem de overflow en dispositivos con bot�n f�sico de men�.
    private void overflowEnDispositivoConTeclaMenu() {
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

}
