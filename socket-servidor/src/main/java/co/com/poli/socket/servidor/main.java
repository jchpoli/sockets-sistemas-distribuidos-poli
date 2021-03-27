package co.com.poli.socket.servidor;

import java.net.InetAddress;

/**
 * @author Jonathan Castelblanco Higuera <jocastelblanco3@poligran.edu.co>
 */
public class main {

   public static void main(String[] args) {
      try {
         String errorValidacion = null;
         if (args == null || args.length < 1) {
            errorValidacion = "No se envíaron argumentos, es necesario el puerto";
         } else if (args[0] == null || args[0].trim().length() == 0) {
            errorValidacion = "No se envío un puerto";
         }
         if (errorValidacion != null) {
            System.out.println(errorValidacion);
            System.exit(1);
         }
         int puerto = Integer.parseInt(args[0].trim());
         String pathArchivo = "";
         if (args.length == 2 && args[1].trim().length() > 0) {
            pathArchivo = args[1].trim();
         } else {

            String jarPath = main.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            pathArchivo = jarPath.substring(0, jarPath.lastIndexOf("/") + 1) + "cuentas.csv";

         }

         System.out.println("Iniciando servidor en " + InetAddress.getLocalHost() + ":" + puerto);
         SocketServidor socketServidor = new SocketServidor(puerto);
         ControlProceso controlProceso = new ControlProceso(pathArchivo);
         System.out.println("Archivo de cuentas:" + pathArchivo);

         while (true) {
            socketServidor.escucharCliente();
            String mensaje = socketServidor.leerMensajeClienteActual();
            String respuesta = controlProceso.evaluarResponderMensajeCliente(mensaje);
            socketServidor.enviarMensajeClienteActual(respuesta);
            socketServidor.cerrarConexionClienteActual();
         }
      } catch (Exception ex) {
         ex.printStackTrace();
         System.exit(1);
      }
   }
}
