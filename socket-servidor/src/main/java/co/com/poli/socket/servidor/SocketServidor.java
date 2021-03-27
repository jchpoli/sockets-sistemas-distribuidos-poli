package co.com.poli.socket.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestiona la conexión del socket servidor y las conexiónes de los clientes
 *
 * @author Jonathan Castelblanco Higuera <jocastelblanco3@poligran.edu.co>
 */
public class SocketServidor {

   private ServerSocket socket;
   private Socket socketCliente;
   private BufferedReader bufferRecepcion;
   private PrintWriter bufferEnvio;

   /**
    * Crea el socket del servidor publicándolo en el puerto establecido
    *
    * @param puerto
    * @throws IOException
    */
   public SocketServidor(int puerto) throws IOException {
      System.out.println("Creando socket servidor en el puerto " + puerto + "...");
      socket = new ServerSocket(puerto);
   }

   /**
    * Espera la conexión de un cliente (escucha/listen) y carga el socket del cliente de manera local
    *
    * @throws IOException
    */
   public void escucharCliente() throws IOException {
      System.out.println("Escuchando...");
      if (socketCliente != null) {
         socketCliente.close();
      }
      socketCliente = socket.accept();
   }

   /**
    * Lee el mensaje del cliente local/actual
    *
    * @return
    * @throws IOException
    */
   public String leerMensajeClienteActual() throws IOException {
      if (socketCliente != null) {
         bufferRecepcion = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
         StringBuffer stringBuffer = new StringBuffer("");
         String linea = bufferRecepcion.readLine();
         do {
            System.out.println("leyendo linea");
            stringBuffer.append(linea);
         } while (bufferRecepcion.ready() // Verifica que no se bloque si en la siguiente linea no hay nada
                 && (linea = bufferRecepcion.readLine()) != null);
         System.out.println("Mensaje obtenido:" + stringBuffer.toString());
         return stringBuffer.toString();
      }
      return null;
   }

   /**
    * Envía un mensaje al cliente local/actual
    *
    * @param mensaje
    * @throws IOException
    */
   public void enviarMensajeClienteActual(String mensaje) throws IOException {
      if (socketCliente != null) {
         bufferEnvio = new PrintWriter(socketCliente.getOutputStream(), true);
         bufferEnvio.println(mensaje);
      }
   }

   /**
    * Cierra la conexión con el cliente local
    *
    * @throws IOException
    */
   public void cerrarConexionClienteActual() throws IOException {
      if (socketCliente != null) {
         bufferEnvio.close();
         bufferRecepcion.close();
         socketCliente.close();
         socketCliente = null;
      }
   }


}
