package PSP;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Ejercicio 10. El proceso rebelde: ignora la petición de terminar.
 *
 *   Linux   : sh -c "trap '' TERM; sleep 30"
 *             trap '' TERM = «ignora SIGTERM». destroy() (SIGTERM) no le afecta;
 *             destroyForcibly() (SIGKILL, señal 9) no se puede ignorar → código 137 = 128 + 9.
 *   Windows : no existen las señales: destroy() y destroyForcibly() hacen lo mismo
 *             (TerminateProcess), así que el proceso NO puede resistirse y termina con destroy().
 */
public class Ejercicio10 {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] comando = SO.comando(
                new String[]{"sh", "-c", "trap '' TERM; sleep 30"},
                SO.dormir(30));
        Process p = new ProcessBuilder(comando).start();
        Thread.sleep(500);                                   // margen para que la shell instale el trap

        p.destroy();                                         // 1) se lo pedimos
        boolean terminado = p.waitFor(2, TimeUnit.SECONDS);
        System.out.println("¿Ha terminado con destroy()?: " + terminado);

        if (!terminado) {
            p.destroyForcibly();                             // 2) se le obliga
            int codigo = p.waitFor();
            System.out.println("Terminado con destroyForcibly(). Código de salida: " + codigo);
        } else {
            System.out.println("Código de salida: " + p.exitValue());
        }
        // En Linux, sh -c ... deja el «sleep 30» como NIETO: lo terminamos también para no dejarlo huérfano.
        java.util.Iterator<ProcessHandle> nietos = p.descendants().iterator();
        while (nietos.hasNext()) {
            nietos.next().destroyForcibly();
        }
    }
}
