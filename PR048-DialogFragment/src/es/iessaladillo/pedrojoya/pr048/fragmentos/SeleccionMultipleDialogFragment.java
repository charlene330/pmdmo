package es.iessaladillo.pedrojoya.pr048.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import es.iessaladillo.pedrojoya.pr048.R;

public class SeleccionMultipleDialogFragment extends DialogFragment {

	private Dialog dialogo = null;
	private SeleccionMultipleDialogListener listener = null;
	public boolean[] optionIsChecked;

	// Interfaz p�blica para comunicaci�n con la actividad.
	public interface SeleccionMultipleDialogListener {
		public void onNeutralButtonClick(DialogFragment dialog,
				boolean[] optionIsChecked);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder b = new AlertDialog.Builder(this.getActivity());
		b.setTitle(R.string.turno);
		optionIsChecked = new boolean[] { true, false, false };
		b.setMultiChoiceItems(R.array.turnos, optionIsChecked,
				new OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						optionIsChecked[which] = isChecked;
					}
				});
		b.setIcon(R.drawable.ic_launcher);
		b.setNeutralButton(R.string.aceptar, new OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int arg1) {
				// Cierro el di�logo.
				d.dismiss();
				// Notifico al listener.
				listener.onNeutralButtonClick(
						SeleccionMultipleDialogFragment.this, optionIsChecked);
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
			listener = (SeleccionMultipleDialogListener) activity;
		} catch (ClassCastException e) {
			// La actividad no implementa la interfaz, se lanza excepci�n.
			throw new ClassCastException(activity.toString()
					+ " debe implementar SeleccionSimpleDialogListener");
		}
	}

}
