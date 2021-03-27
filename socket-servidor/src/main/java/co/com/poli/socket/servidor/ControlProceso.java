package co.com.poli.socket.servidor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Controla las operaciones a realizar sobre el archivo de cuentas
 *
 * @author Jonathan Castelblanco Higuera <jocastelblanco3@poligran.edu.co>
 */
public class ControlProceso {

   public static final String PROCESADO_OK = "OK";
   public static final String PROCESADO_FAIL = "NO-OK";
   public static final String SEPARADOR = ";";

   private final String rutaArchivoCuentas;

   public ControlProceso(String rutaArchivoCuentas) {
      this.rutaArchivoCuentas = rutaArchivoCuentas;
   }

   /**
    * Evalua la acción a realizar con el mensaje enviado, que puede ser CARGAR_CUENTA o CONSULTAR_SALDO_CUENTA.
    * Si hay un error/inconsistencia en la información suministrada retornará  <code>PROCESADO_FAIL</code>
    *
    * @param mensaje
    * @return <code>PROCESADO_FAIL</code> si hay error, <code>PROCESADO_OK</code> si no lo hay
    */
   public String evaluarResponderMensajeCliente(String mensaje) {
      mensaje = mensaje != null ? mensaje.trim() : "";
      try {
         if (mensaje.startsWith("CARGAR_CUENTA" + SEPARADOR) && mensaje.length() > 14) {
            String datos = mensaje.substring(15);
            Long numero = deducirNumeroCuenta(datos);
            BigDecimal valor = deducirValorCuenta(datos);
            if (valor == null) valor = new BigDecimal("0").setScale(0);
            System.out.println("Cargando cuenta número " + numero + " valor " + valor);
            almacenaDatosCuenta(numero, valor);
            return PROCESADO_OK;
         } else if (mensaje.startsWith("CONSULTAR_SALDO_CUENTA" + SEPARADOR)) {
            Long numero = Long.parseLong(mensaje.substring(24));
            System.out.println("Consultando cuenta " + numero);
            BigDecimal valor = buscarValorCuenta(numero);
            return valor != null ? valor.toPlainString() : PROCESADO_FAIL;
         } else {
            return PROCESADO_FAIL;
         }
      } catch (NumberFormatException ex) {
         ex.printStackTrace();
         return PROCESADO_FAIL;
      }
   }

   /**
    * Crea la cuenta con su valor si no existe.
    * Si existe la cuenta le actualiza su valor
    *
    * @param numero
    * @param valor
    * @return verdadero si se realizó exitosamente
    */
   public boolean almacenaDatosCuenta(Long numero, BigDecimal valor) {
      try {
         File archivo = buscarArchivo();
         Scanner lector = new Scanner(archivo);

         // Acumulador del contenido
         StringBuffer contenidoArchivo = new StringBuffer("");

         // Se recorre el archivo para saber si ya existe la cuenta en este
         BigDecimal valorActualExistente = null;

         // Validamos que tenga lineas el archivo
         while (lector.hasNextLine()) {
            // Valida que la linea tenga contenido
            String datosCuenta = lector.nextLine();
            System.out.println("almacenaDatosCuenta datosCuenta:" + datosCuenta);
            if (datosCuenta != null && datosCuenta.trim().length() > 0) {
               // Acumula el contenido
               contenidoArchivo.append(datosCuenta);
               contenidoArchivo.append('\n');

               // Valida si ya hemos encontrado o no la cuenta
               if (valorActualExistente == null) {
                  Long numeroEncontrado = deducirNumeroCuenta(datosCuenta);
                  if (numeroEncontrado.compareTo(numero) == 0) {
                     valorActualExistente = deducirValorCuenta(datosCuenta);
                  }
               }
            }
         }
         lector.close();

         // Si existe la cuenta se modifica la linea donde se encuentra
         String contenidoNuevo = "";
         if (valorActualExistente != null) {
            BigDecimal nuevoValor = valorActualExistente.add(valor);
            contenidoNuevo = contenidoArchivo
                    .toString()
                    .replaceFirst(numero.toString() + SEPARADOR + ".*", numero.toString() + SEPARADOR + nuevoValor.toPlainString());
         } else {
            // Si no existe se agrega una linea nueva al archivo
            contenidoNuevo = contenidoArchivo.append(numero.toString() + SEPARADOR + valor.toPlainString()).append("\n").toString();
         }

         FileOutputStream fileOut = new FileOutputStream(archivo);
         fileOut.write(contenidoNuevo.getBytes());
         fileOut.close();

         return true;
      } catch (IOException ex) {
         ex.printStackTrace();
         return false;
      }
   }

   /**
    * Busca en el archivo el valor de la cuenta enviada
    *
    * @param numero
    * @return valor. <code>null</code> si no existe la cuenta
    */
   public BigDecimal buscarValorCuenta(Long numero) {
      try {
         File archivo = buscarArchivo();
         Scanner lector = new Scanner(archivo);

         // Validamos que tenga lineas el archivo
         while (lector.hasNextLine()) {
            String datosCuenta = lector.nextLine();
            System.out.println("buscarValorCuenta datosCuenta:" + datosCuenta);
            // Valida que la linea tenga contenido
            if (datosCuenta != null && datosCuenta.trim().length() > 0) {
               Long numeroEncontrado = deducirNumeroCuenta(datosCuenta);
               if (numeroEncontrado.compareTo(numero) == 0) {
                  lector.close();
                  return deducirValorCuenta(datosCuenta);
               }
            }
         }
         lector.close();
      } catch (FileNotFoundException ex) {
         ex.printStackTrace();
         return null;
      }
      return null;
   }

   /**
    * Busca el archivo, y si no existe lo crea
    *
    * @return archivo
    */
   private File buscarArchivo() {
      try {
         File archivoCuentas = new File(this.rutaArchivoCuentas);
         if (!archivoCuentas.exists()) {
            archivoCuentas.createNewFile();
         }
         return archivoCuentas;
      } catch (IOException ex) {
         ex.printStackTrace();
         return null;
      }
   }

   /**
    * Deduce el número de cuenta de una linea con el formato <code>numero:valor</code>
    *
    * @param linea
    * @return número de la cuenta
    */
   private static Long deducirNumeroCuenta(String linea) {
      if (linea.contains(SEPARADOR)) {
         return Long.parseLong(linea.substring(0, linea.indexOf(SEPARADOR)));
      }
      return null;
   }

   /**
    * Deduce el valor de la cuenta de una linea con el formato <code>numero:valor</code>
    *
    * @param linea
    * @return valor de la cuenta
    */
   private static BigDecimal deducirValorCuenta(String linea) {
      if (linea.contains(SEPARADOR) && linea.length() > linea.indexOf(SEPARADOR) + 1) {
         return new BigDecimal(linea.substring(linea.indexOf(SEPARADOR) + 1));
      }
      return null;
   }

}
