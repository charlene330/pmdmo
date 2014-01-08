package es.iessaladillo.pedrojoya.pr028.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import es.iessaladillo.pedrojoya.pr028.R;
import es.iessaladillo.pedrojoya.pr028.fragmentos.AlumnoFragment;
import es.iessaladillo.pedrojoya.pr028.fragmentos.ListaAlumnosFragment;
import es.iessaladillo.pedrojoya.pr028.fragmentos.ListaAlumnosFragment.OnListaAlumnosFragmentListener;
import es.iessaladillo.pedrojoya.pr028.fragmentos.SiNoDialogFragment;
import es.iessaladillo.pedrojoya.pr028.fragmentos.SiNoDialogFragment.SiNoDialogListener;

public class MainActivity extends ActionBarActivity implements
        OnListaAlumnosFragmentListener, SiNoDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se carga el layout que incluye de forma est�tica el fragmento.
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater;
        inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dependiendo de la opci�n de men� seleccionada.
        switch (item.getItemId()) {
        case android.R.id.home:
            // Se vuelve atr�s.
            onBackPressed();
            break;
        case R.id.mnuAlumnoAgregar:
            // Se quiere agregar un alumno.
            onAgregarAlumno();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Muestra la actividad de alumno en "modo agregar".
    @Override
    public void onAgregarAlumno() {
        // Se establece un extra para indicar que querremos que la actividad
        // de alumno funcione en "modo agregar".
        Intent i = new Intent(this, AlumnoActivity.class);
        i.putExtra(AlumnoFragment.EXTRA_MODO, AlumnoFragment.MODO_AGREGAR);
        this.startActivity(i);
    }

    // Muestra la actividad de alumno en "modo editar".
    @Override
    public void onEditarAlumno(long id) {
        // Se establece un extra para indicar que querremos que la actividad
        // de alumno funcione en "modo editar".
        Intent i = new Intent(this, AlumnoActivity.class);
        i.putExtra(AlumnoFragment.EXTRA_MODO, AlumnoFragment.MODO_EDITAR);
        // Se pasa el ID del alumno que queremos editar.
        i.putExtra(AlumnoFragment.EXTRA_ID, id);
        this.startActivity(i);
    }

    // Muestra el fragmento de di�logo de confirmaci�n de eliminaci�n.
    @Override
    public void onConfirmarEliminarAlumnos() {
        SiNoDialogFragment frgDialogo = new SiNoDialogFragment();
        frgDialogo.show(this.getSupportFragmentManager(), "SiNoDialogFragment");
    }

    // Se confirma la eliminaci�n de los alumnos seleccionados.
    @Override
    public void onPositiveButtonClick(DialogFragment dialog) {
        // Se llama al m�todo del fragmento para eliminar los alumnos
        // seleccionados.
        ListaAlumnosFragment frgListaAlumnos = (ListaAlumnosFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frgListaAlumnos);
        frgListaAlumnos.eliminarAlumnos();
    }

    // No se confirma la eliminaci�n de los alumnos seleccionados.
    @Override
    public void onNegativeButtonClick(DialogFragment dialog) {
        // M�todo requerido por la interfaz SiNoDialogListener.
    }

}
