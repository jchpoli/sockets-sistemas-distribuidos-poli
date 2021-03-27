package co.com.poli.socket.cliente;

import java.io.IOException;
import java.util.Scanner;

/**
 * Controla las acciones realizadas por el cliente soportando las operaciones permitidas
 *
 * @author Jonathan Castelblanco Higuera <jocastelblanco3@poligran.edu.co>
 */
public class ControlProceso {

   private final Scanner consola;
   private SocketCliente socketCliente;
   private boolean conError = false;

   /**
    * Constructor que recibe el scanner, la ip y puerto del servidor
    *
    * @param consola
    * @param ip
    * @param puerto
    */
   public ControlProceso(Scanner consola, String ip, int puerto) {
      this.consola = consola;
      // Crea la instancia del objeto que maneja la conexión
      this.socketCliente = new SocketCliente(ip, puerto);
   }

   public boolean isConError() {
      return conError;
   }

   public void crearAgregarValor() {
      try {
         String numeroCuenta = null;
         String valor = null;
         boolean conError = false;
         do {
            if (conError) System.out.println("Ingresó un valores inválidos o no ingresó valores.");

            System.out.println("+-------------------Crear/agregar Valor a Cuenta-------------------+");
            System.out.print("Ingrese el número de la cuenta:");
            numeroCuenta = consola.nextLine();
            System.out.print("Ingrese el valor a agregar a la cuenta:");
            valor = consola.nextLine();
            conError = true;
         } while (numeroCuenta == null || valor == null || numeroCuenta.trim().length() == 0 || valor.trim().length() == 0);
         numeroCuenta = numeroCuenta.trim();
         valor = valor.trim();

         String respuesta = this.socketCliente.enviarRecibirMensaje("CARGAR_CUENTA;" + numeroCuenta + ";" + valor);
         System.out.println("El resultado de la operación es:" + respuesta);
         this.socketCliente.desconectar();
      } catch (IOException ex) {
         ex.printStackTrace();
         System.out.println("Ha ocurrido un error en la creación de la conexión:" + ex.getMessage());
         this.conError = true;
      }
   }

   public void consultarCuenta() {
      try {
         String numeroCuenta = null;
         boolean conError = false;
         do {
            if (conError) System.out.println("Ingresó un valor inválido o no ingresó valor.");

            System.out.println("+-------------------Consultar Valor de Cuenta-------------------+");
            System.out.print("Ingrese el número de la cuenta:");
            numeroCuenta = consola.nextLine();
            conError = true;
         } while (numeroCuenta == null || numeroCuenta.trim().length() == 0);
         numeroCuenta = numeroCuenta.trim();

         String respuesta = this.socketCliente.enviarRecibirMensaje("CONSULTAR_SALDO_CUENTA;" + numeroCuenta);
         System.out.println("El resultado de la operación es:" + respuesta);
         this.socketCliente.desconectar();
      } catch (IOException ex) {
         System.out.println("Ha ocurrido un error en la creación de la conexión:" + ex.getMessage());
         this.conError = true;
      }
   }

   public void terminarConexion() {
      try {
         this.socketCliente.desconectar();
      } catch (IOException ex) {
         System.out.println("Ha ocurrido un error al cerrar la conexión:" + ex.getMessage());
         this.conError = true;
      }
   }

}
