package es.iessaladillo.pedrojoya.pr050;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment {

    // Interfaz p�blica de comunicaci�n con la actividad.
    public interface Listener {
        // Cuando se solicita ver la foto.
        public void onFoto(int fotoResId);
    }

    // Retorna la vista que debe mostrar el fragmento.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    // Al terminar de crearse la actividad con todos sus fragmentos.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Se indica que el fragmento proporciona �tems al men� de opciones.
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    // Cuando se crea el men� de opciones.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Se agregan al men� de opciones los �tems correspondientes al
        // fragmento.
        inflater.inflate(R.menu.fragment_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Cuando se pulsa sobre un �tem del men� de opciones.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dependiendo del item pulsado realizo la acci�n deseada.
        switch (item.getItemId()) {
        case R.id.mnuFoto:
            // Se simula la pulsaci�n de atr�s.
            getActivity().onBackPressed();
            break;
        default:
            // Se propaga el evento porque no ha sido resuelto.
            return super.onOptionsItemSelected(item);
        }
        // Si llegamos aqu� es que se ha resuelto el evento.
        return true;
    }

}
