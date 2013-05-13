package es.iessaladillo.pedrojoya.pr058;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SherlockListView;
import com.actionbarsherlock.view.SherlockListView.MultiChoiceModeListenerCompat;

public class MainActivity extends SherlockActivity {

    private TextView txtAlumno;
    private ActionMode modoIndividual;
    private SherlockListView lstAsignaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configTxtAlumno();
        configLstAsignaturas();
    }

    private void configTxtAlumno() {
        // Se establece el listener para onLongClick sobre txtAlumno.
        txtAlumno = (TextView) this.findViewById(R.id.txtAlumno);
        txtAlumno.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Se activa el modo de acci�n contextual.
                activarModoContextualIndividual(v);
                // Se retorna que se ha procesado el evento.
                return true;
            }
        });
    }

    private void activarModoContextualIndividual(View v) {
        // Se inicia el modo de acci�n contextual pas�ndole el objeto listener
        // que atender� a los eventos del modo de acci�n contextual, que creamos
        // de forma inline.
        modoIndividual = this.startActionMode(new ActionMode.Callback() {
            // Al preparar el modo.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // No se hace nada.
                return false;
            }

            // Al destruir el modo.
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Se destruye el modo de acci�n contextual.
                modoIndividual = null;
            }

            // Al crear el modo de acci�n.
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Se infla la especificaci�n del men� contextual en el men�.
                mode.getMenuInflater().inflate(R.menu.txtalumnos_menu, menu);
                // Se retorna que se ha procesado la creaci�n del modo de acci�n
                // contextual.
                return true;
            }

            // Al pulsar sobre un �tem de acci�n del modo contextual.
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Dependiendo del elemento pulsado.
                switch (item.getItemId()) {
                    case R.id.mnuEditar:
                        Toast.makeText(getApplicationContext(),
                                R.string.editar, Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mnuEliminar:
                        Toast.makeText(getApplicationContext(),
                                R.string.eliminar, Toast.LENGTH_LONG).show();
                        break;
                }
                // Se retorna que se ha procesado el evento.
                return true;
            }
        });
        // Se indica que la vista ha sido seleccionada.
        v.setSelected(true);

    }

    private void configLstAsignaturas() {
        // Se establece el modo de selecci�n m�ltiple modal.
        lstAsignaturas = (SherlockListView) this
                .findViewById(R.id.lstAsignaturas);
        lstAsignaturas
                .setChoiceMode(SherlockListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Se carga la lista con datos.
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(
                this, R.array.asignaturas,
                android.R.layout.simple_list_item_multiple_choice);
        lstAsignaturas.setAdapter(adaptador);
        // Se establece el listener para los eventos del modo de acci�n
        // contextual.
        lstAsignaturas
                .setMultiChoiceModeListener(new MultiChoiceModeListenerCompat() {
                    // Al prepararse el modo.
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
                    public boolean onActionItemClicked(ActionMode menu,
                            MenuItem item) {
                        // Dependiendo del elemento pulsado.
                        switch (item.getItemId()) {
                            case R.id.mnuEditar:
                                Toast.makeText(getApplicationContext(),
                                        R.string.editar, Toast.LENGTH_LONG)
                                        .show();
                                break;
                            case R.id.mnuEliminar:
                                Toast.makeText(getApplicationContext(),
                                        R.string.eliminar, Toast.LENGTH_LONG)
                                        .show();
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
        lstAsignaturas.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adaptador, View v,
                    int position, long id) {
                // Se selecciona el elemento.
                lstAsignaturas.setItemChecked(position, true);

            }
        });
    }

}