package es.iessaladillo.pedrojoya.pr048.fragmentos;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import es.iessaladillo.pedrojoya.pr048.R;
import es.iessaladillo.pedrojoya.pr048.adaptadores.AdaptadorAlbumes;
import es.iessaladillo.pedrojoya.pr048.modelos.Album;

public class AdaptadorDialogFragment extends DialogFragment {

    private Dialog dialogo = null;
    private AdaptadorDialogListener listener = null;
    private ArrayList<Album> albumes;

    // Interfaz p�blica para comunicaci�n con la actividad.
    public interface AdaptadorDialogListener {
        public void onListItemClick(DialogFragment dialog, Album album);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        b.setTitle(R.string.album);
        b.setIcon(R.drawable.ic_launcher);
        // Creo el array de datos.
        albumes = new ArrayList<Album>();
        albumes.add(new Album(R.drawable.veneno, "Veneno", "1977"));
        albumes.add(new Album(R.drawable.mecanico, "Ser� mec�nico por ti",
                "1981"));
        albumes.add(new Album(R.drawable.cantecito, "�chate un cantecito",
                "1992"));
        albumes.add(new Album(R.drawable.carinio,
                "Est� muy bien eso del cari�o", "1995"));
        albumes.add(new Album(R.drawable.paloma, "Punta Paloma", "1997"));
        albumes.add(new Album(R.drawable.puro, "Puro Veneno", "1998"));
        albumes.add(new Album(R.drawable.pollo, "La familia pollo", "2000"));
        albumes.add(new Album(R.drawable.ratito, "Un ratito de gloria", "2001"));
        albumes.add(new Album(R.drawable.hombre, "El hombre invisible", "2005"));
        // Creo el adaptador.
        AdaptadorAlbumes adaptador = new AdaptadorAlbumes(this.getActivity(),
                albumes);
        b.setAdapter(adaptador, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Notifico al listener.
                listener.onListItemClick(AdaptadorDialogFragment.this,
                        albumes.get(which));
            }
        });
        dialogo = b.create();
        return dialogo;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Establece la actividad como listener de los eventos del di�logo.
        try {
            listener = (AdaptadorDialogListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, se lanza excepci�n.
            throw new ClassCastException(activity.toString()
                    + " debe implementar SeleccionDirectaDialogListener");
        }
    }

}
