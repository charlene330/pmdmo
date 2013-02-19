package es.iessaladillo.pedrojoya.pr043;

import android.app.Activity;
import android.os.Bundle;

// Muestra la pantalla de Acerca de con un bot�n para finalizar la actividad.
public class AcercaDeActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca_de);
	}

	// Al pulsar el bot�n btnSalir.
	public void btnSalirOnClick() {
		finish();
	}
}