package PSP;

import java.io.File;
import java.io.IOException;

/**
 * Ejercicio 6. Trabajar en otra carpeta: crear fichero.txt DENTRO de «pruebas» sin poner la ruta en el comando.
 *
 *   Linux   : touch fichero.txt
 *   Windows : cmd /c type nul > fichero.txt   (Windows no tiene touch; «type nul >» crea un fichero vacío)
 * La clave es directory(): fija la carpeta de trabajo del proceso hijo.
 */
public class Ejercicio6 {

    public static void main(String[] args) throws IOException, InterruptedException {
        File carpeta = new File("pruebas");                      // la del ejercicio 4
        if (!carpeta.isDirectory()) {
            System.out.println("Primero ejecuta el Ejercicio4 para crear la carpeta «pruebas».");
            return;
        }
        String[] comando = SO.comando(
                new String[]{"touch", "fichero.txt"},
                new String[]{"cmd", "/c", "type nul > fichero.txt"});
        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.directory(carpeta);                                   // el hijo se ejecuta DENTRO de pruebas/
        int codigo = pb.start().waitFor();
        System.out.println("Código de salida: " + codigo);
        File creado = new File(carpeta, "fichero.txt");          // pruebas/fichero.txt
        System.out.println("¿Existe " + creado.getPath() + "?: " + creado.exists());
    }
}
