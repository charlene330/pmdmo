package es.iessaladillo.pedrojoya.pr052.actividades;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ViewConfiguration;
import es.iessaladillo.pedrojoya.pr052.R;
import es.iessaladillo.pedrojoya.pr052.fragmentos.FotoFragment;
import es.iessaladillo.pedrojoya.pr052.fragmentos.InfoFragment;

public class MainActivity extends Activity implements TabListener {

    // Constantes.
    private static final int POS_TAB_FOTO = 0;
    private static final int POS_TAB_INFO = 1;
    private static final String TAG_FRG_FOTO = "fotoFragment";
    private static final String TAG_FRG_INFO = "infoFragment";
    private static final String STATE_TAB = "tab";

    // Variables.
    private FotoFragment frgFoto;
    private InfoFragment frgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se activa el �tem de overflow en dispositivos con bot�n f�sico de
        // men�.
        overflowEnDispositivoConTeclaMenu();
        // Se configura para action bar para que tenga pesta�as de navegaci�n.
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Se crean las pesta�as (la actividad actuar� como listener) y se
        // a�aden a la action bar (el orden es importante).
        ActionBar.Tab tabFoto = ab.newTab();
        tabFoto.setText(R.string.foto);
        tabFoto.setTabListener(this);
        ActionBar.Tab tabInfo = ab.newTab();
        tabInfo.setText(R.string.info);
        tabInfo.setTabListener(this);
        ab.addTab(tabFoto);
        ab.addTab(tabInfo);
        // Si venimos de un estado anterior.
        if (savedInstanceState != null) {
            // Se coloca en la pesta�a en la que estaba.
            ab.setSelectedNavigationItem(savedInstanceState.getInt(STATE_TAB));
        }
    }

    // Cuando se produce un cambio de configuraci�n.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Almacena qu� pesta�a tenemos seleccionada.
        outState.putInt(STATE_TAB, getActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(outState);
    }

    // Cuando se reselecciona una pesta�a.
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // No se hace nada.
    }

    // Cuando se selecciona una pesta�a
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // Se enlaza el fragmento correspondiente. Si no exist�a se crea y
        // a�ade.
        switch (tab.getPosition()) {
        case POS_TAB_FOTO:
            frgFoto = (FotoFragment) getFragmentManager().findFragmentByTag(
                    TAG_FRG_FOTO);
            if (frgFoto == null) {
                frgFoto = FotoFragment.newInstance(R.drawable.bench);
                ft.add(android.R.id.content, frgFoto, TAG_FRG_FOTO);
            } else {
                ft.attach(frgFoto);
            }
            break;
        case POS_TAB_INFO:
            frgInfo = (InfoFragment) getFragmentManager().findFragmentByTag(
                    TAG_FRG_INFO);
            if (frgInfo == null) {
                frgInfo = new InfoFragment();
                ft.add(android.R.id.content, frgInfo, TAG_FRG_INFO);
            } else {
                ft.attach(frgInfo);
            }
            break;
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // Se desenlaza el fragmento correspondiente.
        switch (tab.getPosition()) {
        case POS_TAB_FOTO:
            if (frgFoto != null) {
                ft.detach(frgFoto);
            }
            break;
        case POS_TAB_INFO:
            if (frgInfo != null) {
                ft.detach(frgInfo);
            }
            break;
        }

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
