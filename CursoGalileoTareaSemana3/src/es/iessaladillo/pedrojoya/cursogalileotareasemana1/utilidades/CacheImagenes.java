package es.iessaladillo.pedrojoya.cursogalileotareasemana1.utilidades;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class CacheImagenes extends LruCache<String, Bitmap> implements
        ImageCache {

    // Constantes.
    private static final int CACHE_SIZE_BYTES = 4 * 1024 * 1024; // 4 MB.

    public CacheImagenes() {
        // Se llama al constructor del padre pas�ndole el tama�o de cach�
        // deseado.
        super(CACHE_SIZE_BYTES);
    }

    @Override
    public Bitmap getBitmap(String key) {
        // Se retorna el bitmap correspondiente a la clave recibida, que se
        // obtiene de la cach�.
        return get(key);
    }

    @Override
    public void putBitmap(String key, Bitmap value) {
        // Se escribe en la cach� el bitmap recibido con la clave recibida.
        put(key, value);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        // Se retorna el tama�o en bytes del bitmap, correspondiente al n�mero
        // de bytes de cada fila por su n�mero de filas.
        return value.getRowBytes() * value.getHeight();
    }

}
