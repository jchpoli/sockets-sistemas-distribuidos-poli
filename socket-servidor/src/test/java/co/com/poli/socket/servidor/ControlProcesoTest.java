package co.com.poli.socket.servidor;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

/**
 * @author Jonathan Castelblanco Higuera <jocastelblanco3@poligran.edu.co>
 */
class ControlProcesoTest {
   static ControlProceso controlProceso;
   static String path = "src/test/resources/prueba.txt";

   @BeforeAll
   public static void config() {
      File f = new File(path);
      if (f.exists()) {
         f.delete();
      }

      controlProceso = new ControlProceso(path);
   }


   @Test
   @Order(3)
   void evaluarResponderMensajeCliente() throws IOException {
      String result = controlProceso.evaluarResponderMensajeCliente("CARGAR_CUENTA;12345;347000200");
      File f = new File(path);
      String contenido = "";
      if (f.exists()) {
         contenido = new String(Files.readAllBytes(f.toPath()));
      }
      System.out.println(contenido);
      Assertions.assertEquals(ControlProceso.PROCESADO_OK, result);
   }

   @Test
   @Order(1)
   void almacenaDatosCuenta() throws IOException {
      controlProceso.almacenaDatosCuenta(Long.valueOf("2101"), new BigDecimal("5400"));
      File f = new File(path);
      String contenido = "";
      if (f.exists()) {
         contenido = new String(Files.readAllBytes(f.toPath()));
      }
      System.out.println(contenido);
      Assertions.assertEquals("2101" + ControlProceso.SEPARADOR + "5400\n", contenido);
   }

   @Test
   @Order(2)
   void buscarValorCuenta() {
      BigDecimal result = controlProceso.buscarValorCuenta(Long.valueOf("2101"));
      System.out.println(result);
      Assertions.assertEquals(new BigDecimal("5400"), result);
   }
}