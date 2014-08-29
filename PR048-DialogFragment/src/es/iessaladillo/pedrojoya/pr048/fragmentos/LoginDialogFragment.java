package es.iessaladillo.pedrojoya.pr048.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import es.iessaladillo.pedrojoya.pr048.R;

public class LoginDialogFragment extends DialogFragment {

    // Variables.
    private LoginDialogListener mListener = null;

    // Interfaz p�blica para comunicaci�n con la actividad.
    public interface LoginDialogListener {
        public void onConectarClick(DialogFragment dialog);

        public void onCancelarClick(DialogFragment dialog);
    }

    // Al crear el di�logo. Retorna el di�logo configurado.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
        b.setTitle(R.string.login);
        b.setView(LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_login, null));
        b.setPositiveButton(R.string.conectar, new OnClickListener() {
            // Al pulsar el bot�n positivo.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se notifica el evento al listener.
                mListener.onConectarClick(LoginDialogFragment.this);
            }
        });
        b.setNeutralButton(R.string.cancelar, new OnClickListener() {
            // Al pulsar el bot�n negativo.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se notifica el evento al listener.
                mListener.onCancelarClick(LoginDialogFragment.this);
            }
        });
        return b.create();
    }

    // Al enlazar el fragmento con la actividad.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Establece la actividad como listener de los eventos del di�logo.
        try {
            mListener = (LoginDialogListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz, se lanza excepci�n.
            throw new ClassCastException(activity.toString()
                    + " debe implementar SiNoDialogListener");
        }
    }

}
