package es.iessaladillo.pedrojoya.mediaplayercontroller;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.MediaController;

public class MainActivity extends Activity implements OnPreparedListener, OnCompletionListener, MediaController.MediaPlayerControl {

	// Variables a nivel de clase.
    private SurfaceView swVisor;
	private SurfaceHolder ventanaReproduccion;
	private EditText txtPath;
	private MediaPlayer reproductor;
	private MediaController controles; 
	private boolean enPausa = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	// Llamo al onCreate del padre.
        super.onCreate(savedInstanceState);
        // Establezco el layout de la actividad.
        setContentView(R.layout.main);
        // Obtengo la referencia a las vistas.
        getVistas();
    }
    
    // Obtiene las referencias a las vistas del layout.
    private void getVistas() {
    	txtPath = (EditText) findViewById(R.id.txtPath);
    	txtPath.setText("sdcard/video.3gp");
    	swVisor = (SurfaceView) this.findViewById(R.id.swVisor);
		// Obtengo la ventana de reproducci�n.
		ventanaReproduccion = swVisor.getHolder();
		/* Para v�deo y c�mara es necesario que sea del tipo PUSH_BUFFERS.
		 * TIENE QUE PONERSE DESPU�S DL ADDCALLBACK.
		 */
    	ventanaReproduccion.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
			}
			else {
				/* Si ya estaba en modo pausa continuo la reproducci�n 
				 * (dejando el modo pausa).
				 */
				reproductor.start();
				enPausa = false;
			}
		}
		
	}

	// Al hacer click en el bot�n btnStop.
	public void btnStopOnClick(View v) {
		if (reproductor != null) {
			// Paro la reproducci�n y en cualquier caso dejo el modo Pausa.
			reproductor.stop();
			enPausa = false;
		}
	}

	// Prepara al reproductor para poder reproducir.
	private void prepararReproductor() {
    	// Obtengo la ruta.
    	String ruta = txtPath.getText().toString();
    	if (!("".equals(ruta))) {
			// Creo el objeto MediaPlayer.
	    	if (reproductor != null) {
	    		reproductor.reset();
	    		reproductor.release();
	    		reproductor = null;
	    	}
	    	reproductor = new MediaPlayer();
	    	try {
	        	// Establezco la ruta del fichero a reproducir en el reproductor.
				reproductor.setDataSource(ruta);
				// Establezco el stream de audio que utilizar� el reproductor. 
				reproductor.setAudioStreamType(AudioManager.STREAM_MUSIC);
				/* Cuando ya est� preparado el reproductor se generar� un evento OnPrepared
				 * que deber� ser gestionado por un Listener.
				 * Indico que ser� la propia actividad quien implementar� la interfaz
				 * del Listener y gestionar� el evento.
				 */
				reproductor.setOnPreparedListener(this);
				reproductor.setOnCompletionListener(this);
				// Realizo la inicializaci�n (preparaci�n) del reproductor.
				//reproductor.prepare();	// s�ncrona.
				reproductor.prepareAsync(); // as�ncrona.
				// Establezco la ventana de reproducci�n que utilizar� el reproductor.
				reproductor.setDisplay(ventanaReproduccion);
			} catch (Exception e) {
				Log.d("Reproductor", "ERROR: " + e.getMessage());
			}
    	}
	}

    // Cuando el reproductor ya est� preparado para reproducir.
	@Override
	public void onPrepared(MediaPlayer repr) {
		// Obtengo la anchura y altura del v�deo y de la pantalla.
		int anchuraVideo = repr.getVideoWidth();
		int alturaVideo = repr.getVideoHeight();
		int anchuraVisor = swVisor.getWidth();
		int alturaVisor = swVisor.getHeight();
		// Calculo las proporciones
		float proporcionAnchura = (float) anchuraVisor / (float) anchuraVideo; 
		float proporcionAltura = (float) alturaVisor / (float) alturaVideo;
		float proporcionAspecto = (float)anchuraVideo / (float)alturaVideo;
		// Obtenemos los par�metros de anchura y altura de la ventana de reproducci�n.
		LayoutParams lp = swVisor.getLayoutParams();
		/* Si la proporci�n de ancho es m�s grande que la proporci�n de alto es 
		 * porque el visor es m�s ancho que alto y por tanto pondremos el alto 
		 * de la ventana al alto del visor y el ancho lo calculamos en base a esta altura, 
		 * aplic�ndole la proporci�n de aspecto.
		 */
		if (proporcionAnchura > proporcionAltura) {
			lp.height = alturaVisor;
			lp.width = (int) (alturaVisor * proporcionAspecto);
		}
		else {
			lp.width = anchuraVisor;
			lp.height = (int) (anchuraVisor / proporcionAspecto);
		}
		// Le asigno los par�metros al visor.
		swVisor.setLayoutParams(lp);	
		// No estoy en modo pausa.
		enPausa = false;
		// Comienzo la reproducci�n.
		repr.start();
    	// Creo la barra de controles y la engancho al visor.
    	controles = new MediaController(this);
    	controles.setMediaPlayer(this);
    	controles.setAnchorView(this.findViewById(R.id.llRaiz));
    	controles.setEnabled(true);
    	controles.show();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Libero los recursos del reproductor y el propio objeto.
		if (reproductor != null) {
			reproductor.release();
			reproductor = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean canPause() {
		// Se puede pausar.
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		// Se puede desplazar hacia atr�s
		return true;
	}

	@Override
	public boolean canSeekForward() {
		// Se puede desplazar hacia delante.
		return true;
	}

	@Override
	public int getBufferPercentage() {
		// Por defecto.
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		// Retorno la posici�n actual del reproductor.
		return reproductor.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		// Retorno la duraci�n del contenido del reproductor.
		return reproductor.getDuration();
	}

	@Override
	public boolean isPlaying() {
		// Retorno si el reproductor est� reproduciendo.
		return reproductor.isPlaying();
	}

	@Override
	public void pause() {
		// Hago pause en el reproductor.
		if (reproductor.isPlaying()) {
			reproductor.pause();
		}
	}

	@Override
	public void seekTo(int pos) {
		// Muevo la posici�n del reproductor.
		reproductor.seekTo(pos);
	}

	@Override
	public void start() {
		// Inicio el reproductor.
		reproductor.start();
	}

}