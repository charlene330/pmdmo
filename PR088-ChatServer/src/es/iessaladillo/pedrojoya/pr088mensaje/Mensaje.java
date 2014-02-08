package es.iessaladillo.pedrojoya.pr088mensaje;

import java.io.Serializable;

// MUY IMPORTANTE: LA CLASE MENSAJE TIENE QUE ESTAR EN EL MISMO PAQUETE EN 
// AMBOS PROYECTOS PARA QUE LA SERIALIZACI�N FUNCIONE CORRECTAMENTE.

// Clase modelo de datos del mensaje que se enviar� a trav�s del socket.
// Debe implementar la interfaz Serializable.
@SuppressWarnings("serial")
public class Mensaje implements Serializable {

    // Propiedades.
    private String texto; // Texto enviado.
    private boolean finalConexion = false; // Indicador de final de la conexi�n.

    // Constructor.
    public Mensaje(String texto, boolean finalConexion) {
        this.texto = texto;
        this.finalConexion = finalConexion;
    }

    // Getters and setters.
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isFinalConexion() {
        return finalConexion;
    }

    public void setFinalConexion(boolean finalConexion) {
        this.finalConexion = finalConexion;
    }

}