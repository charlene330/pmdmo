package es.iessaladillo.pedrojoya.pr101;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

public class ExportarService extends IntentService {

    // Constantes.
    public static final String EXTRA_DATOS = "extra_datos";
    public static final String EXTRA_FILENAME = "extra_filename";
    private static final long ESPERA = 2;
    private static final String NOMBRE_ARCHIVO = "alumnos";

    // Constructor.
    public ExportarService() {
        // El constructor del padre requiere que se le pase un nombre al
        // servicio.
        super("exportar");
    }

    // Cuando se procesa cada llamada.
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // Se simula que tarda en ejecutarse dos segundos.
            TimeUnit.SECONDS.sleep(ESPERA);
            // Se obtiene el directorio en el que crear el archivo.
            String estadoSD = Environment.getExternalStorageState();
            File rootDir;
            if (estadoSD.equals(Environment.MEDIA_MOUNTED)) {
                rootDir = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                rootDir.mkdirs();
            } else {
                rootDir = getFilesDir();
            }
            // Se crea el archivo.
            SimpleDateFormat formateadorFecha = new SimpleDateFormat(
                    "yyyyMMddHHmm", Locale.getDefault());
            String nombreArchivo = NOMBRE_ARCHIVO
                    + formateadorFecha.format(new Date()) + ".txt";
            File outputFile = new File(rootDir, nombreArchivo);
            // Se escriben los datos en el archivo.
            String[] alumnos = intent.getStringArrayExtra(EXTRA_DATOS);
            PrintWriter escritor = new PrintWriter(outputFile);
            for (int i = 0; i < alumnos.length; i++) {
                escritor.println(alumnos[i]);
            }
            escritor.close();
            // Se env�a un broadcast ordenado con el intent de confirmaci�n de
            // la exportaci�n.
            Intent respuestaIntent = new Intent();
            respuestaIntent
                    .setAction("es.iessaladillo.pedrojoya.pr101.action.EXPORTED");
            respuestaIntent.putExtra(EXTRA_FILENAME, Uri.fromFile(outputFile)
                    .toString());
            sendOrderedBroadcast(respuestaIntent, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
