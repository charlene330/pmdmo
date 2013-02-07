package es.iessaladillo.pedrojoya.imagencapturaescala;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	// Constantes.
	final static int RC_CAPTURAR_IMAGEN = 0; // C�digo solicitud Intent.

	// Variables a nivel de clase.
	private ImageView imgFoto;
	String imgPath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imgFoto = (ImageView) findViewById(R.id.imgFoto);
		if (savedInstanceState != null) {
			imgPath = savedInstanceState.getString("imgPath");
		}
	}

	// Al hacer click en el bot�n btnCapturar.
	public void btnCapturarOnClick(View v) {
		// Obtenemos el nombre del archivo con el que debe almacenarse la foto.
		imgPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/mifoto.jpg";
		// Creo el descriptor del archivo.
		File ficheroFoto = new File(imgPath);
		// Obtengo la URI del archivo a partir de su descriptor.
		Uri uriFoto = Uri.fromFile(ficheroFoto);
		// Creo el intent con la acci�n de captura de imagen.
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// Paso como dato extra del intent la uri debe debe almacenarse la foto.
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriFoto);
		// Env�o el intent esperando respuesta.
		startActivityForResult(i, RC_CAPTURAR_IMAGEN);
	}

	// Al retornar resultados.
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// Si todo ha ido bien y el c�digo de petici�n corresponde al de
		// captura.
		if (resultCode == RESULT_OK && requestCode == RC_CAPTURAR_IMAGEN) {
			mostrarImagen();
		}
	}

	// Escala y muestra la imagen en el visor.
	private void mostrarImagen() {
		// Escalo la imagen a 1/4 de su tama�o.
		BitmapFactory.Options opcionesImagen = new BitmapFactory.Options();
		opcionesImagen.inJustDecodeBounds = false;
		opcionesImagen.inSampleSize = 4;
		Bitmap imagen = BitmapFactory.decodeFile(imgPath, opcionesImagen);
		// Muestro la foto
		imgFoto.setImageBitmap(imagen);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Guardo la uri.
		outState.putString("imgPath", imgPath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		if (!imgPath.equals("")) {
			mostrarImagen();
		}
		super.onResume();
	}

}
