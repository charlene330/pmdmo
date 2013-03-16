package es.iessaladillo.pedrojoya.pr047.interfaces;

import es.iessaladillo.pedrojoya.pr047.modelos.Album;

/**
 * Interfaz para la comunicaci�n entre fragmentos, cuando se selecciona un
 * �lbum.
 * 
 */
public interface OnAlbumSelectedListener {

    /**
     * Cuando se selecciona un �lbum.
     * 
     * @param album
     */
    public void onAlbumSelected(Album album);

}
