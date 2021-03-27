package co.com.poli.socket.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Gestiona la conexión del socket cliente contra socket servidor
 *
 * @author Jonathan Castelblanco Higuera <jocastelblanco3@poligran.edu.co>
 */
public class SocketCliente {

   private Socket socket;
   private final String ip;
   private final int puerto;
   private PrintWriter bufferEnvio;
   private BufferedReader bufferRecepcion;

   public SocketCliente(String ip, int puerto) {
      this.ip = ip;
      this.puerto = puerto;
   }

   /**
    * Se conecta al socket servidor creando los buffers para enviar y recibir datos
    *
    * @throws IOException
    */
   public void conectar() throws IOException {
      this.desconectar();
      System.out.println("Conectando...");
      socket = new Socket(ip, puerto);
      bufferEnvio = new PrintWriter(socket.getOutputStream(), true);
      bufferRecepcion = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println("¡Conectado!");
   }

   /**
    * @throws IOException
    */
   public void desconectar() throws IOException {
      if (socket != null) {
         System.out.println("Desconectando...");
         bufferRecepcion.close();
         bufferEnvio.close();
         socket.close();
         socket = null;
         System.out.println("¡Desconectado!");
      }
   }

   /**
    * Envía un mensaje al socket servidor y lee la respuesta entregada
    *
    * @param mensaje
    * @return
    * @throws IOException
    */
   public String enviarRecibirMensaje(String mensaje) throws IOException {
      if (socket == null) {
         this.conectar();
      }
      System.out.println("Enviando mensaje:" + mensaje);
      bufferEnvio.println(mensaje);
      System.out.println("Recibiendo respuesta...");
      return bufferRecepcion.readLine();
   }

}
