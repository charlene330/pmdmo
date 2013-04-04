package es.iessaladillo.pedrojoya.pr049.interfaces;

import es.iessaladillo.pedrojoya.pr049.modelos.Album;

// Interfaz para la comunicaci�n entre fragmentos, cuando se selecciona un
// �lbum.
public interface OnAlbumSelectedListener {

    // Cuando se selecciona un �lbum.
    public void onAlbumSelected(Album album);

}
