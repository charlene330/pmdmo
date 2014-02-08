package es.iessaladillo.pedrojoya.pr087servermulti;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Servidor {

    // Constantes.
    final int PUERTO = 8888;

    // Variables a nivel de clase.
    private ServerSocket skServidor;
    private ThreadPoolExecutor ejecutor;

    // Constructor.
    public Servidor() {
        ejecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        try {
            // Se crea el socket servidor.
            skServidor = new ServerSocket(PUERTO);
            System.out.println("************ SERVIDOR ****************");
            System.out.println("Escuchando el puerto " + PUERTO);
            System.out.println("En Espera....");
            // Bucle de conexi�n de clientes.
            while (true) {
                // Se acepta una conexi�n con un cliente (bloqueante).
                Socket skConexion = skServidor.accept();
                // Se env�a al ejecutor la tarea de comunicaci�n con el cliente.
                ejecutor.execute(new ComunicacionCliente(skConexion));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // M�todo main.
    public static void main(String[] args) {
        new Servidor();
    }

}