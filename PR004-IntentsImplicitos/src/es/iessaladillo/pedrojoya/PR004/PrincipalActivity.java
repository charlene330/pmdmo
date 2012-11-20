package es.iessaladillo.pedrojoya.PR004;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PrincipalActivity extends Activity {

	// Constantes.
	private final int RC_HACER_FOTO = 1; // Result Code intent Hacer Foto
	
	// Cuando se crea la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /** Manejador de los clicks de los botones. Indicado en la prop. onClick de cada bot�n.
	 * @param v Bot�n sobre el que se ha hecho click.
	 */
	public void manejarClick(View v) {
		// Intent impl�cito que ser� enviado.
		Intent intencion;
		// Dependiendo del bot�n pulsado, construyo el intent adecuado y llamo al componente.
		switch(v.getId()) {
		case R.id.btnNavegar:
			/* Construye el intent pas�ndole la acci�n y la URI de los datos.
			 * Para obtener URI utiliza el m�todo est�tico parse de la clase Ui para 
			 * obtener el objeto Uri a partir de una cadena de caracteres que 
			 * especifica una uri.
			 */
			// Acci�n--> VER. Uri--> URL.
			intencion = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.iessaladillo.es"));
			// Env�o el mensaje de intenci�n para que lo reciba un componente Activity.
			startActivity(intencion);
			break;
		case R.id.btnBuscar:
			// Acci�n--> BUSCAR EN INTERNET. Extra -> T�rmino de consulta.
			intencion = new Intent(Intent.ACTION_WEB_SEARCH);
			intencion.putExtra(SearchManager.QUERY, "IES Saladillo");
			startActivity(intencion);
			break;
		case R.id.btnLlamar:
			// Acci�n--> LLAMAR. Uri--> tel:num.
			intencion = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)123456789"));
			startActivity(intencion);
			break;
		case R.id.btnMarcar:
			// Acci�n--> MARCAR. Uri--> tel:num.
			intencion = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+34)12345789"));
			startActivity(intencion);
			break;
		case R.id.btnMostrarMapa:
			// Acci�n--> VER. Uri--> geo:latitud,longitud?z=zoom.
			intencion = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:50.123,7.1434?z=19"));
			startActivity(intencion);
			break;
		case R.id.btnBuscarMapa:
			// Acci�n--> VER. Uri--> geo:latitud,longitud?q=consulta.
			intencion = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=duque de rivas, Algeciras"));
			startActivity(intencion);
			break;
		case R.id.btnHacerFoto:
			/* Al crear el intent se indica s�lo lo acci�n (captura de imagen), 
			 * que est� definida en el paquete android.media.action
			 */
			intencion = new Intent("android.media.action.IMAGE_CAPTURE");
			/* Se realiza el env�o el intent, pero indicando que deberemos recibir
			 * una respuesta (as�ncrona).
			 */
			this.startActivityForResult(intencion, RC_HACER_FOTO);
			break;
		case R.id.btnMostrarContactos:
			// Acci�n--> VER. Uri--> Acceder� al proveedor de contenidos de la aplicaci�n de contactos.
			intencion = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"));
			startActivity(intencion);
			break;
		}
	}

	/* Cuando est� disponible el resultado de una actividad llamada.
	 * Se recibe el c�digo con el que se llamo a la actividad,
	 * el c�digo de respuesta (si ha ido bien o mal),
	 * y un intent con los datos retornados por la actividad llamada.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Si retorna un resultado correcto.
		if (resultCode == Activity.RESULT_OK) {
			// Dependiendo de la actividad llamada que ha retornado el resultado.
			switch(requestCode) {
			case RC_HACER_FOTO:
				/* Obtengo una cadena con la URI de la foto realizada
				 * y la muestro en un Toast.
				 */
				String uriFoto = data.toURI();
				Toast.makeText(this, this.getString(R.string.uri_foto) + ": " + uriFoto, Toast.LENGTH_LONG).show();
			}
		}
	}
}