package es.iessaladillo.pedrojoya.cursogalileotareasemana1.fragmentos;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import es.iessaladillo.pedrojoya.cursogalileotareasemana1.R;

public class InfoTiendaFragment extends Fragment {

    private ImageView btnLlamar;
    private TextView lblTelefono;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Se infla el layout.
        View v = inflater.inflate(R.layout.fragment_tienda_info, container,
                true);
        getVistas(v);
        return v;
    }

    private void getVistas(View v) {
        btnLlamar = (ImageView) v.findViewById(R.id.btnLlamar);
        lblTelefono = (TextView) v.findViewById(R.id.lblTelefono);
        btnLlamar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnLlamarOnClick();
            }
        });

    }

    private void btnLlamarOnClick() {
        // Se crea un intent impl�cito para mostrar el dial, que recibe como
        // data la URI con el n�mero.
        String telefono = lblTelefono.getText().toString();
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + telefono));
        startActivity(i);
    }
}
