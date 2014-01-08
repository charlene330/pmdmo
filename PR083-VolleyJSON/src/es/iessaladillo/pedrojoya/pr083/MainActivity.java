package es.iessaladillo.pedrojoya.pr083;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {

    // Constantes.
    private static final String URL_DATOS = "http://www.json-generator.com/j/bOZlHhPerm";

    // Vistas.
    private ListView lstAlumnos;

    // Variables.
    private MenuItem mnuActualizar;
    private AlumnosAdapter adaptador;
    private RequestQueue colaPeticiones;

    // Al crearse la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstAlumnos = (ListView) this.findViewById(R.id.lstAlumnos);
        // Se obtiene la cola de peticiones de Volley.
        colaPeticiones = App.getRequestQueue();
        // Si hay conexi�n a Internet.
        if (isConnectionAvailable()) {
            // realizarPeticionJSON();
            realizarPeticionGson();
        } else {
            mostrarToast(getString(R.string.no_hay_conexion_a_internet));
        }
    }

    // A�ade a la cola de peticiones una petici�n JSON.
    private void realizarPeticionJSON() {
        // Se crea el listener para la respuesta.
        Listener<JSONArray> listener = new Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // Se oculta el c�rculo de progreso.
                mnuActualizar.setVisible(false);
                // Se procesa la cadena JSON, obteniendo el ArrayList de
                // alumnos.
                List<Alumno> alumnos = procesarJSON(response);
                // Se cargan los alumnos en la lista.
                cargarAlumnos(alumnos);
            }
        };
        // Se crea el listener de error.
        ErrorListener errorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarToast(error.getMessage());
            }
        };
        // Se crea la petici�n.
        JsonArrayRequest peticion = new JsonArrayRequest(URL_DATOS, listener,
                errorListener);
        // Se a�ade la petici�n a la cola de Volley.
        colaPeticiones.add(peticion);
    }

    // A�ade a la cola de peticiones una petici�n Gson.
    private void realizarPeticionGson() {
        // Se crea el listener para la respuesta.
        Listener<ArrayList<Alumno>> listener = new Listener<ArrayList<Alumno>>() {

            @Override
            public void onResponse(ArrayList<Alumno> response) {
                // Se oculta el c�rculo de progreso.
                mnuActualizar.setVisible(false);
                // Se cargan los alumnos en la lista.
                cargarAlumnos(response);
            }
        };
        // Se crea el listener de error.
        ErrorListener errorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarToast(error.getMessage());
            }
        };
        // Se crea la petici�n.
        Gson gson = new Gson();
        Type tipo = new TypeToken<List<Alumno>>() {
        }.getType();

        GsonArrayRequest<ArrayList<Alumno>> peticion = new GsonArrayRequest<ArrayList<Alumno>>(
                Request.Method.GET, URL_DATOS, tipo, listener, errorListener,
                gson);
        // Se a�ade la petici�n a la cola de Volley.
        colaPeticiones.add(peticion);
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

    // Carga los alumnos en el ListView. Recibe la lista de alumnos.
    private void cargarAlumnos(List<Alumno> alumnos) {
        // Se crea y asigna el adaptador para el ListView.
        adaptador = new AlumnosAdapter(this, alumnos);
        lstAlumnos.setAdapter(adaptador);
    }

    // Procesa la cadena JSON y retorna el ArrayList de alumnos.
    private List<Alumno> procesarJSON(JSONArray result) {
        // Se crea el ArrayList a retornar.
        List<Alumno> alumnos = new ArrayList<Alumno>();
        try {
            // Por cada objeto JSON del array JSON
            for (int i = 0; i < result.length(); i++) {
                // Se obtiene el objeto JSON correspondiente al alumno.
                JSONObject alumnoJSON = result.getJSONObject(i);
                // Se crea un objeto alumno.
                Alumno alumno = new Alumno();
                // Se escriben las propiedades del alumno, obtenidas del objeto
                // JSON.
                alumno.setNombre(alumnoJSON.getString(Alumno.KEY_NOMBRE));
                alumno.setDireccion(alumnoJSON.getString(Alumno.KEY_DIRECCION));
                alumno.setTelefono(alumnoJSON.getString(Alumno.KEY_TELEFONO));
                alumno.setCurso(alumnoJSON.getString(Alumno.KEY_CURSO));
                // Se a�ade el alumno al ArrayList.
                alumnos.add(alumno);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Se retorna el ArrayList.
        return alumnos;
    }

    // Procesa la cadena JSON y retorna el ArrayList de alumnos.
    private List<Alumno> procesarGSON(String result) {
        // Se crea el objeto Gson.
        Gson gson = new Gson();
        Type tipoListaAlumnos = new TypeToken<List<Alumno>>() {
        }.getType();
        // Se procesa la cadena JSON.
        List<Alumno> alumnos = gson.fromJson(result, tipoListaAlumnos);
        // Se retorna el ArrayList.
        return alumnos;
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

}