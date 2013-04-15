package es.iesaladillo.pedrojoya.pr053.actividades;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.iesaladillo.pedrojoya.pr053.R;
import es.iesaladillo.pedrojoya.pr053.fragmentos.AlumnoFragment;
import es.iesaladillo.pedrojoya.pr053.fragmentos.NotasFragment;

public class MainActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Se configura para action bar para que tenga pesta�as de navegaci�n,
		// NO muestre el t�tulo (para que haya m�s espacio), pero s� muestre el
		// icono de navegaci�n junto al icono de la aplicaci�n.
		ActionBar ab = getSupportActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayHomeAsUpEnabled(true);
		// Se crean las pesta�as.
		ActionBar.Tab tabAlumno = ab.newTab();
		tabAlumno.setText(R.string.alumno);
		ActionBar.Tab tabNotas = ab.newTab();
		tabNotas.setText(R.string.notas);
		// Se crean los fragmentos de cada pesta�a.
		Fragment frgAlumno = new AlumnoFragment();
		Fragment frgNotas = new NotasFragment();
		// Se crea y asocia el listener a cada pesta�a.
		tabAlumno.setTabListener(new GestorTabListener(frgAlumno));
		tabNotas.setTabListener(new GestorTabListener(frgNotas));
		// Se a�aden las pesta�as a la action bar
		ab.addTab(tabAlumno);
		ab.addTab(tabNotas);
		// Si venimos de un estado anterior.
		if (savedInstanceState != null) {
			// Se coloca en la pesta�a en la que estaba.
			ab.setSelectedNavigationItem(savedInstanceState.getInt("tab"));
		}
	}

	// Al crear la primera vez el men�.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Se infla el men� a partir del XML.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Almacena qu� pesta�a tenemos seleccionada.
		outState.putInt("tab", getSupportActionBar()
				.getSelectedNavigationIndex());
		super.onSaveInstanceState(outState);
	}

	// Muestra una tostada.
	private void mostrarTostada(String mensaje) {
		Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT)
				.show();
	}

	// Gestor de pesta�as.
	public class GestorTabListener implements ActionBar.TabListener {

		// Fragmento correspondiente de la pesta�a.
		private Fragment fragment;

		// Constructor.
		public GestorTabListener(Fragment fg) {
			this.fragment = fg;
		}

		// Al volver a seleccionar una pesta�a.
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// Normalmente no se hace nada.
		}

		// Al convertir en activa una pesta�a.
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Inserto el fragmento correspondiente en el contenedor (en este
			// caso el ra�z del layout de la actividad), reemplazando el que
			// tuviera.
			ft.replace(android.R.id.content, fragment);
		}

		// Al dejar de estar activa una pesta�a.
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// Quito el fragmento del contenedor.
			ft.remove(fragment);
		}
	}

}
