package es.iessaladillo.pedrojoya.pr057;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Vistas.
    private EditText txtAlumno;
    private ListView lstAsignaturas;

    // Variables.
    private ArrayList<String> asignaturas;
    private ArrayAdapter<String> adaptador;

    // Al crear la actividad.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se obtienen y configuran las vistas.
        configTxtAlumno();
        configLstAsignaturas();
    }

    // Obtiene y configura txtAlumno.
    private void configTxtAlumno() {
        // Se establece el listener para onLongClick sobre txtAlumno.
        txtAlumno = (EditText) findViewById(R.id.txtAlumno);
        txtAlumno.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Se activa el modo de acci�n contextual.
                activarModoContextualIndividual(v);
                // Se retorna que el evento ya ha sido procesado.
                return true;
            }
        });
    }

    // Activa el modo de acci�n contextual individual.
    private void activarModoContextualIndividual(View v) {
        // Se inicia el modo de acci�n contextual pas�ndole el objeto listener
        // que atender� a los eventos del modo de acci�n contextual, que creamos
        // de forma inline.
        startActionMode(new ActionMode.Callback() {

            // Al preparar el modo.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // No se hace nada.
                return false;
            }

            // Al crear el modo de acci�n.
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Se infla la especificaci�n del men� contextual en el men�.
                mode.getMenuInflater().inflate(R.menu.txtalumnos_menu, menu);
                // Se retorna que ya se ha procesado la creaci�n del modo de
                // acci�n
                // contextual.
                return true;
            }

            // Al pulsar sobre un �tem de acci�n del modo contextual.
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Dependiendo del �tem de men� pulsado.
                switch (item.getItemId()) {
                case R.id.mnuAprobar:
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.felicidades) + " "
                                    + txtAlumno.getText().toString() + ". "
                                    + getString(R.string.estas_aprobado),
                            Toast.LENGTH_LONG).show();
                    break;
                case R.id.mnuEliminar:
                    // Se borra el texto del EditText.
                    txtAlumno.setText("");
                    break;
                }
                // Se retorna que se ha procesado el evento.
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode arg0) {
                // No se hace nada.
            }

        });
        // Se indica que la vista ha sido seleccionada.
        v.setSelected(true);
    }

    // Obtiene y configura lstAsignaturas.
    private void configLstAsignaturas() {
        // Se establece el modo de selecci�n m�ltiple modal.
        lstAsignaturas = (ListView) this.findViewById(R.id.lstAsignaturas);
        lstAsignaturas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Se carga la lista con datos.
        asignaturas = new ArrayList<String>();
        asignaturas.add("Android");
        asignaturas.add("Acceso a datos");
        asignaturas.add("Libre configuraci�n");
        adaptador = new ArrayAdapter<String>(this, R.layout.activity_main_item,
                R.id.lblAsignatura, asignaturas);
        lstAsignaturas.setAdapter(adaptador);
        // Se establece el listener para los eventos del modo de acci�n
        // contextual.
        lstAsignaturas
                .setMultiChoiceModeListener(new MultiChoiceModeListener() {
                    // Al preparse el modo.
                    @Override
                    public boolean onPrepareActionMode(ActionMode mode,
                            Menu menu) {
                        // No se hace nada.
                        return false;
                    }

                    // Al destruirse el modo.
                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        // No se hace nada.
                    }

                    // Al crear el modo.
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        // Se infla la especificaci�n del men� contextual en el
                        // men�.
                        mode.getMenuInflater().inflate(R.menu.txtalumnos_menu,
                                menu);
                        // Se retorna que ya se ha gestionado el evento.
                        return true;
                    }

                    // Al hacer click sobre un �tem de acci�n del modo
                    // contextual.
                    @Override
                    public boolean onActionItemClicked(ActionMode mode,
                            MenuItem item) {
                        // Dependiendo del elemento pulsado.
                        switch (item.getItemId()) {
                        case R.id.mnuAprobar:
                            String mensaje = "";
                            // Se obtienen los elementos seleccionados.
                            ArrayList<String> elementos = getElementosSeleccionados(
                                    lstAsignaturas, false);
                            // Se a�ade cada elemento al mensaje
                            for (String elemento : elementos) {
                                mensaje = mensaje + elemento + " ";
                            }
                            // Se muestra el mensaje.
                            mensaje = getString(R.string.has_aprobado)
                                    + mensaje;
                            Toast.makeText(getApplicationContext(), mensaje,
                                    Toast.LENGTH_LONG).show();
                            break;
                        case R.id.mnuEliminar:
                            // Se obtienen los elementos seleccionados (y se
                            // quita la selecci�n).
                            ArrayList<String> elems = getElementosSeleccionados(
                                    lstAsignaturas, true);
                            // Se eliminan del adaptador.
                            for (String elemento : elems) {
                                adaptador.remove(elemento);
                            }
                            // Se notifica al adaptador que ha habido cambios.
                            adaptador.notifyDataSetChanged();
                            Toast.makeText(
                                    getApplicationContext(),
                                    elems.size()
                                            + getString(R.string.asignaturas_eliminadas),
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                        // Se retorna que se ha procesado el evento.
                        return true;
                    }

                    // Al cambiar el estado de selecci�n de un elemento.
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode,
                            int position, long id, boolean checked) {
                        // Se actualiza el t�tulo de la action bar contextual.
                        mode.setTitle(lstAsignaturas.getCheckedItemCount()
                                + getString(R.string.de)
                                + lstAsignaturas.getCount());

                    }
                });
        // Un click simple ya activa el modo de acci�n contextual.
        lstAsignaturas.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adaptador, View v,
                    int position, long id) {
                // Se selecciona el elemento.
                lstAsignaturas.setItemChecked(position, true);
            }
        });
    }

    // Retorna un ArrayList con los elementos seleccionados. Recibe la lista y
    // si debe quitarse la selecci�n una vez obtenidos los elementos.
    private ArrayList<String> getElementosSeleccionados(ListView lst,
            boolean uncheck) {
        // ArrayList resultado.
        ArrayList<String> datos = new ArrayList<String>();
        // Se obtienen los elementos seleccionados de la lista.
        SparseBooleanArray selec = lst.getCheckedItemPositions();
        for (int i = 0; i < selec.size(); i++) {
            // Si est� seleccionado.
            if (selec.valueAt(i)) {
                int position = selec.keyAt(i);
                // Se quita de la selecci�n (si hay que hacerlo).
                if (uncheck) {
                    lst.setItemChecked(position, false);
                }
                // Se a�ade al resultado.
                datos.add((String) lst.getItemAtPosition(selec.keyAt(i)));
            }
        }
        // Se retorna el resultado.
        return datos;
    }

}