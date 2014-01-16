package es.iessaladillo.pedrojoya.pr084;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

//Clase que gestiona la cach� de im�genes en memoria.
public class BitmapMemCache extends LruCache<String, Bitmap> implements
        ImageCache {

    // Constructores.
    public BitmapMemCache() {
        // Se llama al otro constructor con el tama�o en KB, calculado como el
        // m�ximo posible para la JVM.
        this((int) (Runtime.getRuntime().maxMemory() / 1024) / 8);
    }

    public BitmapMemCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    // Retorna el tama�o en KB de una imagen de la cach�.
    // Recibe la key del elemento y la imagen.
    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        return bitmap.getByteCount() / 1024;
    }

    // Retorna si existe en la cach� una imagen con esa clave.
    public boolean contains(String key) {
        return get(key) != null;
    }

    // Retorna la imagen correspondiente a una clave.
    public Bitmap getBitmap(String key) {
        return get(key);
    }

    // Escribe una imagen en la cach�.
    // Utiliza la url como clave.
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

}