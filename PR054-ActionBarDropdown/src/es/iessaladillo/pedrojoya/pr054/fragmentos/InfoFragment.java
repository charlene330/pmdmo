package es.iessaladillo.pedrojoya.pr054.fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import es.iessaladillo.pedrojoya.pr054.R;

public class InfoFragment extends Fragment implements OnClickListener {

    // Vistas.
    private TextView lblGusta;
    private int mGusta = 0;

    // Al crear el fragmento.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // El fragmento se mantendr� en memoria entre cambios de configuraci�n.
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
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
        // Se obtienen e inicializan las vistas.
        getVistas();
        super.onActivityCreated(savedInstanceState);
    }

    // Obtiene e inicializa las vistas.
    private void getVistas() {
        ((ImageButton) getView().findViewById(R.id.btnGusta))
                .setOnClickListener(this);
        lblGusta = (TextView) getView().findViewById(R.id.lblGusta);
        lblGusta.setText(mGusta + "");
    }

    // Cuando se hace click sobre el bot�n.
    @Override
    public void onClick(View v) {
        // Se actualiza el contador.
        lblGusta.setText(++mGusta + "");
    }

}
