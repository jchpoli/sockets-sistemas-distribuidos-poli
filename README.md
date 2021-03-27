# sockets-sistemas-distribuidos-poli

## Socket Server

El archivo ejecutable se encuentra en la carpeta _socket-servidor/target/_ y para ejecutarlo utilizar el comando:
```java -jar socket-servidor-1.0-SNAPSHOT.jar 127.0.0.1 <puerto> <ruta-archivo-cuentas>```

**Ejemplo:** ```java -jar socket-servidor-1.0-SNAPSHOT.jar 5200```

El parámetro _ruta-archivo-cuentas_ no es obligatorio, por defecto creará el archivo con el nombre "cuentas.csv" en el directorio donde esté el ejecutable.

## Socket Client

El archivo ejecutable se encuentra en la carpeta _socket-cliente/target/_ y para ejecutarlo utilizar el comando:
```java -jar socket-cliente-1.0-SNAPSHOT.jar <direccion_ip_servidor> <puerto_servidor>```

**Ejemplo:** ```java -jar socket-cliente-1.0-SNAPSHOT.jar 127.0.0.1 5200```
