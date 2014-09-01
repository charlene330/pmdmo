package es.iessaladillo.pedrojoya.pr101;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class ExportarReceiver extends BroadcastReceiver {

    // Constantes.
    private static final int NC_EXPORTADO = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Se obtiene el gestor de notificaciones del sistema.
        NotificationManager mGestor = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Se crea el constructor de notificaciones.
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        // Se configuran los elementos b�sicos de la notificaci�n.
        b.setSmallIcon(R.drawable.ic_launcher); // Icono peque�o.
        b.setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(
                R.drawable.ic_launcher)).getBitmap()); // Icono grande.
        b.setContentTitle("Archivo creado"); // T�tulo (1� l�nea).
        b.setContentText("Se ha generado la exportaci�n"); // Texto (2� l�nea).
        b.setTicker("Archivo de exportaci�n creado"); // Ticker.
        // Se crea el pending intent.
        String filename = intent.getStringExtra(ExportarService.EXTRA_FILENAME);
        Intent mostrarIntent = new Intent(Intent.ACTION_VIEW);
        mostrarIntent.setDataAndType(Uri.parse(filename), "text/plain");
        PendingIntent pi = PendingIntent.getActivity(context, 0, mostrarIntent,
                0);
        b.setContentIntent(pi);
        // Se construye y muestra la notificaci�n, asign�ndole un c�digo de
        // notificaci�n entero.
        mGestor.notify(NC_EXPORTADO, b.build());
    }

}
