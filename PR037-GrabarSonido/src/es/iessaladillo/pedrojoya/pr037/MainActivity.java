package es.iessaladillo.pedrojoya.pr037;

import java.io.IOException;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnPreparedListener,
		OnCompletionListener {

	// Variables a nivel de clase.
	private SeekBar skbBarra;
	private MediaPlayer reproductor;
	private boolean enPausa = false;
	private Handler manejador = new Handler();
	private Runnable notificacion;
	private MediaRecorder grabadora;
	private ImageButton btnRec;
	private boolean grabando = false;
	private String pathGrabacion = "";
	private ImageButton btnPlay;
	private ImageButton btnPause;
	private ImageButton btnStop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Llamo al onCreate del padre.
		super.onCreate(savedInstanceState);
		// Establezco el layout de la actividad.
		setContentView(R.layout.activity_main);
		// Obtengo la referencia a las vistas.
		getVistas();
		// Deshabilito el Stop y Pause.
		btnStop.setEnabled(false);
		btnPause.setEnabled(false);
		// Habilito o deshabilito el Play.
		if (savedInstanceState != null) {
			btnPlay.setEnabled(savedInstanceState.getBoolean("btnPlayEnabled"));
		} else {
			btnPlay.setEnabled(false);
		}		
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
		btnRec = (ImageButton) this.findViewById(R.id.btnRec);
		btnPlay = (ImageButton) this.findViewById(R.id.btnPlay);
		btnPause = (ImageButton) this.findViewById(R.id.btnPause);
		btnStop = (ImageButton) this.findViewById(R.id.btnStop);
	}

	// Al hacer click en btnRec.
	public void btnRecOnClick(View v) {
		// Si no estoy grabando empiezo y si no termino.
		if (!grabando) {
			grabar();
		} else {
			pararGrabacion();
		}
	}

	// Inicia la grabaci�n.
	private void grabar() {
		// Creo el objeto grabadora.
		grabadora = new MediaRecorder();
		// Configuro la grabaci�n con fichero de salida, origen, formato y
		// tipo de codificaci�n.
		pathGrabacion = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/audio.3gp";
		grabadora.setOutputFile(pathGrabacion);
		grabadora.setAudioSource(MediaRecorder.AudioSource.MIC);
		grabadora.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		grabadora.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// Preparo la grabadora (s�ncrona).
		try {
			grabadora.prepare();
		} catch (IOException e) {
			Log.e(getString(R.string.app_name), "Fallo en grabaci�n");
		}
		// Inicio la grabaci�n.
		grabadora.start();
		// Cambio el icono del bot�n y el estado de grabaci�n.
		btnRec.setImageResource(R.drawable.stoprec);
		grabando = true;
		// Deshabilito los botones de reproducci�n.
		btnPlay.setEnabled(false);
		btnStop.setEnabled(false);
		btnPause.setEnabled(false);
	}

	// Para la grabaci�n en curso.
	private void pararGrabacion() {
		// Paro la grabaci�n y libero el objeto grabadora.
		grabadora.stop();
		grabadora.release();
		grabadora = null;
		// Cambio el estado de grabaci�n y el icono del bot�n.
		grabando = false;
		btnRec.setImageResource(R.drawable.rec);
		// Habilito el bot�n de play.
		btnPlay.setEnabled(true);
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
			// Deshabilito los botones de Pause y de Parar.
			btnPause.setEnabled(false);
			btnStop.setEnabled(false);
			// Habilito el bot�n de grabar.
			btnRec.setEnabled(true);
		}
	}

	// Prepara al reproductor para poder reproducir.
	private void prepararReproductor() {
		// Si ya ten�a reproductor, lo elimino.
		if (reproductor != null) {
			reproductor.reset();
			reproductor.release();
			reproductor = null;
		}
		// Creo el objeto MediaPlayer.
		reproductor = new MediaPlayer();
		try {
			// Indico al reproductor el path de la grabaci�n.
			reproductor.setDataSource(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/audio.3gp");
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
		// Activo el bot�n de Pause y de Stop.
		btnPause.setEnabled(true);
		btnStop.setEnabled(true);
		// Desactivo el bot�n de grabaci�n.
		btnRec.setEnabled(false);
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
			// Hago que el manejador ejecute el hilo de notificaci�n dentro
			// de 500ms.
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
		// Deshabilito los botones de Pause y de Parar.
		btnPause.setEnabled(false);
		btnStop.setEnabled(false);
		// Habilito el bot�n de grabar.
		btnRec.setEnabled(true);
	}

	// Cuando se salva el estado en recreaci�n.
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Guardo el estado de enabled del Play.
		outState.putBoolean("btnPlayEnabled", btnPlay.isEnabled());
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
		// Libero los recursos de la grabadora.
		if (grabadora != null) {
			grabadora.release();
			grabadora = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		skbBarra.setProgress(0);
	}

}