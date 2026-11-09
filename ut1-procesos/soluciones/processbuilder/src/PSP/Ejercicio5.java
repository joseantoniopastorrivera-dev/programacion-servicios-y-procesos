package PSP;

import java.io.IOException;
import java.util.Scanner;

/**
 * Ejercicio 5. Comprobador de conexión con ping.
 *
 *   Linux   : ping -c 1 <host>   (-c = count: número de paquetes)
 *   Windows : ping -n 1 <host>   (-n = número de paquetes; -c no existe)
 * Código 0 = responde; distinto de 0 = no responde o no existe el host.
 */
public class Ejercicio5 {

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
