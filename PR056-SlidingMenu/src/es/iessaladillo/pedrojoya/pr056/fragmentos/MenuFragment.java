package es.iessaladillo.pedrojoya.pr056.fragmentos;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import es.iessaladillo.pedrojoya.pr056.R;
import es.iessaladillo.pedrojoya.pr056.adaptadores.AlbumesAdapter;
import es.iessaladillo.pedrojoya.pr056.modelos.Album;

public class MenuFragment extends ListFragment {

    // Interfaz para notificaci�n de eventos desde el fragmento.
    public interface OnAlbumSelectedListener {
        // Cuando se selecciona un �lbum.
        public void onAlbumSelected(Album album);
    }

    // Variables miembro.
    private OnAlbumSelectedListener listener;
    private AlbumesAdapter adaptador;
    int elemSeleccionado = 0;

    // Retorna la vista que debe mostrar el fragmento.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Se retorna la vista que debe utilizar el fragmento, que corresponde
        // al layout con la lista de opciones.
        return inflater.inflate(R.layout.menu_list, null);
    }

    // Al enlazarse el fragmento a la actividad.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Se establece que la propia actividad sea el listener al que
            // informar� el fragmento cuando el usuario seleccione un �lbum.
            listener = (OnAlbumSelectedListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz.
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnAlbumSeleccionadoListener");
        }
    }

    // Al terminar de crearse la actividad completa.
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Se crea el adaptador con los datos correspondientes y se le asigna a
        // la lista.
        adaptador = new AlbumesAdapter(this.getActivity(), getDatos());
        setListAdapter(adaptador);
        // Si tenemos estado previo.
        if (savedInstanceState != null) {
            // Obtengo la posici�n de la lista en la que se encontraba (por
            // defecto, el primero).
            elemSeleccionado = savedInstanceState.getInt("elemSeleccionado", 0);
        }
        // Ponemos el modo de la lista en selecci�n simple, para que el
        // elemento seleccionado quede marcado.
        this.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Mostramos el detalle del elemento seleccionado, que inicialmente
        // ser� el primero.
        notificarSeleccion();
    }

    // Al hacer click sobre un elemento de la lista.
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Se actualiza el elemento seleccionado en la lista.
        elemSeleccionado = position;
        // Se notifica al listener el cambio de selecci�n.
        notificarSeleccion();
    }

    // Notifica al listener que se ha seleccionado un elemento en la lista.
    private void notificarSeleccion() {
        // Establecemos que dicho elemento de la lista ha sido seleccionado.
        this.getListView().setItemChecked(elemSeleccionado, true);
        // Llama al m�todo onAlbumSelected del listener que debe ser
        // informado cuando se seleccione un �lbum.
        if (listener != null) {
            listener.onAlbumSelected((Album) this.getListView()
                    .getItemAtPosition(elemSeleccionado));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("elemSeleccionado", elemSeleccionado);
    }

    // Creo los datos para la lista.
    private ArrayList<Album> getDatos() {
        ArrayList<Album> albumes = new ArrayList<Album>();
        albumes.add(new Album(R.drawable.veneno, "Veneno", "1977"));
        albumes.add(new Album(R.drawable.mecanico, "Ser� mec�nico por ti",
                "1981"));
        albumes.add(new Album(R.drawable.cantecito, "Echate un cantecito",
                "1992"));
        albumes.add(new Album(R.drawable.carinio,
                "Est� muy bien eso del cari�o", "1995"));
        albumes.add(new Album(R.drawable.paloma, "Punta Paloma", "1997"));
        albumes.add(new Album(R.drawable.puro, "Puro Veneno", "1998"));
        albumes.add(new Album(R.drawable.pollo, "La familia pollo", "2000"));
        albumes.add(new Album(R.drawable.ratito, "Un ratito de gloria", "2001"));
        albumes.add(new Album(R.drawable.hombre, "El hombre invisible", "2005"));
        return albumes;
    }

}
