package co.com.poli.socket.cliente;

import java.util.Scanner;

/**
 * @author Jonathan Castelblanco Higuera <jocastelblanco3@poligran.edu.co>
 */
public class main {

   private static Scanner CONSOLA;
   private static ControlProceso controlProceso;

   public static void main(String[] args) {
      System.out.println("Bienvenido al sistema de transacciones por Sockets");

      // Valida la información necesaria como IP y PUERTO
      String errorValidacion = null;
      if (args == null || args.length < 2) {
         errorValidacion = "No envío los dos parametros requeridos IP y Puerto";
      } else if (args[0] == null || args[0].trim().length() == 0) {
         errorValidacion = "No se envío una IP";
      } else if (args[1] == null || args[1].trim().length() <= 1) {
         errorValidacion = "No se envío un puerto";
      }
      if (errorValidacion != null) {
         System.out.println(errorValidacion);
         System.exit(1);
      }

      // Lee la IP y PUERTO para la conexión al servidor Socket
      String ip = args[0].trim();
      int puerto = Integer.parseInt(args[1].trim());

      // Crea un scanner para capturar datos desde la consola
      System.out.println("+-------------------------------------+");
      CONSOLA = new Scanner(System.in);
      try {
         // Crea una instancia de la clase que controla las acciones a realizar
         controlProceso = new ControlProceso(CONSOLA, ip, puerto);
         // Invoca método que consulta y ejecuta la acción a realizar indefinidamente
         solicitarEjecutaAccion();
      } catch (Exception ex) {
         ex.printStackTrace();
      } finally {
         // Nos aseguramos de cerrar las conexiones abiertas por seguridad
         if (controlProceso != null) controlProceso.terminarConexion();
      }
   }

   /**
    * Consulta y ejecuta la acción que va a realizar el cliente de manera recursiva si el cliente así lo quiere
    */
   private static void solicitarEjecutaAccion() {
      String accion = soliciarAccion();
      switch (accion) {
         case "a":
            controlProceso.crearAgregarValor();
            break;
         case "b":
            controlProceso.consultarCuenta();
            break;
         case "x":
            System.exit(0);
            break;
         default:
            System.exit(1);
      }

      // Si hay un error termina el programa porque posiblemente falló la conexión con el socketServer
      if (controlProceso.isConError()) {
         System.exit(1);
      }

      System.out.print("¿Desea volver al menú?(y/n)");
      String valor = CONSOLA.nextLine();
      if (valor != null && valor.toLowerCase().trim().equals("y")) {
         solicitarEjecutaAccion();
      }
   }

   /**
    * Presenta un menú y solicita la acción a realizar
    *
    * @return
    */
   private static String soliciarAccion() {
      String accion = null;
      boolean conError = false;
      String mensaje = "Menú:\na)Crear/agregar Valor a Cuenta.\nb)Consultar Valor de Cuenta.\nx)Salir.\nIngrese la accion a realizar:";
      do {
         if (conError) System.out.println("Ingresó un valor inválido o no ingresó valor.");
         System.out.println("+-------------------------------------+");
         System.out.print(mensaje);
         accion = CONSOLA.nextLine();
         System.out.println("valor ingresado:" + accion);
         conError = true;
      } while (accion == null || !(accion.toLowerCase().equals("a") || accion.toLowerCase().equals("b") || accion.toLowerCase().equals("x")));
      accion = accion.toLowerCase();
      if (accion.equals("x")) {
         System.exit(0);
      }
      return accion;
   }

}
