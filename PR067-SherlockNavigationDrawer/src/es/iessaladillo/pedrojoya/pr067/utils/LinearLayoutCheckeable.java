package es.iessaladillo.pedrojoya.pr067.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class LinearLayoutCheckeable extends LinearLayout implements Checkable {

	// Constantes.
	// Estados adicionales a los que deben reaccionar los drawables.
	private final int[] ESTADOS_ADICIONALES = { android.R.attr.state_checked };
	// Variables miembro.
	boolean checked = false;

	// Constructores.
	public LinearLayoutCheckeable(Context context) {
		super(context);
	}

	public LinearLayoutCheckeable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// Retorna si est� checkeado o no.
	@Override
	public boolean isChecked() {
		return checked;
	}

	// Establece el estado de checked.
	@Override
	public void setChecked(boolean estado) {
		checked = estado;
		// Informo del cambio de estado a los drawables.
		refreshDrawableState();
		// Invalido el estado para que los drawable se repinten.
		invalidate();
	}

	// Cambia el estado de checked.
	@Override
	public void toggle() {
		setChecked(!checked);
	}

	// Al crear los estados de los drawables
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		// Hago que los cree el padre a�adiendo al menos una posici�n extra
		// en el array para el estado que yo voy a a�adir.
		int[] estados = super.onCreateDrawableState(extraSpace + 1);
		// Si est� checkeado.
		if (isChecked()) {
			// Agrego el estado de checkeado.
			mergeDrawableStates(estados, ESTADOS_ADICIONALES);
		}
		// Retorno el array de estados.
		return estados;
	}

}
