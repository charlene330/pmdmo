package es.iessaladillo.pedrojoya.pr047.fragmentos;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import es.iessaladillo.pedrojoya.pr047.R;
import es.iessaladillo.pedrojoya.pr047.adaptadores.AlbumesAdapter;
import es.iessaladillo.pedrojoya.pr047.modelos.Album;

public class ListaFragment extends Fragment {

    // Interfaz para notificaci�n de eventos desde el fragmento.
    public interface OnAlbumSelectedListener {
        // Cuando se selecciona un �lbum.
        public void onAlbumSelected(Album album);
    }

    private ListView lstAlbumes;
    private AlbumesAdapter adaptador;
    private OnAlbumSelectedListener listener;

    // Retorna la vista que mostrar� el fragmento.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflo el layout del fragmento y retorno la vista correspondiente.
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    // Cuando se vincula el fragmento a la actividad.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // Establece que la propia actividad sea el listener al que
            // informar� el fragmento cuando el usuario seleccione un �lbum.
            listener = (OnAlbumSelectedListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz.
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnAlbumSeleccionadoListener");
        }
    }

    // Cuando se ha terminado de crear la actividad completa.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Siempre tenemos que llamar al padre.
        super.onActivityCreated(savedInstanceState);
        // Ya puedo crear el adaptador y asign�rselo al ListView.
        lstAlbumes = (ListView) this.getView().findViewById(R.id.lstAlbumes);
        adaptador = new AlbumesAdapter(this.getActivity(), getDatos());
        lstAlbumes.setAdapter(adaptador);
        // Cuando se haga click sobre un elemento del ListView.
        lstAlbumes.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // Llamo al m�todo onAlbumSelected del listener que debe ser
                // informado cuando se seleccione un �lbum.
                if (listener != null) {
                    listener.onAlbumSelected((Album) lstAlbumes
                            .getItemAtPosition(position));
                }
            }
        });
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
