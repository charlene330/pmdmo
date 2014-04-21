package es.iessaladillo.pedrojoya.pr095;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener,
        MediaPlayer.OnPreparedListener, AdapterView.OnItemClickListener {

    // Constantes.
    private static final String EXTENSION_ARCHIVO = ".mp4";

    // Variables.
    private DownloadManager mGestor;
    private BroadcastReceiver mReceptor;
    private MediaPlayer mReproductor;
    private int mPosCancionActual = -1;

    // Vistas.
    private ImageView imgPlay;
    private ListView lstCanciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getVistas();
        // Se crea el reproductor.
        mReproductor = new MediaPlayer();
        // Se obtiene el gestor de descargas.
        mGestor = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        // Se crea el receptor de eventos relacionados con la descarga.
        mReceptor = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // Se comprueba el estado de la descarga.
                comprobarDescarga(intent);
            }

        };
    }

    // Obtiene e inicializa las vistas.
    private void getVistas() {
        ImageView imgAnterior = (ImageView) findViewById(R.id.imgAnterior);
        imgAnterior.setOnClickListener(this);
        ImageView imgSiguiente = (ImageView) findViewById(R.id.imgSiguiente);
        imgSiguiente.setOnClickListener(this);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgPlay.setOnClickListener(this);
        lstCanciones = (ListView) findViewById(R.id.lstCanciones);
        ArrayList<Cancion> canciones = getListaCanciones();
        ArrayAdapter<Cancion> adaptador = new ArrayAdapter<Cancion>(this,
                android.R.layout.simple_list_item_activated_1, canciones);
        lstCanciones.setAdapter(adaptador);
        lstCanciones.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstCanciones.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Se crea el filtro con las acci�n de descarga finalizada.
        IntentFilter filtro = new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        // Se registra el receptor.
        registerReceiver(mReceptor, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Se quita el registro del recpetor.
        unregisterReceiver(mReceptor);
    }

    @Override
    protected void onDestroy() {
        // Se liberan los recursos asociados al reproductor.
        mReproductor.release();
        mReproductor = null;
        super.onDestroy();
    }

    // Comprueba el estado de la descarga que se ha completado.
    private void comprobarDescarga(Intent intent) {
        // Se obtiene el id de la descarga.
        long downloadId = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        // Se realiza la consulta para obtener los datos de la descarga.
        DownloadManager.Query consulta = new DownloadManager.Query();
        consulta.setFilterById(downloadId);
        Cursor c = mGestor.query(consulta);
        // Se comprueba el registro resultante.
        if (c != null) {
            if (c.moveToFirst()) {
                // Dependiendo del estado de la descarga.
                int estado = c.getInt(c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (estado) {
                // Si la descarga se ha realizado correctamente.
                case DownloadManager.STATUS_SUCCESSFUL:
                    // Se reproduce la canci�n a partir de su Uri local.
                    String sUri = c.getString(c
                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    reproducir(sUri);
                    break;
                // Si se ha producido un error en la descarga.
                case DownloadManager.STATUS_FAILED:
                    // Se informa al usuario del error.
                    String motivo = c.getString(c
                            .getColumnIndex(DownloadManager.COLUMN_REASON));
                    informarError(motivo);
                    break;
                }
            }
            // Se cierra el cursor.
            c.close();
        }
    }

    // Informa al usuario del motivo del error en la descarga.
    private void informarError(String motivo) {
        Toast.makeText(this, getString(R.string.error) + motivo,
                Toast.LENGTH_LONG).show();
    }

    // Cuando se pulsa en una canci�n de la lista.
    @Override
    public void onItemClick(AdapterView<?> lst, View v, int position, long id) {
        // Se descarga la canci�n.
        reproducirCancion(position);
    }

    // Trata de reproducir una canci�n, descarg�ndola si es necesario.
    private void reproducirCancion(int position) {
        // Se establece la canci�n actual y se muestra en la lista.
        mPosCancionActual = position;
        lstCanciones.setItemChecked(position, true);
        // Se comprueba si la canci�n est� disponible en local.
        Cancion cancion = (Cancion) lstCanciones.getItemAtPosition(position);
        if (cancion != null) {
            File directory = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File fichero = new File(directory, cancion.getNombre()
                    + EXTENSION_ARCHIVO);
            if (fichero.exists()) {
                // Se reproduce.
                reproducir(Uri.fromFile(fichero).toString());
            } else {
                // Se descarga.
                descargar(cancion);
            }
        }
    }

    // Reproduce el audio en base a la uri local recibida.
    private void reproducir(String sUri) {
        try {
            // Se configura el reproductor.
            mReproductor.reset();
            mReproductor.setDataSource(this, Uri.parse(sUri));
            mReproductor.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // Se prepara la reproducci�n (as�ncronamente)
            mReproductor.setOnPreparedListener(this);
            mReproductor.prepareAsync();
        } catch (Exception e) {
            Log.d("Reproductor", "ERROR: " + e.getMessage());
        }
    }

    // Cuando ya est� preparada la reproducci�n.
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // Se inicia.
        mReproductor.start();
        // Se actualiza el icono del bot�n al de pausar.
        imgPlay.setImageResource(android.R.drawable.ic_media_pause);
        // Se informa al usuario.
        Toast.makeText(this, getString(R.string.reproduciendo),
                Toast.LENGTH_LONG).show();
    }

    // Descarga una canci�n.
    private void descargar(Cancion cancion) {
        // Se crea la solicitud de descarga.
        DownloadManager.Request solicitud = new DownloadManager.Request(
                Uri.parse(cancion.getUrl()));
        solicitud.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        solicitud.setAllowedOverRoaming(false);
        solicitud.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_MUSIC, cancion.getNombre()
                        + EXTENSION_ARCHIVO);
        // solicitud.setDestinationInExternalFilesDir(this,
        // Environment.DIRECTORY_DOWNLOADS,
        // cancion.getNombre() + EXTENSION_ARCHIVO);
        solicitud.setTitle(cancion.getNombre());
        solicitud.setDescription(cancion.getNombre() + "("
                + cancion.getDuracion() + ")");
        solicitud.allowScanningByMediaScanner();
        solicitud
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // Se encola la solicitud.
        mGestor.enqueue(solicitud);
        // Se informa al usuario.
        Toast.makeText(this, getString(R.string.descargando), Toast.LENGTH_LONG)
                .show();
    }

    // Cuando se pulsa en alg�n bot�n del panel.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imgAnterior:
            imgAnteriorOnClick();
            break;
        case R.id.imgSiguiente:
            imgSiguienteOnClick();
            break;
        case R.id.imgPlay:
            imgPlayOnClick();
            break;
        }
    }

    // Al pulsar sobre reproducir / pausar.
    private void imgPlayOnClick() {
        // Si ya se est� reproduciendo.
        if (mPosCancionActual >= 0 && mReproductor.isPlaying()) {
            // Se pausa la reproducci�n.
            mReproductor.pause();
            // Se actualiza el icono al de reproducir.
            imgPlay.setImageResource(android.R.drawable.ic_media_play);
        } else {
            // Si est�bamos en pausa.
            if (mPosCancionActual >= 0) {
                // Se continua la reproducci�n.
                mReproductor.start();
            } else {
                // Se comienza la reproducci�n de la primera canci�n de la
                // lista.
                reproducirCancion(0);
            }
            // Se actualiza el icono al de pausar.
            imgPlay.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    // Al pulsar sobre Siguiente.
    private void imgSiguienteOnClick() {
        reproducirCancion((mPosCancionActual + 1) % lstCanciones.getCount());
    }

    // Al pulsar sobre Anterior.
    private void imgAnteriorOnClick() {
        int anterior;
        if (mPosCancionActual >= 0) {
            anterior = mPosCancionActual - 1;
            if (anterior < 0) {
                anterior = lstCanciones.getCount() - 1;
            }
        } else {
            anterior = 0;
        }
        reproducirCancion(anterior);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.mnuDescargas:
            // Se muesta la actividad est�ndar de descargas.
            mostrarDescargas();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Muestra la actividad est�ndar de descargas.
    private void mostrarDescargas() {
        Intent i = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    // Crea y retorna el ArrayList de canciones.
    private ArrayList<Cancion> getListaCanciones() {
        ArrayList<Cancion> canciones = new ArrayList<Cancion>();
        canciones
                .add(new Cancion("Morning Mood (by Grieg)", "3:43",
                        "https://www.youtube.com/audiolibrary_download?vid=036500ffbf472dcc"));
        canciones
                .add(new Cancion("Brahms Lullaby", "1:46",
                        "https://www.youtube.com/audiolibrary_download?vid=9894a50b486c6136"));
        canciones
                .add(new Cancion("From Russia With Love", "2:26",
                        "https://www.youtube.com/audiolibrary_download?vid=4e8d1a0fdb3bbe12"));
        canciones
                .add(new Cancion("Les Toreadors from Carmen (by Bizet)",
                        "2:21",
                        "https://www.youtube.com/audiolibrary_download?vid=fafb35a907cd6e73"));
        canciones
                .add(new Cancion("Funeral March (by Chopin)", "9:25",
                        "https://www.youtube.com/audiolibrary_download?vid=4a7d058f20d31cc4"));
        return canciones;
    }

}
