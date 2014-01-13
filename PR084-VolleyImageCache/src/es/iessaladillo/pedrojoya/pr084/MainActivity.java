package es.iessaladillo.pedrojoya.pr084;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity implements EndlessGridView.LoadAgent {

    // Vistas.
    private EndlessGridView lstFotos;

    // Variables.
    private MenuItem mnuActualizar;
    private FotosAdapter adaptador;
    private RequestQueue colaPeticiones;
    private String sUrlSiguiente;

    // Al crearse la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstFotos = (EndlessGridView) this.findViewById(R.id.lstFotos);
        // Se establece la actividad como listener del scroll del gridview.
        lstFotos.setLoadAgent(this);
        // Se obtiene la cola de peticiones de Volley.
        colaPeticiones = App.getRequestQueue();
        // Se crea y asigna el adaptador para el ListView.
        ArrayList<Foto> fotos = new ArrayList<Foto>();
        adaptador = new FotosAdapter(this, fotos);
        lstFotos.setAdapter(adaptador);
        // Se establece la url inicial.
        sUrlSiguiente = Instagram.getRecentMediaURL("algeciras");
        // Si hay conexi�n a Internet se obtienen los datos.
        if (isConnectionAvailable()) {
            obtenerDatos();
        } else {
            mostrarToast(getString(R.string.no_hay_conexion_a_internet));
        }
    }

    // Obtiene los datos JSON de la lista de fotos de Instagram.
    private void obtenerDatos() {
        // Se muestra el c�rculo de progreso.
        if (mnuActualizar != null) {
            mnuActualizar.setVisible(true);
        }
        // Se crea el listener que recibir� la respuesta de la petici�n.
        Response.Listener<JSONObject> listenerRespuesta = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject respuesta) {
                // Se crea la lista de datos parseando la respuesta.
                ArrayList<Foto> lista = procesarRespuesta(respuesta);
                // Se a�aden las fotos de la lista al adaptador.
                agregarAlAdaptador(lista);
                // Se oculta el progreso.
                if (mnuActualizar != null) {
                    mnuActualizar.setVisible(false);
                }
                // Se indica al gridview que ya ha finalizado la carga.
                lstFotos.setLoaded();
            }

            // Agraga al adaptador la lista de fotos obtenidas.
            private void agregarAlAdaptador(ArrayList<Foto> lista) {
                // Se a�ade cada foto al adaotador.
                for (Foto foto : lista) {
                    adaptador.add(foto);
                }
                // Se notifican los cambios en los datos.
                adaptador.notifyDataSetChanged();
            }

        };
        // Se crea la petici�n JSON.
        JsonObjectRequest peticion = new JsonObjectRequest(Method.GET,
                sUrlSiguiente, null, listenerRespuesta, null);
        // Se a�ade la petici�n a la cola de peticiones.
        colaPeticiones.add(peticion);
    }

    // Procesa el objeto JSON de respuesta, retornando la lista de fotos.
    private ArrayList<Foto> procesarRespuesta(JSONObject respuesta) {
        ArrayList<Foto> lista = new ArrayList<Foto>();
        try {
            // Se procesa la respuesta para obtener los datos deseados.
            // Se obtiene cual debe ser la pr�xima petici�n para paginaci�n.
            JSONObject paginationKeyJSONObject = respuesta
                    .getJSONObject(Instagram.PAGINACION_KEY);
            sUrlSiguiente = paginationKeyJSONObject
                    .getString(Instagram.SIGUIENTE_PETICION_KEY);
            // Se obtiene el valor de la clave "data", que correponde al
            // array de datos.
            JSONArray dataKeyJSONArray = respuesta
                    .getJSONArray(Instagram.ARRAY_DATOS_KEY);
            // Por cada uno de los elementos del array de datos.
            for (int i = 0; i < dataKeyJSONArray.length(); i++) {
                // Se obtiene el elemento, que es un JSONObject.
                JSONObject elemento = dataKeyJSONArray.getJSONObject(i);
                // Si el tipo de elemento indica que es una imagen.
                if (elemento.getString(Instagram.TIPO_ELEMENTO_KEY).equals(
                        Instagram.TIPO_ELEMENTO_IMAGEN)) {
                    // Se crea un objeto modelo.
                    Foto foto = new Foto();
                    // Se obtiene el usuario.
                    JSONObject usuario = elemento
                            .getJSONObject(Instagram.USUARIO_KEY);
                    // Se obtiene del usuario el nombre de usuario y se
                    // guarda en el objeto modelo como descripci�n.
                    foto.setDescripcion(usuario
                            .getString(Instagram.NOMBRE_USUARIO_KEY));
                    // Se obtiene la imagen.
                    JSONObject imagen = elemento
                            .getJSONObject(Instagram.IMAGEN_KEY);
                    // Se obtiene la url de la miniatura de la imagen y se
                    // guarda en el objeto modelo.
                    foto.setUrl(imagen.getJSONObject(
                            Instagram.RESOLUCION_MINIATURA_KEY).getString(
                            Instagram.URL_KEY));
                    // Se a�ade el objeto modelo a la lista de datos
                    // para el adaptador.
                    lista.add(foto);
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Se retorna la lista de fotos.
        return lista;
    }

    // Al crearse el men� de opciones.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        mnuActualizar = menu.findItem(R.id.mnuActualizar);
        mnuActualizar.setVisible(false);
        mnuActualizar.setActionView(R.layout.actionview_progreso);
        return super.onCreateOptionsMenu(menu);
    }

    // Retorna si hay conexi�n a la red o no.
    private boolean isConnectionAvailable() {
        // Se obtiene del gestor de conectividad la informaci�n de red.
        ConnectivityManager gestorConectividad = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRed = gestorConectividad.getActiveNetworkInfo();
        // Se retorna si hay conexi�n.
        return (infoRed != null && infoRed.isConnected());
    }

    // Muestra un toast con duraci�n larga.
    private void mostrarToast(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG)
                .show();
    }

    // Cuando el gridview solicta m�s datos.
    @Override
    public void loadData() {
        // Se obtienen m�s fotos.
        obtenerDatos();
    }

}
