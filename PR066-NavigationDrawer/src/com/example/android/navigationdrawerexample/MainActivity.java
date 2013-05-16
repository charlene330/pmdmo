/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawerexample;

import java.util.Locale;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private DrawerLayout menuLateral;
    private ListView lstMenuLateral;
    private ActionBarDrawerToggle activadorMenuLateral;

    private CharSequence tituloMenuLateral;
    private CharSequence tituloActividad;
    private String[] nombresPlanetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialmente el t�tulo del men� lateral coincide con el de la
        // actividad.
        tituloActividad = tituloMenuLateral = getTitle();
        // Se obtiene el array con los nombres de los planetas.
        nombresPlanetas = getResources().getStringArray(R.array.planets_array);

        // Se establecemos el drawable que act�a como sombra del contenido
        // principal cuando se abre el men� lateral.
        menuLateral = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuLateral.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Se carga la lista del men� lateral y se indica el objeto listener que
        // ser� notificado cuando se pulse sobre alguno de sus elementos.
        lstMenuLateral = (ListView) findViewById(R.id.left_drawer);
        lstMenuLateral.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, nombresPlanetas));
        lstMenuLateral
                .setOnItemClickListener(new MenuLateralItemClickListener());

        // Se crea el objeto de v�nculo entre la ActionBar y el men� lateral. El
        // constructor recibe la actividad, el men� lateral, el icono de
        // activaci�n en la ActionBar, el texto de apertura y el de cierre. El
        // objeto debe implementar ciertos m�todos de la interfaz
        // DrawerLayout.DrawerListener.
        activadorMenuLateral = new ActionBarDrawerToggle(this, menuLateral,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            // Al terminar de cerrarse el men� lateral.
            public void onDrawerClosed(View view) {
                // Se reestablece el t�tulo de la ActionBar al valor que tuviera
                // antes de abrir el men� lateral.
                getActionBar().setTitle(tituloActividad);
                // Se recarga el men� de la ActionBar.
                invalidateOptionsMenu();
            }

            // Al terminar de abrirse el men� lateral.
            public void onDrawerOpened(View drawerView) {
                // Se establece como t�tulo de la ActionBar el nombre de la
                // actividad.
                getActionBar().setTitle(tituloMenuLateral);
                // Se recarga el men� de la ActionBar.
                invalidateOptionsMenu();
            }
        };

        // Se enlaza la ActionBar con el men� lateral.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        menuLateral.setDrawerListener(activadorMenuLateral);

        // Se selecciona el primer elemento de la lista del men� lateral para
        // que el contenedor de la actividad principal no se muestre vac�o
        // inicialmente.
        if (savedInstanceState == null) {
            seleccionadoElementoMenuLateral(0);
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
        // Si el men� lateral est� abierto, se ocultan los �tems de men� de la
        // ActionBar relacionados con contenido (ya que �ste est� oculto tras el
        // men� lateral).
        boolean abierto = menuLateral.isDrawerOpen(lstMenuLateral);
        menu.findItem(R.id.action_websearch).setVisible(!abierto);
        return super.onPrepareOptionsMenu(menu);
    }

    // Cuando se selecciona un �tem de men� en la ActionBar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Si se ha pulsado sobre icono de activaci�n del men� lateral no
        // se debe hacer nada m�s, ya que el activador ya se encarga por
        // nosotros de
        // abrir o cerrar el men� lateral.
        if (activadorMenuLateral.onOptionsItemSelected(item)) {
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

    // Clase Listener para cuando se pulsa sobre un elemento del men� lateral
    private class MenuLateralItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // Se selecciona el elemento correspondiente.
            seleccionadoElementoMenuLateral(position);
        }
    }

    // Respuesta a la selecci�n de un elemento en el men� lateral.
    private void seleccionadoElementoMenuLateral(int position) {
        // Se carga en la actividad principal el fragmento correspondiente.
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        // Se marca como seleccionado dicho elemento en la lista.
        lstMenuLateral.setItemChecked(position, true);
        // Se actualiza el t�tulo que debe mostrar la ActionBar, acorde al
        // fragmento que se ha cargado.
        tituloActividad = nombresPlanetas[position];
        getActionBar().setTitle(tituloActividad);
        // Se cierra el men� lateral.
        menuLateral.closeDrawer(lstMenuLateral);
    }

    // Si se usa un objeto activador del men� lateral, se debe sincronizar con
    // la ActionBar una vez creada la actividad.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        activadorMenuLateral.syncState();
    }

    // Si se usa un objeto activador del men� lateral, se debe sincronizar con
    // la ActionBar cuando se cambie la configuraci�n.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        activadorMenuLateral.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet,
                    container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources()
                    .getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(
                    planet.toLowerCase(Locale.getDefault()), "drawable",
                    getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image))
                    .setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }
}