/*
 * ============================================================
 *  Alumno/a: José Antonio Pastor Rivera
 *  PSP · UT1 · Soluciones de los ejercicios de procesos
 * ============================================================
 */

package PSP;

import java.io.IOException;
import java.util.Scanner;

/**
 * Ejercicio 5. Comprobador de conexión con ping.
 *
 *   Linux   : {@code ping -c 1 <host>}   (-c = count: número de paquetes)
 *   Windows : {@code ping -n 1 <host>}   (-n = número de paquetes; -c no existe)
 * Código 0 = responde; distinto de 0 = no responde o no existe el host.
 */
public class Ejercicio5 {

    /**
     * Hace ping a un equipo y dice si hay conexión según el código de salida.
     *
     * @param args no se usan
     * @throws IOException          si no se puede lanzar el proceso
     * @throws InterruptedException si el padre es interrumpido mientras espera
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Host a comprobar: ");
        String host = sc.nextLine().trim();
        String[] comando = SO.comando(
                new String[]{"ping", "-c", "1", host},
                new String[]{"ping", "-n", "1", host});
        Process p = new ProcessBuilder(comando).start();
        int codigo = p.waitFor();
        System.out.println("Código de salida de ping: " + codigo);
        System.out.println(codigo == 0 ? host + " responde" : host + " NO responde");
        sc.close();
    }
}
