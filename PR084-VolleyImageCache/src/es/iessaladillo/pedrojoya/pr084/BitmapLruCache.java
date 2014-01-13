package es.iessaladillo.pedrojoya.pr084;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

// Cach� en memoria de im�genes.
public class BitmapLruCache extends LruCache<String, Bitmap> implements
        ImageCache {

    // Constructor. Recibe el tama�o m�ximo de cach�.
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    // Retorna el tama�o en bytes del elemento en cach� con dicha clave y valor.
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    // Retorna la imagen correspondiente a una clave (url).
    @Override
    public Bitmap getBitmap(String key) {
        return get(key);
    }

    // Escribe en la cach� una imagen y le asocia una clave (url).
    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        put(key, bitmap);
    }
}