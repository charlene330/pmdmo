package es.iessaladillo.pedrojoya.pr057;

import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView txtAlumno;
    private ActionMode modoIndividual;
    private ListView lstAsignaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        // Se establece el modo de selecci�n m�ltiple modal.
        lstAsignaturas = (ListView) this.findViewById(R.id.lstAsignaturas);
        lstAsignaturas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
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

}
