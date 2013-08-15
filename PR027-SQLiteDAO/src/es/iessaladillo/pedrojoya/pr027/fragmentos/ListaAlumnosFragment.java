package es.iessaladillo.pedrojoya.pr027.fragmentos;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import es.iessaladillo.pedrojoya.pr027.R;
import es.iessaladillo.pedrojoya.pr027.adaptadores.ListaAlumnosAdapter;
import es.iessaladillo.pedrojoya.pr027.bd.DAO;
import es.iessaladillo.pedrojoya.pr027.modelos.Alumno;

public class ListaAlumnosFragment extends Fragment {

    // Interfaz de comunicaci�n con la actividad.
    public interface OnListaAlumnosFragmentListener {
        public void onAgregarAlumno();

        public void onEditarAlumno(long id);

        public void onConfirmarEliminarAlumnos();
    }

    // Variables miembro.
    private ListView lstAlumnos;
    private RelativeLayout rlListaVacia;
    private OnListaAlumnosFragmentListener listener;
    private ActionMode modoContextual;

    // Retorna la vista que debe mostrar el fragmento.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Se infla el layout que debe mostrar el fragmento.
        View v = inflater.inflate(R.layout.fragment_lista_alumnos, container,
                false);
        // Se configuran las vistas.
        lstAlumnos = (ListView) v.findViewById(R.id.lstAlumnos);
        rlListaVacia = (RelativeLayout) v.findViewById(R.id.rlListaVacia);
        // Si la lista est� vac�a se muestra un icono y un texto para que al
        // pulsarlo se agregue un alumno.
        rlListaVacia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Se informa al listener (actividad) de que se quiere agregar
                // un alumno.
                listener.onAgregarAlumno();
            }
        });
        lstAlumnos.setEmptyView(rlListaVacia);
        // Al hacer click sobre un elemento de la lista.
        lstAlumnos.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // Se obtiene el alumno sobre el que se ha pulsado.
                Alumno alumno = (Alumno) lstAlumnos.getItemAtPosition(position);
                // Se da la orden de editar.
                listener.onEditarAlumno(alumno.getId());
            }

        });
        // Se establece que se puedan seleccionar varios elementos de la lista.
        lstAlumnos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lstAlumnos.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode arg0) {
            }

            // Al crear el modo de acci�n contextual.
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Se infla la especificaci�n del men� contextual en el
                // men�.
                mode.getMenuInflater().inflate(R.menu.fragment_lista_alumnos,
                        menu);
                // Se retorna que ya se ha gestionado el evento.
                return true;
            }

            // Al pulsar sobre un �tem del modo de acci�n contextual.
            @Override
            public boolean onActionItemClicked(ActionMode modo, MenuItem item) {
                // Dependiendo del elemento pulsado.
                switch (item.getItemId()) {
                case R.id.mnuAlumnoEliminar:
                    // Si hay elementos seleccionados se pide confirmaci�n.
                    if (lstAlumnos.getCheckedItemPositions().size() > 0) {
                        // Se almacena el modo contextual para poder cerrarlo
                        // una vez eliminados.
                        modoContextual = modo;
                        // Se pide confirmaci�n.
                        listener.onConfirmarEliminarAlumnos();
                    }
                    break;
                }
                // Se retorna que se ha procesado el evento.
                return true;
            }

            // Al seleccionar un elemento de la lista.
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                    int position, long id, boolean checked) {
                // Se actualiza el t�tulo de la action bar contextual.
                mode.setTitle(lstAlumnos.getCheckedItemCount() + "");
            }
        });
        // Se retorna la vista a mostrar.
        return v;
    }

    // Al mostrar el fragmento.
    @Override
    public void onResume() {
        // Se carga la lista de alumnos.
        cargarAlumnos();
        super.onResume();
    }

    // Crea el adaptador y carga la lista de alumnos.
    private void cargarAlumnos() {
        // Se obtienen los datos de los alumnos a trav�s del DAO.
        DAO dao = new DAO(this.getActivity()).open();
        ArrayList<Alumno> alumnos = (ArrayList<Alumno>) dao.getAllAlumnos();
        dao.close();
        // Se establece el adaptador para la lista (personalizado).
        lstAlumnos.setAdapter(new ListaAlumnosAdapter(this.getActivity(),
                alumnos));
        // Inicialmente todos los elementos de la lista NO est�n seleccionados.
        for (int i = 0; i < lstAlumnos.getCount(); i++) {
            lstAlumnos.setItemChecked(i, false);
        }
    }

    // Cuando el fragmento es cargado en la actividad.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Se establece la actividad como objeto listener.
            listener = (OnListaAlumnosFragmentListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz.
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnElementoSeleccionadoListener");
        }
    }

    // Elimina de la base de datos los alumnos seleccionados, actualiza el
    // adaptador y cierra el modo de acci�n conextual.
    public void eliminarAlumnos() {
        // Se obtiene el array con las posiciones seleccionadas.
        SparseBooleanArray seleccionados = lstAlumnos.getCheckedItemPositions();
        DAO dao = new DAO(getActivity()).open();
        // Por cada selecci�n.
        for (int i = 0; i < seleccionados.size(); i++) {
            // Se obtiene la posici�n del elemento en el
            // adaptador.
            int position = seleccionados.keyAt(i);
            // Si ha sido seleccionado
            if (seleccionados.valueAt(i)) {
                // Se obtiene el alumo.
                Alumno alu = (Alumno) lstAlumnos.getItemAtPosition(position);
                // Se borra de la base de datos.
                dao.deleteAlumno(alu.getId());
                // Se elimina del adaptador.
                ((ListaAlumnosAdapter) lstAlumnos.getAdapter()).remove(alu);
            }
        }
        dao.close();
        // Se finaliza el modo contextual.
        modoContextual.finish();
        // Se infora al adaptador de que ha habido cambios en sus datos.
        ((ListaAlumnosAdapter) lstAlumnos.getAdapter()).notifyDataSetChanged();
    }

}
