package PSP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Tarea 2 (PDF de teoría). ProcessCom: lanza el comando de args, muestra su salida y si hubo error.
 * Dos versiones de lectura: por BYTES y por LÍNEAS.
 *
 *   Linux   : java -cp bin PSP.Tarea2ProcessCom ping -c 3 google.com
 *   Windows : java -cp bin PSP.Tarea2ProcessCom ping -n 3 google.com
 * Sin argumentos usa un ping a localhost.
 */
public class Tarea2ProcessCom {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] comando = args.length > 0 ? args : SO.comando(
                new String[]{"ping", "-c", "2", "127.0.0.1"},
                new String[]{"ping", "-n", "2", "127.0.0.1"});

        System.out.println("===== Versión 1: lectura de BYTES =====");
        Process p1 = new ProcessBuilder(comando).start();
        leerBytes(p1.getInputStream());
        mostrarErrores(p1);
        System.out.println("Código de salida: " + p1.waitFor());

        System.out.println("\n===== Versión 2: lectura de LÍNEAS =====");
        Process p2 = new ProcessBuilder(comando).start();
        BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String linea;
        int n = 0;
        while ((linea = br.readLine()) != null) {      // readLine: hasta el salto de línea, sin incluirlo
            System.out.println(++n + ": " + linea);
        }
        br.close();
        mostrarErrores(p2);
        System.out.println("Código de salida: " + p2.waitFor());
    }

    /** Lee byte a byte: read() devuelve un int de 0 a 255, o -1 al final del flujo. */
    private static void leerBytes(InputStream in) throws IOException {
        int c;
        while ((c = in.read()) != -1) {
            System.out.print((char) c);                // ojo: con acentos en UTF-8 un carácter son 2 bytes
        }
        in.close();
    }

    private static void mostrarErrores(Process p) throws IOException {
        BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String linea;
        boolean hubo = false;
        while ((linea = err.readLine()) != null) {
            System.out.println("[ERROR] " + linea);
            hubo = true;
        }
        err.close();
        System.out.println(hubo ? "Hubo errores." : "Sin errores.");
    }
}
