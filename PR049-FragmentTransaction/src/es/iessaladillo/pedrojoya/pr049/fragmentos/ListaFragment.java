package es.iessaladillo.pedrojoya.pr049.fragmentos;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import es.iessaladillo.pedrojoya.pr049.R;
import es.iessaladillo.pedrojoya.pr049.adaptadores.ObrasAdapter;
import es.iessaladillo.pedrojoya.pr049.modelos.Obra;

public class ListaFragment extends Fragment implements OnItemClickListener {

    // Interfaz para notificaci�n de eventos desde el fragmento.
    public interface OnObraSelectedListener {
        // Cuando se selecciona una obra.
        public void onObraSelected(Obra obra, int position);
    }

    // Vistas.
    private ListView lstObras;
    private ImageView imgCabecera;

    // Variables.
    private ObrasAdapter mAdaptador;
    private OnObraSelectedListener mListener;
    private boolean mDosPaneles;
    private int mItemSeleccionado = 0;

    // Cuando se crea el fragmento.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Se mantendr� la instancia del fragmento al cambiar la configuraci�n.
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    // Retorna la vista que mostrar� el fragmento.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Se infla el layout del fragmento y se retorna la vista.
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    // Cuando se vincula el fragmento a la actividad.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // La actividad actuar� como listener cuando se seleccione una obra.
            mListener = (OnObraSelectedListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz necesaria.
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnObraSeleccionadoListener");
        }
    }

    // Cuando se desvincula el fragmento de la actividad.
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Cuando se ha terminado de crear la actividad al completo.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Se obtienen e inicializan las vistas.
        getVistas();
        // Se comprueba si existe el fragmento de detalle y por tanto se usan
        // dos paneles.
        FrameLayout flDetalle = (FrameLayout) getActivity().findViewById(
                R.id.flDetalle);
        mDosPaneles = flDetalle != null
                && flDetalle.getVisibility() == View.VISIBLE;
        // El fragmento actuar� como listener cuando se pulse sobre un elemento
        // de la lista.
        lstObras.setOnItemClickListener(this);
        if (mDosPaneles) {
            pulsarItem(mItemSeleccionado);
        } else {
            marcarObra(mItemSeleccionado);
        }
    }

    // Cuando se "pulsa" sobre un elemento. Recibe la posici�n.
    private void pulsarItem(int position) {
        // Se marca la obra seleccionada (por defecto la 0).
        marcarObra(position);
        // Se muestra el detalle de la obra.
        if (mListener != null) {
            // Se llama al m�todo correspondiente del listener.
            mListener.onObraSelected(
                    (Obra) lstObras.getItemAtPosition(position), position);
        }
    }

    // Marca la obra seleccionada. Recibe la obra.
    public void marcarObra(int position) {
        mItemSeleccionado = position;
        Obra obra = (Obra) lstObras.getItemAtPosition(mItemSeleccionado);
        lstObras.setItemChecked(mItemSeleccionado, true);
        lstObras.setSelection(mItemSeleccionado);
        imgCabecera.setImageResource(obra.getFotoResId());
        getActivity().setTitle(obra.getNombre());
    }

    // Obtiene e inicializa las vistas.
    private void getVistas() {
        imgCabecera = (ImageView) getView().findViewById(R.id.imgCabecera);
        // Se crea el adaptador y se asigna al ListView.
        lstObras = (ListView) getView().findViewById(R.id.lstObras);
        mAdaptador = new ObrasAdapter(this.getActivity(), getDatos());
        lstObras.setAdapter(mAdaptador);
    }

    // Retorna el ArrayList de datos para la lista.
    private ArrayList<Obra> getDatos() {
        ArrayList<Obra> obras = new ArrayList<Obra>();
        obras.add(new Obra(R.drawable.light_bulb, "La luz del mundo",
                "Simon Funk", 2011));
        obras.add(new Obra(R.drawable.beach, "Verano en la playa",
                "Simon Funk", 2005));
        obras.add(new Obra(R.drawable.bench, "Merecido descanso", "Simon Funk",
                2001));
        obras.add(new Obra(R.drawable.buildings, "Rascando el cielo",
                "Simon Funk", 2006));
        obras.add(new Obra(R.drawable.capitol, "La cara del poder",
                "Simon Funk", 2012));
        obras.add(new Obra(R.drawable.house, "En mitad de la nada",
                "Simon Funk", 2013));
        obras.add(new Obra(R.drawable.number4, "Fuego en el coraz�n",
                "Simon Funk", 2007));
        obras.add(new Obra(R.drawable.old_man, "Mirar para otro lado",
                "Simon Funk", 2000));
        return obras;
    }

    // Al pulsar sobre un elemento de la lista.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // Se pulsa sobre el item.
        pulsarItem(position);
    }

}
