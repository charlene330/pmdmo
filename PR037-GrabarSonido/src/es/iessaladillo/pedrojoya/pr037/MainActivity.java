package es.iessaladillo.pedrojoya.pr037;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnTouchListener,
        OnPreparedListener, OnCompletionListener, OnInfoListener {

    // Constantes.
    private static final int MAX_DURACION_MS = 3000;

    // Vistas.
    private ImageView btnRec;

    // Variables.
    private MediaPlayer reproductor;
    private MediaRecorder grabadora;
    private boolean grabando = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRec = (ImageView) this.findViewById(R.id.btnRec);
        // La propia actividad actuar� de listener cuando se pulse el bot�n.
        btnRec.setOnTouchListener(this);
    }

    // Al pulsar el bot�n.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Si no se estaba grabando, se inicia la grabaci�n.
        if (!grabando) {
            grabar();
        }
        // Si se suelta el bot�n, se finaliza la grabaci�n.
        if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
            pararGrabacion();
        }
        return false;
    }

    // Inicia la grabaci�n.
    private void grabar() {
        // Se prepara la grabaci�n.
        prepararGrabacion();
        // Se inicia la grabaci�n.
        grabadora.start();
        // Se cambia estado de grabaci�n.
        cambiarEstadoGrabacion(true);
    }

    // Prepara la grabaci�n.
    private void prepararGrabacion() {
        // Se crea el objeto grabadora.
        grabadora = new MediaRecorder();
        // Se configura la grabaci�n con fichero de salida, origen, formato,
        // tipo de codificaci�n y duraci�n m�xima.
        String pathGrabacion = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/audio.3gp";
        grabadora.setOutputFile(pathGrabacion);
        grabadora.setAudioSource(MediaRecorder.AudioSource.MIC);
        grabadora.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        grabadora.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        grabadora.setMaxDuration(MAX_DURACION_MS);
        grabadora.setOnInfoListener(this);
        // Se prepara la grabadora (de forma s�ncrona).
        try {
            grabadora.prepare();
        } catch (IOException e) {
            Log.e(getString(R.string.app_name), "Fallo en grabaci�n");
        }
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        // Si se ya llegado al tiempo m�ximo.
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED
                || what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
            // Se cambia el icono del bot�n para que el usuario se de cuenta de
            // que ya no est� grabando.
            btnRec.setImageResource(R.drawable.ic_micro);
        }
    }

    // Detiene la grabaci�n en curso.
    private void pararGrabacion() {
        // Se detiene la grabaci�n y se liberan los recursos de la grabadora.
        if (grabadora != null) {
            grabadora.stop();
            grabadora.release();
            grabadora = null;
        }
        // Se cambia el estado de grabaci�n y el icono del bot�n.
        cambiarEstadoGrabacion(false);
        // Se prepara la reproducci�n.
        prepararReproductor();
    }

    // Cambia el estado de grabaci�n.
    private void cambiarEstadoGrabacion(boolean estaGrabando) {
        grabando = estaGrabando;
        btnRec.setImageResource(estaGrabando ? R.drawable.ic_micro_recording
                : R.drawable.ic_micro);
    }

    // Prepara al reproductor para poder reproducir.
    private void prepararReproductor() {
        // Si ya exist�a reproductor, se elimina.
        if (reproductor != null) {
            reproductor.reset();
            reproductor.release();
            reproductor = null;
        }
        // Se crea el objeto reproductor.
        reproductor = new MediaPlayer();
        try {
            // Path de la grabaci�n.
            reproductor.setDataSource(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/audio.3gp");
            // Stream de audio que utilizar� el reproductor.
            reproductor.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // Volumen
            reproductor.setVolume(1.0f, 1.0f);
            // La actividad actuar� como listener cuando el reproductor ya est�
            // preparado para reproducir y cuando se haya finalizado la
            // reproducci�n.
            reproductor.setOnPreparedListener(this);
            reproductor.setOnCompletionListener(this);
            // Se prepara el reproductor.
            // reproductor.prepare(); // s�ncrona.
            reproductor.prepareAsync(); // as�ncrona.
        } catch (Exception e) {
            Log.d("Reproductor", "ERROR: " + e.getMessage());
        }
    }

    // Cuando el reproductor ya est� preparado para reproducir.
    @Override
    public void onPrepared(MediaPlayer repr) {
        // Se inicia la reproducci�n.
        repr.start();
        // Se desactiva el bot�n de grabaci�n.
        btnRec.setEnabled(false);
    }

    // Cuando ha finalizado la reproducci�n.
    @Override
    public void onCompletion(MediaPlayer arg0) {
        // Se desactiva el bot�n de grabaci�n.
        btnRec.setEnabled(true);
    }

    // Cuando se pausa la actividad.
    @Override
    protected void onPause() {
        super.onPause();
        // Se liberan los recursos del reproductor.
        if (reproductor != null) {
            reproductor.release();
            reproductor = null;
        }
        // Se liberan los recursos de la grabadora.
        if (grabadora != null) {
            grabadora.release();
            grabadora = null;
        }
    }

}