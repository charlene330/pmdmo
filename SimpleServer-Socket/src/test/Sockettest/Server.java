package test.Sockettest;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	// Variables a nivel de clase.
	Socket skCliente;			// Socket del cliente.
	ServerSocket skServidor;	// Socket del servidor.
	final int PUERTO = 8888;	// Puerto de conexi�n (tanto para servidor como para cliente)
	String ipCliente;			// IP del cliente.
	Mensaje mensaje = null;		// Mensaje recibido.
	String timeStamp;			// C�digo de tiempo actual.

	// Constructor.
	Server() {
		try {
			// Mostramos info en la salida est�ndar.
			System.out.println("************ SERVIDOR ****************");
			// Creamos un socket servidor ServerSocket indicando el puerto de conexi�n.
			skServidor = new ServerSocket(PUERTO);
			// Mostramos info en la salida est�ndar.
			System.out.println("Escuchando el puerto " + PUERTO);
			System.out.println("En Espera....");
			try {
				// Hago que el socket servidor acepte una conexi�n,
				// obteniendo como resultado el socket cliente (se bloquea hasta que conecta).
				skCliente = skServidor.accept();
				// Obtengo el timeStamp (c�digo de tiempo) actual para la info.
				timeStamp = new java.util.Date().toString();
				// Una vez conectados, obtengo la IP del cliente.
				ipCliente = skCliente.getInetAddress().toString();
				// Muestro la infor de la IP en la salida est�ndar.
				System.out.println("[" + timeStamp + "] Conectado al cliente "
						+ "IP:" + ipCliente);
				// Bucle de recepci�n de mensajes desde el cliente.
				while (true) {
					// Creo el flujo de entrada de objetos a partir del flujo del propio socket de cliente.
					// A trav�s del socket se van a enviar objetos Mensaje.
					ObjectInputStream entrada = new ObjectInputStream(
							skCliente.getInputStream());
					// Leemos del flujo de entrada el mensaje enviado por el cliente.
					Object datos = entrada.readObject();
					// Si los datos recibidos son de la clase Mensaje
					if (datos instanceof Mensaje) {
						// Convertimos los datos en un mensaje (cast).
						mensaje = (Mensaje) datos;
						// Si no es el mensaje de final de conexi�n.
						if (!mensaje.finalConexion) {
							// Es un mensaje de texto. Muestro la info del mensaje
							System.out.println("[" + timeStamp + "] "
									+ "Mensaje de [" + ipCliente + "]--> "
									+ mensaje.texto);
						}
						else {
							// Si es el mensaje de fin de la conexi�n.
							// Cierro el socket, el flujo de entrada y muestro la info.
							skCliente.close();
							entrada.close();
							System.out
									.println("["
											+ timeStamp
											+ "] Last_msg detected Conexion cerrada, gracias vuelva pronto");
							// Salgo del bucle de recepci�n de mensajes.
							break;
						}
					} 
					else {
						// Informo de que los datos recibidos no son de la clase Mensaje.
						System.err.println("Los datos recibidos no son de la clase Mensaje.");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("[" + timeStamp + "] Error ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[" + timeStamp + "] Error ");
		}
	}

	// M�todo main.
	public static void main(String[] args) {
		new Server();
	}
}	