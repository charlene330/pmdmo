package es.iessaladillo.pedrojoya.pr018;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

    // Constantes.
    private static final String ESTADO_EFECTO = "mEfecto";
    private static final String ESTADO_FOTO = "mFotoBitmap";

    // Variables
    private Bitmap mFotoBitmapOriginal;

    // Vistas.
    private ImageView imgFoto;
    int mEfecto = R.id.mnuOriginal;

    // Cuando se crea la actividad.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se obtienen e inicializan las vistas.
        getVistas();
        // Se obtiene el bitmap de la foto.
        mFotoBitmapOriginal = BitmapFactory.decodeResource(getResources(),
                R.drawable.bench);
        // Se recupera el estado anterior (efecto y foto resultante).
        if (savedInstanceState != null) {
            mEfecto = savedInstanceState.getInt(ESTADO_EFECTO);
            imgFoto.setImageBitmap((Bitmap) savedInstanceState
                    .getParcelable(ESTADO_FOTO));
        }
    }

    // Obtiene e inicializa las vistas.
    private void getVistas() {
        imgFoto = (ImageView) findViewById(R.id.imgFoto);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Se almacena en el bundle el efecto aplicado y la foto resultante (la
        // clase Bitmap implementa la interfaz Parcelable).
        outState.putInt(ESTADO_EFECTO, mEfecto);
        outState.putParcelable(ESTADO_FOTO,
                ((BitmapDrawable) imgFoto.getDrawable()).getBitmap());
        super.onSaveInstanceState(outState);
    }

    // Cuando se crea el men� de opciones.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se inflo el men� a partir de la especificaci�n XML.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // Se da la posibilidad al sistema de agregar �tems al men�.
        return super.onCreateOptionsMenu(menu);
    }

    // Antes de mostrar el men�.
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Se habilitan todos los �tems del men�.
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setEnabled(true);
        }
        // Se deshabilita el �tem correspondiente al efecto actual.
        if (mEfecto != 0) {
            menu.findItem(mEfecto).setEnabled(false);
        }
        // Se retorna lo que retorne la llamada al padre.
        return super.onPrepareOptionsMenu(menu);
    }

    // Cuando se pulsa un elemento del men�.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Dependiendo del item pulsado realizo la acci�n deseada.
        switch (item.getItemId()) {
        case R.id.mnuOriginal:
            imgFoto.setImageBitmap(mFotoBitmapOriginal);
            break;
        case R.id.mnuGrises:
            imgFoto.setImageBitmap(aEscalaGrises(mFotoBitmapOriginal));
            break;
        case R.id.mnuSepia:
            imgFoto.setImageBitmap(aSepia(mFotoBitmapOriginal));
            break;
        case R.id.mnuAzulado:
            imgFoto.setImageBitmap(aAzulado(mFotoBitmapOriginal));
            break;
        case R.id.mnuVerdoso:
            imgFoto.setImageBitmap(aVerdoso(mFotoBitmapOriginal));
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        // Se almacena el id del men� del efecto actual.
        mEfecto = item.getItemId();
        // Se retorna que ya se ha gestionado el evento.
        return true;
    }

    // Retorna el bitmap recibido en tono verdoso.
    private Bitmap aVerdoso(Bitmap src) {
        return efectoTono(src, 120, 0.1, 0.6, 0.1);
    }

    // Retorna el bitmap recibido en tono azulado.
    private Bitmap aAzulado(Bitmap src) {
        return efectoTono(src, 120, 0.1, 0.1, 0.6);
    }

    // Retorna el bitmap recibido en tono sepia.
    private Bitmap aSepia(Bitmap src) {
        return efectoTono(src, 120, 0.6, 0.4, 0.2);
    }

    // Retorna el bitmap recibido en escala de grises.
    private Bitmap aEscalaGrises(Bitmap src) {
        // Constantes de factores.
        final double FACTOR_ROJO = 0.299;
        final double FACTOR_VERDE = 0.587;
        final double FACTOR_AZUL = 0.114;
        // Se obtiene la anchura y altura de la imagen de origen.
        int anchura = src.getWidth();
        int altura = src.getHeight();
        // Se crea el bitmap de salida a partir del de origen.
        Bitmap bmSalida = Bitmap.createBitmap(anchura, altura, src.getConfig());
        // Se procesa la imagen p�xel a p�xel.
        int pixel, transparencia, rojo, verde, azul;
        for (int x = 0; x < anchura; ++x) {
            for (int y = 0; y < altura; ++y) {
                // Se obtiene el pixel y su info de transparencia y color.
                pixel = src.getPixel(x, y);
                transparencia = Color.alpha(pixel);
                rojo = Color.red(pixel);
                verde = Color.green(pixel);
                azul = Color.blue(pixel);
                // Se realiza la conversi�n a un �nico color proporcional
                // (gris).
                rojo = verde = azul = (int) (FACTOR_ROJO * rojo + FACTOR_VERDE
                        * verde + FACTOR_AZUL * azul);
                // Se escribe el nuevo pixel en el bitmap de salida.
                bmSalida.setPixel(x, y,
                        Color.argb(transparencia, rojo, verde, azul));
            }
        }
        // Se retorna el bitmap de salida.
        return bmSalida;
    }

    // Retorna el bitmap recibo aplic�ndole el cambio de tono dado por la
    // intensidad recibida y el factor de cada color.
    public static Bitmap efectoTono(Bitmap src, int intensidad,
            double factorIntesidadRojo, double factorIntensidadVerde,
            double factorIntensidadAzul) {
        // Constantes de factores.
        final double FACTOR_ROJO = 0.3;
        final double FACTOR_VERDE = 0.59;
        final double FACTOR_AZUL = 0.11;
        // Se obtiene la anchura y altura de la imagen de origen.
        int anchura = src.getWidth();
        int altura = src.getHeight();
        // Se crea el bitmap de salida a partir del de origen.
        Bitmap bmSalida = Bitmap.createBitmap(anchura, altura, src.getConfig());
        // Se procesa la imagen p�xel a p�xel.
        int pixel, transparencia, rojo, verde, azul;
        for (int x = 0; x < anchura; ++x) {
            for (int y = 0; y < altura; ++y) {
                // Se obtiene el pixel y su info de transparencia y color.
                pixel = src.getPixel(x, y);
                transparencia = Color.alpha(pixel);
                rojo = Color.red(pixel);
                verde = Color.green(pixel);
                azul = Color.blue(pixel);
                // Se realiza la conversi�n a un �nico color proporcional
                // (gris).
                rojo = verde = azul = (int) (FACTOR_ROJO * rojo + FACTOR_VERDE
                        * verde + FACTOR_AZUL * azul);
                // Se aplica el factor de intensidad a cada canal.
                rojo += (intensidad * factorIntesidadRojo);
                if (rojo > 255) {
                    rojo = 255;
                }
                verde += (intensidad * factorIntensidadVerde);
                if (verde > 255) {
                    verde = 255;
                }
                azul += (intensidad * factorIntensidadAzul);
                if (azul > 255) {
                    azul = 255;
                }
                // Se escribe el nuevo pixel en el bitmap de salida.
                bmSalida.setPixel(x, y,
                        Color.argb(transparencia, rojo, verde, azul));
            }
        }
        // Se retorna el bitmap de salida.
        return bmSalida;
    }

}
