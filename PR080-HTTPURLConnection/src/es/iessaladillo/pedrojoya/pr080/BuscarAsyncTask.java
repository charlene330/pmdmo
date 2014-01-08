package es.iessaladillo.pedrojoya.pr080;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;

// Tarea as�ncrona que busca un nombre en Google.
public class BuscarAsyncTask extends AsyncTask<String, Void, String> {

    // Interfaz de comunicaci�n con la actividad.
    public interface Callbacks {
        void onPostExecute(BuscarAsyncTask object, String result);
    }

    // Variables.
    Callbacks listener;

    // Constructor.
    public BuscarAsyncTask(Callbacks listener) {
        this.listener = listener;
    }

    // Realiza el procesamiento en segundo plano.
    @Override
    protected String doInBackground(String... params) {
        String resultado = "";
        String contenido = "";
        // Se obtiene el nombre a buscar.
        String nombre = params[0];
        try {
            // Se obtiene la url de b�squeda.
            URL url = new URL("http://www.google.es/search?hl=es&q=\""
                    + URLEncoder.encode(nombre, "UTF-8") + "\"");
            // Se crea la conexi�n.
            HttpURLConnection conexion = (HttpURLConnection) url
                    .openConnection();
            // Se establece el m�todo de conexi�n.
            conexion.setRequestMethod("GET");
            // Google exige identificar el user agent.
            conexion.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
            // Se indica que pretendemos leer del flujo de entrada.
            conexion.setDoInput(true);
            // Se realiza la conexi�n.
            conexion.connect();
            // Si el c�digo de respuesta es correcto.
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Se crea un lector que lee del flujo de entrada de la
                // conexi�n.
                BufferedReader lector = new BufferedReader(
                        new InputStreamReader(conexion.getInputStream()));
                // Se lee l�nea a l�nea y se agrega a la cadena contenido.
                String linea = lector.readLine();
                while (linea != null) {
                    contenido += linea;
                    linea = lector.readLine();
                }
                lector.close();
                // Se busca en el contenido la palabra Aproximadamente.
                int ini = contenido.indexOf("Aproximadamente");
                if (ini != -1) {
                    // Se busca el siguiente espacio en blanco despu�s de
                    // Aproximadamente.
                    int fin = contenido.indexOf(" ", ini + 16);
                    // El resultado corresponde a lo que sigue a
                    // Aproximadamente, hasta el siguiente espacio en blanco.
                    resultado = contenido.substring(ini + 16, fin);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultado;
    }

    // Una vez finalizado el procesamiento en segundo plano.
    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onPostExecute(this, result);
        }
    }

    // Al ser cancelada la tarea.
    @Override
    protected void onCancelled() {
        // Se libera el listener.
        listener = null;
    }

}
