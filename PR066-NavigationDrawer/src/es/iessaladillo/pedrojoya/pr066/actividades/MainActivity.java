package es.iessaladillo.pedrojoya.pr066.actividades;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import es.iessaladillo.pedrojoya.pr066.R;
import es.iessaladillo.pedrojoya.pr066.adaptadores.AlbumesAdapter;
import es.iessaladillo.pedrojoya.pr066.fragmentos.PlanetaFragment;
import es.iessaladillo.pedrojoya.pr066.modelos.Album;

public class MainActivity extends Activity {

    // Variables miembro.
    private DrawerLayout panelNavegacion;
    private ListView lstPanelNavegacion;
    private ActionBarDrawerToggle conmutadorPanelNavegacion;
    private CharSequence tituloPanelNavegacion;
    private CharSequence tituloActividad;
    private AlbumesAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicialmente el t�tulo del panel de navegaci�n coincide con el de la
        // actividad.
        tituloActividad = tituloPanelNavegacion = getTitle();
        // Se establecemos el drawable que act�a como sombra del contenido
        // principal cuando se abre el panel de navegaci�n.
        panelNavegacion = (DrawerLayout) findViewById(R.id.drawer_layout);
        panelNavegacion.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // Se carga la lista del panel de navegaci�n y se indica el objeto
        // listener que ser� notificado cuando se pulse sobre alguno de sus
        // elementos.
        lstPanelNavegacion = (ListView) findViewById(R.id.left_drawer);
        adaptador = new AlbumesAdapter(this, getDatos());
        lstPanelNavegacion.setAdapter(adaptador);
        lstPanelNavegacion
                .setOnItemClickListener(new PanelNavegacionItemClickListener());
        // Se crea el objeto de v�nculo entre la ActionBar y el panel de
        // navegaci�n. El constructor recibe la actividad, el panel de
        // navegaci�n, el icono de activaci�n en la ActionBar, el texto de
        // apertura y el de cierre. El objeto debe implementar ciertos m�todos
        // de la interfaz DrawerLayout.DrawerListener.
        conmutadorPanelNavegacion = new ActionBarDrawerToggle(this,
                panelNavegacion, R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            // Al terminar de cerrarse el panel de navegaci�n.
            public void onDrawerClosed(View view) {
                // Se reestablece el t�tulo de la ActionBar al valor que tuviera
                // antes de abrir el panel de navegaci�n.
                getActionBar().setTitle(tituloActividad);
                // Se recarga el men� de la ActionBar.
                invalidateOptionsMenu();
            }

            // Al terminar de abrirse el panel de navegaci�n.
            public void onDrawerOpened(View drawerView) {
                // Se establece como t�tulo de la ActionBar el nombre de la
                // actividad.
                getActionBar().setTitle(tituloPanelNavegacion);
                // Se recarga el men� de la ActionBar.
                invalidateOptionsMenu();
            }
        };
        // Se establece que la ActionBar muestre un icono para el conmutador.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // Se vincula el conmutador con el panel de navegaci�n.
        panelNavegacion.setDrawerListener(conmutadorPanelNavegacion);
        // Se selecciona el primer elemento de la lista del panel de navegaci�n
        // para que el contenedor de la actividad principal no se muestre vac�o
        // inicialmente.
        if (savedInstanceState == null) {
            panelNavegacionItemSelected(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Llamado autom�ticamente tras cada llamada a invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Si el panel de navegaci�n est� abierto, se ocultan los �tems de men�
        // de la
        // ActionBar relacionados con contenido (ya que �ste est� oculto tras el
        // panel de navegaci�n).
        boolean abierto = panelNavegacion.isDrawerOpen(lstPanelNavegacion);
        menu.findItem(R.id.action_websearch).setVisible(!abierto);
        return super.onPrepareOptionsMenu(menu);
    }

    // Cuando se selecciona un �tem de men� en la ActionBar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Si se ha pulsado sobre icono de activaci�n del panel de navegaci�n no
        // se debe hacer nada m�s, ya que el activador ya se encarga por
        // nosotros de
        // abrir o cerrar el panel de navegaci�n.
        if (conmutadorPanelNavegacion.onOptionsItemSelected(item)) {
            return true;
        }
        // En cualquier otro caso se procesa la selecci�n.
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available,
                            Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Clase Listener para cuando se pulsa sobre un elemento del panel de
    // navegaci�n
    private class PanelNavegacionItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // Se selecciona el elemento correspondiente.
            panelNavegacionItemSelected(position);
        }
    }

    // Respuesta a la selecci�n de un elemento en el panel de navegaci�n.
    private void panelNavegacionItemSelected(int position) {
        // Se carga en la actividad principal el fragmento correspondiente.
        Fragment fragment = new PlanetaFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetaFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        // Se marca como seleccionado dicho elemento en la lista.
        lstPanelNavegacion.setItemChecked(position, true);
        // Se actualiza el t�tulo que debe mostrar la ActionBar, acorde al
        // fragmento que se ha cargado.
        tituloActividad = nombresPlanetas[position];
        getActionBar().setTitle(tituloActividad);
        // Se cierra el panel de navegaci�n.
        panelNavegacion.closeDrawer(lstPanelNavegacion);
    }

    // Si se usa un objeto activador del panel de navegaci�n, se debe
    // sincronizar con la ActionBar una vez creada la actividad.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        conmutadorPanelNavegacion.syncState();
    }

    // Si se usa un objeto activador del panel de navegaci�n, se le debe
    // informar de la
    // nueva configuraci�n.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        conmutadorPanelNavegacion.onConfigurationChanged(newConfig);
    }

    // Creo los datos para la lista.
    private ArrayList<Album> getDatos() {
        ArrayList<Album> albumes = new ArrayList<Album>();
        albumes.add(new Album(R.drawable.veneno, "Veneno", "1977"));
        albumes.add(new Album(R.drawable.mecanico, "Ser� mec�nico por ti",
                "1981"));
        albumes.add(new Album(R.drawable.cantecito, "Echate un cantecito",
                "1992"));
        albumes.add(new Album(R.drawable.carinio,
                "Est� muy bien eso del cari�o", "1995"));
        albumes.add(new Album(R.drawable.paloma, "Punta Paloma", "1997"));
        albumes.add(new Album(R.drawable.puro, "Puro Veneno", "1998"));
        albumes.add(new Album(R.drawable.pollo, "La familia pollo", "2000"));
        albumes.add(new Album(R.drawable.ratito, "Un ratito de gloria", "2001"));
        albumes.add(new Album(R.drawable.hombre, "El hombre invisible", "2005"));
        return albumes;
    }

}