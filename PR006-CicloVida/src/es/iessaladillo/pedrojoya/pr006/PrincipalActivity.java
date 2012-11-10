package es.iessaladillo.pedrojoya.pr006;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PrincipalActivity extends Activity {
 
	// Variables a nivel de clase.
	StringBuilder listado = new StringBuilder();	// Constructor de cadenas.
	TextView lblListado;	// TextView donde se mostrar� el listado de m�todos.
	
	// Env�a un mensaje de depuraci�n
	private void escribirLog (String metodo) {
		// Env�o el log de depuraci�n.
		Log.d("Proyecto Ciclo de Vida", metodo);
		// A�ado a la cadena el nuevo m�todo.
		listado.append(metodo);
		listado.append('\n');
		// Muestro el nuevo listado en el TextView.
		lblListado.setText(listado.toString());
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// Llamo al m�todo del padre.
    	super.onCreate(savedInstanceState);
    	// Muestro el layout.
        setContentView(R.layout.main);
        // Obtengo la referencia al TextView.
        lblListado = (TextView) this.findViewById(R.id.lblListado);
        // Env�o el mensaje de depuraci�n.
        escribirLog("onCreate");
    }

	@Override
	protected void onDestroy() {
		// Llamo al m�todo del padre.
		super.onDestroy();
        // Env�o el mensaje de depuraci�n.
        escribirLog("onDestroy");
	}

	@Override
	protected void onPause() {
		// Llamo al m�todo del padre.
		super.onPause();
        // Env�o el mensaje de depuraci�n.
        escribirLog("onPause");		
	}

	@Override
	protected void onRestart() {
		// Llamo al m�todo del padre.
		super.onRestart();
	    // Env�o el mensaje de depuraci�n.
        escribirLog("onRestart");		
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// Llamo al m�todo del padre.
		super.onRestoreInstanceState(savedInstanceState);
	    // Env�o el mensaje de depuraci�n.
        escribirLog("onRestoreInstanceState");		
	}

	@Override
	protected void onResume() {
		// Llamo al m�todo del padre.
		super.onResume();
	    // Env�o el mensaje de depuraci�n.
        escribirLog("onResume");		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Llamo al m�todo del padre.
		super.onSaveInstanceState(outState);
	    // Env�o el mensaje de depuraci�n.
        escribirLog("onSaveInstanceState");		
	}

	@Override
	protected void onStart() {
		// Llamo al m�todo del padre.
		super.onStart();
	    // Env�o el mensaje de depuraci�n.
        escribirLog("onStart");		
	}

	@Override
	protected void onStop() {
		// Llamo al m�todo del padre.
		super.onStop();
	    // Env�o el mensaje de depuraci�n.
        escribirLog("onStop");		
	}
    
    
}