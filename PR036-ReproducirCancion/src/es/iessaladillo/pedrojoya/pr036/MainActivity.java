package es.iessaladillo.pedrojoya.pr036;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnPreparedListener,
		OnCompletionListener {

	private static final int RC_SELECCIONAR_CANCION = 0;
	// Variables a nivel de clase.
	private SeekBar skbBarra;
	private MediaPlayer reproductor;
	private boolean enPausa = false;
	private Handler manejador = new Handler();
	private Runnable notificacion;
	private Uri uriCancion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Llamo al onCreate del padre.
		super.onCreate(savedInstanceState);
		// Establezco el layout de la actividad.
		setContentView(R.layout.activity_main);
		// Obtengo la referencia a las vistas.
		getVistas();
		// Cuando se desplaza la seekbar.
		skbBarra.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Si ha sido el usuario el que ha cambiado la barra.
				if (fromUser && reproductor.isPlaying()) {
					// Coloco el reproductor en esa posici�n.
					reproductor.seekTo(seekBar.getProgress());
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

		});
	}

	// Obtiene las referencias a las vistas del layout.
	private void getVistas() {
		skbBarra = (SeekBar) this.findViewById(R.id.skbBarra);
	}

	// Al hacer click en btnSeleccionar.
	public void btnSeleccionarOnClick(View v) {
		// Lanzo un intent para seleccionar una canci�n.
		Intent i = new Intent(android.content.Intent.ACTION_GET_CONTENT);
		i.setType("audio/mp3");
		startActivityForResult(i, RC_SELECCIONAR_CANCION);
	}

	// Al retornar de la selecci�n de la canci�n
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Si todo ha ido bien.
		if (resultCode == RESULT_OK && requestCode == RC_SELECCIONAR_CANCION) {
			// Obtengo la uri de la canci�n.
			uriCancion = data.getData();
		}
	}

	// Al hacer click en el bot�n btnPlay.
	public void btnPlayOnClick(View v) {
		// Si es una nueva reproducci�n, la inicio.
		prepararReproductor();
	}

	// Al hacer click en el bot�n btnPause.
	public void btnPauseOnClick(View v) {
		if (reproductor != null) {
			if (!enPausa) {
				// Si no estaba en modo pausa, pauso el reproductor.
				reproductor.pause();
				enPausa = true;
			} else {
				// Si ya estaba en modo pausa continuo la reproducci�n.
				reproductor.start();
				actualizarProgreso();
				// Dejo de estar en modo pausa.
				enPausa = false;
			}
		}

	}

	// Al hacer click en el bot�n btnStop.
	public void btnStopOnClick(View v) {
		if (reproductor != null) {
			// Paro la reproducci�n.
			reproductor.stop();
			// Dejo de estar en modo pausa.
			enPausa = false;
			// Coloco la barra al principio y elimino los callbacks.
			skbBarra.setProgress(0);
			manejador.removeCallbacks(notificacion);
		}
	}

	// Prepara al reproductor para poder reproducir.
	private void prepararReproductor() {
		// Si tengo canci�n para reproducir.
		if (uriCancion != null) {
			// Si ya ten�a reproductor, lo elimino.
			if (reproductor != null) {
				reproductor.reset();
				reproductor.release();
				reproductor = null;
			}
			// Creo el objeto MediaPlayer.
			reproductor = new MediaPlayer();
			try {
				// Indico al reproductor la uri de la canci�n.
				reproductor.setDataSource(this, uriCancion);
				// Establezco el stream de audio que utilizar� el reproductor.
				reproductor.setAudioStreamType(AudioManager.STREAM_MUSIC);
				// Cuando ya est� preparado el reproductor se generar� un evento
				// OnPrepared que deber� ser gestionado por un Listener. Indico
				// que ser� la propia actividad quien implementar� la interfaz
				// del Listener y gestionar� el evento.
				reproductor.setOnPreparedListener(this);
				reproductor.setOnCompletionListener(this);
				// Realizo la inicializaci�n (preparaci�n) del reproductor.
				// reproductor.prepare(); // s�ncrona.
				reproductor.prepareAsync(); // as�ncrona.
			} catch (Exception e) {
				Log.d("Reproductor", "ERROR: " + e.getMessage());
			}
		}
	}

	// Cuando el reproductor ya est� preparado para reproducir.
	@Override
	public void onPrepared(MediaPlayer repr) {
		// Establezco el m�ximo de la seekbar.
		skbBarra.setMax(reproductor.getDuration());
		skbBarra.setProgress(0);
		// No estoy en modo pausa.
		enPausa = false;
		// Comienzo la reproducci�n.
		repr.start();
		// Inicio el hilo de notificaci�n para actualizar la barra.
		actualizarProgreso();
	}

	// Actualiza la barra en base al progreso del contenido del mediaplayer.
	private void actualizarProgreso() {
		// Actualizo la posici�n en la barra.
		skbBarra.setProgress(reproductor.getCurrentPosition());
		// Si est� reproduci�ndose.
		if (reproductor.isPlaying()) {
			// Creo un hilo de notificaci�n para que se ejecute de nuevo.
			notificacion = new Runnable() {
				public void run() {
					actualizarProgreso();
				}
			};
			// Hago que el manejador ejecute el hilo de notificaci�n tr�s 500 ms.
			manejador.postDelayed(notificacion, 500);
		}
		// Si ya se ha terminado de reproducir.
		else {
			// Si no estoy en pausa pongo la barra al principio.
			if (!enPausa) {
				skbBarra.setProgress(0);
			}
		}
	}

	// Cuando ha terminado de reproducirse la canci�n.
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// Reinicio la barra.
		skbBarra.setProgress(0);
		// Cancelo los mensajes al hilo de notificaci�n.
		manejador.removeCallbacks(notificacion);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Libero los recursos del reproductor y el propio objeto.
		if (reproductor != null) {
			reproductor.release();
			reproductor = null;
		}
		// Dejo de enviar mensajes al hilo de notificaci�n.
		manejador.removeCallbacks(notificacion);
	}

	@Override
	protected void onResume() {
		super.onResume();
		skbBarra.setProgress(0);
	}

}