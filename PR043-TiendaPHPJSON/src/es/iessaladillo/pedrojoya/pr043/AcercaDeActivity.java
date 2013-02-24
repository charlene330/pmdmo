package es.iessaladillo.pedrojoya.pr043;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

// Muestra la pantalla de Acerca de con un bot�n para finalizar la actividad.
public class AcercaDeActivity extends Activity {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca_de);
		// Activo el bot�n de Home en la Action Bar (si la versi�n lo permite).
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	// Al seleccionar una opci�n del men� de opciones.
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Dependiendo de la opci�n de men� seleccionada.
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}