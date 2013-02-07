package es.iessaladillo.pedrojoya.videocapturaintent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {

	// Constantes.
	public static int RC_CAPTURAR_VIDEO = 1; // Request Code Intent.

	// Variables a nivel de clase.
	Button btnCapturar;
	Button btnReproducir;
	VideoView vvReproductor;
	Uri uriVideo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Obtengo la referencia a las vistas y la inicializo.
		initVistas();
		if (savedInstanceState != null) {
			uriVideo = Uri.parse(savedInstanceState.getString("uri"));
			btnReproducir.setEnabled(true);
		}
	}

	// Obtiene la referencia las vistas y las inicializa.
	private void initVistas() {
		btnCapturar = (Button) findViewById(R.id.btnCapturar);
		btnReproducir = (Button) findViewById(R.id.btnReproducir);
		vvReproductor = (VideoView) this.findViewById(R.id.vvReproductor);
		vvReproductor.setMediaController(new MediaController(this));
		// El bot�n de reproducir no debe estar activo hasta que haya un v�deo
		// capturado.
		btnReproducir.setEnabled(false);
	}

	// Al hacer click sobre btnCapturar.
	public void btnCapturarOnClick(View v) {
		// Creo un intent con la acci�n de captura de v�deo.
		Intent i = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		// Env�o el intent a alguna actividad, esperando respuesta.
		startActivityForResult(i, RC_CAPTURAR_VIDEO);
	}

	// Al hacer click sobre btnReproducir.
	public void btnReproducirOnClick(View v) {
		// Establezco la uri del v�deo en el reproductor y comienzo la
		// reproduci�n.
		vvReproductor.setVideoURI(uriVideo);
		vvReproductor.start();
	}

	// Al recibir la respuesta.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Si todo ha ido bien y la respuesta corresponde a la petici�n de
		// captura.
		if (resultCode == RESULT_OK && requestCode == RC_CAPTURAR_VIDEO) {
			// Obtengo de los datos del intent resultado la uri del v�deo.
			uriVideo = data.getData();
			// Activo el bot�n de reproducir porque ya hay v�deo.
			btnReproducir.setEnabled(true);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Almaceno la uri del v�deo (si ya la ten�a).
		if (uriVideo != null) {
			outState.putString("uri", uriVideo.toString());
		}
		super.onSaveInstanceState(outState);
	}

}
